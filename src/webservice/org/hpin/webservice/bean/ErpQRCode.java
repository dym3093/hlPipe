package org.hpin.webservice.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hpin.common.core.orm.BaseEntity;

/**
 * 二维码
 * @author tangxing
 * @date 2016-8-18上午11:31:32
 */
@XmlRootElement(name="eventQRCodeInfo") 
@XmlAccessorType(XmlAccessType.FIELD)
public class ErpQRCode extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String eventsNo;
	
	private Date eventsDate;
	
	private String provinceName;	//省份名字
	
	private String provinceId;
	
	private String cityName;		//城市名字
	
	private String cityId;
	
	private String banchCompanyName;//支公司名
	
	private String banchCompanyId;
	
	private String ownedCompanyName;//所属公司名
	
	private String ownedCompanyId;	
	
	private String combo;			//套餐
	
	private String level;			//等级
	
	private String expectNum;		//预计人数
	
	private String batchNo;			//批次号
	
	private String QRCodeName;		//二维码name
	
	private String QRCodePath;		//二维码路径
	
	private String QRCodeLocalPath;	//在线预览地址
	
	/**
	 * 二维码状态
	 * 0--未生成  1--处理中  2--已生成  3--已失效  
	 */
	private String QRCodeStatus;
	
	private String keyword;			//关键字
	
	private Date expiryDate;		//有效期
	
    private String createUserName;//创建者
    
    private Date createTime;//创建时间
    
    private String updateUserName;//更新用户名
    
    private Date updateTime;//更新时间
	
	private String isDelete;		//是否删除
	
	private String eventsId; //场次id

	public String getEventsId() {
		return eventsId;
	}

	public void setEventsId(String eventsId) {
		this.eventsId = eventsId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getQRCodeLocalPath() {
		return QRCodeLocalPath;
	}

	public void setQRCodeLocalPath(String qRCodeLocalPath) {
		QRCodeLocalPath = qRCodeLocalPath;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getEventsNo() {
		return eventsNo;
	}

	public void setEventsNo(String eventsNo) {
		this.eventsNo = eventsNo;
	}
	
	public Date getEventsDate() {
		return eventsDate;
	}

	public void setEventsDate(Date eventsDate) {
		this.eventsDate = eventsDate;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getBanchCompanyName() {
		return banchCompanyName;
	}

	public void setBanchCompanyName(String banchCompanyName) {
		this.banchCompanyName = banchCompanyName;
	}

	public String getBanchCompanyId() {
		return banchCompanyId;
	}

	public void setBanchCompanyId(String banchCompanyId) {
		this.banchCompanyId = banchCompanyId;
	}

	public String getOwnedCompanyName() {
		return ownedCompanyName;
	}

	public void setOwnedCompanyName(String ownedCompanyName) {
		this.ownedCompanyName = ownedCompanyName;
	}

	public String getOwnedCompanyId() {
		return ownedCompanyId;
	}

	public void setOwnedCompanyId(String ownedCompanyId) {
		this.ownedCompanyId = ownedCompanyId;
	}

	public String getCombo() {
		return combo;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getExpectNum() {
		return expectNum;
	}

	public void setExpectNum(String expectNum) {
		this.expectNum = expectNum;
	}

	public String getQRCodeStatus() {
		return QRCodeStatus;
	}

	public void setQRCodeStatus(String qRCodeStatus) {
		QRCodeStatus = qRCodeStatus;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getQRCodeName() {
		return QRCodeName;
	}

	public void setQRCodeName(String qRCodeName) {
		QRCodeName = qRCodeName;
	}

	public String getQRCodePath() {
		return QRCodePath;
	}

	public void setQRCodePath(String qRCodePath) {
		QRCodePath = qRCodePath;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "ErpQRCode [id=" + id + ", eventsNo=" + eventsNo
				+ ", eventsDate=" + eventsDate + ", provinceName="
				+ provinceName + ", provinceId=" + provinceId + ", cityName="
				+ cityName + ", cityId=" + cityId + ", banchCompanyName="
				+ banchCompanyName + ", banchCompanyId=" + banchCompanyId
				+ ", ownedCompanyName=" + ownedCompanyName
				+ ", ownedCompanyId=" + ownedCompanyId + ", combo=" + combo
				+ ", level=" + level + ", expectNum=" + expectNum
				+ ", batchNo=" + batchNo + ", QRCodeName=" + QRCodeName
				+ ", QRCodePath=" + QRCodePath + ", QRCodeStatus="
				+ QRCodeStatus + ", keyword=" + keyword + ", expiryDate="
				+ expiryDate + ", createUserName=" + createUserName
				+ ", createTime=" + createTime + ", updateUserName="
				+ updateUserName + ", updateTime=" + updateTime + ", isDelete="
				+ isDelete + "]";
	}

}
