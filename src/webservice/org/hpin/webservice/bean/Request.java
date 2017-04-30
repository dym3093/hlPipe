package org.hpin.webservice.bean;

import javax.xml.bind.annotation.XmlRootElement;

import org.hpin.common.core.orm.BaseEntity;
@XmlRootElement(name = "request")
public class Request extends BaseEntity{
	private String id;
	private String code;
	private String name;
	private String sex;
	private String age;
	private String phone;
	private String branchcompany;
	private String project;
	private String sampletype;
	private String salesman;
	private String entering;
	private String institution;
	private String samplingdate;
	private String collectiondate;
	private String simplestatus;
	private String page;
	private String filepath;
	
	
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBranchcompany() {
		return branchcompany;
	}
	public void setBranchcompany(String branchcompany) {
		this.branchcompany = branchcompany;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getEntering() {
		return entering;
	}
	public void setEntering(String entering) {
		this.entering = entering;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getSamplingdate() {
		return samplingdate;
	}
	public void setSamplingdate(String samplingdate) {
		this.samplingdate = samplingdate;
	}
	public String getCollectiondate() {
		return collectiondate;
	}
	public void setCollectiondate(String collectiondate) {
		this.collectiondate = collectiondate;
	}
	public String getSimplestatus() {
		return simplestatus;
	}
	public void setSimplestatus(String simplestatus) {
		this.simplestatus = simplestatus;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	
}
