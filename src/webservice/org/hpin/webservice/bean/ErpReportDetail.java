/**
 * @author DengYouming
 * @since 2016-10-26 下午6:06:10
 */
package org.hpin.webservice.bean;

import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;

/**
 * table: ERP_REPORT_DETAIL
 * 基因报告明细表,用于存放基因报告单的信息
 * @author DengYouming
 * @since 2016-10-26 下午6:06:10
 */
public class ErpReportDetail extends BaseEntity{
	
	private static final long serialVersionUID = -5550732178645471115L;
	
	/** 1. ID VARCHAR2(32) */ 
	private String id; ;
	/** 2. 文件名 VARCHAR2(1024) */ 
	private String fileName; ;
	/** 3. 文件后缀名 VARCHAR2(32) */ 
	private String fileSuffix; ;
	/** 4. 文件保存的路径 VARCHAR2(1024) */ 
	private String filePath; ;
	/** 5. 文件预览地址 VARCHAR2(1024) */ 
	private String viewPath; ;
	/** 6. 会员表ID VARCHAR2(32) */ 
	private String customerId; ;
	/** 7. 姓名 VARCHAR2(256) */ 
	private String name; ;
	/** 8. 条码 VARCHAR2(256) */ 
	private String code; ;
	/** 9. 性别 VARCHAR2(32) */ 
	private String gender; ;
	/** 10. 场次号 VARCHAR2(256) */ 
	private String eventsNo; ;
	/** 11. 批次号 VARCHAR2(256) */ 
	private String batchNo; ;
	/** 12. 团单号 VARCHAR2(256) */ 
	private String groupOrderNo; ;
	/** 13. 备注 VARCHAR2(1024) */ 
	private String remark; ;
	/** 14. 创建时间 DATE */ 
	private Date createTime; ;
	/** 15. 创建人ID VARCHAR2(36) */ 
	private String createUserId; ;
	/** 16. 创建人姓名 VARCHAR2(256) */ 
	private String createUser; ;
	/** 17. 修改时间 DATE */ 
	private Date updateTime; ;
	/** 18. 修改人ID VARCHAR2(36) */ 
	private String updateUserId; ;
	/** 19. 修改人姓名 VARCHAR2(256) */ 
	private String updateUser; ;
	/** 20. 状态(新增:0, ,删除:-1,等状态) NUMBER */ 
	private Integer status;
	/** 21. 金域报告ID VARCHAR2(256) */
	private String reportId;
	
	public static final String F_ID = "id";
	public static final String F_FILENAME = "fileName";
	public static final String F_FILESUFFIX = "fileSuffix";
	public static final String F_FILEPATH = "filePath";
	public static final String F_VIEWPATH = "viewPath";
	public static final String F_CUSTOMERID = "customerId";
	public static final String F_NAME = "name";
	public static final String F_CODE = "code";
	public static final String F_GENDER = "gender";
	public static final String F_EVENTSNO = "eventsNo";
	public static final String F_BATCHNO = "batchNo";
	public static final String F_GROUPORDERNO = "groupOrderNo";
	public static final String F_REMARK = "remark";
	public static final String F_CREATETIME = "createTime";
	public static final String F_CREATEUSERID = "createUserId";
	public static final String F_CREATEUSER = "createUser";
	public static final String F_UPDATETIME = "updateTime";
	public static final String F_UPDATEUSERID = "updateUserId";
	public static final String F_UPDATEUSER = "updateUser";
	public static final String F_STATUS = "status";
	public static final String F_REPORTID = "reportId";

	public ErpReportDetail() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getViewPath() {
		return viewPath;
	}

	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEventsNo() {
		return eventsNo;
	}

	public void setEventsNo(String eventsNo) {
		this.eventsNo = eventsNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getGroupOrderNo() {
		return groupOrderNo;
	}

	public void setGroupOrderNo(String groupOrderNo) {
		this.groupOrderNo = groupOrderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Override
	public String toString() {
		return "ErpReportDetail ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (fileName != null ? "fileName=" + fileName + ", " : "")
				+ (fileSuffix != null ? "fileSuffix=" + fileSuffix + ", " : "")
				+ (filePath != null ? "filePath=" + filePath + ", " : "")
				+ (viewPath != null ? "viewPath=" + viewPath + ", " : "")
				+ (customerId != null ? "customerId=" + customerId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (code != null ? "code=" + code + ", " : "")
				+ (gender != null ? "gender=" + gender + ", " : "")
				+ (eventsNo != null ? "eventsNo=" + eventsNo + ", " : "")
				+ (batchNo != null ? "batchNo=" + batchNo + ", " : "")
				+ (groupOrderNo != null ? "groupOrderNo=" + groupOrderNo + ", "
						: "")
				+ (remark != null ? "remark=" + remark + ", " : "")
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (createUserId != null ? "createUserId=" + createUserId + ", "
						: "")
				+ (createUser != null ? "createUser=" + createUser + ", " : "")
				+ (updateTime != null ? "updateTime=" + updateTime + ", " : "")
				+ (updateUserId != null ? "updateUserId=" + updateUserId + ", "
						: "")
				+ (updateUser != null ? "updateUser=" + updateUser + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (reportId != null ? "reportId=" + reportId : "") + "]";
	}

}
