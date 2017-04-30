package org.hpin.webservice.foreign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 获取会员报告
 * @author DengYouming
 * @since 2016-10-11 下午4:15:02
 */
public class FetchReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//	private ThreadPoolTaskExecutor taskExecutor;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchReportServlet() {
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
		Logger log = Logger.getLogger("FetchReportServlet");
		//返回的JSON数据
		JSONObject json = new JSONObject();
		//接收到的请求数据
		String reqStr;
		
		JSONObject reqJson;
		try {
			request.setCharacterEncoding("UTF8");
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(),
													"utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}			
				
			if(StringUtils.isNotEmpty(sb.toString())){
				reqStr = sb.toString();	
				log.info("接收到的请求数据  reqStr: "+reqStr);
			}else{
				String value;
				reqJson = new JSONObject();
				request.getParameterMap();
				// old methode
				//Map<String,String> param = (Map<String,String>)request.getParameterMap();
				Map<String,String> param = this.parseToNormalMap(request);
				Iterator<String> iter = param.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next().trim();
					log.info("key: "+key);
					if("name".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("name:["+value+"]");
							reqJson.put("name", value.trim());
						}
					}
					if("code".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("code:["+value+"]");
							reqJson.put("code", value.trim());
						}
					}
					if("eventsNo".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("eventsNo:["+value+"]");
							reqJson.put("eventsNo", value.trim());
						}
					}
					if("batchNo".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("batchNo:["+value+"]");
							reqJson.put("batchNo", value.trim());
						}
					}
					if("groupOrderNo".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("groupOrderNo:["+value+"]");
							reqJson.put("serviceId", value.trim());
						}
					}
					if("serviceId".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							log.info("serviceId:["+value+"]");
							reqJson.put("serviceId", value.trim());
						}
					}
					if("reportId".equalsIgnoreCase(key)){
						value = this.convertByKey(request,key);
						if(StringUtils.isNotEmpty(value)){
							reqJson.put("reportId", value.trim());
						}
					}
					//据此判定下载的文件, [0:基因报告和报告单全部下载， 1：只下载基因报告， 2：只下载报告单]
					if("down".equalsIgnoreCase(key)){
						value = new String(request.getParameter("down").getBytes("iso-8859-1"),"UTF-8");
						if(StringUtils.isNotEmpty(value)){
							reqJson.put("down", value.trim());
						}
					}
				}
				//不标明下载类型，默认为全部下载，设置为0
				if(!reqJson.has("down")){
					reqJson.put("down", "0");
				}
				reqStr = reqJson.toString();
				log.info("接收到的请求数据 ，转换成JSON字符串 reqStr: "+reqStr);
			}

			if(StringUtils.isNotEmpty(reqStr)){
				log.info("发起线程前的JSON字符串 reqStr: "+reqStr);
				JSONObject receviceJson = new JSONObject(reqStr);
				ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) SpringTool.getBean("taskExecutor");
				if(receviceJson.has("report")){
					String reportInfo = receviceJson.getString("report");
					//有报告信息，则为金域传送的数据，另外进行解析处理
					if(StringUtils.isNotEmpty(reportInfo)){
						DecodeReportThread reportThread = new DecodeReportThread(reportInfo);
						Future<Map<String,String>> future = taskExecutor.submit(reportThread);
						json.put("code",200);
						json.put("msg","调用成功");
					}else{
						json.put("code", 200);
						json.put("msg", "报告数据为空不进行处理");
					}
				}else {
					GainReportThread gainReportThread = new GainReportThread(reqStr);
					Future<Map<String,String>> future = taskExecutor.submit(gainReportThread);
					json.put("code", 200);
					json.put("msg", "调用成功");
				}
			}else{
				json.put("code", 5001);
				json.put("msg", "空数据");
			}
			
			log.info("返回给金域的数据 jsonStr: "+json.toString());
			response.setContentType("application/json; charset=utf-8");
			json.write(response.getWriter());
		} catch (Exception e) {
			log.info(e);
		}

	}
	
	private String convertByKey(HttpServletRequest request, String key ){
		String val = null;
		try {
			val = new String(request.getParameter(key).getBytes("iso-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}

	private Map<String,String> parseToNormalMap(HttpServletRequest request){
		Map<String,String> map = new HashMap<String, String>();
		if(request!=null){
			Map props = request.getParameterMap();
			Iterator iter = props.entrySet().iterator();
			Map.Entry entry;
			String name;
			String value = "";
			while (iter.hasNext()){
				entry = (Map.Entry) iter.next();
				name = (String) entry.getKey();
				Object valueObj = entry.getValue();
				if(null==valueObj){
					value = "";
				}else if(valueObj instanceof String[]){
					String[] values = (String[]) valueObj;
					for (int i=0; i<values.length; i++){
						value = values[i]+",";
					}
					value = value.substring(0, value.length()-1);
				}else {
					value = valueObj.toString();
				}
				map.put(name,value);
			}
		}
		return map;
	}

}
