/**
 * @author DengYouming
 * @since 2016-10-11 下午5:41:29
 */
package org.hpin.webservice.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hpin.common.core.orm.BaseEntity;
import org.hpin.webservice.util.JaxbDateAdapter;

/**
 * 德润订单类
 * @author DengYouming
 * @since 2016-10-11 下午5:41:29
 */
@XmlRootElement(name="order") 
@XmlAccessorType(XmlAccessType.FIELD)
public class ErpOrder extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	/** 1. VARCHAR2(32) 主键 */
	private String id; 
	 /** 2. VARCHAR2(100) 订单号 */
	private String orderNo; 
	 /** 3. DATE 订单日期 */
	@XmlJavaTypeAdapter(JaxbDateAdapter.class)
	private Date orderTime; 
	 /** 4. VARCHAR2(50) 姓名 */
	private String name; 
	 /** 5. VARCHAR2(50) 性别（男、女） */
	private String gender; 
	 /** 6. VARCHAR2(10) 年龄 */
	private String age; 
	 /** 7. VARCHAR2(50) 证件类型1010101：身份证 1010106：其他 */
	private String docType; 
	 /** 8. VARCHAR2(50) 证件号 */
	private String docNo; 
	 /** 9. VARCHAR2(20) 电话号码 */
	private String phone; 
	 /** 10. VARCHAR2(100) 套餐名 */
	private String setmealName; 
	 /** 11. NUMBER 身高 */
	private Integer height; 
	 /** 12. NUMBER 体重 */
	private Integer weight; 
	 /** 13. VARCHAR2(200) 家族病史 */
	private String familyHistory; 
	 /** 14. VARCHAR2(100) 监护人姓名 */
	private String guardianName; 
	 /** 15. VARCHAR2(100) 监护人联系方式 */
	private String guardianTel; 
	 /** 16. VARCHAR2(100) 与监护人关系 */
	private String relationship; 
	 /** 17. VARCHAR2(100) 采样包寄送地址 */
	private String sampleAddress; 
	 /** 18. VARCHAR2(100) 报告寄送地址 */
	private String reportAddress; 
	 /** 19. VARCHAR2(100) 数据来源 */
	private String dataSource; 
	 /** 20. DATE 更新日期 */
	private Date updateTime; 
	 /** 21. DATE 入库日期 */
	private Date createTime;
	
	public static final String F_ID = "id";
	public static final String F_ORDERNO = "orderNo";
	public static final String F_ORDERTIME = "orderTime";
	public static final String F_NAME = "name";
	public static final String F_GENDER = "gender";
	public static final String F_AGE = "age";
	public static final String F_DOCTYPE = "docType";
	public static final String F_DOCNO = "docNo";
	public static final String F_PHONE = "phone";
	public static final String F_SETMEALNAME = "setmealName";
	public static final String F_HEIGHT = "height";
	public static final String F_WEIGHT = "weight";
	public static final String F_FAMILYHISTORY = "familyHistory";
	public static final String F_GUARDIANNAME = "guardianName";
	public static final String F_GUARDIANTEL = "guardianTel";
	public static final String F_RELATIONSHIP = "relationship";
	public static final String F_SAMPLEADDRESS = "sampleAddress";
	public static final String F_REPORTADDRESS = "reportAddress";
	public static final String F_DATASOURCE = "dataSource";
	public static final String F_UPDATETIME = "updateTime";
	public static final String F_CREATETIME = "createTime";
	
	public ErpOrder() {
		super();
	}

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

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSetmealName() {
		return setmealName;
	}

	public void setSetmealName(String setmealName) {
		this.setmealName = setmealName;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getFamilyHistory() {
		return familyHistory;
	}

	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getGuardianTel() {
		return guardianTel;
	}

	public void setGuardianTel(String guardianTel) {
		this.guardianTel = guardianTel;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
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

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ErpOrder [id=" + id + ", orderNo=" + orderNo + ", oderTime="
				+ orderTime + ", name=" + name + ", gender=" + gender + ", age="
				+ age + ", docType=" + docType + ", docNo=" + docNo
				+ ", phone=" + phone + ", setmealName=" + setmealName
				+ ", height=" + height + ", weight=" + weight
				+ ", familyHistory=" + familyHistory + ", guardianName="
				+ guardianName + ", guardianTel=" + guardianTel
				+ ", relationship=" + relationship + ", sampleAddress="
				+ sampleAddress + ", reportAddress=" + reportAddress
				+ ", dataSource=" + dataSource + ", updateTime=" + updateTime
				+ ", createTime=" + createTime + "]";
	}
	
}
