/**
 * @author DengYouming
 * @since 2016-7-30 下午6:35:25
 */
package org.hpin.webservice.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.hpin.webservice.mail.MailSenderInfo;
import org.hpin.webservice.mail.SimpleMailSender;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 通用工具类
 * @author DengYouming
 * @since 2016-7-30 下午6:35:25
 */
public class Tools {

	public static final String DATE_FORMAT_DEFAULT = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_ALL = "yyyyMMddHHmmssSSS";
	public static final String DATE_FORM_SIMPLE= "yyyy-MM-dd";
	public static final String DATE_FORM_SIMPLE_NO_LINE= "yyyyMMdd";

	public static final String UTF8 = "UTF8";

	static SimpleDateFormat sdf = new SimpleDateFormat();

	static CloseableHttpClient httpclient = HttpClients.createDefault();

	public static String httpTest(String str){
		String content = "done";
		try {
			String url = "http://gene.healthlink.cn:8088/websGene/fetchReport?serviceId="+str;
			HttpGet get = new HttpGet(url);
			 httpclient.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static HashMap<String, Object> toHashMap(String jsonStr){  
	       HashMap<String, Object> data = new HashMap<String, Object>();  
	       JSONObject json = JSONObject.fromObject(jsonStr);
	       if(json!=null){
		       // 将json字符串转换成jsonObject  
		       Iterator it = json.keys();  
		       // 遍历jsonObject数据，添加到Map对象  
		       while (it.hasNext()){  
		           String key = String.valueOf(it.next());
		           Object value = json.get(key);  
		           System.out.println("key: "+key+" , value: "+value.toString());
		           if(value instanceof String){
		        	   String valStr = (String) value;
		        	   if(valStr.startsWith("\"")){
		        		   valStr = valStr.substring(1, valStr.length()-1);
		        		   if(valStr.startsWith("{")||valStr.startsWith("[")){
		        			   if(valStr.contains(",")){
		        				   String[] valArr = valStr.split(",");
		        				   for (int i = 0; i < valArr.length; i++) {
									data.put(valArr[i].split(":")[0], valArr[i].split(":")[1]);
								}
		        			   }else{
		        				   data.put(key, valStr);
		        			   }
		        		   }
		        		   System.out.println("key2: "+key+" , value2: "+valStr);
		        	   }
		           }
		           data.put(key, value);  
		       }  
	       }
	       return data;  
	}  
	
	public static boolean sendMail(String content, File... files){
		return sendMail(null, null, null, content, files);
		
	}
	
	public static boolean sendMail(String fromMail, String fromMailPwd, String toMail, String content, File... files){
		boolean flag = false;
		
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.exmail.qq.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true); 
		
		fromMail = "gene@healthlink.cn";// 用公共邮箱发件
		fromMailPwd = "Yue123.com";// 公共邮箱密码
		
		toMail = "gene@healthlink.cn";// 公共邮箱收件
		
		mailInfo.setUserName(fromMail);	
		mailInfo.setPassword(fromMailPwd);
		mailInfo.setFromAddress(toMail);
		mailInfo.setContent(content);
		
		List<File> attaches = null;
		String[] attachFileNames = null;
		if(files!=null&&files.length>0){
			attaches = new ArrayList<File>();
			attachFileNames = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				attaches.add(files[i]);
				attachFileNames[i] = files[i].getPath();
			}
			mailInfo.setAttaches(attaches);
			mailInfo.setAttachFileNames(attachFileNames);
		}
		// 发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		// 发送文体格式
		sms.sendTextMail(mailInfo);
		// 发送html格式
		flag = SimpleMailSender.sendHtmlMail(mailInfo);
		return flag;
	}
	
	public static boolean isEmpty(String content){
		return content==null||content.length()==0;
	}
	
	public static boolean isNotEmpty(String content){
		return !isEmpty(content);
	}
	
	public static String object2JsonStr(Object obj){
		String content = null;
		if(obj!=null){
			JSONObject json =JSONObject.fromObject(obj);
			content = json.toString();
		}
		return content;
	}
	
   /**
    * 列名转换成属性名
    * @param orgName
    * @return String
    * @author DengYouming
    * @since 2016-10-25 下午8:20:09
    */
   public static String colToField(String orgName){
	 //orgName转换后的写法
	   StringBuffer buff = null;
	   String propName = ""; 
		//如果有下划线，转换成驼峰写法
		if(orgName.indexOf("_")>-1){
			buff = new StringBuffer();
			String[] strArr = orgName.split("_");				
			for (int j = 0; j < strArr.length; j++) {
				if(j==0){
					//第一个单词小写
					buff.append(strArr[j].toLowerCase());
					continue;
				}
				buff.append(firstCharUpcase(strArr[j]));
			}
			//说明已经过转换
			propName = buff.toString();
		}else{
			//如果是单个单词，直接转换成小写属性名
			propName = firstCharUpcase(orgName.toLowerCase());
		}
		return propName;
   }
 
