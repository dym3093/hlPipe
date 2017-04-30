package org.hpin.webservice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Response {
	private String identityStatus;
	private String code;
	private String reportStatus;
	private String pdfPath;
	//@XmlElement(name = "tranCode")
	public String getIdentityStatus() {
		return identityStatus;
	}
	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
