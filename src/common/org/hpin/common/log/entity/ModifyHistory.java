package org.hpin.common.log.entity;

import java.util.Date;
import java.util.Set;

import org.hpin.common.core.orm.BaseEntity;

/**
 * <p>@desc : 日志框架主Entity</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 6:13:34 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ModifyHistory extends BaseEntity{

	protected String id ;
	
	protected String parentClazz ;
	
	protected String parentPrimaryKey ;
	
	protected String targetClazz ;
	
	protected String targetPrimaryKey ;
	
	protected Set<ModifyHistoryItem> items ;
	
	protected Date updateTime ;
	
	protected String updateUserId ;
	
	protected String operType ;
	
	protected String state;

	public String getId() {
		return id ;
	}
	
	public void setId(String id) {
		this.id = id ;
	}

	public String getParentClazz() {
		return parentClazz ;
	}

	public void setParentClazz(String parentClazz) {
		this.parentClazz = parentClazz ;
	}

	public String getParentPrimaryKey() {
		return parentPrimaryKey ;
	}

	public void setParentPrimaryKey(String parentPrimaryKey) {
		this.parentPrimaryKey = parentPrimaryKey ;
	}
	
	public String getTargetClazz() {
		return targetClazz ;
	}
	
	public void setTargetClazz(String targetClazz) {
		this.targetClazz = targetClazz ;
	}
	
	public String getTargetPrimaryKey() {
		return targetPrimaryKey ;
	}
	
	public void setTargetPrimaryKey(String targetPrimaryKey) {
		this.targetPrimaryKey = targetPrimaryKey ;
	}
	
	public Set<ModifyHistoryItem> getItems() {
		return items ;
	}

	public void setItems(Set<ModifyHistoryItem> items) {
		this.items = items ;
	}
	
	public Date getUpdateTime() {
		return updateTime ;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime ;
	}
	
	public String getUpdateUserId() {
		return updateUserId ;
	}
	
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId ;
	}

	public String getOperType() {
		return operType ;
	}
	
	public void setOperType(String operType) {
		this.operType = operType ;
	}

	public String getState() {
		return state ;
	}

	public void setState(String state) {
		this.state = state ;
	}

}
