package org.hpin.common.export.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-6-15 上午2:39:16</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */

@Namespace("/system")
@Action("exportHtml")
@Results({
	@Result(name = "success", 
			type = "stream", 
			params = {	"contentType" , "text/html" , 
						"inputName" , "exportHtml" , 
						"contentDisposition" , "attachment;filename=\"${fileName}\"" , 
						"bufferSize" , "8192"
					 }
			)
})
public class ExportHtmlAction extends BaseAction {

	private String fileName ;
	
	private String htmlInfo ;
	
	public String getFileName() {
		return fileName ;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName ;
	}

	public String getHtmlInfo() {
		return htmlInfo ;
	}
	
	public void setHtmlInfo(String htmlInfo) {
		this.htmlInfo = htmlInfo ;
	}

	@Override()
	public String execute(){
		htmlInfo = HttpTool.getParameter("htmlInfo") ;
		return SUCCESS ;
	}
	
	public InputStream getExportHtml(){
		ByteArrayOutputStream os = new ByteArrayOutputStream() ;
		
		try {
	    // 将封装的html信息写入output流
			os.write(htmlInfo.getBytes()) ;
		} catch (IOException e) {
			e.printStackTrace() ;
		}
		// 创建一个新分配的byte字节数组。
		byte[] content = os.toByteArray() ;			
		try{
			os.flush() ;
			os.close() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		fileName =  getFileName() == null ? "exportHtml" : getFileName() + ".html" ; 
		
		// 创建一个新的字节数组输出流
		HttpServletResponse response = ServletActionContext.getResponse() ;
		response.setContentLength(os.size()) ; 
		InputStream inputStream = new ByteArrayInputStream(content) ;
		
		return inputStream ;
	}
}

