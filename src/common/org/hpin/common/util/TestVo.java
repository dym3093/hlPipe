package org.hpin.common.util;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Jul 18, 2012 5:10:01 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class TestVo {

	/**
	 * 变更项(英文)
	 */
	private String changeItem;
	/**
	 * 变更项(中文)
	 */
	private String changeItemCH;
	/**
	 * 当前值
	 */
	private String valueOfBefore;
	/**
	 * 变更后的值
	 */
	private String valueOfChange;
	/**
	 * 变更项类型
	 */
	private String changeType;
	
	public String getChangeItem() {
		return changeItem ;
	}
	
	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem ;
	}
	
	public String getChangeItemCH() {
		return changeItemCH ;
	}
	
	public void setChangeItemCH(String changeItemCH) {
		this.changeItemCH = changeItemCH ;
	}
	
	public String getValueOfBefore() {
		return valueOfBefore ;
	}
	
	public void setValueOfBefore(String valueOfBefore) {
		this.valueOfBefore = valueOfBefore ;
	}
	
	public String getValueOfChange() {
		return valueOfChange ;
	}
	
	public void setValueOfChange(String valueOfChange) {
		this.valueOfChange = valueOfChange ;
	}
	
	public String getChangeType() {
		return changeType ;
	}
	
	public void setChangeType(String changeType) {
		this.changeType = changeType ;
	}
	
	
}

