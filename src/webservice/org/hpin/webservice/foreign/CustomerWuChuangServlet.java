package org.hpin.webservice.foreign;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.GeneCustomerService;


/**
 * 
 * @description: 获取请求,并解析;该类只提供无创调用;
 * create by henry.xu 2016年12月6日
 */
public class CustomerWuChuangServlet  extends HttpServlet {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	 /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerWuChuangServlet() {
        super();
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		try {
			request.setCharacterEncoding("UTF-8");
			//参数接收;
			String json = request.getParameter("customersJson");
			log.info("receiveExaminedObject==>获取到的json数据: " + json);
			String resultJson = "";
			try {
				if(StringUtils.isNotEmpty(json)) {
					GeneCustomerService geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
					boolean flag = geneCustomerService.saveCustomerReceive(json);
					
					if(flag) {
						resultJson = geneCustomerService.receiveExaminedObject();
					} else  {
						resultJson = "{\"result\":\"" + false + "\"}";
					}
					
				} else {
					resultJson = "{\"result\":\"" + false + "\"}";
				}
				
			}catch (Exception e) {
				log.error("receiveExaminedObject==>异常跑出: ", e);
			}
			
			//通过下面的方式把需要的内容发送到客户端
			log.info("receiveExaminedObject==>result: " + resultJson);
			PrintWriter out = response.getWriter();
			out.println(resultJson);
		} catch (IOException e) {
			log.error("数据返回servlet错误!", e);
		}
		
		
	}

}
