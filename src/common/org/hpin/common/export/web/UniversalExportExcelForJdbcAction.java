package org.hpin.common.export.web;

import static org.apache.poi.hssf.usermodel.HSSFCell.ENCODING_UTF_16;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.ObjectUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;
import org.hpin.common.util.StrUtils;
import org.hpin.common.widget.pagination.Page;
import org.hpin.common.widget.web.bo.ExportExcelAssistForJdbcVo;



/**
 * <p>@desc : 通用导出Excel组件的JDBC版本Action</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-8-31 上午10:28:51</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */
@Namespace("/system")
@Action("universalExportExcelForJdbc")
@Results({
	@Result(name = "success", 
			type = "stream", 
			params = {	"contentType" , "application/vnd.ms-excel" , 
						"inputName" , "universalExcel" , 
						"contentDisposition" , "attachment;filename=\"${fileName}\"" , 
						"bufferSize" , "40960"
					 }
			)
})
public class UniversalExportExcelForJdbcAction extends BaseAction {
	
	private List<ExportExcelAssistForJdbcVo> excelJdbcVoList ;
	
	private String fileName ;
	
	private final String QUERY_METHOD = "queryResults" ;
	
	private final String ID_TO_NAME_METHOD = "id2Name" ;
	
	public String getFileName() {
		try{
			return new String(fileName.getBytes() , "ISO8859-1") ;
		}catch(Exception e){
			return this.fileName ;
		}
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName ;
	}
	
	public List<ExportExcelAssistForJdbcVo> getExcelJdbcVoList() {
		return excelJdbcVoList ;
	}
	
	public void setExcelJdbcVoList(List<ExportExcelAssistForJdbcVo> excelJdbcVoList) {
		this.excelJdbcVoList = excelJdbcVoList ;
	}

