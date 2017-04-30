package org.hpin.webservice.bean;

import java.util.Date;

public class CustomerRelationShipPro {
	private String id; //主键ID
	private String customerRelationShipId; //支公司ID
	private String projectCode; //项目编码
	private String projectName; //项目名称
	private String projectOwner; //项目负责人
	private String projectType;  //项目类型(癌筛/基因)
	private String linkName; //远盟链接人
	private String linkTel; //远盟链接人电话
	private String remark;  //记录
	private String mailAddress; //邮件地址
	private String reception; //收件人
	private String receptionTel; //收件人电话
	private Date createTime; //创建日期;
	private String createUserId; //创建人id
	private String isDeleted; //是否删除;(0未删除,1删除)
	private Date deleteTime; //删除时间
	private String deleteUserId; //删除人ID
	private Date updateTime; //修改时间;
	private String updateUserId; //修改人;

	//  add by YoumingDeng 2016-12-06 start
    public static final String F_ID = "id";
    public static final String F_CUSTOMERRELATIONSHIPID = "customerRelationShipId";
    public static final String F_PROJECTCODE = "projectCode";
    public static final String F_PROJECTNAME = "projectName";
    public static final String F_PROJECTOWNER = "projectOwner";
    public static final String F_PROJECTTYPE = "projectType";
    public static final String F_LINKNAME = "linkName";
    public static final String F_LINKTEL = "linkTel";
    public static final String F_REMARK = "remark";
    public static final String F_MAILADDRESS = "mailAddress";
    public static final String F_RECEPTION = "reception";
    public static final String F_RECEPTIONTEL = "receptionTel";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_DELETETIME = "deleteTime";
    public static final String F_DELETEUSERID = "deleteUserId";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";

    public CustomerRelationShipPro() {
    }
	//  add by YoumingDeng 2016-12-06 end

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerRelationShipId() {
		return customerRelationShipId;
	}
	public void setCustomerRelationShipId(String customerRelationShipId) {
		this.customerRelationShipId = customerRelationShipId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectOwner() {
		return projectOwner;
	}
	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getReception() {
		return reception;
	}
	public void setReception(String reception) {
		this.reception = reception;
	}
	public String getReceptionTel() {
		return receptionTel;
	}
	public void setReceptionTel(String receptionTel) {
		this.receptionTel = receptionTel;
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
	
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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
	
	
	
	
	
}
