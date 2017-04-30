package org.hpin.webservice.util;


import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;




public class JaxbXmlBeanUtils {
	public static String bean2Xml(Object bean) {
	   StringWriter sw = new StringWriter();
       try{
		   JAXBContext context = JAXBContext.newInstance(bean.getClass());
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
	        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");       
	        m.marshal(bean,sw);
        } catch (JAXBException e) {
			e.printStackTrace();
		}
          return sw.toString();
	}
	public static Object xml2Bean(String xml,Class className) {
		StringReader sr = new StringReader(xml); 
		Unmarshaller um;
		try{
           JAXBContext context = JAXBContext.newInstance(className);
            um =context.createUnmarshaller();    
            return (Object)um.unmarshal(sr);
		  } catch (JAXBException e) {
				e.printStackTrace();
		 }
		return null;
	} 
   public static void main(String[] args) {
	   //test 目标目录    
//	   String  source = "F:\\ftp\\test1\\123.xlsx";
//	   File sourcefiles = new File(source); 
//	   String  dest = "F:\\ftp\\test\\123.xlsx";
//	   File destfiles = new File(dest); 
//	   String date=StaticMehtod.getYYMMDD();
//	   String oldpath=dest.substring(0, dest.lastIndexOf("\\")+1);
//	    String filename=dest.substring(dest.lastIndexOf("\\")+1, dest.length());
//	   if(destfiles.exists()){ 	
//		   String datepath=oldpath+date+"\\"+filename;
//		   File datepathfiles = new File(datepath);   
//		   if(datepathfiles.exists()){ 	
//			   //同天重复循环3次生成子目录
//			   for(int i=1;i<=3;i++){
//				   String datepathI=oldpath+date+String.valueOf(i)+"\\";
//			   }
//		   }else{
//		     try {
//			   FileUtils.copyFile(sourcefiles, datepathfiles);
//		     } catch (IOException e) {
//			   e.printStackTrace();
//		     }
//		   }
//		   
//	   }else{
//		     try {
//			   FileUtils.copyFile(sourcefiles, destfiles);
//		     } catch (IOException e) {
//			   e.printStackTrace();
//		     }
//	   }
//	   System.out.println("------------");
	}
}
