/**
 * @author DengYouming
 * @since 2016-7-25 下午5:46:23
 */
package org.hpin.webservice.bean.jz;

import java.io.Serializable;
import java.util.Date;

/**
 * 【金埻开放平台】联系人
 * @author DengYouming
 * @since 2016-7-25 下午5:46:23
 */
public class Contact implements Serializable{

	private static final long serialVersionUID = -4006501547616261162L;
	
	/** id */
	private String id;
	/** 联系人姓名 */
	private String name;
	/** 联系人性别 1-女性, 2 - 男性 */
	private Integer gender;
	/** 联系人电话 */
	private String phone;
	
	private Date createDate;
	
	public static String F_ID = "id";
	public static String F_NAME = "name";
	public static String F_GENDER = "gender";
	public static String F_PHONE = "phone";
	
	public Contact() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

/*	@Override
	public String toString() {
		return "ContactJZ [id=" + id + ", name=" + name + ", gender=" + gender
				+ ", phone=" + phone + "]";
	}*/
	
}
