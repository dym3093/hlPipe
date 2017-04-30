package org.hpin.common.core.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类的基类
 * @author thinkpad<duan update>
 * @Apr 2, 2009
 */
@SuppressWarnings("serial")
public class BaseEntity implements Serializable{

	
	/** 创建人名称*/
	protected String createUserId;
	/** 创建时间*/
	protected Date createTime;
	/**最后修改人*/
	protected String updateUserId;
	/**最后修改时间*/
	protected Date updateTime;
	/**创建人部门编号*/
	protected String createDeptId;
	/**删除位*/
	protected Integer isDeleted;
	/**删除人*/
	protected String deleteUserId;
	/**删除时间*/
	protected Date deleteTime;
	
	protected String remark;
	
	protected String deptId;

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(String createDeptId) {
		this.createDeptId = createDeptId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(String deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
