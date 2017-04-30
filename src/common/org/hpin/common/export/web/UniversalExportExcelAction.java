package org.hpin.common.export.web;

import static org.apache.poi.hssf.usermodel.HSSFCell.ENCODING_UTF_16;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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
import org.hpin.common.util.ReflectionUtils;
import org.hpin.common.widget.pagination.Page;
import org.hpin.common.widget.web.bo.ExportExcelAssistVo;


/**
 * <p>@desc : 通用导出ExcelAction</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-6-10 下午12:43:23</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
@Namespace("/system")
@Action("universalExportExcel")
@Results({
	@Result(name = "success", 
			type = "stream", 
			params = {	"contentType" , "application/vnd.ms-excel" , 
						"inputName" , "universalExcel" , 
						"contentDisposition" , "attachment;filename=\"${fileName}\"" , 
						"bufferSize" , "8192"
					 }
			)
})
public class UniversalExportExcelAction extends BaseAction {
	
	private List<ExportExcelAssistVo> excelVoList ;
	
	private String fileName ;
	
	private final String ID_TO_NAME_METHOD = "id2Name" ;
	
	private final String ID_TO_FIELD_METHOD = "id2Field" ;
	
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

	public List<ExportExcelAssistVo> getExcelVoList() {
		return excelVoList ;
	}
	
	public void setExcelVoList(List<ExportExcelAssistVo> excelVoList) {
		this.excelVoList = excelVoList ;
	}

	@Override()
	public String execute() throws Exception{
		Map map = buildSearch() ;
		String oldAction = HttpTool.getParameter("oldAction") ;
		String methodName = HttpTool.getParameter("methodName") ;
		String fileName = HttpTool.getParameter("fileName") ;
		String cycleName = HttpTool.getParameter("cycleName") ;
		String serviceName = HttpTool.getParameter("beanName") ;
		
		this.setFileName(fileName) ;
		
		Class actionClazz = Class.forName(oldAction) ;
		Object actionObj = actionClazz.newInstance() ;
		Method[] methods = actionClazz.getDeclaredMethods() ;
		Object obj = null ;
		page = new Page(HttpTool.getPageNum() , HttpTool.getPageSize());
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				if(StringUtils.isNotBlank(cycleName) && !"null".equals(cycleName) ){
					Object bean = SpringTool.getBean(serviceName) ;
					obj = method.invoke(actionObj, new Object[]{page , map , cycleName , bean}) ;
				}else{
					obj = method.invoke(actionObj , new Object[]{page , map}) ;
				}	
			}
		}
		page = (Page)obj ;
		long count = page.getTotalCount() ;
		page.setPageSize((int)count) ;
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				if(StringUtils.isNotBlank(cycleName) && !"null".equals(cycleName)){
					Object bean = SpringTool.getBean(serviceName) ;
					obj = method.invoke(actionObj, new Object[]{page , map , cycleName , bean}) ;
				}else{
					obj = method.invoke(actionObj , new Object[]{page , map}) ;
				}	
			}
		}
		page = (Page)obj ;
		
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
	 * 创建内容style
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle createNumberStyle(HSSFWorkbook wb){
		HSSFCellStyle styleNumber = wb.createCellStyle() ;
		styleNumber.setAlignment(HSSFCellStyle.ALIGN_CENTER) ;
		styleNumber.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER) ;
		styleNumber.setWrapText(true);
		HSSFFont fontContent = wb.createFont() ;
		fontContent.setFontName("宋体") ;
		styleNumber.setFont(fontContent) ;
		styleNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN) ;
		styleNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN) ;
		styleNumber.setBorderRight(HSSFCellStyle.BORDER_THIN) ;
		styleNumber.setBorderTop(HSSFCellStyle.BORDER_THIN) ;
		styleNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		return styleNumber ;
	}
	/**
	 * 获取输出流，发送给浏览器，提供下载
	 * @return
	 * @throws Exception
	 */
	public InputStream getUniversalExcel() throws Exception{
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet() ;
		
		int number = excelVoList.size() + 1 ;
		String[] title = new String[number] ;
		title[0] = "序号" ;
		for(int i = 0 ; i < excelVoList.size() ; i ++){
			title[i + 1] = excelVoList.get(i).getColumnChName() ;
		}
		
		ExportExcelAssistVo[] voArray = new ExportExcelAssistVo[excelVoList.size()] ;
		for(int i = 0 ; i < excelVoList.size() ; i ++){
			voArray[i] = excelVoList.get(i) ;
		}
		
		/*
		 *	设置整体列宽为5000 
		 */
		for(int i = 0 ; i < number ; i ++){
			sheet.setColumnWidth(i , (short)5000) ;
		}
		
		HSSFCellStyle titleStyle = createTitleStyle(wb) ;
		HSSFCellStyle contentStyle = createContentStyle(wb) ;
		HSSFCellStyle numberStyle = createNumberStyle(wb) ;
		
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
			
			/*
			 * 此循环中代码构成：
			 * 1、集合中有多少数据，导出的excel中数据部分就有多少行，所以第一部分代码是创建excel的row
			 * 2、每一行每个单元格，都需要有数据填充，填充步骤：
			 * 		a、根据当前列表集合中的单个对象，反射获取所有的method
			 * 		b、根据前台获取的配置信息，参考所有的method的name，如果匹配，则获取值，填充至表格中
			 */
			
			Object o = result.get(i) ;
			//获取列表对象的class
			Class clazz = o.getClass() ;
			
			//获取class所有的方法
			List<Method> methods = ReflectionUtils.getAllMethodAndSuperClass(clazz) ;
			for(int j = 0 ; j < voArray.length ; j ++){
				//创建第i个单元格
				HSSFCell celli = _row.createCell(j + 1) ;
				
				celli.setCellType(ENCODING_UTF_16) ;
				if((null!=getRealValue(methods , voArray[j] , o))&&getRealValue(methods , voArray[j] , o) instanceof Float){
						celli.setCellStyle(numberStyle) ;
						float value = 0;
						if(null!=getRealValue(methods , voArray[j] , o)){
							value = Float.parseFloat(getRealValue(methods , voArray[j] , o).toString());
						}
						celli.setCellValue(value) ;
				}else if((null!=getRealValue(methods , voArray[j] , o))&&getRealValue(methods , voArray[j] , o) instanceof Double){
					celli.setCellStyle(numberStyle) ;
					double value = 0;
					if(null!=getRealValue(methods , voArray[j] , o)){
						value = Double.parseDouble(getRealValue(methods , voArray[j] , o).toString());
					}
					celli.setCellValue(value) ;
				}else if((null!=getRealValue(methods , voArray[j] , o))&&getRealValue(methods , voArray[j] , o) instanceof Date){
					celli.setCellStyle(contentStyle) ;
					String value = getRealValue(methods , voArray[j] , o).toString();
					if(value.endsWith("00:00:00.0")){
						celli.setCellValue(value.substring(0, 10)) ; 
					}else{
						celli.setCellValue(value.substring(0, 19)) ; 
					}
				}else{
					celli.setCellStyle(contentStyle) ;
					celli.setCellValue(getRealValue(methods , voArray[j] , o) == null ? "" : getRealValue(methods , voArray[j] , o).toString()) ;
				}
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
	
	/**
	 * 根据方法集合，辅助vo和列表单个对象，获取真实需要的结果值
	 * @param methods
	 * @param vo
	 * @param o
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private Object getRealValue(List<Method>  methods, ExportExcelAssistVo vo , Object o) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, SecurityException, NoSuchMethodException{
		Object obj = null ;
		for(Method method : methods){
			if(method.getName().equals(ReflectionUtils.parseGetName(vo.getColumnEnName()))){
				obj = method.invoke(o) ;
			}
		}
		if(StringUtils.isNotBlank(vo.getId2NameClassName()) && !"undefined".equals(vo.getId2NameClassName())){
			String id2NameClassName = vo.getId2NameClassName() ;
			Object _object = SpringTool.getBean(id2NameClassName) ;
			Method[] _methods = _object.getClass().getDeclaredMethods() ;
			for(Method m : _methods){
				if(m.getName().equals(ID_TO_NAME_METHOD)){
					obj = m.invoke(_object , obj) ;
				}
			}
		}
		if(StringUtils.isNotBlank(vo.getId2FieldClassName()) && !"undefined".equals(vo.getId2FieldClassName())){
			String[] array = vo.getId2FieldClassName().split("&") ;
			if(array != null && array.length > 1){ 
				String id2FieldClassName = array[0] ;
				String fieldName = array[1] ;
				//ID2NameService service = (ID2NameService) SpringTool.getBean(ID2NameService.class);
				//obj = service.id2Field(ObjectUtils.toString(obj), id2FieldClassName, fieldName) ;
				Object _object = SpringTool.getBean(id2FieldClassName) ;
				Method method = _object.getClass().getMethod(ReflectionUtils.parseGetName(fieldName) , String.class , String.class , String.class) ;
				obj = method.invoke(_object , new Object[]{ObjectUtils.toString(obj), id2FieldClassName, fieldName}) ;
			}
		}
		return obj ;
	}
	
}

