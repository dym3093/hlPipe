package org.hpin.base.dict.service;

/**
 * <p>
 * Title:
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
public interface IDictRelationItem {
    /**
     * 获取目的项id，以逗号隔开
     * 
     * @return 返回目的itemId，如：1,2,3
     */
    public String getDestinationItemIds();

    /**
     * 获取源item id
     * 
     * @return 返回源字典项id
     */
    public String getSourceItemId();

    /**
     * 获取目的字典类
     * 
     * @return 返回目的字典类
     */
    public String getDestinationDictKey();

    /**
     * 获取目的字典ID
     * 
     * @return 返回目的字典id
     */
    public String getDestinationDictId();
}
