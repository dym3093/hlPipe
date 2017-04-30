package org.hpin.webservice.bean;


import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hpin.common.core.orm.BaseEntity;
import org.hpin.common.log.annotation.LogAnnotation;

/**
 * ErpCustomer entity. @author MyEclipse Persistence Tools
 * customer
 */
@XmlRootElement(name="customer") 
@XmlAccessorType(XmlAccessType.FIELD)
public class ErpCustomer extends BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 7853784298785494920L;
	private String id;
	private String eventsNo;//场次号
	private String code;//条形码
	private String name;//姓名
	private String phone;//电话
	private String documentType;//证件类型
	private String idno;//证号
	/*@LogAnnotation(name = "套餐名称",id2NameClass = "org.ymjy.combo.dao.ComboDao")*/
	private String setmealName;//套餐
	private String sex;//性别
	private String age;//年龄
	private String branchCompany;//支公司
	private String branchCompanyId;//支公司ID
	private String sampleType;//样本类型
	private Date samplingDate;//采样时间
	private String salesMan;//保险公司营销员
	private String familyHistory;//家族疾病史
	private String ownedCompany;//所属公司
	private String ownedCompanyId;//所属公司
	private Integer isDeleted;
	private String createUserName;
	private Date createTime;
	private String updateUserName;
	private Date updateTime;
	private String provice;//省份
	private String city;//市
	private String note;//备注
	private String pdffilepath;//pdf文件路径
	private String ymSalesman;//远盟营销员
	private String testInstitution;//检测机构
	private String settlement_status;//结算状态
	
	/* 比对场次增加字段 */
	private Date eventsTime;	//场次时间
	
	/** 场次客户比对状态 （0--比对有场次    1--比对无场次） **/
	private String comparStatus;
	private String failBtachId;		//比对失败批次ID
	
	private String status; //状态 add by DengYouming 2016-05-01
	private String department; // 部门  add by DengYouming 2016-09-22
	private Integer statusYm; //客户报告状态 add by YoumingDeng 2016-12-14

	//  add by DengYouming 2016-05-07
	public static final String F_ID = "id";
	public static final String F_EVENTSNO = "eventsNo";
	public static final String F_CODE = "code";
	public static final String F_NAME = "name";
	public static final String F_PHONE = "phone";
	public static final String F_DOCUMENTTYPE = "documentType";
	public static final String F_IDNO = "idno";
	public static final String F_SETMEALNAME = "setmealName";
	public static final String F_SEX = "sex";
	public static final String F_AGE = "age";
	public static final String F_BRANCHCOMPANY = "branchCompany";
	public static final String F_BRANCHCOMPANYID = "branchCompanyId";
	public static final String F_SAMPLETYPE = "sampleType";
	public static final String F_SAMPLINGDATE = "samplingDate";
	public static final String F_SALESMAN = "salesMan";
	public static final String F_FAMILYHISTORY = "familyHistory";
	public static final String F_OWNEDCOMPANY = "ownedCompany";
	public static final String F_OWNEDCOMPANYID = "ownedCompanyId";
	public static final String F_ISDELETED = "isDeleted";
	public static final String F_CREATEUSERNAME = "createUserName";
	public static final String F_CREATETIME = "createTime";
	public static final String F_UPDATEUSERNAME = "updateUserName";
	public static final String F_UPDATETIME = "updateTime";
	public static final String F_PROVICE = "provice";
	public static final String F_CITY = "city";
	public static final String F_NOTE = "note";
	public static final String F_PDFFILEPATH = "pdffilepath";
	public static final String F_YMSALESMAN = "ymSalesman";
	public static final String F_TESTINSTITUTION = "testInstitution";
	public static final String F_STATUS = "status";
	public static final String F_DEPARTMENT = "department";
	public static final String F_STATUSYM= "statusYm";

	/*
	 * modified by henry.xu 20161130
	 * 字段添加;  身高(height)、体重(weight)，保险公司营销员工号(salesManNo)、
	 * 既往病史(customerHistory)、监护人姓名(guardianName)、关系(relationship)（父亲、母亲、祖父、祖母、外祖父、外祖母、其他）；
	 * */
	private String height; //身高
	private String weight; //体重
	private String salesManNo; //营销员工号
	private String customerHistory; //既往病史;
	private String guardianName; //监护人姓名;
	private String guardianPhone; //监护人姓名;
	private String relationship; //关系;
	private String otherCompanyId; //知康temp_wuchuang标记中的支公司id create by henry.xu 20161219
	
	public String getOtherCompanyId() {
		return otherCompanyId;
	}

	public void setOtherCompanyId(String otherCompanyId) {
		this.otherCompanyId = otherCompanyId;
	}

	public String getGuardianPhone() {
		return guardianPhone;
	}

	public void setGuardianPhone(String guardianPhone) {
		this.guardianPhone = guardianPhone;
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

	public String getSalesManNo() {
		return salesManNo;
	}

	public void setSalesManNo(String salesManNo) {
		this.salesManNo = salesManNo;
	}

	public String getCustomerHistory() {
		return customerHistory;
	}

	public void setCustomerHistory(String customerHistory) {
		this.customerHistory = customerHistory;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getSettlement_status() {
		return settlement_status;
	}

	public void setSettlement_status(String settlement_status) {
		this.settlement_status = settlement_status;
	}

	public String getTestInstitution() {
		return testInstitution;
	}

	public void setTestInstitution(String testInstitution) {
		this.testInstitution = testInstitution;
	}

	public String getYmSalesman() {
		return ymSalesman;
	}

	public void setYmSalesman(String ymSalesman) {
		this.ymSalesman = ymSalesman;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventsNo() {
		return this.eventsNo;
	}

	public void setEventsNo(String eventsNo) {
		this.eventsNo = eventsNo;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getIdno() {
		return this.idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getSetmealName() {
		return this.setmealName;
	}

	public void setSetmealName(String setmealName) {
		this.setmealName = setmealName;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBranchCompany() {
		return this.branchCompany;
	}

	public void setBranchCompany(String branchCompany) {
		this.branchCompany = branchCompany;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public Date getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}

	public String getSalesMan() {
		return salesMan;
	}

	public void setSalesMan(String salesMan) {
		this.salesMan = salesMan;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getOwnedCompany() {
		return ownedCompany;
	}

	public void setOwnedCompany(String ownedCompany) {
		this.ownedCompany = ownedCompany;
	}

	public String getBranchCompanyId() {
		return branchCompanyId;
	}

	public void setBranchCompanyId(String branchCompanyId) {
		this.branchCompanyId = branchCompanyId;
	}

	public String getOwnedCompanyId() {
		return ownedCompanyId;
	}

	public void setOwnedCompanyId(String ownedCompanyId) {
		this.ownedCompanyId = ownedCompanyId;
	}

	public String getPdffilepath() {
		return pdffilepath;
	}

	public void setPdffilepath(String pdffilepath) {
		this.pdffilepath = pdffilepath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEventsTime() {
		return eventsTime;
	}

	public void setEventsTime(Date eventsTime) {
		this.eventsTime = eventsTime;
	}

	public String getComparStatus() {
		return comparStatus;
	}

	public void setComparStatus(String comparStatus) {
		this.comparStatus = comparStatus;
	}

	public String getFailBtachId() {
		return failBtachId;
	}

	public void setFailBtachId(String failBtachId) {
		this.failBtachId = failBtachId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getStatusYm() {
		return statusYm;
	}

	public void setStatusYm(Integer statusYm) {
		this.statusYm = statusYm;
	}

	@Override
	public String toString() {
		return "ErpCustomer{" +
				"id='" + id + '\'' +
				", eventsNo='" + eventsNo + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", phone='" + phone + '\'' +
				", documentType='" + documentType + '\'' +
				", idno='" + idno + '\'' +
				", setmealName='" + setmealName + '\'' +
				", sex='" + sex + '\'' +
				", age='" + age + '\'' +
				", branchCompany='" + branchCompany + '\'' +
				", branchCompanyId='" + branchCompanyId + '\'' +
				", sampleType='" + sampleType + '\'' +
				", samplingDate=" + samplingDate +
				", salesMan='" + salesMan + '\'' +
				", familyHistory='" + familyHistory + '\'' +
				", ownedCompany='" + ownedCompany + '\'' +
				", ownedCompanyId='" + ownedCompanyId + '\'' +
				", isDeleted=" + isDeleted +
				", createUserName='" + createUserName + '\'' +
				", createTime=" + createTime +
				", updateUserName='" + updateUserName + '\'' +
				", updateTime=" + updateTime +
				", provice='" + provice + '\'' +
				", city='" + city + '\'' +
				", note='" + note + '\'' +
				", pdffilepath='" + pdffilepath + '\'' +
				", ymSalesman='" + ymSalesman + '\'' +
				", testInstitution='" + testInstitution + '\'' +
				", settlement_status='" + settlement_status + '\'' +
				", eventsTime=" + eventsTime +
				", comparStatus='" + comparStatus + '\'' +
				", failBtachId='" + failBtachId + '\'' +
				", status='" + status + '\'' +
				", department='" + department + '\'' +
				", statusYm=" + statusYm +
				", height='" + height + '\'' +
				", weight='" + weight + '\'' +
				", salesManNo='" + salesManNo + '\'' +
				", customerHistory='" + customerHistory + '\'' +
				", guardianName='" + guardianName + '\'' +
				", guardianPhone='" + guardianPhone + '\'' +
				", relationship='" + relationship + '\'' +
				", otherCompanyId='" + otherCompanyId + '\'' +
				'}';
	}
}