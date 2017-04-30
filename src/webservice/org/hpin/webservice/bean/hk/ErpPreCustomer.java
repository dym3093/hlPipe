package org.hpin.webservice.bean.hk;

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 弘康客户信息预存表
 * Created by Damian on 16-12-28.
 */
public class ErpPreCustomer extends BaseEntity{

    /** 1 .	VARCHAR2(32 BYTE) 主键id */
    private String id;
    /** 2 .	VARCHAR2(200 BYTE) 投保人姓名 */
    private String applicantName;
    /** 3 .	VARCHAR2(200 BYTE) 投保人身份证号 */
    private String applicantIdcard;
    /** 4 .	VARCHAR2(100 BYTE) 投保人手机号 */
    private String applicantPhone;
    /** 5 .	VARCHAR2(10 BYTE) 投保人性别 */
    private String applicantSex;
    /** 6 .	VARCHAR2(200 BYTE) 投保人年龄 */
    private String applicantAge;
    /** 7 .	VARCHAR2(200 BYTE) 被检人姓名 */
    private String wereName;
    /** 8 .	VARCHAR2(200 BYTE) 被检人身份证号 */
    private String wereIdcard;
    /** 9 .	VARCHAR2(100 BYTE) 被检人手机号 */
    private String werePhone;
    /** 10 .	VARCHAR2(10 BYTE) 被检人性别 */
    private String wereSex;
    /** 11 .	VARCHAR2(200 BYTE) 被检人年龄 */
    private String wereAge;
    /** 12 .	VARCHAR2(200 BYTE) 检测盒邮寄地址 */
    private String checkboxEmilAddr;
    /** 13 .	VARCHAR2(200 BYTE) 报告寄送地址 */
    private String reportSendAddr;
    /** 14 .	VARCHAR2(200 BYTE) 检测套餐 */
    private String checkCobmo;
    /** 15 .	VARCHAR2(200 BYTE) 保单号 */
    private String numPolicy;
    /** 16 .	DATE 订单生成日期 */
    private Date orderCreateDate;
    /** 17 .	VARCHAR2(200 BYTE) 备注 */
    private String remark;
    /** 18 .	VARCHAR2(200 BYTE) 场次号 */
    private String eventsNo;
    /** 19 .	VARCHAR2(200 BYTE) 被检人身高 */
    private String wereHeight;
    /** 20 .	VARCHAR2(200 BYTE) 体重 */
    private String wereWeight;
    /** 21 .	VARCHAR2(200 BYTE) 家族病史 */
    private String familyHistory;
    /** 22 .	VARCHAR2(200 BYTE) 既往病史 */
    private String customerHistory;
    /** 23 .	VARCHAR2(200 BYTE) 报告接收人姓名 */
    private String reportReceiveName;
    /** 24 .	VARCHAR2(200 BYTE) 电话 */
    private String phone;
    /** 25 .	VARCHAR2(100 BYTE) 地址 */
    private String address;
    /** 26 .	DATE 创建日期 */
    private Date createTime;
    /** 27 .	VARCHAR2(32 BYTE) 创建人 */
    private String createUserId;
    /** 28 .	DATE 修改日期 */
    private Date updateTime;
    /** 29 .	VARCHAR2(32 BYTE) 修改人 */
    private String updateUserId;
    /** 30 .	VARCHAR2(32 BYTE) 条码 */
    private String code;
    /** 31 .	VARCHAR2(32 BYTE) ERP_CUSTOMER表的ID */
    private String erpCustomerId;
    /** 32.     VARCAHR2(32 BYTE) 申请单号ID, 表(erp_application) */
    private String erpApplicationId;

    private String ymCombo; //远盟套餐;
    private String emsNumber; //快递单号;   create by henry.xu 20170117
    private String companyId ; //支公司ID;  create by henry.xu 20170118
    private String shipPorId;  // 项目id;  create by henry.xu 20170118

    private String batchNo;		//批次号(修改版，增加批次号); add by leslieTong. 2017-2-13

    private Integer statusYm;	//客户状态(修改版); add by leslieTong. 2017-2-14
    
    private String batchPre; //批次前置(HK) create by henry.xu 20170217

    /* 平安新增字段  */
    /** 履约单号（客户订单号）  **/
    private String performNo;

    /** 套餐详细检测项 */
    private String detailedTestItem;

    /** 履约单状态 （2：申请中；  4：检测中；  5：检测失败；  6：完成；）*/
    private String performStatus;

    /** 检测失败原因 */
    private String checkFailCaus;

    /** 对方系统报告名称 */
    private String systemReportName;

    /** 确认受检时间  */
    private Date confirmCaryTime;

