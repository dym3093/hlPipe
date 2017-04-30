package org.hpin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * dom4j工具类
 * 
 * @author thinkpad
 * 
 */
public class XmlDom4j {
	
	private static Logger logger = Logger.getLogger(XmlDom4j.class) ;

	private static Document document = null;

	private static SAXReader saxReader = null;

	/**
	 * 读取XML文件
	 */
	public static Document loadFile(String fileName) {
		try {
			// 从类路径读取配置文件。
			ClassLoader cl = XmlDom4j.class.getClassLoader();
			// 加载数据字典分类初始化文件
			InputStream stream = cl.getResourceAsStream(fileName);
			SAXReader reader = new SAXReader();
			document = reader.read(stream);
			stream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	
	/**
	 * 加载xml文件
	 * @param filePath　绝对路径
	 * @return
	 */
	public static Document loadXML(String filePath) {
		System.out.println("    ") ;
		logger.info("XmlDom4j类加载xml文件的filePath为:" + filePath) ;
		System.out.println("    ") ;
		SAXReader reader = new SAXReader();
		FileInputStream fis = null ;
		try {
			File file = new File(filePath) ;
			fis = new FileInputStream(file) ;
			System.out.println("file的路径----->" + file.getPath()) ;
		}catch(Exception exc){
			exc.printStackTrace() ;
			logger.error("XmlDom4j根据filePath" + filePath + "创建文件失败！" ) ;
		}
		try{	
			document = reader.read(fis);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("XmlDom4j类中SAXReader读取路径为-->" + filePath + "的文件" + new File(filePath).getName() + "失败============\n\n") ;
		}finally{
			try {
				fis.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	/**
	 * 取得Root节点
	 * 
	 * @param doc
	 * @return
	 */
	public static Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	public static SAXReader getSaxReader() {
		synchronized (XmlDom4j.class){
			if (saxReader==null){
				saxReader = new SAXReader();
			}
		}
		return saxReader;
	}
	/**UPDATE_USER_NAME
	 * 读取XML文件
	 * @param xmlFile
	 * @return Document
	 * @throws DocumentException
	 * @since 2017-03-07
	 * @author Damian
	 */
	public static Document parse(File xmlFile) throws DocumentException {
		synchronized (XmlDom4j.class) {
			if (xmlFile != null) {
				document = getSaxReader().read(xmlFile);
			}
		}
		return document;
	}
}
