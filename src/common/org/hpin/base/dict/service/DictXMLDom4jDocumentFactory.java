package org.hpin.base.dict.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.hpin.base.dict.exceptions.DocumentCreateException;
import org.hpin.common.util.StaticMethod;


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
public class DictXMLDom4jDocumentFactory {

    /**
     * 构造方法初始化缓存map
     */
    public DictXMLDom4jDocumentFactory() {
        cache = new HashMap();
    }

    /**
     * 缓存document
     */
    private Map cache;

    /**
     * xml注册在其中,如key=major,value=classpath:com/hpin/sample.xml
     */
    private Properties register;

    /**
     * @return the register
     */
    public Properties getRegister() {
        return register;
    }

    /**
     * @param register
     *            the register to set
     */
    public void setRegister(Properties register) {
        this.register = register;
    }

    private SAXReader saxReader;

    public void setSaxReader(SAXReader saxReader) {
        this.saxReader = saxReader;
    }

    public Document getDocument(String key) throws DocumentCreateException {
        //xml地址
        String xmlPath = null;
        try {
            if (cache.containsKey(key)) {
                return (Document) cache.get(key);
            } else {
                //在xml注册器中查找key对应的xml地址
                xmlPath = (String) this.register.get(key);
                // TODO 可能使用Url方式读取
                Document doc = saxReader
                        .read(StaticMethod.getFileUrl(xmlPath));
                //入cache
                cache.put(key, doc);
                return doc;
            }

        } catch (Exception e) {
            throw new DocumentCreateException("初使化" + xmlPath + "文件不成功\n"
                    + e.getMessage());
        }
    }
}
