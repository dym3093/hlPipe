package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 报告图片信息表
 * @author Damian
 * @since 2017-02-06
 */
public class ErpReportdetailImgtask extends BaseEntity {

    /** 1. VARCHAR2(100 BYTE) 主键 */
    private String id;
    /** 2. VARCHAR2(100 BYTE) erp_customer表ID */
    private String customerId;
    /** 3. VARCHAR2(100 BYTE) 条码 */
    private String code;
    /** 4. VARCHAR2(100 BYTE) 会员姓名 */
    private String userName;
    /** 5. NUMBER 会员电话 */
    private String phoneNo;
    /** 6. VARCHAR2(20 BYTE) 出生日期 */
    private String birthday;
    /** 7. NUMBER 图片生成状态,0:未生成;1：已生成 */
    private Integer state;
    /** 8. DATE 创建时间 */
    private Date createTime;
    /** 9. NUMBER 是否删除,0：有效;1：已删除 */
    private Integer isDeleted;
    /** 10. VARCHAR2(100 BYTE) 身份证号 */
    private String idNo;
    
	private String pdfName;
	private String filePath;
	private String batchNo;

    public static final String F_ID = "id";
    public static final String F_CUSTOMERID = "customerId";
    public static final String F_CODE = "code";
    public static final String F_USERNAME = "userName";
    public static final String F_PHONENO = "phoneNo";
    public static final String F_BIRTHDAY = "birthday";
    public static final String F_STATE = "state";
    public static final String F_CREATETIME = "createTime";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_IDNO = "idNo";

    public ErpReportdetailImgtask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
    
}
