package org.hpin.webservice.bean;

import java.util.Date;

/**
 * 
 * <p>Description: 报告客户信息</p>
 * @author henry.xu
 * @date 2017年2月23日
 */
public class ReportCustomerInfo {

	private String id;	//主键ID
	private String code;//条形码
	private String name;	//姓名
	private String sex;	//性别
	private String phone;	//手机号
	private String combo;	//套餐名称
	private String batch;	//批次号
	private Date receivedDate;	//收样日期
	private Date reportLaunchDate;	//报告发布日期
	private String reportAccountName;	//用户名
	private String reportAccountPass;	//用户密码
	private Date createTime;	//创建日期
	private String createUserId;	//创建人
	private Date updateTime;	//修改日期
	private String updateUserId;//	修改人
	private Integer isDeleted;	//是否删除
	
	/**
	 * update henry.xu 2017-03-14
	 * 添加: reportId 报告ID; reportNum 报告编号
	 * 
	 * */
	private String reportId;  //报告ID
	private String reportNum; //报告编号
	private Integer isSuccess; //是否成功上传; 默认为-1, 成功为1, 失败为0
	
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportNum() {
		return reportNum;
	}
	public void setReportNum(String reportNum) {
		this.reportNum = reportNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCombo() {
		return combo;
	}
	public void setCombo(String combo) {
		this.combo = combo;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getReportLaunchDate() {
		return reportLaunchDate;
	}
	public void setReportLaunchDate(Date reportLaunchDate) {
		this.reportLaunchDate = reportLaunchDate;
	}
	public String getReportAccountName() {
		return reportAccountName;
	}
	public void setReportAccountName(String reportAccountName) {
		this.reportAccountName = reportAccountName;
	}
	public String getReportAccountPass() {
		return reportAccountPass;
	}
	public void setReportAccountPass(String reportAccountPass) {
		this.reportAccountPass = reportAccountPass;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