    /** 上传报告时间  */
    private Date uploadReportTime;

    /* 监护人信息 */
    /** 监护人姓名 */
    private String guardianName;
    /** 关系 */
    private String relationship;

    /** 报告地址 */
    private String filePath;
    
    /** 天津邮政检测码 */
    private String authId;
    

    public static final String F_ID = "id";
    public static final String F_APPLICANTNAME = "applicantName";
    public static final String F_APPLICANTIDCARD = "applicantIdcard";
    public static final String F_APPLICANTPHONE = "applicantPhone";
    public static final String F_APPLICANTSEX = "applicantSex";
    public static final String F_APPLICANTAGE = "applicantAge";
    public static final String F_WERENAME = "wereName";
    public static final String F_WEREIDCARD = "wereIdcard";
    public static final String F_WEREPHONE = "werePhone";
    public static final String F_WERESEX = "wereSex";
    public static final String F_WEREAGE = "wereAge";
    public static final String F_CHECKBOXEMILADDR = "checkboxEmilAddr";
    public static final String F_REPORTSENDADDR = "reportSendAddr";
    public static final String F_CHECKCOBMO = "checkCobmo";
    public static final String F_NUMPOLICY = "numPolicy";
    public static final String F_ORDERCREATEDATE = "orderCreateDate";
    public static final String F_REMARK = "remark";
    public static final String F_EVENTSNO = "eventsNo";
    public static final String F_WEREHEIGHT = "wereHeight";
    public static final String F_WEREWEIGHT = "wereWeight";
    public static final String F_FAMILYHISTORY = "familyHistory";
    public static final String F_CUSTOMERHISTORY = "customerHistory";
    public static final String F_REPORTRECEIVENAME = "reportReceiveName";
    public static final String F_PHONE = "phone";
    public static final String F_ADDRESS = "address";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";
    public static final String F_CODE = "code";
    public static final String F_ERPCUSTOMERID= "erpCustomerId";
    public static final String F_ERPAPPLICATIONID = "erpApplicationId";
    public static final String F_YMCOMBO = "ymCombo";
    public static final String F_EMSNUMBER = "emsNumber";
    public static final String F_COMPANYID = "companyId ";
    public static final String F_SHIPPORID = "shipPorId";
    public static final String F_AUTHID = "authId";


    public ErpPreCustomer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantIdcard() {
        return applicantIdcard;
    }

