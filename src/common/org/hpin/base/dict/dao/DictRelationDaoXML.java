package org.hpin.base.dict.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.exceptions.DocumentCreateException;
import org.hpin.base.dict.service.DictRelationItemXML;
import org.hpin.base.dict.service.DictRelationXML;
import org.hpin.base.dict.service.DictXMLDom4jDocumentFactory;
import org.hpin.base.dict.service.IDictRelation;
import org.hpin.base.dict.service.IDictRelationItem;
import org.hpin.base.dict.util.Util;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Title:字典关联关系xml访问
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2007-10-25 16:31:06
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 */
@Repository()
public class DictRelationDaoXML implements IDictRelationDao {
    /**
     * document工厂
     */
    private DictXMLDom4jDocumentFactory dictXMLDom4jDocumentFactory;

    /**
     * @param dictXMLDom4jDocumentFactory
     *            the dictXMLDom4jDocumentFactory to set
     */
    public void setDictXMLDom4jDocumentFactory(
            DictXMLDom4jDocumentFactory dictXMLDom4jDocumentFactory) {
        this.dictXMLDom4jDocumentFactory = dictXMLDom4jDocumentFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictRelationDao#findRelation(java.lang.Object,
     *      java.lang.Object)
     */
    public IDictRelationItem findRelationItem(Object relationId,
            Object sourceItemId) throws DictDAOException {
        //取document
        Document doc = this.getDocument(relationId);
        //查询某个item关系
        Element element = (Element) doc
                .selectSingleNode("//dict-relations/dict-relation[@id='"
                        + Util.getId((String) relationId)
                        + "']/item-relation[@sourceItemId='" + sourceItemId
                        + "']");
        if (element == null) {
            throw new DictDAOException(relationId + ",sourceItemId"
                    + sourceItemId + " is not found");
        }
        //将xml mapping成对象
        DictRelationItemXML item = new DictRelationItemXML();
        item.setDestinationDictId(element.attributeValue("destinationDictId"));
        item
                .setDestinationDictKey(element
                        .attributeValue("destinationDictKey"));
        item
                .setDestinationItemIds(element
                        .attributeValue("destinationItemIds"));
        item.setSourceItemId(element.attributeValue("sourceItemId"));
        return item;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictRelationDao#findRelations(java.lang.Object)
     */
    public IDictRelation findRelation(Object relationId)
            throws DictDAOException {
        //取document
        Document doc = this.getDocument(relationId);
        //查询某个item关系
        Element element = (Element) doc
                .selectSingleNode("//dict-relations/dict-relation[@id='"
                        + Util.getId((String) relationId) + "']");
        if (element == null) {
            throw new DictDAOException(relationId + " is not found");
        }
        //将dict-relation结点mapping
        DictRelationXML relation = new DictRelationXML();

        relation.setId(element.attributeValue("id"));
        relation.setSourceDictKey(element.attributeValue("sourceDictKey"));
        relation.setSourceDictId(element.attributeValue("sourceDictId"));
        //构建dict-relation子结点
        List items = new ArrayList();
        for (Iterator it = element.elementIterator(); it.hasNext();) {
            Element elem = (Element) it.next();
            //将 item-relation xml 结点 mapping成对象
            DictRelationItemXML item = new DictRelationItemXML();
            item.setDestinationDictId(elem.attributeValue("destinationDictId"));
            item.setDestinationDictKey(elem
                    .attributeValue("destinationDictKey"));
            item.setDestinationItemIds(elem
                    .attributeValue("destinationItemIds"));
            item.setSourceItemId(elem.attributeValue("sourceItemId"));
            items.add(item);
        }
        relation.setDictRelationItems(items);
        return relation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictRelationDao#findRelationItems(java.lang.Object)
     */
    public List findRelationItems(Object relationId) throws DictDAOException {
        //取document
        Document doc = this.getDocument(relationId);
        //查询某个item关系
        Element element = (Element) doc
                .selectSingleNode("//dict-relations/dict-relation[@id='"
                        + Util.getId((String) relationId) + "']");
        if (element == null) {
            throw new DictDAOException(relationId + " is not found");
        }
        //构建dict-relation子结点
        List items = new ArrayList();
        for (Iterator it = element.elementIterator(); it.hasNext();) {
            Element elem = (Element) it.next();
            //将 item-relation xml 结点 mapping成对象
            DictRelationItemXML item = new DictRelationItemXML();
            item.setDestinationDictId(elem.attributeValue("destinationDictId"));
            item.setDestinationDictKey(elem
                    .attributeValue("destinationDictKey"));
            item.setDestinationItemIds(elem
                    .attributeValue("destinationItemIds"));
            item.setSourceItemId(elem.attributeValue("sourceItemId"));
            items.add(item);
        }
        return items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictRelationDao#findRelationButItems(java.lang.Object)
     */
    public IDictRelation findRelationButItems(Object relationId)
            throws DictDAOException {
        //取document
        Document doc = this.getDocument(relationId);
        //查询某个item关系
        Element element = (Element) doc
                .selectSingleNode("//dict-relations/dict-relation[@id='"
                        + Util.getId((String) relationId) + "']");
        if (element == null) {
            throw new DictDAOException(relationId + " is not found");
        }
        //将dict-relation结点mapping
        DictRelationXML relation = new DictRelationXML();
        relation.setId(element.attributeValue("id"));
        relation.setSourceDictKey(element.attributeValue("sourceDictKey"));
        relation.setSourceDictId(element.attributeValue("sourceDictId"));
        return relation;
    }

    /**
     * 通过factory获取document
     * 
     * @param dictId
     *            key&id 格式
     * @return
     * @throws DictDAOException
     */
    private Document getDocument(Object dictId) throws DictDAOException {
        Document doc = null;
        try {
            doc = dictXMLDom4jDocumentFactory.getDocument(Util
                    .getDictKey((String) dictId));
        } catch (DocumentCreateException e) {
            throw new DictDAOException(e);
        }
        return doc;
    }

}
