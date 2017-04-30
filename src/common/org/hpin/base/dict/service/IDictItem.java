package org.hpin.base.dict.service;

/**
 * <p>
 * Title:无论是db,xml存储方式，字典类必须提供以下几个方法
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 */
public interface IDictItem {
    /**
     * 名称
     *  
     */
    public Object getItemName();

    /**
     * 描述
     *  
     */
    public Object getItemDescription();

    /**
     * 唯一标识
     *  
     */
    public Object getItemId();
}
