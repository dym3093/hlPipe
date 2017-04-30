package org.hpin.webservice.bean.yg;

import java.io.Serializable;
import java.util.Date;

/**
 * 保险公司客户预置表
 * @description: 
 * create by henry.xu 2016年12月15日
 */
public class ErpBxCompanyPreSet implements Serializable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1432198565286869337L;
	private String id; //主键ID
	private String applicationNo; //申请单号
	private String eventsNo; //场次号
	private String customerRelationshipId;//支公司ID
	private String customerSkuNum;//客户唯一识别号
	private String customerName;//姓名
	private String comboName;//套餐
	private Date createTime;//创建时间
	private String createUserId;//创建人
	private Date updateTime;//修改时间
	private String updateUserId;//修改人
	private String isUseable;//可用标记
	private String comboShowName;//套餐显示名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getEventsNo() {
		return eventsNo;
	}
	public void setEventsNo(String eventsNo) {
		this.eventsNo = eventsNo;
	}
	public String getCustomerRelationshipId() {
		return customerRelationshipId;
	}
	public void setCustomerRelationshipId(String customerRelationshipId) {
		this.customerRelationshipId = customerRelationshipId;
	}
	public String getCustomerSkuNum() {
		return customerSkuNum;
	}
	public void setCustomerSkuNum(String customerSkuNum) {
		this.customerSkuNum = customerSkuNum;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getIsUseable() {
		return isUseable;
	}
	public void setIsUseable(String isUseable) {
		this.isUseable = isUseable;
	}
	public String getComboShowName() {
		return comboShowName;
	}
	public void setComboShowName(String comboShowName) {
		this.comboShowName = comboShowName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
