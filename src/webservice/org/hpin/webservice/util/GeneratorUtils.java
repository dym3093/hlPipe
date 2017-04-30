/**
 * @author DengYouming
 * @since 2016-7-29 下午12:12:42
 */
package org.hpin.webservice.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 金埻接口签名生成算法
 * @author DengYouming
 * @since 2016-7-29 下午12:12:42
 */
public class GeneratorUtils {
	
	private static GeneratorUtils generator;
	
	private static AsciiComparator asciiComp = new AsciiComparator();
	
	public static void main(String[] args) throws Exception {
		
	}
	
	public void createJSONStr(){
	}
	
	private GeneratorUtils(){
		
	}
	
	public static GeneratorUtils getInstance(){
		if(null==generator){
			generator = new GeneratorUtils();
		}
		return generator;
	}
	
	private static LinkedHashMap<String, Object> sortKeyByAsciiAsc(Map<String, Object> params){
		LinkedHashMap<String, Object> linkedMap = null;
		TreeMap<String, Object> treeMap = null;
		if(!params.isEmpty()){
			linkedMap = new LinkedHashMap<String, Object>();
			treeMap = new TreeMap<String, Object>(asciiComp);
			treeMap.putAll(params);
			linkedMap.putAll(treeMap);
			for (String key : linkedMap.keySet()) {
				System.out.println(key+"="+linkedMap.get(key));
			}
		}
		return linkedMap;
	}

	/**
	 * JSON对象或者字符串转换成HashMap对象
	 * @param object
	 * @return HashMap
	 * @author DengYouming
	 * @throws JSONException 
	 * @since 2016-7-30 上午10:50:57
	 */
	public static HashMap<String, Object> toHashMap(Object object) throws JSONException{  
	       HashMap<String, Object> data = new HashMap<String, Object>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = null;
	       if(object instanceof JSONObject){
	    	   jsonObject = (JSONObject) object;
	       }else{
	    	   jsonObject = new JSONObject(object);
	       }
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = (String) it.next();
	           Object value = jsonObject.get(key);
	           String oStr = null;
	           if(value instanceof String){
	        	   oStr = (String)value;
	        	   oStr = oStr.substring(1, oStr.length()-1);
	           }
	           System.out.println("key: "+key+" , oStr: "+oStr);
	           data.put(key, oStr);  
	       }  
	       return data;  
	}  
	
	/**
	 * 返回随机32位字符串
	 * @param lowerCase 是否小写， true:返回小写格式， false：大写格式
	 * @return String
	 * @author DengYouming
	 * @since 2016-7-30 上午10:55:55
	 */
	public static String randomUUID(boolean lowerCase){
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		if(lowerCase){
			uuid = uuid.toLowerCase();
		}else{
			uuid = uuid.toUpperCase();
		}
		return uuid;
	}
	
	/**
	 * 根据传入的JSON对象和key创建金埻接口密钥
	 * @param mJsonObject JSONObject
	 * @param key String 
	 * @return String
	 * @author DengYouming
	 * @since 2016-8-5 下午12:03:01
	 */
	public static String generateSign(JSONObject mJsonObject,String key) {
		Logger logger = Logger.getLogger("GeneratorUtils");
		logger.info("mJsonObject String: "+mJsonObject.toString());
		logger.info("key: "+key);
		StringBuilder urlParam = new StringBuilder(jsonToURLParams(mJsonObject,false));
		urlParam.append("&key=").append(key);
		System.out.println("生成sign之前   urlParam.toString(): "+urlParam.toString());
		logger.info("urlParam.toString(): "+urlParam.toString());
//		String sign = MD5Util.encodeByMD5(urlParam.toString());
		String sign = MD5Util.md5Str(urlParam.toString());
		logger.info("sign: "+sign);
		return sign;
	}
	
	 /**
	 * 将JSONObject参数转换成Get请求的参数格式
	 * 
	 * @param mJsonObject
	 * @return
	 */
	public static String jsonToURLParams(JSONObject mJsonObject, boolean encode) {

		Iterator<String> keys = mJsonObject.keys();
		HashMap<String, Object> params = new HashMap<String, Object>();

		while (keys.hasNext()) {
			String key = keys.next();
			try {
				Object value = mJsonObject.get(key);

				if (value instanceof Number) {
					params.put(key, mJsonObject.get(key));
				} else {
					if (encode) {
						try {
							params.put(key, URLEncoder.encode(mJsonObject.getString(key),"utf-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} else {
						params.put(key, mJsonObject.getString(key));
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		Collection<String> keyset = params.keySet();
		List<String> list = new ArrayList<String>(keyset);

		// 对key键值按字典升序排序
		Collections.sort(list);

		StringBuilder mStringBuilder = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			Object value = params.get(list.get(i));
			mStringBuilder.append(list.get(i));
			mStringBuilder.append("=");

			// 这里主用于解决浮点型，小数为0时会自动添加.0，造成签名错误的问题，不处理会出现这样的错误。
			// 例如：20，会改成20.0
			if (value instanceof Number) {
				Number srcVal = (Number) value;

				// 如果长整和浮点值相等，说明小数点后面为0，那里转值和签名统一用去掉小数点来处理
				if (srcVal.longValue() == srcVal.floatValue()) {
					mStringBuilder.append(srcVal.longValue());
				} else {// 否则带小数点和小数位数值传值和签名
					mStringBuilder.append(value);
				}

			} else {
				mStringBuilder.append(value);
			}

			if (i < list.size() - 1) {
				mStringBuilder.append("&");
			}
		}

		return mStringBuilder.toString();
	}
	
	/**
	 * 返回32位随机字符串（大写格式）
	 * @return String
	 * @author DengYouming
	 * @since 2016-7-30 上午10:57:12
	 */
	public static String randomUUID(){
		return randomUUID(false);
	}
	
}
