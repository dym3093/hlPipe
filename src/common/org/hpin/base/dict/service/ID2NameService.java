package org.hpin.base.dict.service;

import org.hpin.base.dict.dao.ID2NameDAO;
import org.hpin.base.dict.util.Util;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.orm.BaseService;
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
 * 
 */
@Service(value = "org.hpin.base.dict.service.ID2NameService")
@Transactional()
public class ID2NameService extends BaseService  {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hpin.base.service.ID2NameManager#id2Name(java.lang.String,
	 *      java.lang.String)
	 */
	public String id2Name(String id, String beanId) {
		String name = null;
		try {
			// 通过beanid取bean
			ID2NameDAO dao = (ID2NameDAO)SpringTool.getBean(beanId);
			// 转换后的name
			name = dao.id2Name(id);
		} catch (Exception e) {
			// 取id2name失败后的name默认值
			name = Util.idNoName();
		}
		if (name == null || "".equals(name)) {
			name = Util.idNoName();
		}
		return name;
	}
	public String id2Field(String id, String beanId,String field) {
		String name = null;
		try {
			// 通过beanid取bean
			ID2NameDAO dao = (ID2NameDAO)SpringTool.getBean(beanId);
			// 转换后的name
			name = dao.id2Field(id,beanId, field);
		} catch (Exception e) {
			// 取id2name失败后的name默认值
			name = Util.idNoName();
		}
		if (name == null || "".equals(name)) {
			name = Util.idNoName();
		}
		return name;
	}

}
