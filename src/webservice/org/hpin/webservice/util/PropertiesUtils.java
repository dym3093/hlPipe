/**
 * @author DengYouming
 * @since 2016-7-29 下午9:52:59
 */
package org.hpin.webservice.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * @author DengYouming
 * @since 2016-7-29 下午9:52:59
 */
public class PropertiesUtils {

	private static Properties prop;

	/**
	 * @description 加载配置文件
	 * @author YoumingDeng
	 * @since: 2016/12/21 3:23
	 */
	public static Properties loadProp(String propName){
		List<File> fileList = Tools.findFile(propName);
		File propFile = null;
		if(fileList!=null&&fileList.size()>0){
			propFile = fileList.get(0);
			try {
				InputStream is = new FileInputStream(propFile);
				if(is!=null) {
					BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
					prop = new Properties();
					prop.load(br);
					if(br!=null){
						br.close();
					}
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * @description 获取配置文件中相关key的String值
	 * @param propName 配置文件名称，可以不包含后缀
	 * @param key
	 * @author YoumingDeng
	 * @since: 2016/12/21 3:23
	 */
	public static String getString(String propName, String key){
		String value = null;
		if(StringUtils.isNotEmpty(propName)&& StringUtils.isNotEmpty(key)){
			if(!propName.contains(".properties")){
				propName = propName+".properties";
			}
			if(prop!=null){
				value = prop.getProperty(key);
				if(StringUtils.isEmpty(value)){
					loadProp(propName);
				}
			}else{
				loadProp(propName);
			}
			if(StringUtils.isEmpty(value)) {
				value = prop.getProperty(key);
			}
		}
		return value;
	}

	/**
	 * @description 获取配置文件中的值，并转换成对应的Integer值
	 * @author YoumingDeng
	 * @since: 2016/12/21 3:25
	 */
	public static Integer getInt(String propName, String key){
		Integer value = null;
		String tempStr = getString(propName, key);
		if(StringUtils.isNotEmpty(tempStr)){
			value = Integer.valueOf(tempStr);
		}
		return value;
	}
}
