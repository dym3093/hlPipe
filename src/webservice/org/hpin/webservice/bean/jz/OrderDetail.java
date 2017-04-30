/**
 * @author DengYouming
 * @since 2016-7-25 下午5:37:38
 */
package org.hpin.webservice.bean.jz;

import java.io.Serializable;

/**
 * 【金埻开放平台】团单详情类
 * @author DengYouming
 * @since 2016-7-25 下午5:37:38
 */
public class OrderDetail implements Serializable{

	private static final long serialVersionUID = 3537424890430412933L;

	/** 1. 服务唯一标示 */
	private String serviceId;
	/** 2. 合作方预约时提交的ID */
	private String corServiceId;
	/** 3. 团单名称 */
	private String name;
	/** 4. 团单时间 */
	private String bookTime;
	/** 5. 服务状态 详情见 团单状态列表 */
	private Integer state;
	/** 6. 联系人 */
	private Contact contact;
	/** 7. 地址 */
	private Address address;
	/** 8. 服务项目 */
	private String serviceItems;
	
	public OrderDetail() {
		super();
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCorServiceId() {
		return corServiceId;
	}

	public void setCorServiceId(String corServiceId) {
		this.corServiceId = corServiceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getServiceItems() {
		return serviceItems;
	}

	public void setServiceItems(String serviceItems) {
		this.serviceItems = serviceItems;
	}
	
}
