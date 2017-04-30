package org.hpin.common.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * @author Yuejiaqi
 * 特殊样式EXCEL导出公用类
 */
public class ExcelUtil {
	/**
	 * 字体样式这是公用类
	 * @param font
	 * @param boldweight 宽度
	 * @param italic 斜体 true/false
	 * @param fontName 字体名
	 * @param height 高度
	 * @param color 颜色
	 * @return
	 */
	public static Font creatrFont(XSSFFont font, short boldweight,boolean italic,String fontName, short height,short color) {
		font.setBoldweight(boldweight);
		font.setFontName(fontName);
		font.setItalic(italic);
        font.setFontHeight(height);
        font.setColor(color);
		return font;
	}

	/**、
	 * 创建单元格
	 * @param cell 单元格对象
	 * @param map 样式map
	 * @param value 值
	 * @return
	 */
	public static Cell createCell(Cell cell ,Map<String,CellStyle> map,Object value){
		if(value instanceof String){
			cell.setCellStyle(map.get("String"));
			cell.setCellValue(value.toString());
		}else if(value instanceof Integer){
			cell.setCellStyle(map.get("Integer"));
			cell.setCellValue(Integer.parseInt(value.toString()));
		}
		else if(value instanceof Double){
			
			cell.setCellValue(Double.parseDouble(value.toString()));
            cell.setCellStyle(map.get("Double"));
		}else{
			cell.setCellValue(value.toString());
		}
		return cell;
	}
	public static Cell createCell(Cell cell , CellStyle style,String value){
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}
	/**
	 * 设置各项标题
	 * @param row 行
	 * @param cellNum 单元格起始列
	 * @param style 样式
	 * @param obj 标题可变参数
	 */
	public static void titleTool(Row row,int cellNum,CellStyle style,Object...obj){
		for(Object title:obj){
			createCell(row.createCell(cellNum++),style,title.toString());
		}
	}
	/**
	 * 设置各项标题
	 * @param row 行
	 * @param cellNum 单元格起始列
	 * @param cellStyleMap 样式Map
	 * @param obj 标题可变参数
	 */
	public static void valueTool(Row row,int cellNum,Map<String,CellStyle> cellStyleMap,Object...object){
		for(Object value:object){
			createCell(row.createCell(cellNum++),cellStyleMap,value);
		}
	}
	
	/**
	 * 主标题样式
	 * @param style
	 */
	public static CellStyle titleStyle(CellStyle style){
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index);
        return style;
	}
	
	/**
	 * 服务项目标题样式
	 * @param style
	 * @return 
	 */
	public static CellStyle elementTitleStyle(CellStyle style){
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   
		style.setFillForegroundColor(HSSFColor.LIME.index);
		return style;
	}
	
	/**
	 * 服务项目标题样式
	 * @param style
	 * @return 
	 */
	public static CellStyle tableTitleStyle(CellStyle style){
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   
		style.setFillForegroundColor(HSSFColor.LIME.index);
		return style;
	}
	/**
	 * 服务项目内容样式
	 * @param style
	 */
	public static CellStyle tableValueStyle(CellStyle style){
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		return style;
	}
	
	/**
	 * 单元格样式
	 * @param style 样式
	 * @return
	 */
	public static CellStyle commentStyle(XSSFCellStyle style) {
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		return style;
	}
	
	/**
	 * 单元格样式
	 * @param style 样式
	 * @return
	 */
	public static CellStyle createStyle(XSSFCellStyle style) {
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index); 
		return style;
	}
	
	/**
	 * double 数据类型Style
	 * @param style
	 * @return
	 */
	public static CellStyle createDoubleStyle(XSSFCellStyle style) {
		style.setBorderBottom((short)1);  
		style.setBorderLeft((short)1);  
		style.setBorderRight((short)1);  
		style.setBorderTop((short)1);  
		style.setBottomBorderColor(HSSFColor.BLACK.index); 
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		return style;
	}
	/**
	 * 行数据补齐
	 * @param listVal 行数据已存在list
	 * @param size 需要的列数
	 */
	public static void sameListSize(List listVal,int size){
		int num=size-listVal.size();
		for(int i=0;i<num;i++){
			listVal.add("");
		}
	}
	
}
