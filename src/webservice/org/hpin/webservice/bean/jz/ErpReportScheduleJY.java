package org.hpin.webservice.bean.jz;/**
 * Created by admin on 2016/11/29.
 */

import org.hpin.common.core.orm.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时获取报告任务表(金域)
 *
 * @author YoumingDeng
 * @create 2016-11-29 15:40
 */
public class ErpReportScheduleJY extends BaseEntity {

    /**
     * 1. ID VARCHAR2(32)
     */
    private String id;
    /**
     * 2. ERP_REPORT_ORG_JY表ID VARCHAR2(32)
     */
    private String idRelated;
    /**
     * 3. 条形码 VARCHAR2(256)
     */
    private String code;
    /**
     * 4. 姓名 VARCHAR2(256)
     */
    private String name;
    /**
     * 5. 身份证号/证件号 VARCHAR2(256)
     */
    private String idNo;
    /**
     * 6. 性别 VARCHAR2(32)
     */
    private String gender;
    /**
     * 7. 生日 DATE
     */
    private Date birthday;
    /**
     * 8. 电话 VARCHAR2(256)
     */
    private String phone;
    /**
     * 9. 套餐名 VARCHAR2(256)
     */
    private String combo;
    /**
     * 10. 场次号 VARCHAR2(256)
     */
    private String eventsNo;
    /**
     * 11. 批次号 VARCHAR2(256)
     */
    private String batchNo;
    /**
     * 12. 团单号(对应金域serviceId) VARCHAR2(256)
     */
    private String groupOrderNo;
    /**
     * 13. 报告ID（来自金域） VARCHAR2(100)
     */
    private String reportId;
    /**
     * 14. 报告名称 VARCHAR2(256)
     */
    private String reportName;
    /**
     * 15. 采样日期 DATE
     */
    private Date samplingDate;
    /**
     * 16. 接受到报告日期 DATE
     */
    private Date entryDate;
    /**
     * 17. 发布报告时间 DATE
     */
    private Date publishedDate;
    /**
     * 18. 备注 VARCHAR2(1024)
     */
    private String remark;
    /**
     * 19. 是否删除 0-否1-是 NUMBER
     */
    private Integer isDeleted;
    /**
     * 20. 创建时间 DATE
     */
    private Date createTime;
    /**
     * 21. 创建人ID VARCHAR2(100)
     */
    private String createUserId;
    /**
     * 22. 创建人姓名 VARCHAR2(256)
     */
    private String createUserName;
    /**
     * 23. 修改时间 DATE
     */
    private Date updateTime;
    /**
     * 24. 修改人ID VARCHAR2(100)
     */
    private String updateUserId;
    /**
     * 25. 修改人姓名 VARCHAR2(256)
     */
    private String updateUserName;
    /**
     * 26. 状态(0:未完成，1：已完成) NUMBER
     */
    private Integer status;
    /**
     * 27. 操作次数 NUMBER
     */
    private Integer counter;

    public static final String F_ID = "id";
    public static final String F_IDRELATED = "idRelated";
    public static final String F_CODE = "code";
    public static final String F_NAME = "name";
    public static final String F_IDNO = "idNo";
    public static final String F_GENDER = "gender";
    public static final String F_BIRTHDAY = "birthday";
    public static final String F_PHONE = "phone";
    public static final String F_COMBO = "combo";
    public static final String F_EVENTSNO = "eventsNo";
    public static final String F_BATCHNO = "batchNo";
    public static final String F_GROUPORDERNO = "groupOrderNo";
    public static final String F_REPORTID = "reportId";
    public static final String F_REPORTNAME = "reportName";
    public static final String F_SAMPLINGDATE = "samplingDate";
    public static final String F_ENTRYDATE = "entryDate";
    public static final String F_PUBLISHEDDATE = "publishedDate";
    public static final String F_REMARK = "remark";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_CREATEUSERNAME = "createUserName";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";
    public static final String F_UPDATEUSERNAME = "updateUserName";
    public static final String F_STATUS = "status";
    public static final String F_COUNTER = "counter";

    public ErpReportScheduleJY() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRelated() {
        return idRelated;
    }

    public void setIdRelated(String idRelated) {
        this.idRelated = idRelated;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getEventsNo() {
        return eventsNo;
    }

    public void setEventsNo(String eventsNo) {
        this.eventsNo = eventsNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getGroupOrderNo() {
        return groupOrderNo;
    }

    public void setGroupOrderNo(String groupOrderNo) {
        this.groupOrderNo = groupOrderNo;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(Date samplingDate) {
        this.samplingDate = samplingDate;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Integer getIsDeleted() {
        return isDeleted;
    }

    @Override
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "ErpReportScheduleJY{" +
                "id='" + id + '\'' +
                ", idRelated='" + idRelated + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", idNo='" + idNo + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", combo='" + combo + '\'' +
                ", eventsNo='" + eventsNo + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", groupOrderNo='" + groupOrderNo + '\'' +
                ", reportId='" + reportId + '\'' +
                ", reportName='" + reportName + '\'' +
                ", samplingDate=" + samplingDate +
                ", entryDate=" + entryDate +
                ", publishedDate=" + publishedDate +
                ", remark='" + remark + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", createUserName='" + createUserName + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateUserName='" + updateUserName + '\'' +
                ", status=" + status +
                ", counter=" + counter +
                '}';
    }
}
