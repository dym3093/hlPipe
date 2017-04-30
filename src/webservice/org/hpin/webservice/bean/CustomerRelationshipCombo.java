package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

/**
 * HlLinkmanRelation entity. @author MyEclipse Persistence Tools
 */

public class CustomerRelationshipCombo extends BaseEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2349608123383103070L;
	private String id;
	/**branycompan perimay ID*/
	private String customerRelationShipId;
	/**comboID*/
	private String comboId;
	/**模块类型01合约02产品*/
	private String name;
	private CustomerRelationShip customerRelationShip;
    private Combo combo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerRelationShipId() {
		return customerRelationShipId;
	}
	public void setCustomerRelationShipId(String customerRelationShipId) {
		this.customerRelationShipId = customerRelationShipId;
	}
	public String getComboId() {
		return comboId;
	}
	public void setComboId(String comboId) {
		this.comboId = comboId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CustomerRelationShip getCustomerRelationShip() {
		return customerRelationShip;
	}
	public void setCustomerRelationShip(CustomerRelationShip customerRelationShip) {
		this.customerRelationShip = customerRelationShip;
	}
	public Combo getCombo() {
		return combo;
	}
	public void setCombo(Combo combo) {
		this.combo = combo;
	}
    
	
}