package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

public class CustomerRelationshipLink extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723720373947588169L;
	private String id;
	private Integer num;
	
	/** 姓名*/
	private String linkMan;
	/** 性别*/
	private String sex;
	/**部门*/
	private String department;
	/**职务*/
	private String position;
	/**与远盟对接职能*/
	private String functions;
	private String qq;
	private String tel;
	private String phone;
	private String fax;
	/**个人账号*/
	private String account;
	/**账户名*/
	private String personName;
	/**　开户行 */
	private String bank;
	private String email;
	private String remark;
	private String customerRelationShipId;
	private String customerRelationShipName;
	private CustomerRelationShip customerRelationShip;
	
	
	public CustomerRelationShip getCustomerRelationShip() {
		return customerRelationShip;
	}
	public void setCustomerRelationShip(CustomerRelationShip customerRelationShip) {
		this.customerRelationShip = customerRelationShip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getFunctions() {
		return functions;
	}
	public void setFunctions(String functions) {
		this.functions = functions;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCustomerRelationShipName() {
		return customerRelationShipName;
	}
	public void setCustomerRelationShipName(String customerRelationShipName) {
		this.customerRelationShipName = customerRelationShipName;
	}
	public String getCustomerRelationShipId() {
		return customerRelationShipId;
	}
	public void setCustomerRelationShipId(String customerRelationShipId) {
		this.customerRelationShipId = customerRelationShipId;
	}
	/*public CustomerRelationShip getCustomerRelationShip() {
		return customerRelationShip;
	}
	public void setCustomerRelationShip(CustomerRelationShip customerRelationShip) {
		this.customerRelationShip = customerRelationShip;
	}*/
	


}