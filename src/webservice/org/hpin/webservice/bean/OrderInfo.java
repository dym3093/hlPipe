package org.hpin.webservice.bean;//

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.hpin.common.core.orm.BaseEntity;

/**
 * 订单信息实体类
 * @author ybc
 * @since 2016-06-06
 */
@XmlRootElement
public class OrderInfo extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2433245136088384300L;

	private String id;//主键
	
	private String orderNo;//订单号
	
	private String status;//订单状态
	
	private String name;//姓名
	
	private String idno;//有效证件号
	
	private String sex;//性别
	
	private Integer age;//年龄
	
	private String setmealName;//套餐名
	
	private String phone;//手机号
	
	private String sampleAddress;//采样盒接受地址
	
	private String reportAddress;//报告接受地址
	
	private String guardianName;//监护人姓名
	
	private String guardianPhone;//监护人手机号码
	
	private String relationship;//关系(监护人与被检测者的关系)
	
	private String familyHistory;//家庭病史
	
	private String height;//身高
	
	private String weight;//体重
	
	private Date createdate;//创建日期
	
	private String note;//备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSetmealName() {
		return setmealName;
	}

	public void setSetmealName(String setmealName) {
		this.setmealName = setmealName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSampleAddress() {
		return sampleAddress;
	}

	public void setSampleAddress(String sampleAddress) {
		this.sampleAddress = sampleAddress;
	}

	public String getReportAddress() {
		return reportAddress;
	}

	public void setReportAddress(String reportAddress) {
		this.reportAddress = reportAddress;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getGuardianPhone() {
		return guardianPhone;
	}

	public void setGuardianPhone(String guardianPhone) {
		this.guardianPhone = guardianPhone;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Override
	public String toString() {
		return "OrderInfo [id=" + id + ", orderNo=" + orderNo + ", status="
				+ status + ", name=" + name + ", idno=" + idno + ", sex=" + sex
				+ ", age=" + age + ", setmealName=" + setmealName + ", phone="
				+ phone + ", sampleAddress=" + sampleAddress
				+ ", reportAddress=" + reportAddress + ", guardianName="
				+ guardianName + ", guardianPhone=" + guardianPhone
				+ ", relationship=" + relationship + ", familyHistory="
				+ familyHistory + ", height=" + height + ", weight=" + weight
				+ ", createdate=" + createdate + ", note=" + note + "]";
	}
	
}
