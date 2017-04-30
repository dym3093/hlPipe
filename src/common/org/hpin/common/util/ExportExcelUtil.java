package org.hpin.common.util;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *<p>@Desc：导出Excel辅助工具包</p> 
 *
 *<p>@author : 胡五音</p>
 *
 *<p>@CreateDate：Dec 14, 2011 3:40:54 PM</p>
 *<p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>  
 */

public class ExportExcelUtil {

	private static HSSFWorkbook wb ;
	private static HSSFSheet sheet ;
	
	public ExportExcelUtil(){
		
	}
	
	private static ExportExcelUtil instance ;
	
	/**
	 * 单例，获取工具包实例
	 * @return
	 */
	public static ExportExcelUtil getInstance(){
		if(instance == null){
			instance = new ExportExcelUtil() ;
		}
		return instance ; 
	}
	
	/**
	 * 创建WorkBook工作簿
	 * @return
	 */
	public static HSSFWorkbook createHSSFWorkbook(){
		wb = new HSSFWorkbook() ;
		return wb ;
	}
	
	/**
	 * 创建sheet工作表
	 * @return
	 */
	public static HSSFSheet getHSSFSheet(){
		if(wb == null) createHSSFWorkbook() ;
		sheet = wb.createSheet() ;
		return sheet ;
	}
	
	/**
	 * 创建Sheet工作表，并根据参数设置统一表头格式
	 * @param titleArray
	 * @return
	 */
	public static HSSFSheet getHSSFSheet(String[] titleArray , List objList , Class clazz){
		sheet = getHSSFSheet() ;
		int columnCount = titleArray == null ? 0 : titleArray.length ;
		// 给工作表列定义列宽(实际应用自己更改列数)
		for (int i = 0; i < columnCount; i++) {
			sheet.setColumnWidth((short)i, (short)3000);
		}
		
		//创建第一行的单元格格式
		HSSFCellStyle cellStyleOfRow1 = wb.createCellStyle();
		cellStyleOfRow1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyleOfRow1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyleOfRow1.setWrapText(false);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 250);
		cellStyleOfRow1.setFont(font);
		
		if(titleArray != null){
			HSSFRow row = sheet.createRow(0) ;
			HSSFCell[] cell = new HSSFCell[columnCount]  ;
			for(int i = 0 ; i < columnCount ; i ++){
				cell[i].setCellStyle(cellStyleOfRow1) ;
				cell[i].setCellValue(titleArray[i]) ;
			}
			//数据
			for (int j = 0; j< objList.size(); j++) {
				 HSSFRow _row  = sheet.createRow(j+1);
				 Object[] obj = (Object[])objList.get(j);
				 HSSFCell _cell = _row.createCell(columnCount); 
				 _cell.setCellValue(String.valueOf(obj[j]));
			}
			try {
				FileOutputStream fout = new FileOutputStream("E:/产品会员错误数据.xls");
				wb.write(fout);
				fout.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return sheet ;
	}
	
}
