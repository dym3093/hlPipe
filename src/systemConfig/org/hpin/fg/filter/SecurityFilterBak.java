package org.hpin.fg.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hpin.common.core.SystemConstant;

/**
 * 权限控制标签，暂时验证是否登录，暂未时间写是否有权限访问
 * 
 * @author thinkpad
 * @data Jan 13, 2010
 */
public class SecurityFilterBak implements Filter {

	protected FilterConfig filterConfig = null;

	private String notCheckLoginUrl = null;

	public void destroy() {
		
	}

	/**
	 * 安全过滤
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		// 获取当前访问URL
		String currentUrl = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		
		if (notCheckLoginUrl.indexOf(currentUrl) >= 0) {
			if(currentUrl.indexOf("login.action") >= 0){
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			else if(currentUrl.indexOf("loginForCustomerService.action") >= 0){
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			} 
		}
		
		if(currentUrl.indexOf("/checkPaymentVoucher/checkPaymentVoucher") >= 0){
			filterChain.doFilter(servletRequest, servletResponse) ;
			return ;
		}
		
		// 没有登录的用户跳转到登陆页面
		if (null == request.getSession().getAttribute("currentUser")) {
			PrintWriter out = response.getWriter();
			out.println("<script>top.location.href = '"
					+ request.getContextPath() + "/security/security!login.action" + "'</script>");
			out.close();
			return;
		}
		
		
		// 过滤掉当前访问URL后面参数
		currentUrl = currentUrl.substring(1);
		if (currentUrl.lastIndexOf("?") > 0) {
			currentUrl = currentUrl.substring(0, currentUrl.lastIndexOf("?"));
		}
		// 对需要安全控制的URL进行验证
		filterChain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * 初始化不需要验证的的URL
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		notCheckLoginUrl = SystemConstant
				.getSystemConstant("not_check_url_list");
	}
}
