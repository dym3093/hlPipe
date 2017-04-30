package org.hpin.webservice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息临时表;
 * @description: 
 * create by henry.xu 2016年12月2日
 */
public class ErpCustomerTempWuChuang implements Serializable{
	
	/**
	 * 序列号;
	 */
	private static final long serialVersionUID = 1L;
	
	private String id; //主键ID
	private String branchCompanyId; //支公司ID
	private String branchCompany; //支公司名称
	private String ownedCompany; //总公司名称
	private String ownedCompanyId; //总公司id
	private String code; //条形码
	private String testInstitution; //机构
	private String salesMan; //营销人
	private String salesManNo; //营销人员编号
	private String familyHistory; //家庭病史
	private String customerhistory; //既往病史
	private String idno; //生分证
	private String sex; //性别
	private String age; //年龄
	private String phone; //电话
	private String height; //高
	private String weight; //重
	private String guardianname; //监护人
	private String guardianphone; //监护人电话
	private Date   createTime; //创建时间
	private String name; //姓名;
	private String isMatch; //是否匹配;
	private String birthday; //出生日期;
	
	private String projectType ; //项目类型名称; 对应表T_PROJECT_TYPE  create by henry.xu 20170109
	private String bloodType; //血型：A型、B型、O型、AB型、未知       create by henry.xu 20170208 
	
	private String openId; //微信标识
	
	private String salesChannel; //渠道
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBranchCompanyId() {
		return branchCompanyId;
	}
	public void setBranchCompanyId(String branchCompanyId) {
		this.branchCompanyId = branchCompanyId;
	}
	public String getBranchCompany() {
		return branchCompany;
	}
	public void setBranchCompany(String branchCompany) {
		this.branchCompany = branchCompany;
	}
	public String getOwnedCompany() {
		return ownedCompany;
	}
	public void setOwnedCompany(String ownedCompany) {
		this.ownedCompany = ownedCompany;
	}
	public String getOwnedCompanyId() {
		return ownedCompanyId;
	}
	public void setOwnedCompanyId(String ownedCompanyId) {
		this.ownedCompanyId = ownedCompanyId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTestInstitution() {
		return testInstitution;
	}
	public void setTestInstitution(String testInstitution) {
		this.testInstitution = testInstitution;
	}
	public String getSalesMan() {
		return salesMan;
	}
	public void setSalesMan(String salesMan) {
		this.salesMan = salesMan;
	}
	public String getSalesManNo() {
		return salesManNo;
	}
	public void setSalesManNo(String salesManNo) {
		this.salesManNo = salesManNo;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getCustomerhistory() {
		return customerhistory;
	}
	public void setCustomerhistory(String customerhistory) {
		this.customerhistory = customerhistory;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getGuardianname() {
		return guardianname;
	}
	public void setGuardianname(String guardianname) {
		this.guardianname = guardianname;
	}
	public String getGuardianphone() {
		return guardianphone;
	}
	public void setGuardianphone(String guardianphone) {
		this.guardianphone = guardianphone;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}

}
