package org.hpin.webservice.bean.jz;/**
 * Created by admin on 2016/11/29.
 */

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 报告URL地址信息表(ERP_REPORT_SCHEDULE_JY的从表)
 *
 * @author YoumingDeng
 * @create 2016-11-29 17:45
 */
public class ErpReportUrlJY extends BaseEntity{

    /** 1. ID VARCHAR2(32) */
    private String id;
    /** 2. ERP_REPORT_SCHEDULE_JY表ID VARCHAR2(32) */
    private String idRelated;
    /** 3. 条形码 VARCHAR2(256) */
    private String code;
    /** 4. 姓名 VARCHAR2(256) */
    private String name;
    /** 5. 电话 VARCHAR2(256) */
    private String phone;
    /** 6. 文件类型 VARCHAR2(128) */
    private String fileType;
    /** 7. 报告URL地址 VARCHAR2(512) */
    private String url;
    /** 8. HTTP状态码（200，500等） NUMBER */
    private Integer httpCode;
    /** 9. 是否删除 ：0-否，1-是 NUMBER */
    private Integer isDeleted;
    /** 10. 创建时间 DATE */
    private Date createTime;
    /** 11. 创建人ID VARCHAR2(100) */
    private String createUserId;
    /** 12. 创建人姓名 VARCHAR2(256) */
    private String createUserName;
    /** 13. 修改时间 DATE */
    private Date updateTime;
    /** 14. 修改人ID VARCHAR2(100) */
    private String updateUserId;
    /** 15. 修改人姓名 VARCHAR2(256) */
    private String updateUserName;
    /** 16. 状态(0:未完成，1：已完成) NUMBER */
    private Integer status;
    /** 17. 操作次数 NUMBER */
    private Integer counter;

    public static final String F_ID = "id";
    public static final String F_IDRELATED = "idRelated";
    public static final String F_CODE = "code";
    public static final String F_NAME = "name";
    public static final String F_PHONE = "phone";
    public static final String F_FILETYPE = "fileType";
    public static final String F_URL = "url";
    public static final String F_HTTPCODE = "httpCode";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_CREATEUSERNAME = "createUserName";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";
    public static final String F_UPDATEUSERNAME = "updateUserName";
    public static final String F_STATUS = "status";
    public static final String F_COUNTER = "counter";
    public static final String F_REMARK= "remark";

    public ErpReportUrlJY() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
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

}
