package org.hpin.webservice.bean;
// default package


import java.util.Date;

import org.hpin.common.core.orm.BaseEntity;


/**
 * Combo entity. @author MyEclipse Persistence Tools
 */

public class Combo  extends BaseEntity {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 2655608489835531074L;
	private String id;
     private String comboName;
     private String productName;
     private String comboContent;
     private Date createTime;
     private Date updateTime;
     private Date deleteTime;
     private int isDelete;
     private String createUser;
     private String updateUser;
     private String deleteUser;



   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getComboName() {
        return this.comboName;
    }
    
    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getProductName() {
        return this.productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComboContent() {
        return this.comboContent;
    }
    
    public void setComboContent(String comboContent) {
        this.comboContent = comboContent;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return this.deleteTime;
    }
    
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public int getIsDelete() {
        return this.isDelete;
    }
    
    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUser() {
        return this.createUser;
    }
    
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getDeleteUser() {
        return this.deleteUser;
    }
    
    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }
   








}