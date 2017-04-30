package org.hpin.base.dict.dao;

import java.util.List;

import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.exceptions.DictServiceException;
import org.hpin.base.dict.service.IDictRelation;
import org.hpin.base.dict.service.IDictRelationItem;
import org.springframework.stereotype.Repository;



/**
 * <p>
 * Title: 操作关联关系
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
 */
@Repository()
public interface IDictRelationDao {
    /**
     * 通过关联id获取关联关系
     * 
     * @param relationId
     *            关联id
     * @return 返回关联关系
     * @throws DictDAOException
     */
    public IDictRelation findRelation(Object relationId)
            throws DictDAOException;

    /**
     * 通过关联关系id+源字典项id获取关联关系
     * 
     * @param relationId
     * @param sourceItemId
     * @return
     * @throws DictServiceException
     */
    public IDictRelationItem findRelationItem(Object relationId,
            Object sourceItemId) throws DictDAOException;

    /**
     * 通过关联关系id+取所有关联子项
     * 
     * @param relationId
     * @param sourceItemId
     * @return
     * @throws DictServiceException
     */
    public List findRelationItems(Object relationId) throws DictDAOException;

    /**
     * 通过relationId取关联关系，关系中不包括关联项内容
     * 
     * @param relationId
     * @return
     * @throws DictDAOException
     */
    public IDictRelation findRelationButItems(Object relationId)
            throws DictDAOException;

}
