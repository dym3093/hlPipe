package org.hpin.webservice.bean;

import java.io.Serializable;
import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;

public class ErpReportdetailPDFContent extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String pdfname;
	
	private String username;
	
	private String age;
	
	private String code;
	
	private String sex;
	
	private String filesize;
	
	private String md5;
	
	private String batchno;
	
	private String filepath;
	
	private int isrecord;
	
	private int isrepeat;
	
	private int matchstate;
	
	private Date createdate;
	
	private Date updatedate;
	
	private String customerid;
	
	private String provice;
	
	private String city;
	
	private String branch_company;
	
	private String events_no;
	
	private String setmeal_name;
	
	private String sales_man;
	
	private String ps;
	
	private String printbthno;
	
	private String printtaskno;
	
	private String settlement_status;
	
	private String matchstateView;//显示状态含义,数据库并无此字段
	
	private String reportType; //add by henry.xu 20170411

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getPdfname() {
		return pdfname;
	}

	public void setPdfname(String pdfname) {
		this.pdfname = pdfname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getIsrecord() {
		return isrecord;
	}

	public void setIsrecord(int isrecord) {
		this.isrecord = isrecord;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public int getMatchstate() {
		return matchstate;
	}

	public void setMatchstate(int matchstate) {
		this.matchstate = matchstate;
	}

	public int getIsrepeat() {
		return isrepeat;
	}

	public void setIsrepeat(int isrepeat) {
		this.isrepeat = isrepeat;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranch_company() {
		return branch_company;
	}

	public void setBranch_company(String branch_company) {
		this.branch_company = branch_company;
	}

	public String getEvents_no() {
		return events_no;
	}

	public void setEvents_no(String events_no) {
		this.events_no = events_no;
	}

	public String getSetmeal_name() {
		return setmeal_name;
	}

	public void setSetmeal_name(String setmeal_name) {
		this.setmeal_name = setmeal_name;
	}

	public String getSales_man() {
		return sales_man;
	}

	public void setSales_man(String sales_man) {
		this.sales_man = sales_man;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getPrintbthno() {
		return printbthno;
	}

	public void setPrintbthno(String printbthno) {
		this.printbthno = printbthno;
	}

	public String getPrinttaskno() {
		return printtaskno;
	}

	public void setPrinttaskno(String printtaskno) {
		this.printtaskno = printtaskno;
	}

	public String getSettlement_status() {
		return settlement_status;
	}

	public void setSettlement_status(String settlement_status) {
		this.settlement_status = settlement_status;
	}

	public String getMatchstateView() {
		return matchstateView;
	}

	public void setMatchstateView(String matchstateView) {
		this.matchstateView = matchstateView;
	}

	@Override
	public String toString() {
		return "ErpReportdetailPDFContent [id=" + id + ", pdfname=" + pdfname
				+ ", username=" + username + ", age=" + age + ", code=" + code
				+ ", sex=" + sex + ", filesize=" + filesize + ", md5=" + md5
				+ ", batchno=" + batchno + ", filepath=" + filepath
				+ ", isrecord=" + isrecord + ", isrepeat=" + isrepeat
				+ ", matchstate=" + matchstate + ", createdate=" + createdate
				+ ", updatedate=" + updatedate + ", customerid=" + customerid
				+ ", provice=" + provice + ", city=" + city
				+ ", branch_company=" + branch_company + ", events_no="
				+ events_no + ", setmeal_name=" + setmeal_name + ", sales_man="
				+ sales_man + ", ps=" + ps + ", printbthno=" + printbthno
				+ ", printtaskno=" + printtaskno + ", settlement_status="
				+ settlement_status + ", matchstateView=" + matchstateView
				+ "]";
	}
	
}
