package org.hpin.webservice.bean;

import java.io.Serializable;
import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;

/**
 * @author Carly
 * @since 2016年9月21日11:03:24
 * 打印批次任务内容表，在放入ERP_REPORTDETAIL_PDFCONTENT表的同时也会放入数据到该表，
 * 获取内容都在这张表中获取，不会从以前的pdfcontent表中获取，ERP_REPORTDETAIL_PDFCONTENT用于再次打印任务的数据来源
 */
public class ErpPrintTaskContent extends BaseEntity implements Serializable{
	
	
	private String id;	
	/**数据来源表的id，现在从pdfcontent表和repeatprinttask中获取，字段type中0是pdfcontent，1是repeatprinttask表,2是来自金域*/
	private String pdfContentId;
	/**省*/
	private String province;
	/**市*/
	private String city;
	/**支公司id*/
	private String branchCompanyId;
	/**所属公司id*/
	private String ownedCompanyId;
	/**套餐*/
	private String combo;
	/**销售人员*/
	private String saleman;
	/**文件路径*/
	private String filePath;
	/**打印状态,0:未打印,1:已打印*/
	private String ps;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	/**0不是，1是，2是来自金域*/
	private String isManuallyAdd;
	/**打印批次号*/
	private String printBatchNo;
	/**客户id*/
	private String customerId;
	/**条形码*/
	private String code;
	/**客户姓名*/
	private String userName;
	/**性别*/
	private String gender;
	/**年龄*/
	private String age;
	/**人员部门*/
	private String dept;
	/**打印任务号，在确认打印的时候放入*/
	private String printTaskNo;
	/**项目编码*/
	private String projectCode;
	/**读取pdf时的批次号*/
	private String batchNo;
	/**0从pdfcontent表中获取的，1是repeatPrintTask表中获取，2是来自金域*/
	private String type;
	/**报告类型0，基因；1：癌筛；2：1+x；3：无创 @since 2016年12月9日10:19:13 @author Carly*/
	private Integer reportType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPdfContentId() {
		return pdfContentId;
	}
	public void setPdfContentId(String pdfContentId) {
		this.pdfContentId = pdfContentId;
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
	public String getCombo() {
		return combo;
	}
	public void setCombo(String combo) {
		this.combo = combo;
	}
	public String getSaleman() {
		return saleman;
	}
	public void setSaleman(String saleman) {
		this.saleman = saleman;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsManuallyAdd() {
		return isManuallyAdd;
	}
	public void setIsManuallyAdd(String isManuallyAdd) {
		this.isManuallyAdd = isManuallyAdd;
	}
	public String getPrintBatchNo() {
		return printBatchNo;
	}
	public void setPrintBatchNo(String printBatchNo) {
		this.printBatchNo = printBatchNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getPrintTaskNo() {
		return printTaskNo;
	}
	public void setPrintTaskNo(String printTaskNo) {
		this.printTaskNo = printTaskNo;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getReportType() {
		return reportType;
	}
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	
	
}