    public void setApplicantIdcard(String applicantIdcard) {
        this.applicantIdcard = applicantIdcard;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantSex() {
        return applicantSex;
    }

    public void setApplicantSex(String applicantSex) {
        this.applicantSex = applicantSex;
    }

    public String getApplicantAge() {
        return applicantAge;
    }

    public void setApplicantAge(String applicantAge) {
        this.applicantAge = applicantAge;
    }

    public String getWereName() {
        return wereName;
    }

    public void setWereName(String wereName) {
        this.wereName = wereName;
    }

    public String getWereIdcard() {
        return wereIdcard;
    }

    public void setWereIdcard(String wereIdcard) {
        this.wereIdcard = wereIdcard;
    }

    public String getWerePhone() {
        return werePhone;
    }

    public void setWerePhone(String werePhone) {
        this.werePhone = werePhone;
    }

    public String getWereSex() {
        return wereSex;
    }

    public void setWereSex(String wereSex) {
        this.wereSex = wereSex;
    }

    public String getWereAge() {
        return wereAge;
    }

    public void setWereAge(String wereAge) {
        this.wereAge = wereAge;
    }

    public String getCheckboxEmilAddr() {
        return checkboxEmilAddr;
    }

    public void setCheckboxEmilAddr(String checkboxEmilAddr) {
        this.checkboxEmilAddr = checkboxEmilAddr;
    }

    public String getReportSendAddr() {
        return reportSendAddr;
    }

    public void setReportSendAddr(String reportSendAddr) {
        this.reportSendAddr = reportSendAddr;
    }

    public String getCheckCobmo() {
        return checkCobmo;
    }

    public void setCheckCobmo(String checkCobmo) {
        this.checkCobmo = checkCobmo;
    }

    public String getNumPolicy() {
        return numPolicy;
    }

    public void setNumPolicy(String numPolicy) {
        this.numPolicy = numPolicy;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEventsNo() {
        return eventsNo;
    }

    public void setEventsNo(String eventsNo) {
        this.eventsNo = eventsNo;
    }

    public String getWereHeight() {
        return wereHeight;
    }

    public void setWereHeight(String wereHeight) {
        this.wereHeight = wereHeight;
    }

    public String getWereWeight() {
        return wereWeight;
    }

    public void setWereWeight(String wereWeight) {
        this.wereWeight = wereWeight;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getCustomerHistory() {
        return customerHistory;
    }

    public void setCustomerHistory(String customerHistory) {
        this.customerHistory = customerHistory;
    }

    public String getReportReceiveName() {
        return reportReceiveName;
    }

    public void setReportReceiveName(String reportReceiveName) {
        this.reportReceiveName = reportReceiveName;
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

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getCreateUserId() {
        return createUserId;
    }

    @Override
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getUpdateUserId() {
        return updateUserId;
    }

    @Override
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErpCustomerId() {
        return erpCustomerId;
    }

    public void setErpCustomerId(String erpCustomerId) {
        this.erpCustomerId = erpCustomerId;
    }

    public String getErpApplicationId() {
        return erpApplicationId;
    }

    public void setErpApplicationId(String erpApplicationId) {
        this.erpApplicationId = erpApplicationId;
    }

    public String getYmCombo() {
        return ymCombo;
    }

    public void setYmCombo(String ymCombo) {
        this.ymCombo = ymCombo;
    }

    public String getEmsNumber() {
        return emsNumber;
    }

    public void setEmsNumber(String emsNumber) {
        this.emsNumber = emsNumber;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getShipPorId() {
        return shipPorId;
    }

    public void setShipPorId(String shipPorId) {
        this.shipPorId = shipPorId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getStatusYm() {
        return statusYm;
    }

    public void setStatusYm(Integer statusYm) {
        this.statusYm = statusYm;
    }

    public String getPerformNo() {
        return performNo;
    }

    public void setPerformNo(String performNo) {
        this.performNo = performNo;
    }

    public String getDetailedTestItem() {
        return detailedTestItem;
    }

    public void setDetailedTestItem(String detailedTestItem) {
        this.detailedTestItem = detailedTestItem;
    }

    public String getPerformStatus() {
        return performStatus;
    }

    public void setPerformStatus(String performStatus) {
        this.performStatus = performStatus;
    }

    public String getCheckFailCaus() {
        return checkFailCaus;
    }

    public void setCheckFailCaus(String checkFailCaus) {
        this.checkFailCaus = checkFailCaus;
    }

    public String getSystemReportName() {
        return systemReportName;
    }

    public void setSystemReportName(String systemReportName) {
        this.systemReportName = systemReportName;
    }

    public Date getConfirmCaryTime() {
        return confirmCaryTime;
    }

    public void setConfirmCaryTime(Date confirmCaryTime) {
        this.confirmCaryTime = confirmCaryTime;
    }

    public Date getUploadReportTime() {
        return uploadReportTime;
    }

    public void setUploadReportTime(Date uploadReportTime) {
        this.uploadReportTime = uploadReportTime;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBatchPre() {
		return batchPre;
	}

	public void setBatchPre(String batchPre) {
		this.batchPre = batchPre;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	@Override
    public String toString() {
        return "ErpPreCustomer{" +
                "id='" + id + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", applicantIdcard='" + applicantIdcard + '\'' +
                ", applicantPhone='" + applicantPhone + '\'' +
                ", applicantSex='" + applicantSex + '\'' +
                ", applicantAge='" + applicantAge + '\'' +
                ", wereName='" + wereName + '\'' +
                ", wereIdcard='" + wereIdcard + '\'' +
                ", werePhone='" + werePhone + '\'' +
                ", wereSex='" + wereSex + '\'' +
                ", wereAge='" + wereAge + '\'' +
                ", checkboxEmilAddr='" + checkboxEmilAddr + '\'' +
                ", reportSendAddr='" + reportSendAddr + '\'' +
                ", checkCobmo='" + checkCobmo + '\'' +
                ", numPolicy='" + numPolicy + '\'' +
                ", orderCreateDate=" + orderCreateDate +
                ", remark='" + remark + '\'' +
                ", eventsNo='" + eventsNo + '\'' +
                ", wereHeight='" + wereHeight + '\'' +
                ", wereWeight='" + wereWeight + '\'' +
                ", familyHistory='" + familyHistory + '\'' +
                ", customerHistory='" + customerHistory + '\'' +
                ", reportReceiveName='" + reportReceiveName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", code='" + code + '\'' +
                ", erpCustomerId='" + erpCustomerId + '\'' +
                ", erpApplicationId='" + erpApplicationId + '\'' +
                ", ymCombo='" + ymCombo + '\'' +
                ", emsNumber='" + emsNumber + '\'' +
                ", companyId='" + companyId + '\'' +
                ", shipPorId='" + shipPorId + '\'' +
                '}';
    }
}
