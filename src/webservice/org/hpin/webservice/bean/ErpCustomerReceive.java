package org.hpin.webservice.bean;

import java.util.Date;

/**
 * 
 * @description: erp_customer_receive表;
 * create by henry.xu 2016年12月2日
 */
public class ErpCustomerReceive {
	private String id; //主键ID
	private String batchNo;//批次号（年月日时分秒，此接口被调用一次，不管接口有多少人，此批次号都是一样的）
	private String serviceCode;//条形码
	private String userName;//姓名
	private String sex;//性别
	private String birthday;//出生日期
	private String companyId;//支公司id
	private String companyName;//支公司名称
	private String ownerCompanyId;//总公司id
	private String ownerCompanyName;//总公司名称
	private String examDate;//检测日期
	private String examTime;//检测时间
	private String eventsType;//场次类型（上午场：6：00-15：00/下午场：15：00-18：00/晚场：18：00-23：00）
	private String isMatch;//是否匹配 1匹配, 0 未匹配;
	private String returnFlag; //“Yes”—信息是否回传（Yes：通过公司二维码扫描进入的客户需要晚上回推给远盟，No：通过场次二维码扫描进入的客户信息不需要晚上回推给远盟/通过知情同意书直接在无创检测设备
	private String other; //备用字段; 无创传过来的支公司Id;
	private String sourceFrom; //数据来源;
	private String projectType; //项目类型编码
	
	private Date createTime;
	private Date updateTime;
	
	private String reportType; //设备套餐名称;
	
	
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getSourceFrom() {
		return sourceFrom;
	}
	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getReturnFlag() {
		return returnFlag;
	}
	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOwnerCompanyId() {
		return ownerCompanyId;
	}
	public void setOwnerCompanyId(String ownerCompanyId) {
		this.ownerCompanyId = ownerCompanyId;
	}
	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}
	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}
	
	public String getExamDate() {
		return examDate;
	}
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	public String getExamTime() {
		return examTime;
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	public String getEventsType() {
		return eventsType;
	}
	public void setEventsType(String eventsType) {
		this.eventsType = eventsType;
	}
	public String getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}

}
