package org.hpin.common.log.entity;

import java.util.Date;


/**
 * <p>@desc : 日志框架子Entity，每修改一个属性，对应此表中的一条记录</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 6:25:26 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ModifyHistoryItem{
	
	protected String id ;
	
	protected ModifyHistory modifyHistory ;
	
	protected String fieldName ;
	
	protected String oldValue ;
	
	protected String newValue ;
	
	protected String fieldDesc ;
	
	protected String historyId ;
	
	protected Date createTime;
	
	public void setId(String id){
		this.id = id ;
	}

	public void setModifyHistory(ModifyHistory modifyHistory){
		this.modifyHistory = modifyHistory ;
	}

	public void setFieldName(String fieldName){
		this.fieldName = fieldName ;
	}

	public void setOldValue(String oldValue){
		this.oldValue = oldValue ;
	}

	public void setNewValue(String newValue){
		this.newValue = newValue ;
	}

	public void setFieldDesc(String fieldDesc){
		this.fieldDesc = fieldDesc ;
	}

	public String getId() {
		return id;
	}

	public ModifyHistory getModifyHistory() {
		return modifyHistory;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}
	
	public String getHistoryId(){
		return historyId ;
	}
	
	public void setHistoryId(String historyId){
		this.historyId = historyId ;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
