package org.hpin.fg.filter;

import java.io.IOException;

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
public class SecurityFilter implements Filter {

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
		String currentUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
		
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
