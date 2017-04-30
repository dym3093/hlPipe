package org.hpin.common.widget.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.hpin.common.util.ServletUtils;
import org.hpin.common.util.StrUtils;
import org.hpin.common.widget.pagination.Page;

/**
 * 返回标签
 * 
 * @author thinkpad
 * @version Sep 16, 2008
 */
@SuppressWarnings("serial")
public class BackTag extends TagSupport {

	private String url = null;

	public void setUrl(String url) {
		this.url = url;
	}

	public int doStartTag() throws JspException {
		StringBuffer searchHtml = new StringBuffer();
		// 取得Page对象
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		if (StrUtils.isNotNullOrBlank(request.getAttribute("backUrl"))) {
			url = (String) request.getAttribute("backUrl");
		}
		// 取得request所有的查询参数
		Map paramMap = ServletUtils.getParametersStartWith(request,
				"pass,filter", true);
		searchHtml
				.append("<input  type='button' class='button' value='返  回' style='cursor: pointer' onClick='goBack()' />");
		searchHtml
				.append("<form name='searchBackFormTemp' id='searchBackFormTemp'  method='post' action='"
						+ url + "' style='display:none'>");
		for (Object key : paramMap.keySet()) {
			Object objectValue = paramMap.get(key);
			if (!objectValue.getClass().isArray()) {
				if (StrUtils.isNotNullOrBlank(objectValue.toString())) {
					// 将查询参数以_TEMP结尾保存到hidden元素中去
					searchHtml.append("<input type='hidden' name='" + key + "'"
							+ " value='" + objectValue.toString() + "'>");
				}
			} else {
				String[] objectValueArray = (String[]) request.getAttribute(key
						.toString());
				for (int i = 0; i < objectValueArray.length; i++) {
					searchHtml.append("<input type='hidden' name='" + key + "'"
							+ " value='" + objectValueArray[i] + "'>");
				}
			}
		}
		if (request.getAttribute("page") != null) {
			searchHtml
					.append("<input type='hidden' name='_pageNum_TEMP'"
							+ " value='"
							+ ((Page) request.getAttribute("page"))
									.getPageNum() + "'>");
		} else if (request.getAttribute("_pageNum") != null) {
			searchHtml.append("<input type='hidden' name='_pageNum_TEMP'"
					+ " value='" + request.getAttribute("_pageNum") + "'>");

		}
		searchHtml.append("</form>");

		searchHtml.append("<script type='text/javascript'>\n");
		searchHtml.append("function goBack(){");
		searchHtml.append("if($$('searchBackFormTemp')==null){");
		searchHtml.append("alert('参数设置错位,没有找到临时FORM')");
		searchHtml.append(" }else{");
		searchHtml.append(" $$('searchBackFormTemp').submit();");
		searchHtml.append(" }");
		searchHtml.append(" }");
		searchHtml.append("</script>");
		try {
			pageContext.getOut().println(searchHtml.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		searchHtml = null;
		return SKIP_BODY;

	}
}
