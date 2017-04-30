package org.hpin.webservice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Dom4jDealUtil {
	private static Logger log = Logger.getLogger("pushCustomerGenerCode");

	/**
	 * create by henry.xu; 20170208
	 * @description 将xml字符串转换成map, 该方法使用与所有一层结构的xml字符串解析;
	 * @param xml
	 * @return Map
	 */
	public static Map<String, String> readStringXmlOut(String xml) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Document doc = null;
		try {
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml); 
			// 获取根节点
			Element rootElt = doc.getRootElement(); 

			// 获取根节点下的子节点head
			Iterator<?> iter = rootElt.elementIterator();
			while (iter.hasNext()) {
				Element recordEless = (Element) iter.next();
				resultMap.put(recordEless.getName(), recordEless.getText());

			}

		} catch (Exception e) {
			log.error("Dom4jDealUtil.readStringXmlOut方法,xml字符串处理异常!", e);
		}

		return resultMap;
	}

	/**
	 * create by henry.xu; 20170208
	 * 注意跟上面的方法的区别, 该方法可以包含上面的readStringXmlOut方法, 不懂慎用
	 * @description 将xml字符串转换成map, 该方法使用与所有两层结构的xml字符串解析;
	 * 如<root><a>1</a><b><a>1</a><a>1</a></b></root>
	 * @param xml, elementsName传入需要进入的节点,以及等级.1为默认及为空的情况或者不存在; 2标示有子节点的子节点, 3.....后面出现4,可以自己加;
	 * map{param, "**"}map{level, "2"}
	 * @return Map
	 */
	public static Map<String, Object> readStringXml2Out(String xml, List<Map<String, String>> elementsName) {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Document doc = null;
		try {
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml); 
			// 获取根节点
			Element rootElt = doc.getRootElement(); 
			// 获取根节点下的子节点
			Iterator<?> iter = rootElt.elementIterator();
			List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
			//开始遍历
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				
				//当存在要分级的情况进入判断;
				if(elementsName != null && elementsName.size() > 0 ) {
					
					for(Map<String, String> mapstr : elementsName) {
						String level = mapstr.get("level"); //等级
						String params = mapstr.get("params"); //进入的节点
						if(params.equals(recordEle.getName())){
							//获取指定节点下的子节点;
							Iterator<?> levelIter = recordEle.elementIterator();
							if("2".equals(level)) {
								Map<String, String> mapLevel = new HashMap<String, String>();
								while(levelIter.hasNext()) {
									Element level2Ele = (Element) levelIter.next();
									mapLevel.put(level2Ele.getName(), level2Ele.getText());
								}
								resultMap.put(recordEle.getName(), mapLevel);
							} else if("3".equals(level)) {
								List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
								Map<String, String> mapLevel3 = null;
								while(levelIter.hasNext()) {
									Element level3Ele = (Element) levelIter.next();
									Iterator<?> levelItemIter = level3Ele.elementIterator();
									mapLevel3 = new HashMap<String, String>();
									while(levelItemIter.hasNext()) {
										Element levelItemEle = (Element) levelItemIter.next();
										mapLevel3.put(levelItemEle.getName(), levelItemEle.getText());
									}
									
									listMap.add(mapLevel3);
								}
								
								resultMap.put(recordEle.getName(), listMap);
							}
						}
					}
					
				} else {
					resultMap.put(recordEle.getName(), recordEle.getText());
				}
			}
			
			if(listMaps != null && listMaps.size() > 0) {
				resultMap.put("lists", listMaps);
			}
				
		} catch (Exception e) {
			log.error("Dom4jDealUtil.readStringXml2Out方法,xml字符串处理异常!", e);
		}

		return resultMap;
	}

	/*public static void main(String[] args) {
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();
		
		map1.put("params", "validateInfo");
		map1.put("level", "2");
		
		map2.put("params", "customerList");
		map2.put("level", "3");
		list.add(map1);
		list.add(map2);
		System.out.println(readStringXml2Out("<?xml version=\"1.0\" encoding=\"utf-8\"?><reqInfo><validateInfo><accountName>shenyou</accountName><password>shenyou</password></validateInfo><customerList><customer><code>条形码</code><name>姓名</name><sex>性别</sex><phone>手机号</phone><setmealName>套餐名称</setmealName><batchNo>批次号</batchNo><receiveDate>收样日期</receiveDate><reportDate>报告发布日期</reportDate></customer><customer><code>条形码</code><name>姓名</name><sex>性别</sex><phone>手机号</phone><setmealName>套餐名称</setmealName><batchNo>批次号</batchNo><receiveDate>收样日期</receiveDate><reportDate>报告发布日期</reportDate></customer></customerList></reqInfo>", list));
	}*/
}
