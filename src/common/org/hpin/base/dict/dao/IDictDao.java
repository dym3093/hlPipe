package org.hpin.base.dict.dao;

import java.util.List;

import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.service.IDict;
import org.hpin.base.dict.service.IDictItem;
import org.springframework.stereotype.Repository;



/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 *  
 */
@Repository()
public interface IDictDao {
    /**
     * 取字典中某类列表
     * 
     * @param dictId
     *            字典中某类别
     * @return 字典中列表
     */
    public List findItemList(Object dictId) throws DictDAOException;

    /**
     * 通过类别+唯一标识取出某字典对象
     * 
     * @param dictId
     *            类别
     * @param itemId
     *            唯一标识
     * @return 字典对象
     */
    public IDictItem findItem(Object dictId, Object itemId)
            throws DictDAOException;

    /**
     * 取某类字典
     * 
     * @param dictId
     *            字典id
     * @return
     * @throws DictDAOException
     */
    public IDict findDict(Object dictId) throws DictDAOException;

}
