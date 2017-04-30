package org.hpin.webservice.bean;

import org.hpin.common.core.orm.BaseEntity;

import java.util.Date;

/**
 * 图片信息明细表
 * Created by root on 17-2-6.
 */
public class ErpReportdetailImginfo extends BaseEntity{

    /** 1. VARCHAR2(100 BYTE) 主键ID */
    private String id;
    /** 2. VARCHAR2(100 BYTE) erp_reportdetail_imgtask表ID */
    private String taskId;
    /** 3. VARCHAR2(1000 BYTE) 报告图片地址 */
    private String imgPath;
    /** 4. DATE 创建时间 */
    private Date createTime;
    /** 5. NUMBER 是否删除,0:有效；1:无效 */
    private Integer isDeleted;
    /** 6. NUMBER 图片排序 */
    private Integer imgOrder;

    public ErpReportdetailImginfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public Integer getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(Integer imgOrder) {
        this.imgOrder = imgOrder;
    }
}
