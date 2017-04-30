package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

public class LogInfo  extends BaseEntity{
	private String id;
	private String code;
	private String name;
	private String idcard;
	private String tel;
	private String identityStatus;
	private String reportStatus;
	private String pdfPath;
	
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
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getIdentityStatus() {
		return identityStatus;
	}
	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	
	
	
}
