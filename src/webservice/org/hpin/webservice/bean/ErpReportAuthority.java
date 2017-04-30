package org.hpin.webservice.bean;
/**
 * Created by admin on 2016/12/6.
 */

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 报告权限表
 *
 * @author YoumingDeng
 * @since 2016-12-06 11:37
 */
public class ErpReportAuthority extends BaseEntity{

    /** 1. ID VARCHAR2(32) */
    private String id;
    /** 2. 项目编号 VARCHAR2(256) */
    private String projectNo;
    /** 3. 过滤条件，0推送，1不推送 VARCHAR2(128) */
    private String filterNo;
    /** 4. 预留字段 VARCHAR2(1024) */
    private String reserve1;
    /** 5. 预留字段 VARCHAR2(1024) */
    private String reserve2;
    /** 6. 预留字段 VARCHAR2(1024) */
    private String reserve3;
    /** 7. 预留字段 VARCHAR2(1024) */
    private String reserve4;
    /** 8. 是否删除,0不删除,1删除 NUMBER */
    private Integer isDeleted;
    /** 9. 备注 VARCHAR2(1024) */
    private String remark;
    /** 10. 创建时间 DATE */
    private Date createTime;
    /** 11. 创建人ID VARCHAR2(100) */
    private String createUserId;
    /** 12. 创建人姓名 VARCHAR2(256) */
    private String createUser;
    /** 13. 修改时间 DATE */
    private Date updateTime;
    /** 14. 修改人ID VARCHAR2(100) */
    private String updateUserId;
    /** 15. 修改人姓名 VARCHAR2(256) */
    private String updateUser;
    /** 16. 其他状态 NUMBER */
    private Integer status;

    public static final String F_ID = "id";
    public static final String F_PROJECTNO = "projectNo";
    public static final String F_FILTERNO = "filterNo";
    public static final String F_RESERVE1 = "reserve1";
    public static final String F_RESERVE2 = "reserve2";
    public static final String F_RESERVE3 = "reserve3";
    public static final String F_RESERVE4 = "reserve4";
    public static final String F_ISDELETED = "isDeleted";
    public static final String F_REMARK = "remark";
    public static final String F_CREATETIME = "createTime";
    public static final String F_CREATEUSERID = "createUserId";
    public static final String F_CREATEUSER = "createUser";
    public static final String F_UPDATETIME = "updateTime";
    public static final String F_UPDATEUSERID = "updateUserId";
    public static final String F_UPDATEUSER = "updateUser";
    public static final String F_STATUS = "status";

    public ErpReportAuthority() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getFilterNo() {
        return filterNo;
    }

    public void setFilterNo(String filterNo) {
        this.filterNo = filterNo;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
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
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ErpReportAuthority{" +
                "id='" + id + '\'' +
                ", projectNo='" + projectNo + '\'' +
                ", filterNo='" + filterNo + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                ", reserve3='" + reserve3 + '\'' +
                ", reserve4='" + reserve4 + '\'' +
                ", isDeleted=" + isDeleted +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", status=" + status +
                '}';
    }
}
