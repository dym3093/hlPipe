package org.hpin.common.verifycode.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;
import org.hpin.common.util.StaticMethod;
import org.hpin.common.verifycode.Service.VerifyCodeService;


/**
 * <p>@desc : 加载验证码Action</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 13, 2013 4:26:23 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@SuppressWarnings("serial")
@Namespace("/verify")
@Action("verifyCode")
@Results({
	
})
public class VerifyCodeAction extends BaseAction {
		
	/**
	 * 默认的验证码长度为4
	 */
	private final static int DEFAULT_LENGTH = 4 ;
	
	/**
	 * 默认验证码类型为“去除字母O的字母和数字组合”
	 */
	private final static int DEFAULT_TYPE = 1 ;

	public String execute() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest() ;
		int length = StaticMethod.nullObject2int(HttpTool.getParameter("length")) == 0 ? DEFAULT_LENGTH : StaticMethod.nullObject2int(HttpTool.getParameter("length")) ;
		int type = StaticMethod.nullObject2int(HttpTool.getParameter("type")) == 0 ? DEFAULT_TYPE : StaticMethod.nullObject2int(HttpTool.getParameter("type")) ;
		HttpServletResponse response = ServletActionContext.getResponse() ;
		HttpSession session = request.getSession() ;
		response.setContentType("image/jpeg") ;
		ServletOutputStream sos = response.getOutputStream() ; 
		//设置浏览器不要缓存此图片
		response.setHeader("Pragma" , "No-cache" ) ;
		response.setHeader("Cache-Control", "no-cache" ) ;
		response.setDateHeader("Expires" , 0) ;
		
		BufferedImage image = VerifyCodeService.getImage(session , length , type) ;
		//将图像输出到客户端
		ByteArrayOutputStream bos = new ByteArrayOutputStream () ; 
		ImageIO.write(image , "JPEG" , bos) ;
		byte[] buf = bos.toByteArray() ; 
		response.setContentLength(buf.length) ;
		sos.write(buf) ;
		bos.close() ; 
		sos.close() ; 
		return NONE ;
	}
}

