package org.hpin.common.widget.web.bo;


/**
 * <p>@desc : 通用导出Excel的JDBC版本vo</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : 2013-8-31 上午10:52:51</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */
public class ExportExcelAssistForJdbcVo {

	/**
	 * 导出Excel表头名称
	 */
	private String columnChName ;
	
	/**
	 * sql查询结果ResultSet中每一列的索引值
	 */
	private int columnIndex ;
	
	/**
	 * 如果是数据字典，此属性记录id2Name方法所在的class
	 */
	private String id2NameClassName ;

	public String getColumnChName() {
		return columnChName ;
	}

	public void setColumnChName(String columnChName) {
		this.columnChName = columnChName ;
	}

	public int getColumnIndex() {
		return columnIndex ;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex ;
	}
	
	public String getId2NameClassName() {
		return id2NameClassName ;
	}
	
	public void setId2NameClassName(String id2NameClassName) {
		this.id2NameClassName = id2NameClassName ;
	}
	
}

