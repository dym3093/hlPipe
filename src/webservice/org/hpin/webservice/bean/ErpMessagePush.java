package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息推送表
 * Created by Damian on 2017/1/1.
 */
public class ErpMessagePush extends BaseEntity implements Serializable{

    /** 1. ID VARCHAR2(32) */
    private String id;
    /** 2. 条码 VARCHAR2(256) */
    private String code;
    /** 3. 姓名 VARCHAR2(256) */
    private String name;
    /** 4. 电话 VARCHAR2(128) */
    private String phone;
    /** 5. 场次号 VARCHAR2(256) */
    private String eventsNo;
    /** 6. 客户状态 NUMBER */
    private Integer statusYm;
    /** 7. 推送的名称 VARCAHR2(1024) */
    private String pushName;
    /** 8. 推送的内容 CLOB */
    private String message;
    /** 9. 关键字 VARCHAR2(256) */
    private String keyWord;
    /** 10. 方法名 VARCHAR2(256) */
    private String methodName;
    /** 11. 方法参数 VARCAHR2(1024) */
    private String parameters;
    /** 12. 备注 VARCAHR2(1024) */
    private String remark;
    /** 13. 执行状态(-1:删除,0:未推送,1:已推送) NUMBER 0 */
    private Integer status;
    /** 14. 执行次数 NUMBER 0 */
    private Integer counter;
    /** 15. 创建时间 DATE SYSDATE */
    private Date createTime;
    /** 16. 创建人 VARCHAR2(256) */
    private String createUserName;
    /** 17. 创建人ID VARCHAR2(32) */
    private String createUserId;
    /** 18. 修改时间 DATE */
    private Date updateTime;
    /** 19. 修改人 VARCHAR2(256) */
    private String updateUserName;
    /** 20. 修改人ID VARCHAR2(32) */
    private String updateUserId;

    public static final String F_ID = "id";
    public static final String F_CODE = "code";
    public static final String F_NAME = "name";
    public static final String F_PHONE = "phone";
    public static final String F_EVENTSNO = "eventsNo";
    public static final String F_STATUSYM = "statusYm";
    public static final String F_PUSHNAME = "pushName";
    public static final String F_MESSAGE = "message";
    public static final String F_KEYWORD = "keyWord";
    public static final String F_METHODNAME = "methodName";
    public static final String F_PARAMETERS = "parameters";
    public static final String F_REMARK = "remark";
    public static final String F_STATUS = "status";
    public static final String F_COUNTER = "counter";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERNAME = "createUserName";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERNAME = "updateUserName";
    public static final String F_UPDATEUSERID = "updateUserId";

    public ErpMessagePush() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEventsNo() {
        return eventsNo;
    }

    public void setEventsNo(String eventsNo) {
        this.eventsNo = eventsNo;
    }

    public Integer getStatusYm() {
        return statusYm;
    }

    public void setStatusYm(Integer statusYm) {
        this.statusYm = statusYm;
    }

    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public String toString() {
        return "ErpMessagePush{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", eventsNo='" + eventsNo + '\'' +
                ", statusYm=" + statusYm +
                ", pushName='" + pushName + '\'' +
                ", message='" + message + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters='" + parameters + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", counter=" + counter +
                ", createTime=" + createTime +
                ", createUserName='" + createUserName + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserName='" + updateUserName + '\'' +
                ", updateUserId='" + updateUserId + '\'' +
                '}';
    }
}
