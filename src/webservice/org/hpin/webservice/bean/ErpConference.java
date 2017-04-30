/**
 * 
 */
package org.hpin.webservice.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;

/**
* 公司名称: 远盟康健(北京)科技有限公司
* 
* author: dengqin 
* createDate: 2016-3-29 上午11:53:42
*/
/**
 * @author dengqin
 *
 */
public class ErpConference extends BaseEntity{

    private String id;
    private String conferenceType;//会议类型
    private String conferenceNo;//会议号
    private Date conferenceDate;//会议日期
    private String address;//会议地址
    private String branchCompany;//支公司
    private String branchCompanyId;//支公司ID
    private String ownedCompany;//所属公司
    private String ownedCompanyId;//所属公司ID
    private Integer headcount;//预计人数
    private Integer nowHeadcount;//现有人数
    private Integer isDeleted;//是否删除
    private Integer isExpress;//是否寄快递
    private String createUserName;//创建者
    private Date createTime;//创建时间
    private String updateUserName;//更新用户名
    private Date updateTime;//更新时间
    private String ename;       //快递姓名
    private String etrackingNumber;//快递单号
    private Date edate;//快递时间
    private String hour;//时间
    private String provice; //省份
    private String city;//城市
    private Integer settNumbers;	//结算的人数
    private Double produceCost;	//产生的费用
    private String proUser;		//项目负责人
    private String proCode;		//项目编码
    private String proBelong;	//项目名称
    private String customerRelationShipProId; //HL_CUSTOMER_RELATIONSHIP_PRO的ID  create by henry.xu 20160919; 
    private String cancelReason; //取消原因  create by sirius.ma 20161122;
    
    
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getCustomerRelationShipProId() {
		return customerRelationShipProId;
	}
	public void setCustomerRelationShipProId(String customerRelationShipProId) {
		this.customerRelationShipProId = customerRelationShipProId;
	}
	public String getProUser() {
		return proUser;
	}
	public void setProUser(String proUser) {
		this.proUser = proUser;
	}
	public String getProCode() {
		return proCode;
	}
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	public String getProBelong() {
		return proBelong;
	}
	public void setProBelong(String proBelong) {
		this.proBelong = proBelong;
	}
	/**
     * @return the ownedCompanyId
     */
    public String getOwnedCompanyId() {
        return ownedCompanyId;
    }
    /**
     * @param ownedCompanyId the ownedCompanyId to set
     */
    public void setOwnedCompanyId(String ownedCompanyId) {
        this.ownedCompanyId = ownedCompanyId;
    }
    /**
     * @return the branchCompanyId
     */
    public String getBranchCompanyId() {
        return branchCompanyId;
    }
    /**
     * @param branchCompanyId the branchCompanyId to set
     */
    public void setBranchCompanyId(String branchCompanyId) {
        this.branchCompanyId = branchCompanyId;
    }
    /**
     * @return the conferenceType
     */
    public String getConferenceType() {
        return conferenceType;
    }
    /**
     * @param conferenceType the conferenceType to set
     */
    public void setConferenceType(String conferenceType) {
        this.conferenceType = conferenceType;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the conferenceNo
     */
    public String getConferenceNo() {
        return conferenceNo;
    }
    /**
     * @param conferenceNo the conferenceNo to set
     */
    public void setConferenceNo(String conferenceNo) {
        this.conferenceNo = conferenceNo;
    }
    /**
     * @return the conferenceDate
     */
    public Date getConferenceDate() {
        return conferenceDate;
    }
    /**
     * @param conferenceDate the conferenceDate to set
     */
    public void setConferenceDate(Date conferenceDate) {
        this.conferenceDate = conferenceDate;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the branchCompany
     */
    public String getBranchCompany() {
        return branchCompany;
    }
    /**
     * @param branchCompany the branchCompany to set
     */
    public void setBranchCompany(String branchCompany) {
        this.branchCompany = branchCompany;
    }
    /**
     * @return the ownedCompany
     */
    public String getOwnedCompany() {
        return ownedCompany;
    }
    /**
     * @param ownedCompany the ownedCompany to set
     */
    public void setOwnedCompany(String ownedCompany) {
        this.ownedCompany = ownedCompany;
    }
    /**
     * @return the headcount
     */
    public Integer getHeadcount() {
        return headcount;
    }
    /**
     * @param headcount the headcount to set
     */
    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }
    /**
     * @return the nowHeadcount
     */
    public Integer getNowHeadcount() {
        return nowHeadcount;
    }
    /**
     * @param nowHeadcount the nowHeadcount to set
     */
    public void setNowHeadcount(Integer nowHeadcount) {
        this.nowHeadcount = nowHeadcount;
    }
    /**
     * @return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }
    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    /**
     * @return the isExpress
     */
    public Integer getIsExpress() {
        return isExpress;
    }
    /**
     * @param isExpress the isExpress to set
     */
    public void setIsExpress(Integer isExpress) {
        this.isExpress = isExpress;
    }
    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }
    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * @return the updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    /**
     * @param updateUserName the updateUserName to set
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * @return the ename
     */
    public String getEname() {
        return ename;
    }
    /**
     * @param ename the ename to set
     */
    public void setEname(String ename) {
        this.ename = ename;
    }
    /**
     * @return the etrackingNumber
     */
    public String getEtrackingNumber() {
        return etrackingNumber;
    }
    /**
     * @param etrackingNumber the etrackingNumber to set
     */
    public void setEtrackingNumber(String etrackingNumber) {
        this.etrackingNumber = etrackingNumber;
    }
    /**
     * @return the edate
     */
    public Date getEdate() {
        return edate;
    }
    /**
     * @param edate the edate to set
     */
    public void setEdate(Date edate) {
        this.edate = edate;
    }
    /**
     * @return the hour
     */
    public String getHour() {
        return hour;
    }
    /**
     * @param hour the hour to set
     */
    public void setHour(String hour) {
        this.hour = hour;
    }
    /**
     * @return the provice
     */
    public String getProvice() {
        return provice;
    }
    /**
     * @param provice the provice to set
     */
    public void setProvice(String provice) {
        this.provice = provice;
    }
    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
	public Integer getSettNumbers() {
		return settNumbers;
	}
	public void setSettNumbers(Integer settNumbers) {
		this.settNumbers = settNumbers;
	}
	public Double getProduceCost() {
		return produceCost;
	}
	public void setProduceCost(Double produceCost) {
		this.produceCost = produceCost;
	}
	
}
