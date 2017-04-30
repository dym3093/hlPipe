/**
 * @author DengYouming
 * @since 2016-7-22 下午5:04:46
 */
package org.hpin.webservice.util;

import java.io.IOException;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author DengYouming
 * @since 2016-7-22 下午5:04:46
 */
public class HttpUtils {

	private static Logger utilsLog = Logger.getLogger("HttpUtils");
	private static HttpClient client;
	
    private static RequestConfig requestConfig = RequestConfig.custom()  
            .setSocketTimeout(15000)  
            .setConnectTimeout(15000)  
            .setConnectionRequestTimeout(15000)  
            .build();  
	
	public static final String URL = "url";
	public static final String METHOD = "Method";	
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";
	public static final String PARAMETERS = "Parameters";
	public static final String CODE = "code";	
	public static final String MSG = "msg";
	public static final String DATA = "data";
	public static final String POST = "post";
	public static final String GET = "get";
	
	public static final String UTF8 = "UTF-8";
	
	
	/**
	 * 发送HttpPost请求
	 * @param httpPost HttpPost
	 * @return
	 * @author DengYouming
	 * @since 2016-7-25 下午4:52:21
	 */
	public static HttpResponse sendHttpPost(HttpPost httpPost) {  
	    CloseableHttpClient httpClient = null;  
	    CloseableHttpResponse response = null;  
	    try {  
	        // 创建默认的httpClient实例.  
	        httpClient = HttpClients.createDefault();  
	        httpPost.setConfig(requestConfig);  
	        // 执行请求  
	        response = httpClient.execute(httpPost);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            // 关闭连接,释放资源  
	        	if(httpPost != null){
	        		httpPost.releaseConnection();
	        	}
	            if (response != null) {  
	                response.close();  
	            }  
	            if (httpClient != null) {  
	                httpClient.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return response;  
	 }
	
	/**
	 * 获取HttpPost返回的文本内容
	 * @param httpPost
	 * @return String
	 * @author DengYouming
	 * @throws IOException 
	 * @throws ParseException 
	 * @since 2016-7-25 下午4:53:30
	 */
	public static String fetchHttpPostContent(HttpPost httpPost) throws ParseException, IOException {  
	    CloseableHttpResponse response = null;  
	    HttpEntity entity = null;  
	    String respContent = null;  
	    try {  
	        // 执行请求  
	        response = (CloseableHttpResponse) sendHttpPost(httpPost);  
	        entity = response.getEntity();  
	        respContent = EntityUtils.toString(entity, "UTF-8");  
	    }finally {  
	        try {  
	            // 关闭连接,释放资源  
	        	if(httpPost != null){
	        		httpPost.releaseConnection();
	        	}
	            if (response != null) {  
	                response.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    utilsLog.info("content: "+respContent);
	    return respContent;  
	 }
	
	/**
	 * 获取HttpPost返回的JSONObject
	 * @param httpPost HttpPost
	 * @return JSONObject
	 * @throws ParseException
	 * @throws IOException
	 * @author DengYouming
	 * @throws JSONException 
	 * @since 2016-7-29 下午6:40:50
	 */
	public static JSONObject fetchHttpPostJson(HttpPost httpPost) throws ParseException, IOException, JSONException{
		JSONObject json = null;
		String respContent = null;
		if(httpPost!=null){
			respContent = fetchHttpPostContent(httpPost);
			if(respContent!=null&&respContent.length()>0){
				json = new JSONObject(respContent);
			}
		}
		return json;
	}
	
	/**
	 * 发送HttpGet请求
	 * @param httpGet HttpGet
	 * @return HttpResponse
	 * @author DengYouming
	 * @since 2016-7-25 下午4:31:47
	 */
	public static HttpResponse sendHttpGet(HttpGet httpGet) {  
	    CloseableHttpClient httpClient = null;  
	    CloseableHttpResponse response = null;  
	    try {  
	        // 创建默认的httpClient实例.  
	        httpClient = HttpClients.createDefault();  
	        httpGet.setConfig(requestConfig);  
	        // 执行请求  
	        response = httpClient.execute(httpGet);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            // 关闭连接,释放资源  
	        	if(httpGet != null){
	        		httpGet.releaseConnection();
	        	}
	            if (response != null) {  
	                response.close();  
	            }  
	            if (httpClient != null) {  
	                httpClient.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return response;  
	 }
	
	/**
	 * 获取HttpGet返回的文本内容
	 * @param httpGet HttpGet
	 * @return String
	 * @author DengYouming
	 * @since 2016-7-25 下午4:31:47
	 */
	public static String fetchHttpGetContent(HttpGet httpGet) {  
		HttpResponse response = null;
	    HttpEntity entity = null;  
	    String respContent = null;  
        try {
	        response = sendHttpGet(httpGet);  
	        entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        utilsLog.info("content: "+respContent);
	    return respContent;  
	 }
	
	
	/**
	 * 创建HttpClient
	 * @return HttpClient
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-7-25 下午4:43:00
	 */
	public static HttpClient createHttpClient() throws Exception{
		if(client==null){
			client = HttpClients.createDefault();
		}
		return client;
	}
	
	public static HttpGet createHttpGet(String jsonStr) throws JSONException{
		HttpGet myGet = null;
		if(jsonStr!=null&&jsonStr.length()>0){
			JSONObject json = new JSONObject(jsonStr);
			if(json!=null&&json.length()>0){
				myGet = createHttpGet(json);
			}
		}
		return myGet;
	}
	
	public static HttpGet createHttpGet(JSONObject json) throws JSONException{
		HttpGet myGet = null;
		if(json!=null&&json.length()>0){
			String urlOrg = json.getString(URL)==null?"":json.getString(URL);
			if(json.getString(URL)!=null){
				json.remove(URL);
			}
		
			String contentType = json.getString(CONTENT_TYPE)==null?"text/json;charset=utf-8":json.getString(CONTENT_TYPE);
			if(json.getString(CONTENT_TYPE)!=null){
				json.remove(CONTENT_TYPE);
			}
			String urlParam = GeneratorUtils.jsonToURLParams(json,false);
			myGet = new HttpGet(urlOrg+"?"+urlParam);
			myGet.setHeader(CONTENT_TYPE, contentType);
		}
		return myGet;
	}
	
	
	/**
	 * 根据条件创建HttpPost请求
	 * @param url 地址
	 * @param contentType 文本格式
	 * @param jsonStr JSON字符串
	 * @return HttpPost
	 * @author DengYouming
	 * @since 2016-7-30 上午11:28:26
	 */
	public static HttpPost createHttpPost(String url, String contentType, String jsonStr){
		HttpPost httpPost = null;
		if(url!=null&&url.length()>0&&contentType!=null&&contentType.length()>0&&jsonStr!=null&&jsonStr.length()>0){
			httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(jsonStr,UTF8);
			utilsLog.info(" createHttpPost jsonStr: "+jsonStr);
			utilsLog.info(" createHttpPost entity.toString(): "+entity.toString());
			
			entity.setContentType("text/json");
			httpPost.setEntity(entity);
		}else{
			throw new NullArgumentException("Can not create HttpPost by null object!");
		}
		return httpPost;
	}
	
	public static HttpGet createHttpGet(String url, String contentType, String jsonStr) throws Exception{
		HttpGet httpGet = null;
		if(url!=null&&url.length()>0&&contentType!=null&&contentType.length()>0&&jsonStr!=null&&jsonStr.length()>0){
				String paramStr = GeneratorUtils.jsonToURLParams(new JSONObject(jsonStr), false);
				utilsLog.info("url all: "+url+"?"+paramStr);
				httpGet = new HttpGet(url+"?"+paramStr);
				if(httpGet!=null){
					httpGet.setHeader(CONTENT_TYPE, contentType);
				}
		}else{
			throw new NullArgumentException("Can not create HttpPost by null object!");
		}
		return httpGet;
	}
	
	/**
	 * 关闭HttpClient
	 * @param entity
	 * @return boolean
	 * @author DengYouming
	 * @since 2016-7-25 下午4:43:32
	 */
	public static boolean closeHttpClient(HttpClient entity){
		boolean flag = false;
		if(entity!=null){
			HttpClientUtils.closeQuietly(entity);
			utilsLog.info("entity: "+entity);
		}
		return flag;
	}
	
	/**
	 * 
	 * @param url
	 * @param method
	 * @param contentType
	 * @param jsonStr
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-19 下午6:29:05
	 */
	public static String sendRequest(String url, String method, String contentType, String jsonStr) throws Exception{
		Logger log = Logger.getLogger("sendRequest");
		log.info("url: "+url);
		log.info("method: "+method);
		log.info("contentType: "+contentType);
		log.info("jsonStr: "+jsonStr);
		String content = null;
		if(StringUtils.isEmpty(method)){
			method = POST;
		}
		if(StringUtils.isEmpty(contentType)){
			contentType = CONTENT_TYPE;
		}
		HttpEntity entity = sendRequestEntity(url, method, contentType, jsonStr);
		if(entity!=null){
			content = EntityUtils.toString(entity, UTF8);
		}
		log.info("返回结果 content:"+content);
		return content;
	}
	
	/**
	 * 发起请求，返回HttpEntity
	 * @param url 
	 * @param method post/get
	 * @param contentType
	 * @param jsonStr
	 * @return HttpEntity
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-19 下午6:33:18
	 */
	public static HttpEntity sendRequestEntity(String url, String method, String contentType, String jsonStr) throws Exception{
		Logger log = Logger.getLogger("sendRequestEntity");
		log.info("url: "+url);
		log.info("method: "+method);
		log.info("contentType: "+contentType);
		log.info("jsonStr: "+jsonStr);
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		
		if(StringUtils.isEmpty(url)){
			log.info("url为空");
			return entity;
		}
		
		if(StringUtils.isEmpty(jsonStr)){
			log.info("jsonStr为空");
			return entity;
		}
		
		if(POST.equalsIgnoreCase(method)){
			post = HttpUtils.createHttpPost(url, contentType, jsonStr);
			if(post!=null){
				response = HttpUtils.createHttpClient().execute(post);
			}
		}
		if(GET.equalsIgnoreCase(method)){
			get = HttpUtils.createHttpGet(url, contentType, jsonStr);
			if(get!=null){
				response = HttpUtils.createHttpClient().execute(get);
			}
		}
		if(response!=null){
			entity = response.getEntity();
		}
		return entity;
	}
	
	public static boolean isEmpty(String content){
		return content==null||content.length()==0;
	}
	
	public static boolean isNotEmpty(String content){
		return !isEmpty(content);
	}
	
}
