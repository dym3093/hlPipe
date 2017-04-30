package org.hpin.webservice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hpin.webservice.bean.ErpReportdetailPDFContent;
import org.hpin.webservice.bean.PreSalesMan;

/**
 * 处理接口中相关返回字符串使用;
 * 具体方法最好指明那些地方在使用;避免混淆
 * @description: 
 * create by henry.xu 2017年2月10日
 */
public class ReturnStringUtils {
	private static final String PDF_HTTP_PATH = "http://img.healthlink.cn:8099/ymReport/";
	private static final String PDF_STATIC_PATH = "/home/ftp/transact/";
	
	/**
	 * 接口报告查询返回xml拼接;
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年4月12日
	 */
	public static String geneReportInfoResult(String identityStatus, String code, 
			String reportStatus, List<ErpReportdetailPDFContent> pdfContents) {
		StringBuilder resultXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		
		resultXml.append("<response>");
		resultXml.append("<identityStatus>"+identityStatus+"</identityStatus>");
		resultXml.append("<code>"+code+"</code>");
		resultXml.append("<reportStatus>"+reportStatus+"</reportStatus>");
		resultXml.append("<pdfList>");
		
		if(pdfContents != null && pdfContents.size() > 0) {
			for(ErpReportdetailPDFContent pdf : pdfContents) {
				resultXml.append("<pdfPath>"+pdf.getFilepath().replace(PDF_STATIC_PATH, PDF_HTTP_PATH)+"</pdfPath>");
			}
		}
		
		resultXml.append("</pdfList>");
		resultXml.append("</response>");

		return resultXml.toString();
	}
	
	/**
	 * 报告客户信息返回字符串处理;
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年2月23日
	 */
	public static String reportCustomerInfoResult(String flag, String message) {
		StringBuilder resultXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		resultXml.append("<result>");
		resultXml.append("<flag>"+flag+"</flag>"); //（0：失败  1：成功）
		resultXml.append("<message>"+message+"</message>"); //（失败原因）
		resultXml.append("</result>");
		return resultXml.toString();
	}
	
	/**
	 * 调用方法verifySalesMan中返回字符串使用;
	 * 方便统一维护;以及抽取;
	 * create by henry.xu 2017年2月10日
	 * @return
	 */
	public static String validateWCString(PreSalesMan preSalesMan, String saCha) {
		StringBuilder resultXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><respStatus>");
		String result = "";//1为成功0为失败
		String message = "";
		String companyId = "";
		String companyName = "";
		String ownerCompanyId = "";
		String ownedCompanyName = "";
		String projectType = "";
		String salesManName = "";
		String salesManNo = "";
		String salesChannel = saCha;
		
		if(preSalesMan != null) {
			result = "1";
			companyId = preSalesMan.getYmCompanyId();
			companyName = preSalesMan.getYmCompany();
			ownerCompanyId = preSalesMan.getYmOwncompanyId();
			ownedCompanyName = preSalesMan.getYmOwncompany();
			salesManName = preSalesMan.getSalesman();
			salesManNo = preSalesMan.getEmployeeNo();
			//salesChannel = preSalesMan.getMark();
			String lenStr = "";
			if(StringUtils.isNotEmpty(salesChannel)) {
				int len = salesChannel.length();
				lenStr = salesChannel.substring(len-2, len);
			}
			
			if("wc".equals(lenStr)) { //河北微磁
				projectType = "PCT_005";
			} else if("wd".equals(lenStr)) { //河北生物电;
				projectType = "PCT_004";
			}
			
		} else {
			result = "0";
			
			if("taipingwc".equals(saCha)) {
				message = "输入的代理人信息不存在，请联系您的太平代理人核实其姓名和工号信息！";
				
			} else {
				message = "输入的代理人信息不存在，请核实您填写的信息！";
				
			}
			
		}
		resultXml.append("<result>"+result+"</result>"); //1为成功0为失败
		resultXml.append("<message>"+message+"</message>"); 
		resultXml.append("<companyId>"+companyId+"</companyId>"); 
		resultXml.append("<companyName>"+companyName+"</companyName>"); 
		resultXml.append("<ownerCompanyId>"+ownerCompanyId+"</ownerCompanyId>"); 
		resultXml.append("<ownedCompanyName>"+ownedCompanyName+"</ownedCompanyName>"); 
		resultXml.append("<projectType>"+projectType+"</projectType>"); 
		resultXml.append("<salesManName>"+salesManName+"</salesManName>"); 
		resultXml.append("<salesManNo>"+salesManNo+"</salesManNo>"); 
		resultXml.append("<salesChannel>"+salesChannel+"</salesChannel>"); 
		
		resultXml.append("</respStatus>");
		return resultXml.toString();
	}
	
	/**
	 * 场次号处理
	 * create by henry.xu 20170216
	 * @param num
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getEventsNo(String num, Date date, String batchPre) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("HHmmssSSS");
		String lastNumber = num.substring(num.length() - 3);
		String newNumber = String.valueOf(Integer.parseInt(lastNumber) + 1);
		if (newNumber.length() == 1) {
			newNumber = "00" + newNumber;
		}
		if (newNumber.length() == 2) {
			newNumber = "0" + newNumber;
		}
		String temp = batchPre + sf.format(date) + sf2.format(new Date()) + newNumber;
		return temp;
	}
}
