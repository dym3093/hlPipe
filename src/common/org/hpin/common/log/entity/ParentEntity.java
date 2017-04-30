package org.hpin.common.log.entity;

import org.hpin.common.core.orm.BaseEntity;


/**
 * <p>@desc : 	日志框架的基类，定义id属性以及其对应setter方法和getter方法 ;
 * 				系统模块的所有Entity的基类是BaseEntity，而BaseEntity又是继承此类，所以，默认情况下系统所有模块都可以记录日志
 * 				这样可以方便的取到当前记录日志模块的pojo的id属性
 * </p>
 * 
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : Apr 22, 2013 5:44:12 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ParentEntity extends BaseEntity{

	protected String id ;
	
	protected String hisName ;
	
	protected String emerCenterName ;
	
	protected String orgName ;
	
	protected String orgId ;
	
	protected String userName ;
	
	protected String hlHidId;
	
	//服务业务
	protected String contractName;
	
	protected String productName;
	
	protected String templetName;
	
	protected String ruleName;
	
	protected String businessType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHisName() {
		return hisName;
	}

	public void setHisName(String hisName) {
		this.hisName = hisName;
	}

	public String getEmerCenterName() {
		return emerCenterName;
	}

	public void setEmerCenterName(String emerCenterName) {
		this.emerCenterName = emerCenterName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHlHidId() {
		return hlHidId;
	}

	public void setHlHidId(String hlHidId) {
		this.hlHidId = hlHidId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTempletName() {
		return templetName;
	}

	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
}
