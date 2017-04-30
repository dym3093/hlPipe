package org.hpin.common.widget.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;
import org.hpin.common.util.HttpTool;


/**
 * <p>@desc : 生成验证码标签</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 13, 2013 4:56:19 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class VerifyCodeTag extends TagSupport {

	private Integer length = 4 ;
	
	private Integer type = 1 ; 
	
	private String alt = "看不清？点击更换验证码" ;
	
	private String valign = "top" ;
	
	public Integer getLength() {
		return length ;
	}

	public void setLength(Integer length) {
		this.length = length ;
	}
	
	public Integer getType() {
		return type ;
	}
	
	public void setType(Integer type) {
		this.type = type ;
	}
	
	public String getAlt() {
		return alt ;
	}
	
	public void setAlt(String alt) {
		this.alt = alt ;
	}

	public String getValign() {
		return valign;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}

	public int doStartTag() throws JspException{
		String path = ServletActionContext.getRequest().getContextPath() ;
		StringBuffer outHtml = new StringBuffer("<img src = \"").append(path) ;
		outHtml.append("/verify/verifyCode.do") ;
		outHtml.append("?length=").append(length) ;
		outHtml.append("&type=").append(type).append("\" ") ;
		outHtml.append("style=\"cursor: hand; border: 1px solid #ccc; vertical-align: "+valign+";\" ") ;
		outHtml.append(" title = \"").append(alt).append("\" ") ;
		outHtml.append(" onClick = \"changeVCode(this , '").append(path).append("' , '").append(length).append("' , '").append(type).append("');\" />") ;
		/** 生成验证码 **/
		outHtml.append("<script type=\"text/javascript\">");
		outHtml.append("function changeVCode(obj , path , length , type){");
		outHtml.append("var d = Math.random() ;");
		outHtml.append("obj.src = path + '/verify/verifyCode.do?length=' + length + '&type=' + type + '&r=' + d ;");
		outHtml.append("}</script>");

	
		try{
			pageContext.getOut().println(outHtml.toString()) ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		outHtml = null ;
		return SKIP_BODY ;
	}
}

