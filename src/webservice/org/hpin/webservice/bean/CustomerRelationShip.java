package org.hpin.webservice.bean;

import java.util.Date;
import java.util.Set;

import org.hpin.common.core.orm.BaseEntity;

public class CustomerRelationShip extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332095716319765204L;

	private String id;
	
	/* 公司编码 */
	private String companyCode;
	/* 公司名称 */
	private String branchCommany;
	/**总公司ID*/
	private String ownedCompany;
	/* 远盟业务人员 */
	private String salesman;
	/* 省份 */
	private String province;
	/* 城市 */
	private String city;
	/* 城市 */
	private String tel;
	/* 对应账号 */
	private String account;
	/* 单位性质 */
	private String orgNature;
	/* 上级机构全称ID */
	private String superiorOrgId;
	/* 详细地址  */
	private String address;
	/* 客户分类 */
	private String customerType;
	/* 部门ID */
	private String deptId;
	/* 企业网址 */
	private String website;
	/* 客户类型 */
	private String customType;
    /* 状态标识 */
	private Integer flag;
	/* 对应套餐信息逗号分割 */
	private String combo;
	/* 总公司名称  */ 
	private String customerNameSimple;
	
	/** 总公司简称 */
	private String customerNameShort; // add by DengYouming 2016-08-16
	
	private Integer isDelete;
	private Date deleteTime;
	private String deleteUserId;
	private CustomerRelationShip customerRelationShip;
	
	/*
	 * 根据需求28,添加字段.
	 * 项目负责人:proUser
	 * 项目归属:proBelong
	 * 项目编码:proCode
	 * 收件人:recipients
	 * 收件人电话:recipientsTEL
	 * 邮寄地址:mailAddr
	 * create by henry.xu 2016.8.10;
	 */
	/**项目负责人*/
	private String proUser;
	/**项目归属*/
	private String proBelong;
	/**项目编码*/
	private String proCode;
	/**收件人*/
	private String recipients;
	/**收件人电话*/
	private String recipientsTel;
	/**邮寄地址*/
	private String mailAddr;
	
	private Set<CustomerRelationshipLink> customerRelationshipLinkSet;
	private Set<CustomerRelationshipCombo> customerRelationshipComboSet;
	
	
	public String getProUser() {
		return proUser;
	}

	public void setProUser(String proUser) {
		this.proUser = proUser;
	}

	public String getProBelong() {
		return proBelong;
	}
	
	public void setProBelong(String proBelong) {
		this.proBelong = proBelong;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getRecipientsTel() {
		return recipientsTel;
	}

	public void setRecipientsTel(String recipientsTel) {
		this.recipientsTel = recipientsTel;
	}

	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Integer getIsDelete() {
		return isDelete;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Set<CustomerRelationshipLink> getCustomerRelationshipLinkSet() {
		return customerRelationshipLinkSet;
	}

	public void setCustomerRelationshipLinkSet(
			Set<CustomerRelationshipLink> customerRelationshipLinkSet) {
		this.customerRelationshipLinkSet = customerRelationshipLinkSet;
	}

	public Set<CustomerRelationshipCombo> getCustomerRelationshipComboSet() {
		return customerRelationshipComboSet;
	}

	public void setCustomerRelationshipComboSet(
			Set<CustomerRelationshipCombo> customerRelationshipComboSet) {
		this.customerRelationshipComboSet = customerRelationshipComboSet;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(String deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getBranchCommany() {
		return branchCommany;
	}

	public void setBranchCommany(String branchCommany) {
		this.branchCommany = branchCommany;
	}

	public String getOwnedCompany() {
		return ownedCompany;
	}

	public void setOwnedCompany(String ownedCompany) {
		this.ownedCompany = ownedCompany;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOrgNature() {
		return orgNature;
	}

	public void setOrgNature(String orgNature) {
		this.orgNature = orgNature;
	}

	public String getSuperiorOrgId() {
		return superiorOrgId;
	}

	public void setSuperiorOrgId(String superiorOrgId) {
		this.superiorOrgId = superiorOrgId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getCombo() {
		return combo;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public String getCustomerNameSimple() {
		return customerNameSimple;
	}

	public void setCustomerNameSimple(String customerNameSimple) {
		this.customerNameSimple = customerNameSimple;
	}

	public CustomerRelationShip getCustomerRelationShip() {
		return customerRelationShip;
	}

	public void setCustomerRelationShip(CustomerRelationShip customerRelationShip) {
		this.customerRelationShip = customerRelationShip;
	}

	public String getCustomerNameShort() {
		return customerNameShort;
	}

	public void setCustomerNameShort(String customerNameShort) {
		this.customerNameShort = customerNameShort;
	}
}