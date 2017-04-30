package org.hpin.common.widget.web.bo;


/**
 * <p>@desc : 通用导出Excel辅助POJO</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-6-10 下午5:49:57</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ExportExcelAssistVo {

	/**
	 * excel导出时，对应entity中的属性名
	 */
	private String columnEnName ;
	
	/**
	 * excel导出时，生成的最终表头名称
	 */
	private String columnChName ;
	
	/**
	 * 标注id2NameClass全路径，将导出的结果集中，需要转换的信息进行id2Name的转换
	 */
	private String id2NameClassName ;
	
	/**
	 * 标注id2FieldClass全路径，将导出的结果集中，需要转换的信息进行id2Filed的转换
	 */
	private String id2FieldClassName ;

	public String getColumnEnName() {
		return columnEnName ;
	}

	public void setColumnEnName(String columnEnName) {
		this.columnEnName = columnEnName ;
	}

	public String getColumnChName() {
		return columnChName ;
	}

	public void setColumnChName(String columnChName) {
		this.columnChName = columnChName ;
	}
	
	public String getId2NameClassName() {
		return id2NameClassName ;
	}
	
	public void setId2NameClassName(String id2NameClassName) {
		this.id2NameClassName = id2NameClassName ;
	}

	public String getId2FieldClassName() {
		return id2FieldClassName;
	}

	public void setId2FieldClassName(String id2FieldClassName) {
		this.id2FieldClassName = id2FieldClassName;
	}
	
}

