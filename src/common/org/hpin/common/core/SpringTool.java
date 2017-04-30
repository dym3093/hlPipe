package org.hpin.common.core;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 封装spring上下文,取得bean实例的工具类
 * 
 * @author thinkpad
 * @Apr 14, 2009
 */
public class SpringTool {

	private static final Logger logger = LoggerFactory
			.getLogger(SpringTool.class);

	private static ApplicationContext wac = null;// spring上下文

	private static SpringTool instance;

	/**
	 * 取得平台静态实例的引用
	 * 
	 * @return 平台的静态实例
	 */
	public static SpringTool initInstance(ServletContext servletContext) {
		if (instance == null) {
			instance = new SpringTool();
			SpringTool.wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(servletContext);
		}
		return instance;
	}

	/**
	 * 根据Spring地址加载配置文件
	 * 
	 * @param ApplicationContextUrl
	 * @return
	 */
	public static SpringTool initInstance(String ApplicationContextUrl) {
		if (instance == null) {
			instance = new SpringTool();
			SpringTool.wac = new ClassPathXmlApplicationContext(
					ApplicationContextUrl);
		}
		return instance;
	}

	/**
	 * 取得一个bean实例
	 * 
	 * @param beanId
	 *            bean的标识符
	 * @return bean实例
	 */
	public static Object getBean(String beanId) {
		Assert.notNull(instance, "SpringTool未在系统加载时初始化");
		Object object = null;
		try {
			object = wac.getBean(beanId);
		} catch (BeansException e) {
			logger.error("spring Bean对象无法获取，请核实！", e);
		}
		return object;
	}

	/**
	 * 取得bean的一个实例，必须确保bean的ID命名规范符合类全路径名称或者接口名称
	 * 
	 * @param clazz
	 *            获取Bean实例的类型
	 * @return bean的实例
	 */
	public static Object getBean(Class clazz) {
		Assert.notNull(instance, "SpringTool未在系统加载时初始化");
		Object object = getBean(clazz.getName());
		return object;
	}

}
