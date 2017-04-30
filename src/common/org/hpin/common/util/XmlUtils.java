package org.hpin.common.util;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.XMLWriter;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.dao.ErpPreCustomerDao;
import org.hpin.webservice.foreign.DownloadThread;
import org.hpin.webservice.service.hk.ErpPreCustomerService;
import org.hpin.webservice.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class XmlUtils {

	private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);
	/**
	 * 读配置文件转换为map
	 * @param xmlPath
	 * @return
	 */
	public static Map<String,String> readXml2Map(String xmlPath){
		Document document = XmlDom4j.loadXML(xmlPath);
		Element root = document.getRootElement();
		List<Element> resultElement = root.elements();//值结点
		Map<String,String> keyValue = new HashMap<String,String>();
		for(Element e:resultElement){
			String nodeName = e.getName();
			String method = e.attributeValue("method");
			keyValue.put(nodeName, method);
		}
		return keyValue;
	}
	
	/**
	 * 改变xml文件中的日期
	 * @param xmlPath
	 * @param now
	 */
	public static void changDate(String xmlPath,String now,String xmlCode){
		
		logger.info("XmlUtils中，changeDate传递进来的xmlPath是：" + xmlPath) ;
	    Document document = XmlDom4j.loadXML(xmlPath);
		System.out.println("start xml date........................................................");
		Element root = document.getRootElement();
		System.out.println("xml加载根节点：" + root.getName()) ;
		System.out.println("start xml date root........................................................");
		System.out.println("*********************************分隔符*************************************") ;
		System.out.println("*********************************分隔符*************************************") ;
		try{
			System.out.println("第一层:::::::::::::" + root.element("MsgData").getName()) ;
		}catch(Exception e){
			logger.error("读取<MsgData>节点失败！==============================") ;
		}
		
		Element resultElement = root.element("MsgData").element("Transaction").element(xmlCode).element("HL_DATE");
		System.out.println("start xml date resultElement........................................................");
		resultElement.setText(now);
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileOutputStream(xmlPath));
			writer.write(document);
			System.out.println("start xml write document date........................................................");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 类型转换
	 * @param type
	 * @return
	 */
	public static Class parseType(String type){
		Class aa = null;
		if(StringUtils.isNotBlank(type)){
			if("String".equals(type)){
				aa =  String.class;
			}
			if("Integer".equals(type)){
				aa =  Integer.class;
			}
			if("Double".equals(type)){
				aa =  Double.class;
			}
			if("Date".equals(type)){
				aa =  Date.class;
			}
			if("Long".equals(type)){
				aa = Long.class;
			}
		}
		return aa;
	}
	
	/**
	 * 解析xml为对象
	 * @param dataXmlPath
	 * @param fieldXmlPath
	 * @param a
	 * @param dataRoot
	 */
	public static List<Object> parseXml2Object(String dataXmlPath,String fieldXmlPath,Class a,String dataRoot){
		List<Object> entityList = new ArrayList<Object>();
		logger.info("XmlUtils中，parseXml2Object传递进来的xmlPath是：" + dataXmlPath) ;
		Document document = XmlDom4j.loadXML(dataXmlPath);
		Element root = document.getRootElement();
		Element msgElement = root.element("MsgData"); //获得MsgData结点
		List<Element> traElementList = msgElement.elements("Transaction");
		for(Element traElement:traElementList){
			Element hlElement = traElement.element(dataRoot);
			Object entity = null;
			try {
				entity = a.newInstance();
				//读配置文件
				Map<String,String> fieldMap = readXml2Map(fieldXmlPath);
				Set<String> field = fieldMap.keySet();
				Iterator<String> it = field.iterator();
				System.out.println("field值是："+fieldMap.size());
				while(it.hasNext()){
					String fieldName =  it.next();//字段名称
					System.out.println("===="+fieldName);
					Element e = hlElement.element(fieldName);
					Object value = e.getTextTrim(); 
					if(StringUtils.isBlank(value.toString())){
						continue;
					}
					String type = fieldMap.get(fieldName).split("_")[0];
					String method = fieldMap.get(fieldName).split("_")[1];
					System.out.println("tpye===="+type+" method=="+method);
					value = ReflectionUtils.convertStringToObject(value, parseType(type));
					System.out.println("value======"+value);
					//反射调用方法
					a.getMethod(method,new Class[]{parseType(type)}).invoke(entity, new Object[]{value});
					System.out.println(fieldName+"的值是："+value);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("出错信息：======================== " + e1.getMessage()) ;
				logger.error("XmlUtils --------> 出错啦！");
			}
			entityList.add(entity);
		}
		return entityList;
	}
	
	/**
	 *
	 * @param xml xml字符串
	 * @param parent 查找属性所在的一级根目录
	 * @param destAttr 属性名
	 * @return Map
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-6-23 下午5:27:11
	 */
	public static Map<String,String> fetchXmlValue(String xml, String parent, String[] destAttr) throws Exception{
		Map<String,String> result = null;
		if(null!=xml&&xml.trim().length()>0){
			result = new LinkedHashMap<String, String>();
			   if(!StringUtils.containsIgnoreCase(xml, "root")){
				   if(xml.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")){
					   xml = xml.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "<root>")+"</root>";
				   }else if(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
					   xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<root>")+"</root>";
				   }else{
					   xml = "<root>"+xml+"</root>";
				   }
			   }else{
				   if(xml.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")){
					   xml = xml.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
				   }else if(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
					   xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
				   }
			   }
			 System.out.println("new xml: "+xml);
	         Document doc = DocumentHelper.parseText(xml);
		     Element root = doc.getRootElement();  
			 Element eParent = root.element(parent);
			 for (int i = 0; i < destAttr.length; i++) {
				 Element eDest = eParent.element(destAttr[i]);
				 if(eDest != null) {
					 result.put(destAttr[i], eDest.getStringValue().trim());
				 } else {
					 result.put(destAttr[i], null);
					 
				 }
			}
		}
		return result;
	}
	
	/**
	 * 根据XML文件名和XPath表达式查找对应的元素内的文本内容，有多个元素时以逗号分隔
	 * @param xmlName
	 * @param xPath
	 * @return String
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @author Damian
	 * @since 2017-03-07
	 */
	public static String getSingleTxt(String xmlName, String xPath) throws FileNotFoundException, DocumentException {
		String txt = null;
		if (StringUtils.isNotEmpty(xmlName) && StringUtils.isNotEmpty(xPath)) {
			List<String> txtList = getTextByXPath(xmlName, xPath);
			if (!CollectionUtils.isEmpty(txtList)) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < txtList.size(); i++) {
					buffer.append(txtList.get(i) + ",");
				}
				txt = buffer.toString().substring(0, buffer.toString().length() - 1);
			}
		}
		return txt;
	}

	/**
	 * 根据XML文件名和XPath查找对应的元素的文本内容
	 *
	 * @param xmlName
	 * @param xPath
	 * @return List
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @author Damian
	 * @since 2017-03-07
	 */
	public static List<String> getTextByXPath(String xmlName, String xPath) throws FileNotFoundException, DocumentException {
		List<String> txtList = null;
		if (StringUtils.isNotEmpty(xmlName) && StringUtils.isNotEmpty(xPath)) {
			List<Element> elementList = getElementsByXPath(xmlName, xPath);
			if (!CollectionUtils.isEmpty(elementList)) {
				txtList = new ArrayList<String>();
				for (Element element : elementList) {
					txtList.add(element.getTextTrim());
				}
			}
		}
		return txtList;
	}

	/**
	 * 根据XML文件名和XPath查找对应的元素
	 *
	 * @param xmlName
	 * @param xPath
	 * @return List
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @author Damian
	 * @since 2017-03-07
	 */
	public static List<Element> getElementsByXPath(String xmlName, String xPath)
			throws FileNotFoundException, DocumentException {
		List<Element> elementList = null;
		if (StringUtils.isNotEmpty(xmlName) && StringUtils.isNotEmpty(xPath)) {
			if (xmlName.indexOf(".xml") < 0) {
				xmlName = xmlName + ".xml";
			}
			//TODO 速度太慢，需要优化 mark by Damian 2017-03-11
			List<File> fileList = FileUtil.findFile(xmlName);
			if (!CollectionUtils.isEmpty(fileList)) {
				//同名情况下，只取第一个
				File xFile = fileList.get(0);
				Document document = XmlDom4j.parse(xFile);
				List<Node> nodeList = document.selectNodes(xPath);
				if (!CollectionUtils.isEmpty(nodeList)) {
					elementList = new ArrayList<Element>();
					for (Node node : nodeList) {
						if (node instanceof Element) {
							elementList.add((Element) node);
						}
					}
				} else {
					throw new XPathException("Can not found elements by XPath: " + xPath);
				}
			} else {
				throw new FileNotFoundException("Can not found file by fileName: " + xmlName);
			}
		}
		return elementList;
	}

	public static void main(String[] args){
	}
}
