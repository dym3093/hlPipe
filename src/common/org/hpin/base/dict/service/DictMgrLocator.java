package org.hpin.base.dict.service;

import org.hpin.common.core.SpringTool;


/**
 * <p>
 * Title:字典的locator
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 * 
 */
public class DictMgrLocator {
	/**
	 * 获取字典service
	 * 
	 * @return 字典service
	 */
	public static IDictService getDictService() {
		return (IDictService) SpringTool.getBean(
				"DictService");
	}

	/**
	 * 获取将id转换为名称的service
	 * 
	 * @return 将id转换为名称的service
	 */
	public static ID2NameService getId2NameService() {
		return (ID2NameService)SpringTool.getBean(
				"ID2NameGetServiceCatch");
	}
}
