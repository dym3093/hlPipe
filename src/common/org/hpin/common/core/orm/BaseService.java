package org.hpin.common.core.orm;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.SpringTool;
import org.hpin.common.widget.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础Service
 * 
 * @author thinkpad 2010-9-26
 */
@Transactional()
public class BaseService {

	public BaseDao getDao() {
		GenericDao genericDao = (GenericDao) SpringTool
				.getBean(GenericDao.class);
		return genericDao;
	}
	

	/**
	 * 获取实体类类型
	 * 
	 * @return
	 */
	private Class getClazz() {
		Class clazz = null;
		
		String className = this.getClass().getName().replace("service",
				"entity");
		
		className=className.substring(0, className.length()-7);
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	/**
	 * 获取实体类类型
	 * 
	 * @return
	 */
	private Class getClazz2() {
		Class clazz = null;
		
		String className = this.getClass().getName().replace("service",
				"model");
		
		className=className.substring(0, className.length()-7);
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            对象ID
	 * @return 对象
	 */
	public BaseEntity findById(String id) {
		return getDao().findById(getClazz(), id);
	}
	
	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            对象ID
	 * @return 对象
	 */
	public BaseEntity findById(long id) {
		return getDao().findById(getClazz(), new Long(id));
	}
	
	/**
	 * 获取所有的实体
	 * 
	 * @return
	 */
	public List findAll() {
		return getDao().findAll(getClazz(), "id", false);
	}

	/**
	 * 获取所有的实体
	 * 
	 * @return
	 */
	public List findAll(String orderProperty, Boolean isAsc) {
		return getDao().findAll(getClazz(), orderProperty, isAsc);
	}
	
	/**
	 * 保存实体类
	 * 
	 * @param baseEntity
	 */
	public void save(BaseEntity baseEntity) {
		getDao().save(baseEntity);
	}

	/**
	 * 修改实体类
	 * 
	 * @param baseEntity
	 */
	public void update(BaseEntity baseEntity) {
		getDao().update(baseEntity);
	}

	/**
	 * 删除实体类
	 * 
	 * @param ids
	 */
	public void deleteIds(String ids) {
		String[] idArray = ids.split(",");
		for (int i = 0; i < idArray.length; i++) {
			//getDao().delete(getClazz(), new Long(idArray[i]));
			getDao().delete(getClazz(), idArray[i]);
		}
	}

	/**
	 * 分页获取对象集合
	 * 
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage(Page page, Map searchMap) {
		return getDao().findByPage(getClazz(), page, searchMap);
	}
	/**
	 * 分页获取对象集合
	 * 业务流程费用审核信息
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage2(Page page, Map searchMap) {
		return getDao().findByPage(getClazz2(), page, searchMap);
	}

}
