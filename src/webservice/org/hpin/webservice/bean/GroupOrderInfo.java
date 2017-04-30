package org.hpin.webservice.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hpin.common.core.orm.BaseEntity;
import org.hpin.webservice.util.JaxbDateAdapter;

@XmlRootElement(name="groupOrderInfo") 
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupOrderInfo extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private String id;//主键
	
	private String orderNo;//订单号
	
	@XmlJavaTypeAdapter(JaxbDateAdapter.class)
	private Date orderDate;//订单日期
	
	private String name;//姓名
	
	private String phone;//手机号
	
	private String address;//地址
	
	private String type;//类型
	
	private BigDecimal price;//价格

	@XmlElement(name="comboItem", type=GroupOrderCombo.class)
	@XmlElementWrapper(name="comboList")
	private List<GroupOrderCombo> comboList = new ArrayList<GroupOrderCombo>(); //套餐列表
	
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
	
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<GroupOrderCombo> getComboList() {
		return comboList;
	}

	public void setComboList(List<GroupOrderCombo> comboList) {
		this.comboList = comboList;
	}

	
}
