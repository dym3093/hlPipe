package org.hpin.webservice.foreign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.util.Tools;
import org.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Servlet implementation class TriggerServlet
 */
//@WebServlet("/noticeSaveTestees")
public class NoticeSaveTesteesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//	private ThreadPoolTaskExecutor taskExecutor;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeSaveTesteesServlet() {
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
		Logger logger = Logger.getLogger("NoticeSaveTesteesServlet");
		
		Logger remoteLog = Logger.getLogger("remoteLog");
		try {
			request.setCharacterEncoding("UTF8");
			remoteLog.info("发起请求的地址： "+request.getRemoteAddr());
			remoteLog.info("发起请求的主机： "+request.getRemoteHost());
			remoteLog.info("发起请求的端口： "+request.getRemotePort());
			remoteLog.info("发起请求的用户： "+request.getRemoteUser());
			
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			
			String jyStr = sb.toString();
			
			String serviceId = null;
					
			JSONObject json = new JSONObject();
			
			logger.info("jyStr: "+jyStr);
			
			if(StringUtils.isNotEmpty(jyStr)){
				JSONObject jyJson = new JSONObject(jyStr);
				if(jyJson.has("serviceId")){
					serviceId = jyJson.getString("serviceId").trim();
				}
				String state = null;
				if(jyJson.has("state")){
					state = jyJson.getString("state");
				}
				logger.info("state: "+state);
			}else{
				json.put("code", 400);
				json.put("msg", "金域提交的为空数据");
			}
				
			if(StringUtils.isEmpty(serviceId)){
				serviceId = request.getParameter("serviceId");
			}
				
				logger.info("serviceId: "+serviceId);
				
				remoteLog.info("发送过来的内容： "+jyStr);
				remoteLog.info("发送过来的serviceId： "+serviceId);
				
				if(StringUtils.isNotEmpty(serviceId)){
					TesteesThread testeesThread = new TesteesThread(serviceId);
					ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
					if(taskExecutor!=null){
						//@SuppressWarnings("unchecked")
						Future<String> future = taskExecutor.submit(testeesThread);
						if(future!=null){
							json.put("code", 200);
							json.put("msg", "执行成功");
							if(future.isDone()){
								String respStr = future.get();
								logger.info("执行线程后返回的字符串: "+respStr);
								if(respStr!=null&&respStr.length()>0){
									json.put("code", 200);
									json.put("msg", "执行成功");
									
									JSONObject result = new JSONObject(respStr);
									String mailContent = result.get("mailContent")==null?"无相关信息":(String) result.get("mailContent");
									logger.info("mailContent: "+mailContent);
									//发送邮件
									Tools.sendMail(mailContent, null);
								}else{
									json.put("code", 5001);
									json.put("msg", "返回数据为空");
								}
							}
						}
					}
				}else{
					json.put("code", 400);
					json.put("msg", "serviceId为空");
				}
		/*	}else{
				json.put("code", 400);
				json.put("msg", "空数据");
			}*/
			logger.info("返回给金域的数据 jsonStr: "+json.toString());
			response.setContentType("application/json; charset=utf-8");
			json.write(response.getWriter());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

}
