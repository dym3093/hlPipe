package org.hpin.base.dict.service;

import java.util.ArrayList;
import java.util.List;

import org.hpin.base.dict.dao.SysDictJDBCDao;
import org.hpin.base.dict.dao.SysDictTypeDao;
import org.hpin.base.dict.entity.SysDictType;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.StaticMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "org.hpin.base.dict.service.SysDictTypeService")
@Transactional()
public class SysDictTypeService extends BaseService {
	@Autowired
	private SysDictTypeDao dictTypeDao;
	@Autowired
	private SysDictJDBCDao dictjdbc;
	
	/*
	 * id2name，即字典id转为字典名称 added by qinmin
	 * 
	 * @see com.hpin.base.dao.ID2NameDAO#id2Name(java.lang.String)
	 */
	public String id2Name(final String id) throws DictDAOException {
		return dictTypeDao.id2Name(id);
	}
	/*
	 * @see com.hpin.commons.system.dict.service.IHpinSystemDictTypeManager#getHpinSystemDictTypes(com.hpin.commons.system.dict.model.HpinSystemDictType)
	 */
	public List getHpinSystemDictTypes() {
		return dictTypeDao.getSysDictTypes();
	}

	/*
	 * @see com.hpin.commons.system.dict.service.IHpinSystemDictTypeManager#getHpinSystemDictType(String
	 *      id)
	 */
	public SysDictType getHpinSystemDictType(final String id) {
		return dictTypeDao.getSysDictType(new Integer(id));
	}

	/*
	 * @see com.hpin.commons.system.dict.service.IHpinSystemDictTypeManager#saveHpinSystemDictType(HpinSystemDictType
	 *      hpinSystemDictType)
	 */
	public void saveHpinSystemDictType(SysDictType hpinSystemDictType) {
		dictTypeDao.saveSysDictType(hpinSystemDictType);
	}

	/*
	 * @see com.hpin.commons.system.dict.service.IHpinSystemDictTypeManager#removeHpinSystemDictType(String
	 *      id)
	 */
	public void removeHpinSystemDictType(final String id) {
		dictTypeDao.removeSysDictType(new Integer(id));
	}

	public String getParentTypeName(String _strCurCode) {
		return dictTypeDao.getParentTypeName(_strCurCode);
	}

	/**
	 * 根据字典id查询字典名字
	 */
	public SysDictType getDictByDictId(String _strDictId) {
		return dictTypeDao.getDictByDictId(_strDictId);
	}
	public List getworkplanDictByDictId() {
		return dictTypeDao.getworkplanDictByDictId();
	}

	/**
	 * 查询下一级信息
	 * 
	 * @param parentdictid
	 * @return
	 */
	public ArrayList getDictSonsByDictid(String parentdictid) {
		ArrayList list = new ArrayList();
		list = dictTypeDao.getDictSonsByDictid(parentdictid);
		return list;
	}

	/**
	 * 根据字id查询字典信息
	 * 
	 * @param dictid
	 * @return
	 */
	public SysDictType getDictTypeByDictid(String dictid) {
		return dictTypeDao.getDictTypeByDictid(dictid);
	}

	/**
	 * 查询最大的字典ID
	 * 
	 * @param pardictid
	 * @param len
	 * @return
	 */
	public String getMaxDictid(String pardictid) {

		// 
		String maxDictId = "";
		String release = StaticMethod
				.null2String(((HPINAttributesService) SpringTool.getBean(HPINAttributesService.class)).getRelease());

		// 获得可用Id顺序，版本发布降序，未发布升序
		if ("true".equals(release)) {
			maxDictId = dictjdbc.getMaxDeptidDESC(pardictid, pardictid.length()
					+ HpinSystemDictUtil.DICTID_BETWEEN_LENGTH);
		} else if ("false".equals(release)) {
			maxDictId = dictjdbc.getMaxDeptid(pardictid, pardictid.length()
					+ HpinSystemDictUtil.DICTID_BETWEEN_LENGTH);
		} else {
			maxDictId = dictjdbc.getMaxDeptid(pardictid, pardictid.length()
					+ HpinSystemDictUtil.DICTID_BETWEEN_LENGTH);
		}
		return maxDictId;

	}

	/**
	 * 根据字典ID删除记录
	 * 
	 * @param dictid
	 */
	public void removeDictByDictid(String dictid) {
		dictjdbc.removeDictByDictid(dictid);
	}

	/**
	 * 判断是否有相同级别的字典类型
	 * 
	 * @param systype
	 * @return
	 */
	public boolean isHaveSameLevel(String parentdictid, String systype) {
		return dictTypeDao.isHaveSameLevel(parentdictid, systype);
	}

	/**
	 * 更新某字典类型的叶子节点
	 * 
	 * @param dictid
	 * @param leaf
	 */
	public void updateParentDictLeaf(String dictid, String leaf) {
		dictjdbc.updateParentDictLeaf(dictid, leaf);
	}

	/**
	 * 查询code的字典信息
	 * 
	 * @param code
	 * @return
	 */
	public List getDictByCode(String code) {
		return dictTypeDao.getDictByCode(code);
	}

	/**
	 * 根据 parentDictId 和 dictCode获取 HpinSystemDictType
	 * 
	 * @param sysDictType
	 * @param hpinSystemDictCode
	 * @return String HpinSystemDictType
	 * @author liqiuye 2007-11-14
	 */
	public SysDictType getDictByDictType(String dictCode,
			String parentDictId) {

		return dictTypeDao.getDictByDictType(dictCode, parentDictId);
	}

	/**
	 * 根据 dictCode获取 HpinSystemDictType
	 * 
	 * @param hpinSystemDictCode
	 * @return String HpinSystemDictType
	 * @author liqiuye 2007-11-14
	 */
	public SysDictType getDictByDictType(String dictCode) {

		return dictTypeDao.getDictByDictType(dictCode);
	}

	public boolean isCodeExist(String dictCode, String dictId) {
		return dictTypeDao.isCodeExist(dictCode, dictId);
	}

	public String getDictIdByDictCode(String dictCode) {
		return dictTypeDao.getDictIdByDictCode(dictCode);
	}

	public String getDictIdByDictCode(String parentId, String dictCode) {
		return dictTypeDao.getDictIdByDictCode(parentId, dictCode);
	}
	
	public List<SysDictType> findSysteDictListByRemark(String remark){
		return dictTypeDao.findSysteDictListByRemark(remark) ;
	}
}