	@Override()
	public String execute() throws Exception{
		Map map = buildSearch() ;
		String oldAction = HttpTool.getParameter("oldAction") ;
		String methodName = HttpTool.getParameter("methodName" , QUERY_METHOD) ;
		String fileName = HttpTool.getParameter("fileName");
		if(StrUtils.isNotNullOrBlank(fileName)&&"stat33".equals(fileName)){
			fileName = fileName+"-"+HttpTool.getParameter("filter_and_hylType_EQ_S") ;
		}
		if(StrUtils.isNotNullOrBlank(fileName)&&"stat54".equals(fileName)){
			fileName = fileName+"-"+HttpTool.getParameter("filter_and_type_EQ_S") ;
		}
		//String viewName = HttpTool.getParameter("viewName") ;
		//String tempTableName = HttpTool.getParameter("tempTableName") ;
		String configId = HttpTool.getParameter("configId");
		String field=HttpTool.getParameter("field");
		
		if(StrUtils.isNotNullOrBlank(configId)&&"stat33".equals(configId)){
			configId = configId+"-"+HttpTool.getParameter("filter_and_hylType_EQ_S") ;
		}
		if(StrUtils.isNotNullOrBlank(configId)&&"stat54".equals(configId)){
			configId = configId+"-"+HttpTool.getParameter("filter_and_type_EQ_S") ;
		}
		this.setFileName(fileName) ;
		
		Class actionClazz = Class.forName(oldAction) ;
		Object actionObj = actionClazz.newInstance() ;
		Method[] methods = actionClazz.getDeclaredMethods() ;
		Object obj = null ;
		page = new Page(HttpTool.getPageNum() , HttpTool.getPageSize());
		List valueList = new ArrayList() ;
		/*String sql = GenerateParamsUtils.generateParamsSql(map , viewName , valueList) ;
		boolean flag = false ; 	//标示是否是存储过程，默认为否
		if(StringUtils.isNotBlank(tempTableName) && !"null".equals(tempTableName)){
			sql = "select * from " + tempTableName ;
			valueList.clear() ;
			flag = true ;
		}*/
		
		Map searchMap = buildSearch() ;
		
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				if(StringUtils.isBlank(field) || "null".equals(field)){
					obj = method.invoke(actionObj , new Object[]{page , configId , searchMap}) ;
				}else{
					obj = method.invoke(actionObj , new Object[]{page , configId , field , searchMap}) ;
				}
				break ;
			}
		}
		page = (Page)obj ;
		long count = page.getTotalCount() ;
		page.setPageSize((int)count) ;
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				if(StringUtils.isBlank(field) || "null".equals(field)){
					obj = method.invoke(actionObj , new Object[]{page , configId , searchMap}) ;
				}else{
					obj = method.invoke(actionObj , new Object[]{page , configId , field , searchMap}) ;
				}
				break ;
			}
		}
		page = (Page)obj ;
		fileName = HttpTool.getParameter("fileName");
		configId = HttpTool.getParameter("configId");
		return SUCCESS ;
	}
	
	/**
	 * 创建标题style
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle createTitleStyle(HSSFWorkbook wb){
		HSSFCellStyle styleTitle = wb.createCellStyle() ;
		styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER) ;
		styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER) ;
		styleTitle.setWrapText(false) ;
		styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND) ;
		styleTitle.setFillForegroundColor(HSSFColor.BLACK.index) ;
		HSSFFont fontTitle = wb.createFont() ;
		fontTitle.setFontName("宋体") ;
		fontTitle.setFontHeight((short)200) ;
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD) ;
		fontTitle.setColor(HSSFColor.WHITE.index) ;
		styleTitle.setFont(fontTitle) ;
		styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN) ;
		styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN) ;
		styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN) ;
		styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN) ;
		return styleTitle ;
	}
	
	/**
	 * 创建内容style
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle createContentStyle(HSSFWorkbook wb){
		HSSFCellStyle styleContent = wb.createCellStyle() ;
		styleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER) ;
		styleContent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER) ;
		styleContent.setWrapText(true);
		HSSFFont fontContent = wb.createFont() ;
		fontContent.setFontName("宋体") ;
		styleContent.setFont(fontContent) ;
		styleContent.setBorderBottom(HSSFCellStyle.BORDER_THIN) ;
		styleContent.setBorderLeft(HSSFCellStyle.BORDER_THIN) ;
		styleContent.setBorderRight(HSSFCellStyle.BORDER_THIN) ;
		styleContent.setBorderTop(HSSFCellStyle.BORDER_THIN) ;
		return styleContent ;
	}
	
	/**
	 * 获取输出流，发送给浏览器，提供下载
	 * @return
	 * @throws Exception
	 */
	public InputStream getUniversalExcel() throws Exception{
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet() ;
		
		int number = excelJdbcVoList.size() + 1 ;//这个位置抛的
		String[] title = new String[number] ;
		title[0] = "序号" ;
		for(int i = 0 ; i < excelJdbcVoList.size() ; i ++){
			title[i + 1] = excelJdbcVoList.get(i).getColumnChName() ;
		}
		
		ExportExcelAssistForJdbcVo[] voArray = new ExportExcelAssistForJdbcVo[excelJdbcVoList.size()] ;
		for(int i = 0 ; i < excelJdbcVoList.size() ; i ++){
			voArray[i] = excelJdbcVoList.get(i) ;
		}
		
		/*
		 *	设置整体列宽为5000 
		 */
		for(int i = 0 ; i < number ; i ++){
			sheet.setColumnWidth(i , (short)5000) ;
		}
		
		HSSFCellStyle titleStyle = createTitleStyle(wb) ;
		HSSFCellStyle contentStyle = createContentStyle(wb) ;
		
		/*
		 * 创建表头
		 */
		int c = 0 ;
		HSSFRow row = sheet.createRow(c) ;
		HSSFCell[] cell = new HSSFCell[number] ;
		for(int i = 0 ; i < number ; i ++){
			cell[i] = row.createCell((short)i) ;
		}
		for(int i = 0 ; i < number ; i ++){
			cell[i].setCellStyle(titleStyle) ;
			cell[i].setCellValue(title[i]) ;
		}
		
		/*
		 * 创建表格数据主体
		 */
		List result = page.getResults() ;
		for(int i = 0 ; i < result.size() ; i ++){
			//创建行
			HSSFRow _row = sheet.createRow(1 + i) ;

			//创建第一个单元格“序号”
			HSSFCell cell0 = _row.createCell(0) ;
			cell0.setCellStyle(contentStyle) ;
			cell0.setCellType(ENCODING_UTF_16) ;
			cell0.setCellValue(i + 1) ;
			
			Object[] array = (Object[])result.get(i) ;
			
			/*
			 * 此循环中代码构成：
			 * 1、集合中有多少数据，导出的excel中数据部分就有多少行，所以第一部分代码是创建excel的row
			 * 2、每一行每个单元格，都需要有数据填充，填充步骤：
			 * 		a、根据当前列表集合中的单个对象，反射获取所有的method
			 * 		b、根据前台获取的配置信息，参考所有的method的name，如果匹配，则获取值，填充至表格中
			 */
			
			for(int j = 0 ; j < voArray.length ; j ++){
				//创建第i个单元格
				HSSFCell celli = _row.createCell(j + 1) ;
				celli.setCellStyle(contentStyle) ;
				celli.setCellType(ENCODING_UTF_16) ;
				//celli.setCellValue(getRealValue(methods , voArray[j] , o) == null ? "" : getRealValue(methods , voArray[j] , o).toString()) ;
				//celli.setCellValue(array[voArray[j].getColumnIndex()] == null ? "" : array[voArray[j].getColumnIndex()].toString()) ;
				celli.setCellValue(array[voArray[j].getColumnIndex()] == null ? "" : getRealValue(array[voArray[j].getColumnIndex()] , voArray[j].getId2NameClassName())) ;
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream() ;
		try {
	    // 将封装的excel写入output流
			wb.write(os) ;
		} catch (IOException e) {
			e.printStackTrace() ;
		}
		// 创建一个新分配的byte字节数组。
		byte[] content = os.toByteArray() ;			
		try{
			os.flush() ;
			os.close() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		fileName =  getFileName() == null ? "ExportExcel" : getFileName() + ".xls" ; 
		
		// 创建一个新的字节数组输出流
		HttpServletResponse response = ServletActionContext.getResponse() ;
		response.setContentLength(os.size()) ; 
		InputStream inputStream = new ByteArrayInputStream(content) ;
		
		return inputStream ;
	}
	
	public String getRealValue(Object _obj , String className) throws Exception{
	
		if(StringUtils.isBlank(className) || "undefined".equals(className)) return ObjectUtils.toString(_obj) ;
		Object object = SpringTool.getBean(className) ;
		Method method = object.getClass().getMethod(ID_TO_NAME_METHOD , String.class) ;
		Object obj = method.invoke(object , new Object[]{_obj}) ;
		return ObjectUtils.toString(obj) ;
	}
	
}

