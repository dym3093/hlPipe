/**
 * @author DengYouming
 * @since 2016-7-25 下午5:53:58
 */
package org.hpin.webservice.bean.jz;

import java.io.Serializable;

/**
 * 【金埻开放平台】地址结构
 * @author DengYouming
 * @since 2016-7-25 下午5:53:58
 */
public class Address implements Serializable{

	private static final long serialVersionUID = -1831326550969822278L;

	/** 1. 省份ID */
	private Integer provinceId;
	/** 2. 省份名称  */
	private String provinceName;
	/** 3. 服务地址城市Id */
	private Integer cityId;
	/** 4. 服务地址城市  */
	private String cityName;
	/** 5. 服务地址区县ID */
	private Integer districtId;
	/** 6. 服务地址区县 */
	private String districtName;
	/** 7. 服务详细地址 */
	private String address;
	/** 8. 邮编 */
	private String zipCode;
	
	public static String F_PROVINCEID = "provinceId";
	public static String F_PROVINCENAME = "provinceName";
	public static String F_CITYID = "cityId";
	public static String F_CITYNAME = "cityName";
	public static String F_DISTRICTID = "districtId";
	public static String F_DISTRICTNAME = "districtName";
	public static String F_ADDRESS = "address";
	public static String F_ZIPCODE = "zipCode";
	
	public Address() {
		super();
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
}
