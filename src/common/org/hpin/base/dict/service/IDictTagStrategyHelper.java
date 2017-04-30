package org.hpin.base.dict.service;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

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
@Service(value = "org.hpin.system.dict.entity.IDictTagStrategyHelper")
@Transactional()
public interface IDictTagStrategyHelper {
    /**
     * 标签结束
     * 
     * @param dictTag
     *            标签传递的属性
     * @param pageContext
     *            page上下文
     * @return
     * @throws JspException
     */
    public int doEndTag(IDictTagHelper dictTag, PageContext pageContext)
            throws JspException;

    /**
     * 标签开始
     * 
     * @param dictTag
     *            标签传递的属性
     * @param pageContext
     *            page上下文
     * @return
     * @throws JspException
     */
    public int doStartTag(DictTagHelper dictTag, PageContext pageContext)
            throws JspException;
}
