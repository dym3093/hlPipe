package org.hpin.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 *<p>@Desc：系统自定义异常的基类</p> 
 *
 *<p>@author : 胡五音</p>
 *
 *<p>@CreateDate：Mar 7, 2012 10:21:30 AM</p>
 *<p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>  
 */

public class SystemException extends Exception {

	private static final long serialVersionUID = 1L ;

	protected String exceptionMessage ;
	
	protected Logger log = Logger.getLogger(this.getClass()) ;
	
	public SystemException(){
		super("系统出现异常！") ;
		log.error("系统出现异常！") ;
	}
	
	public SystemException(Throwable cause){
		super(cause) ;
		log.error(cause.getStackTrace()) ;
	}
	
	public SystemException(String eMessage){
		super(eMessage) ;
		log.error(eMessage) ;
	}
		
	public String getMessage(){
		return super.getMessage() ;
	}
	
	public void printStackTrace(){
		super.printStackTrace() ;
	}
	
	public void printStackTrace(PrintStream s){
		super.printStackTrace(s) ;
	}
	
	public void printStackTrace(PrintWriter w){
		super.printStackTrace(w) ;
	}
}
