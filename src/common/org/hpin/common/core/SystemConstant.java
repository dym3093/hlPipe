package org.hpin.common.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.util.Assert;

/**
 * 基础系统静态常量类
 * 
 * @author thinkpad
 * @Apr 26, 2010
 */
public class SystemConstant {

	public static Properties constants = null;// 系统变量存放区
	
	public static Properties logProperties = new Properties();// 日志Key集合

	public static String sys_resources = null;// 系统资源字符串

	/**
	 * 获取系统变量
	 * 
	 * @param constantName
	 *            属性名称
	 * @return
	 */
	public static String getSystemConstant(String constantName) {
		String property = null;
		if (null == constants) {
			initSystemProperties();
		}
		property = constants.getProperty(constantName);
		Assert.notNull(property, "系统变量" + constantName + "不存在");
		return property;
	}
	
	/**
	 * 初始化系统属性文件
	 */
	private static void initSystemProperties() {
		constants = new Properties();
		ClassLoader cl = SystemConstant.class.getClassLoader();
		// 加载数据字典分类初始化文件
		InputStream stream = cl.getResourceAsStream("system.properties");
		try {
			constants.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
