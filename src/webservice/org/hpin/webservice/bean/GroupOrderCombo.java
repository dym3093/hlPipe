package org.hpin.webservice.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hpin.common.core.orm.BaseEntity;

/**
 * 团购订单套餐
 * @author ybc
 * @since 2016-06-22
 */
@XmlRootElement(name="comboItem") 
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupOrderCombo extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private String id;//主键
	
	private String orderNo;//订单号
	
	private String comboName;//套餐名
	
	private String comboNum;//套餐数量

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

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	public String getComboNum() {
		return comboNum;
	}

	public void setComboNum(String comboNum) {
		this.comboNum = comboNum;
	}
	
	
}
