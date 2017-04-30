package org.hpin.webservice.bean.ty;

import org.hpin.common.core.orm.BaseEntity;

/**
 * 天津邮政检测码类
 * @author machuan
 * @date 2017年1月17日
 */
public class ErpValidCodeDetail extends BaseEntity{

	private static final long serialVersionUID = 1L;
	/** 1 .	VARCHAR2(32 BYTE) 主键id */
    private String id;
    /** 2 .	VARCHAR2(32 BYTE) 检测码 */
    private	String validCode;
    /** 3 .	VARCHAR2(10 BYTE) 是否被使用 */
    private String isUsed;
    /** 4 .	VARCHAR2(2000 BYTE) 预留字段1*/
    private String att1;
    /** 5 .	VARCHAR2(2000 BYTE) 预留字段2*/
    private String att2;
    /** 6 .	VARCHAR2(2000 BYTE) 预留字段3*/
    private String att3;
    /** 7 .	VARCHAR2(32 BYTE) 支公司ID*/
    private String branchId;
    /** 8 .	VARCHAR2(32 BYTE) 项目ID*/
    private String projectId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public String getAtt1() {
		return att1;
	}
	public void setAtt1(String att1) {
		this.att1 = att1;
	}
	public String getAtt2() {
		return att2;
	}
	public void setAtt2(String att2) {
		this.att2 = att2;
	}
	public String getAtt3() {
		return att3;
	}
	public void setAtt3(String att3) {
		this.att3 = att3;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
