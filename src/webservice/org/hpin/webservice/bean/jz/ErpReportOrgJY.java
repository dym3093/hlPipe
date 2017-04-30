package org.hpin.webservice.bean.jz;/**
 * Created by admin on 2016/11/28.
 */

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 原始报告信息表(金域)
 *
 * @author YoumingDeng
 * @create 2016-11-28 11:36
 */
public class ErpReportOrgJY extends BaseEntity{

    /** 1. ID VARCHAR2(32) */
    private String id;
    /** 2. 报告ID（来自金域） VARCHAR2(100) */
    private String reportId;
    /** 3. 报告名称 VARCHAR2(256) */
    private String reportName;
    /** 4. 条码(唯一的) VARCHAR2(256) */
    private String barcode;
    /** 5. 服务ID（团单号） VARCHAR2(256) */
    private String serviceId;
    /** 6. 远盟的场次号 VARCHAR2(256) */
    private String corServiceId;
    /** 7. 用户提交过来的corOwnerId VARCHAR2(256) */
    private String corOwnerId;
    /** 8. 姓名 VARCHAR2(256) */
    private String userName;
    /** 9. 电话 VARCHAR2(256) */
    private String phone;
    /** 10. 性别 VARCHAR2(32) */
    private String gender;
    /** 11. 生日 DATE */
    private Date birthday;
    /** 12. 年龄 NUMBER(3) */
    private Integer age;
    /** 13. 岁 VARCHAR2(32) */
    private String ageUnit;
    /** 14. 检测项目代码(单项+组合项) VARCHAR2(256) */
    private String itemCodes;
    /** 15. 采样时间(2006-01-02 15:04:05 -0700) VARCHAR2(128) */
    private String samplingAt;
    /** 16. 状态 NUMBER(3) */
    private Integer state;
    /** 17. 接受到报告时间 VARCHAR2(128) */
    private String entryAt;
    /** 18. 发布报告时间 VARCHAR2(128) */
    private String publishedAt;
    /** 19. "报告结果(原始报告单，实验室报告单) " VARCHAR2(1024) */
    private String rawResults;
    /** 20. 报告结果（JSON字符串） CLOB */
    private String results;
    /** 21. PDF的URL地址 VARCHAR2(512) */
    private String pdfUrl;
    /** 22. WAP版报告地址 VARCHAR2(512) */
    private String wapShowUrl;
    /** 23. WEB版报告地址 VARCHAR2(512) */
    private String webShowUrl;
    /** 24. 健康建议 CLOB */
    private String healthAdvice;
    /** 25. 血液结果解读(JSON字符串) CLOB */
    private String anomalyIndex;
    /** 26. 是否删除 0-否1-是 NUMBER */
    private Integer isDeleted;
    /** 27. 创建时间 DATE */
    private Date createTime;
    /** 28. 创建人ID VARCHAR2(100) */
    private String createId;
    /** 29. 创建人姓名 VARCHAR2(256) */
    private String createBy;
    /** 30. 修改时间 DATE */
    private Date updateTime;
    /** 31. 修改人ID VARCHAR2(100) */
    private String updateUserId;
    /** 32. 修改人姓名 VARCHAR2(256) */
    private String updateBy;
    /** 33. 状态 NUMBER */
    private Integer status;
    /** 34. 操作次数 NUMBER */
    private Integer counter;

    public static final String F_ID = "id";
    public static final String F_REPORTID = "reportId";
    public static final String F_REPORTNAME = "reportName";
    public static final String F_BARCODE = "barcode";
    public static final String F_SERVICEID = "serviceId";
    public static final String F_CORSERVICEID = "corServiceId";
    public static final String F_COROWNERID = "corOwnerId";
    public static final String F_USERNAME = "userName";
    public static final String F_PHONE = "phone";
    public static final String F_GENDER = "gender";
    public static final String F_BIRTHDAY = "birthday";
    public static final String F_AGE = "age";
    public static final String F_AGEUNIT = "ageUnit";
    public static final String F_ITEMCODES = "itemCodes";
    public static final String F_SAMPLINGAT = "samplingAt";
    public static final String F_STATE = "state";
    public static final String F_ENTRYAT = "entryAt";
    public static final String F_PUBLISHEDAT = "publishedAt";
    public static final String F_RAWRESULTS = "rawResults";
    public static final String F_RESULTS = "results";
    public static final String F_PDFURL = "pdfUrl";
    public static final String F_WAPSHOWURL = "wapShowUrl";
    public static final String F_WEBSHOWURL = "webShowUrl";
    public static final String F_HEALTHADVICE = "healthAdvice";
    public static final String F_ANOMALYINDEX = "anomalyIndex";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEID = "createId";
    public static final String F_CREATEBY = "createBy";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";
    public static final String F_UPDATEBY = "updateBy";
    public static final String F_STATUS = "status";
    public static final String F_COUNTER = "counter";

    public ErpReportOrgJY() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getCorOwnerId() {
        return corOwnerId;
    }

    public void setCorOwnerId(String corOwnerId) {
        this.corOwnerId = corOwnerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    public String getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(String itemCodes) {
        this.itemCodes = itemCodes;
    }

    public String getSamplingAt() {
        return samplingAt;
    }

    public void setSamplingAt(String samplingAt) {
        this.samplingAt = samplingAt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEntryAt() {
        return entryAt;
    }

    public void setEntryAt(String entryAt) {
        this.entryAt = entryAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getRawResults() {
        return rawResults;
    }

    public void setRawResults(String rawResults) {
        this.rawResults = rawResults;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getWapShowUrl() {
        return wapShowUrl;
    }

    public void setWapShowUrl(String wapShowUrl) {
        this.wapShowUrl = wapShowUrl;
    }

    public String getWebShowUrl() {
        return webShowUrl;
    }

    public void setWebShowUrl(String webShowUrl) {
        this.webShowUrl = webShowUrl;
    }

    public String getHealthAdvice() {
        return healthAdvice;
    }

    public void setHealthAdvice(String healthAdvice) {
        this.healthAdvice = healthAdvice;
    }

    public String getAnomalyIndex() {
        return anomalyIndex;
    }

    public void setAnomalyIndex(String anomalyIndex) {
        this.anomalyIndex = anomalyIndex;
    }

    @Override
    public Integer getIsDeleted() {
        return isDeleted;
    }

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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
        return "ErpReportOrgJY{" +
                "id='" + id + '\'' +
                ", reportId='" + reportId + '\'' +
                ", reportName='" + reportName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", corServiceId='" + corServiceId + '\'' +
                ", corOwnerId='" + corOwnerId + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", ageUnit='" + ageUnit + '\'' +
                ", itemCodes='" + itemCodes + '\'' +
                ", samplingAt='" + samplingAt + '\'' +
                ", state=" + state +
                ", entryAt='" + entryAt + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", rawResults='" + rawResults + '\'' +
                ", results=" + results +
                ", pdfUrl='" + pdfUrl + '\'' +
                ", wapShowUrl='" + wapShowUrl + '\'' +
                ", webShowUrl='" + webShowUrl + '\'' +
                ", healthAdvice=" + healthAdvice +
                ", anomalyIndex=" + anomalyIndex +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", createId='" + createId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", status=" + status +
                ", counter=" + counter +
                '}';
    }
}
