/**
 * @author DengYouming
 * @since 2016-9-14 上午10:14:08
 */
package org.hpin.webservice.bean;

import java.io.Serializable;
import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;

/**
 * 定时任务表
 * @author DengYouming
 * @since 2016-9-14 上午10:14:08
 */
public class ErpScheduleJob extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3364521889437437053L;
	
	/** 1. ID VARCHAR2(36) */
	private String id; 
 /** 2. 关联ID VARCHAR2(36) */
	private String relatedId; 
 /** 3. 对应的表名 VARCHAR2(256) */
	private String relatedTable; 
 /** 4. 参数 VARCHAR2(1024) */
	private String params; 
 /** 5. 方法 VARCHAR2(256) */
	private String method; 
 /** 6. 信息类型 VARCHAR2(256) */
	private String infoType; 
 /** 7. 信息内容 VARCHAR2(2048) */
	private String info; 
 /** 8. 执行时间 VARCHAR2(256) */
	private String scheduleTime; 
 /** 9. 定时任务 VARCHAR2(1024) */
	private String job; 
 /** 10. 关键字 VARCHAR2(1024) */
	private String keyWord; 
 /** 11. 备注 VARCHAR2(1000) */
	private String remark; 
 /** 12. 创建时间 DATE */
	private Date createTime; 
 /** 13. 创建人ID VARCHAR2(100) */
	private String createUserId; 
 /** 14. 创建人姓名 VARCHAR2(256) */
	private String createUser; 
 /** 15. 修改时间 DATE */
	private Date updateTime; 
 /** 16. 修改人ID VARCHAR2(100) */
	private String updateUserId; 
 /** 17. 修改人姓名 VARCHAR2(256) */
	private String updateUser; 
 /** 18. 状态(新增,删除等状态) NUMBER */
	private Integer status;
 /** 19. 执行次数 NUMBER */
	private Integer count;	
 /** 20. 下载类型  NUMBER */
	private Integer down;	
	
	public static final String F_ID = "id";
	public static final String F_RELATEDID = "relatedId";
	public static final String F_RELATEDTABLE = "relatedTable";
	public static final String F_PARAMS = "params";
	public static final String F_METHOD = "method";
	public static final String F_INFOTYPE = "infoType";
	public static final String F_INFO = "info";
	public static final String F_SCHEDULETIME = "scheduleTime";
	public static final String F_JOB = "job";
	public static final String F_KEYWORD = "keyWord";
	public static final String F_REMARK = "remark";
	public static final String F_CREATETIME = "createTime";
	public static final String F_CREATEUSERID = "createUserId";
	public static final String F_CREATEUSER = "createUser";
	public static final String F_UPDATETIME = "updateTime";
	public static final String F_UPDATEUSERID = "updateUserId";
	public static final String F_UPDATEUSER = "updateUser";
	public static final String F_STATUS = "status";
	public static final String F_COUNT = "count";
	public static final String F_DOWN = "down";
	
	public ErpScheduleJob() {
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
	public String getRelatedTable() {
		return relatedTable;
	}
	public void setRelatedTable(String relatedTable) {
		this.relatedTable = relatedTable;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getDown() {
		return down;
	}

	public void setDown(Integer down) {
		this.down = down;
	}

	@Override
	public String toString() {
		return "ErpScheduleJob ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (relatedId != null ? "relatedId=" + relatedId + ", " : "")
				+ (relatedTable != null ? "relatedTable=" + relatedTable + ", "
						: "")
				+ (params != null ? "params=" + params + ", " : "")
				+ (method != null ? "method=" + method + ", " : "")
				+ (infoType != null ? "infoType=" + infoType + ", " : "")
				+ (info != null ? "info=" + info + ", " : "")
				+ (scheduleTime != null ? "scheduleTime=" + scheduleTime + ", "
						: "")
				+ (job != null ? "job=" + job + ", " : "")
				+ (keyWord != null ? "keyWord=" + keyWord + ", " : "")
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
				+ (count != null ? "count=" + count + ", " : "")
				+ (down != null ? "down=" + down : "") + "]";
	}
	
}