	/**
	 * 首字母大写，其他全小写
	 * @param content
	 * @return String
	 * @author DengYouming
	 * @since 2016-5-6 下午6:19:01
	 */
	private static String firstCharUpcase(String content){
		return content.substring(0, 1).toUpperCase().concat(content.substring(1).toLowerCase());
	}

	/**
	 * 获取 yyyyMMddHHmmssSSS格式的时间字符串(例：201611251155436)
	 * @return String
     */
	public static String getTimeStr(){
		return getTimeStr(null);
	}

	/**
	 * 根据传入的格式获取对应时间格式的字符串
	 * @param format 格式类型
	 * @return String
     */
	public static String getTimeStr(String format){
		Date curr = Calendar.getInstance().getTime();
		if(format==null||format.trim().length()==0){
			format = DATE_FORMAT_ALL;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String nowTimeStr = sdf.format(curr);
		return nowTimeStr;
	}

	 /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * create by henry.xu 20161125
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

	/**
	 * 根据指定的日期格式获取日期
	 * @param dateStr 日期字符串
	 * @param datePattern 日期格式
     * @return Date
     */
	public static Date getDateFromStr(String dateStr, String datePattern){
		Date date = null;
		if(datePattern==null|| datePattern.trim().length()==0){
			datePattern = DATE_FORM_SIMPLE;
		}
		if(dateStr!=null&&dateStr.trim().length()>0){
			sdf.applyPattern(datePattern);
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 根据文件名获取其文件类型
	 * @param str 文件名
	 * @return String 后缀名
     */
	public static String getSuffix(String str){
		String suffix = null;
		if(isNotEmpty(str)&&str.contains(".")){
			suffix = str.substring(str.lastIndexOf(".")+1);
		}
		return suffix;
	}

	/**
	 * @description 查找项目中的文件
	 * @param fileName 文件名
	 * @author YoumingDeng
	 * @since: 2016/12/21 3:19
	 */
	public static List<File> findFile(String fileName){
		return findFile(fileName,null,true) ;
	}
	/**
	 * @description 查找项目中的文件
	 * @param fileName 文件名
	 * @param dir 查找的目录，如果为空，则从 /WEB-INF/classes中查找
	 * @param isExact 是否完全匹配查找，true:是，false:否
	 * @author YoumingDeng
	 * @since: 2016/12/20 2:57
	 */
	public static List<File> findFile(String fileName, String dir, boolean isExact ) {
		List<File> list = null;
		if (fileName != null && fileName.trim().length() > 0) {
			if (StringUtils.isEmpty(dir)) {
				//dir = System.getProperty("user.dir");
				dir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			}
			File dirFile = new File(dir);
			list = new ArrayList<File>();
			if (dirFile.exists() && dirFile.isDirectory()) {
				File[] files = dirFile.listFiles();
				if (files != null && files.length > 0) {
					File existDir = null;
					for (int i = 0; i < files.length; i++) {
						existDir = files[i];
						if (existDir.isDirectory() || !existDir.getPath().contains("lib")) {
							list.addAll(findFile(fileName, files[i].getPath(), isExact));
						}
					}
				}
			} else {
				//严格等于
				if (isExact) {
					if (fileName.equals(dirFile.getName())) {
						list.add(dirFile);
					}
				} else {
					if (StringUtils.containsIgnoreCase(dirFile.getName(), fileName)) {
						list.add(dirFile);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 随机产生7位长度的数字字符串
	 * @return String
	 * @author YoumingDeng
     */
	public static String getNumber7FromRandom(){
		Random r = new Random();
		int ranNum = r.nextInt(10000000);
		while(ranNum<1000000){
			ranNum = r.nextInt(10000000);
		}
		return String.valueOf(ranNum);
	}

	/**
	 * 字符串根据编码转换,默认UTF8编码格式
	 * @param content
	 * @return String
	 * @author Damian
	 * @since 2016-12-28
	 */
	public static String decodeStr(String content){
		return decodeStr(content,UTF8);
    }

	/**
	 * 字符串根据编码转换,默认UTF8编码格式
	 * @param content
	 * @param format 编码格式，null默认为UTF8
	 * @return String
	 * @author Damian
	 * @since 2016-12-28
	 */
	public static String decodeStr(String content, String format){
		String respStr = null;
		if(StringUtils.isNotEmpty(content)&&StringUtils.isNotEmpty(format)){
			if(StringUtils.isEmpty(format)){
				format = UTF8;
			}
			respStr = new String(content.getBytes(), Charset.forName(format));
		}
		return respStr;
	}

	/**
	 * 获取Map中的value
	 * @param map
	 * @param key
	 * @return String
	 */
	public static String getValTrim(Map<String,String> map, String key){
		String val = null;
		if(!CollectionUtils.isEmpty(map)&&StringUtils.isNotEmpty(key)){
			val = map.get(key);
			if(StringUtils.isNotEmpty(val)){
				val = val.trim();
			}
		}
		return val;
	}
}













