package org.hpin.common.widget.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.hpin.common.util.ServletUtils;
import org.hpin.common.util.StrUtils;

/**
 * 参数传递TAG,将系统需要参数传递的内容进行传递
 * 
 * @author thinkpad 2010-9-26
 */
public class TransferTag extends TagSupport {

	private boolean isForm = false;

	private String transferStr = null;

	public int doStartTag() throws JspException {
		StringBuffer searchHtml = new StringBuffer();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map paramMap = null;
		if (null == transferStr) {
			paramMap = ServletUtils.getParametersStartWith(request,
					"pass,filter", true);
		} else {
			paramMap = ServletUtils.getParametersStartWith(request,
					transferStr, true);
		}
		if (isForm) {
			searchHtml
					.append("<form id='transferForm'  method='post' style='display:none'>");
		}
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
		if (isForm) {
			searchHtml.append("</form>");
		}
		try {
			pageContext.getOut().println(searchHtml.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public boolean getIsForm() {
		return isForm;
	}

	public void setIsForm(boolean isForm) {
		this.isForm = isForm;
	}

	public String getTransferStr() {
		return transferStr;
	}

	public void setTransferStr(String transferStr) {
		this.transferStr = transferStr;
	}
}
