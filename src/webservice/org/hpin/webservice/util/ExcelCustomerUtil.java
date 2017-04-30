package org.hpin.webservice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hpin.common.util.DateUtils;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.dao.CustomerRelationshipDao;

public class ExcelCustomerUtil {
	/**
	 * 配置邮件附件excel生成地址.
	 * 该地址使用测试环境和正式环境,本地如(E:/)
	 */
	public static String excelAddress = "/home/ymdata/"; 
	
	/**
	 * 生成excel
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年2月28日
	 */
	public static File dealExcelCreate(List<ErpCustomer> customersExcel, ErpEvents events, CustomerRelationshipDao shipDao) {
		
		File file = null;
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("客户信息");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 
        
        HSSFCell cell = row.createCell(0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);  
        
        cell = row.createCell(1);  
        cell.setCellValue("采样日期");  
        cell.setCellStyle(style);  
        
        cell = row.createCell(2);  
        cell.setCellValue("客户检测码");  
        cell.setCellStyle(style);  
       
        cell = row.createCell(3);  
        cell.setCellValue("姓名");  
        cell.setCellStyle(style);
        
        cell = row.createCell(4);  
        cell.setCellValue("性别");  
        cell.setCellStyle(style);
        
        cell = row.createCell(5);  
        cell.setCellValue("年龄");  
        cell.setCellStyle(style);
        
        cell = row.createCell(6);  
        cell.setCellValue("身份证号");  
        cell.setCellStyle(style);
        
        cell = row.createCell(7);  
        cell.setCellValue("电话");  
        cell.setCellStyle(style);
        
        cell = row.createCell(8);  
        cell.setCellValue("二维码对应支公司");  
        cell.setCellStyle(style);
        
        cell = row.createCell(9);  
        cell.setCellValue("设备所在支公司");  
        cell.setCellStyle(style);
        
        cell = row.createCell(10);  
        cell.setCellValue("套餐名");  
        cell.setCellStyle(style);
        
        cell = row.createCell(11);  
        cell.setCellValue("营销员");  
        cell.setCellStyle(style);
        
        cell = row.createCell(12);  
        cell.setCellValue("营销员工号");  
        cell.setCellStyle(style);
        
        cell = row.createCell(13);  
        cell.setCellValue("身高");  
        cell.setCellStyle(style);
        
        cell = row.createCell(14);  
        cell.setCellValue("体重");  
        cell.setCellStyle(style);
        
        ErpCustomer customer = null;
        for (int i=0; i<customersExcel.size(); i++) {
        	customer = customersExcel.get(i);
        	row = sheet.createRow(i + 1);
        	row.createCell(0).setCellValue(i+1); //序号
        	row.createCell(1).setCellValue(DateUtils.dateToStr(customer.getSamplingDate(), "yyyy-MM-dd")); //采样日期
        	row.createCell(2).setCellValue(customer.getCode());  //客户检测码
        	row.createCell(3).setCellValue(customer.getName()); //姓名
        	row.createCell(4).setCellValue(customer.getSex()); //性别
        	row.createCell(5).setCellValue(customer.getAge()); //年龄
        	row.createCell(6).setCellValue(customer.getIdno()); //身份证号
        	row.createCell(7).setCellValue(customer.getPhone()); //电话
        	row.createCell(8).setCellValue(customer.getBranchCompany()); //二维码对应支公司
        	String otherCompanyId = customer.getOtherCompanyId();
        	CustomerRelationShip ship = (CustomerRelationShip) shipDao.findById(CustomerRelationShip.class, otherCompanyId);
        	String otherCompanyName = null;
        	if(ship != null) {
        		otherCompanyName = ship.getBranchCommany();
        	}
        	row.createCell(9).setCellValue(otherCompanyName); //设备所在支公司
        	row.createCell(10).setCellValue(customer.getSetmealName()); //套餐名
        	row.createCell(11).setCellValue(customer.getSalesMan()); //营销员
        	row.createCell(12).setCellValue(customer.getSalesManNo()); //营销员工号
        	row.createCell(13).setCellValue(customer.getHeight()); //身高
        	row.createCell(14).setCellValue(customer.getWeight()); //体重
        	
		}
        try {
        	//批次号-采样日期-支公司-远盟对接人-人数.xls
        	String filePath =  excelAddress + events.getBatchNo()+"-"+ DateUtils.dateToStr(customer.getSamplingDate(), "yyyyMMdd") + "-" + events.getBranchCompany() + "-" + events.getYmSalesman() + "-" + events.getHeadcount() + ".xls" ;   
        	FileOutputStream fout = new FileOutputStream(filePath); 
			wb.write(fout);
			
			file = new File(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return file;
	}
	
	public static void deleteFile(List<File> files) {
		if(files != null && files.size() > 0) {
			for (File file : files) {
				// 路径为文件且不为空则进行删除  
				if (file.isFile() && file.exists()) {  
					file.delete();  
				}  
			}
		}
	}
}
