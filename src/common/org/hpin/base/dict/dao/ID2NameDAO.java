package org.hpin.base.dict.dao;

import org.hpin.base.dict.exceptions.DictDAOException;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * Title:id转成名称
 * </p>
 * <p>
 * Description:当某个表，如用户表，userId要转换成userName，这时需要在UserDAOImpl中实现该接口，并实现该方法
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 */
@Repository()
public interface ID2NameDAO {
    /**
     * id转name
     * 
     * @param id
     *            一般为表中的主键
     * @return 返回主键对应的name(自定义)
     * @throws DictDAOException
     */
    public String id2Name(String id) throws DictDAOException;
    /**
     * id转换成需要的字段
     * @param id 一般为表中的主键
     * @param field 需要查询的字段
     * @return 返回主键对应的name(自定义)
     * @throws DictDAOException
     */
    public String id2Field(String id,String beanId,String field) throws DictDAOException;
    
   
}
