package org.hpin.base.dict.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hpin.base.dict.dao.IDictDao;
import org.hpin.base.dict.dao.IDictRelationDao;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.exceptions.DictServiceException;
import org.hpin.base.dict.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 */
@Service(value = "org.hpin.system.dict.service.DictService")
@Transactional()
public class DictService {

    private Logger logger = Logger.getLogger(DictService.class);

    /**
     * 字典访问对象，可为xml,db访问或更多持久层技术
     */
    @Autowired
    private IDictDao dictDao;

    /**
     * 字典关系操作,可为xml,db技术
     */
    @Autowired
    private IDictRelationDao dictRelationDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getDict(java.lang.Object,
     *      java.lang.Object)
     */
    public IDictItem getDictItem(Object dictId, Object itemId)
            throws DictServiceException {
    	if(itemId == null || itemId.equals("")){
    		return null;
    	}
        IDictItem dict;
        try {
            dict = this.dictDao.findItem(dictId, itemId);
        } catch (DictDAOException e) {
            throw new DictServiceException(e);
        }
        return dict;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getList(java.lang.Object)
     */
    public List getDictItems(Object dictId) throws DictServiceException {
        List list = null;
        try {
            list = this.dictDao.findItemList(dictId);
        } catch (DictDAOException e) {
            throw new DictServiceException(e);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#id2description(java.lang.Object,
     *      java.lang.Object)
     */
    public Object itemId2description(Object dictId, Object itemId)
            throws DictServiceException {
        IDictItem dict = this.getDictItem(dictId, itemId);
        return dict != null ? dict.getItemDescription() : "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#id2name(java.lang.Object,
     *      java.lang.Object)
     */
    public Object itemId2name(Object dictId, Object itemId)
            throws DictServiceException {
        IDictItem dict = this.getDictItem(dictId, itemId);
        return dict != null ? dict.getItemName() : "";
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#type2description(java.lang.Object)
     */
    public Object dictId2description(Object dictId) throws DictServiceException {
        IDict dict;
        try {
            dict = dictDao.findDict(dictId);
        } catch (DictDAOException e) {
            throw new DictServiceException(e);
        }
        return dict.getDictDescription();
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getRelationItems(java.lang.Object)
     */
    public List getRelationItems(Object relationId) throws DictServiceException {
        try {
            return this.dictRelationDao.findRelationItems(relationId);
        } catch (DictDAOException e) {
            logger.error(e);
            throw new DictServiceException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getRelationItem(java.lang.Object,
     *      java.lang.Object)
     */
    public IDictRelationItem getRelationItem(Object relationId,
            Object sourceItemId) throws DictServiceException {
        try {
            return this.dictRelationDao.findRelationItem(relationId,
                    sourceItemId);
        } catch (DictDAOException e) {
            logger.error(e);
            throw new DictServiceException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getRelationButItems(java.lang.Object)
     */
    public IDictRelation getRelationButItems(Object relationId)
            throws DictServiceException {
        try {
            return this.dictRelationDao.findRelationButItems(relationId);
        } catch (DictDAOException e) {
            logger.error(e);
            throw new DictServiceException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getRelatedList(java.lang.String,
     *      java.lang.String)
     */
    public List getRelatedList(String id, String relationId)
            throws DictServiceException {
        List list = new ArrayList();
        IDictRelationItem item = null;
        try {
            //取关联关系
            item = this.dictRelationDao.findRelationItem(relationId, id);
        } catch (DictDAOException e) {
            logger.error(e);
            //若关联关系都取不到则直接返回一个empty list
            return list;
        }
        if (item != null
                && (item.getDestinationItemIds() != null || !"".equals(item
                        .getDestinationItemIds().trim()))) {
            //去掉分隔符转成数组
            String[] items = item.getDestinationItemIds().split(
                    Util.getDestinationItemIdsSplit());
            //取映射后的结果
            list = this.getDictItems(Util.constituteDictId(item
                    .getDestinationDictKey(), item.getDestinationDictId()),
                    items);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getDictItems(java.lang.String,
     *      java.lang.String[])
     */
    public List getDictItems(String dictId, String[] itemIds)
            throws DictServiceException {
        //结果
        List list = new ArrayList();
        //若为空则直接返回list
        if (itemIds != null && itemIds.length > 0) {
            //遍历ids
            for (int i = 0; i < itemIds.length; i++) {
                try {
                    //通过id取 dictItem(字典项)
                    IDictItem item = this.dictDao.findItem(dictId, itemIds[i]);
                    //加入结果
                    list.add(item);
                } catch (DictDAOException e) {
                    logger.error(e);
                }
            }
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.service.IDictService#getDictItems(java.lang.String)
     */
    public List getDictItemsForRelation(String relationId) throws DictServiceException {
        IDictRelation relation = this.getRelationButItems(relationId);
        List list = this.getDictItems(Util.constituteDictId(relation
                .getSourceDictKey(), relation.getSourceDictId()));
        return list;
    }
}
