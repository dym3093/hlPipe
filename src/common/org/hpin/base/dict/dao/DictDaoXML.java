package org.hpin.base.dict.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.exceptions.DocumentCreateException;
import org.hpin.base.dict.service.DictItemXML;
import org.hpin.base.dict.service.DictXML;
import org.hpin.base.dict.service.DictXMLDom4jDocumentFactory;
import org.hpin.base.dict.service.IDict;
import org.hpin.base.dict.service.IDictItem;
import org.hpin.base.dict.util.Util;
import org.springframework.stereotype.Repository;



/**
 * <p>
 * Title:操作xml字典
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
public class DictDaoXML implements IDictDao {

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
     * @see com.hpin.commons.system.dict.dao.IDictDao#findDict(java.lang.Object,
     *      java.lang.Object)
     */
    public IDictItem findItem(Object dictId, Object itemId)
            throws DictDAOException {
        //取document
        Document doc = this.getDocument(dictId);
        Element element = (Element) doc.selectSingleNode("//dicts/dict[@id='"
                + Util.getId((String) dictId) + "']/item[@id='" + itemId
                + "']");
        if (element == null) {
            throw new DictDAOException(dictId + ",itemId" + itemId
                    + " is not found");
        }
        DictItemXML item = new DictItemXML();
        item.setDescription(element.attributeValue("description"));
        item.setId(element.attributeValue("id"));
        item.setName(element.attributeValue("name"));
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictDao#findList(java.lang.Object)
     */
    public List findItemList(Object dictId) throws DictDAOException {
        //取document
        Document doc = this.getDocument(dictId);
        //查找对应的dict
        List list = doc.selectNodes("//dicts/dict[@id='"
                + Util.getId((String) dictId) + "']/item");
        //若找不到则抛出异常
        if (list == null || list.isEmpty()) {
            throw new DictDAOException(dictId + " is not found");
        }
        //存放dictItem列表
        List result = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Element element = (Element) it.next();
            DictItemXML item = new DictItemXML();
            item.setDescription(element.attributeValue("description"));
            item.setId(element.attributeValue("id"));
            item.setName(element.attributeValue("name"));
            result.add(item);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hpin.commons.system.dict.dao.IDictDao#findDict(java.lang.Object)
     */
    public IDict findDict(Object dictId) throws DictDAOException {
        //取document
        Document doc = this.getDocument(dictId);

        //查找对应的dict
        Element element = (Element) doc.selectSingleNode("//dicts/dict[@id='"
                + Util.getId((String) dictId) + "']");
        //若找不到则抛出异常
        if (element == null) {
            throw new DictDAOException(dictId + " is not found");
        }

        DictXML dict = new DictXML();
        dict.setDescription(element.attributeValue("description"));
        dict.setId(element.attributeValue("id"));
        return dict;
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
