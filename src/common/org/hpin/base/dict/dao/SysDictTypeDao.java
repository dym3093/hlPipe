package org.hpin.base.dict.dao;

// java standard library
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hpin.base.dict.entity.SysDictType;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.dict.util.Util;
import org.hpin.common.core.orm.BaseDao;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;


@Repository(value="org.hpin.base.dict.dao.SysDictTypeDao")
public class SysDictTypeDao extends BaseDao implements ID2NameDAO {

	public List<SysDictType> findSysteDictListByRemark(final String remark){
		final String queryStr = " from SysDictType d where d.dictRemark = ?" ;
		List<SysDictType> dictList = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = null ;
				query = session.createQuery(queryStr).setParameter(0 , remark) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		return dictList ;
	}
	
	/**
	 * 
	 */
	public List getSysDictTypes() {
		final String hql = "from SysDictType" ;
		 List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			 @Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				 Query query = null ;
				 query = session.createQuery(hql) ;
				 query.setCacheable(true) ;
				 return query.list() ;
			}
		 }) ;
		 return list ;
	}

	/*
	 * @see com.hpin.commons.system.dict.dao.ISysDictTypeDao#getSysDictType(Integer
	 *      id)
	 */
	public SysDictType getSysDictType(final Integer id) {
		SysDictType SysDictType = (SysDictType) getHibernateTemplate()
				.get(SysDictType.class, id);
		if (SysDictType == null) {
			throw new ObjectRetrievalFailureException(SysDictType.class,
					id);
		}

		return SysDictType;
	}

	/*
	 * @see com.hpin.commons.system.dict.dao.ISysDictTypeDao#saveSysDictType(SysDictType
	 *      SysDictType)
	 */
	public void saveSysDictType(final SysDictType SysDictType) {
		if ((SysDictType.getId() == null)
				|| (SysDictType.getId().equals("")))
			getHibernateTemplate().save(SysDictType);
		else
			getHibernateTemplate().saveOrUpdate(SysDictType);
	}

	/*
	 * @see com.hpin.commons.system.dict.dao.ISysDictTypeDao#removeSysDictType(Integer
	 *      id)
	 */
	public void removeSysDictType(final Integer id) {
		getHibernateTemplate().delete(getSysDictType(id));
	}

	public String getParentTypeName(String _strCurCode) {
		String _strRtn = "root";

		if (_strCurCode.length() > 4) {
			final String _strParentCode = _strCurCode.substring(0, _strCurCode
					.length() - 3);
			final String _strHQL = "select dict.dictName from SysDictType dict where dict.dictId = ?" ;
			List _objResult = super.getHibernateTemplate().executeFind(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException , SQLException {
					Query query = null ;
					query = session.createQuery(_strHQL).setParameter(0 , _strParentCode) ;
					query.setCacheable(true) ;
					return query.list() ;
				}
			}) ;
			if (_objResult.get(0) != null) {
				_strRtn = (String) _objResult.get(0);
			}
		}
		return _strRtn;
	}

	/**
	 * 根据字典ID查询字典名称
	 */
	public SysDictType getDictByDictId(final String _strDictId) {
		final String _strHQL = " from SysDictType dicttype where dicttype.dictId = ? " ;
		
		List<SysDictType> dictList = super.getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = null ;
				query = session.createQuery(_strHQL).setParameter(0 , _strDictId) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		
		return dictList.size() == 0 ? null : dictList.get(0) ;
	}
	public List getworkplanDictByDictId() {
		final String hql = "from SysDictType SysDictType where SysDictType.leaf in ('0','1')" ;
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override()
			public Object doInHibernate(Session session) throws HibernateException , SQLException{
				Query query = null ;
				query = session.createQuery(hql) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		});
		return list ;
	}

	/**
	 * 查询下一级信息
	 * 
	 * @param parentdictid
	 * @return
	 */
	public ArrayList getDictSonsByDictid(final String parentdictid) {
		final String hql = " from SysDictType dicttype where dicttype.parentDictId = ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , parentdictid) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		return (ArrayList) list;
	}

	/**
	 * 根据字id查询字典信息
	 * 
	 * @param dictid
	 * @return
	 */
	public SysDictType getDictTypeByDictid(final String dictid) {
		final String hql = " from SysDictType dicttype where dicttype.dictId = ? order by dicttype.dictCode";
		
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictid) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
				
		if (list != null && list.size() > 0)
			return (SysDictType) list.get(0);
		else
			return null;
	}

	/**
	 * 判断是否有相同级别的字典类型
	 * 
	 * @param systype
	 * @return
	 */
	public boolean isHaveSameLevel(final String parentdictid, final String systype) {
		boolean flag = false;
		final String hql = " from SysDictType dicttype where dicttype.parentDictId = ? and dicttype.sysType = ? ";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , parentdictid).setParameter(1 , Integer.parseInt(systype)) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		if (list != null && list.size() > 0) {
			flag = true;
		}
		return flag;

	}

	/**
	 * 查询code的字典信息
	 * 
	 * @param code
	 * @return
	 */
	public List getDictByCode(final String code) {
		final String hql = " from SysDictType dicttype where dicttype.dictCode = ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , code) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		return list;
	}

	/*
	 * id2name，即字典id转为字典名称 added by qinmin
	 * 
	 * @see com.hpin.base.dao.ID2NameDAO#id2Name(java.lang.String)
	 */
	public String id2Name(final String id) throws DictDAOException {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				String queryStr = " from SysDictType dictType where dictType.dictId=:dictId";

				Query query = session.createQuery(queryStr);
				query.setString("dictId", id);
				query.setFirstResult(0);
				query.setMaxResults(1);
				query.setCacheable(true) ;
				List list = query.list();
				SysDictType dictType = null;

				if (list != null && !list.isEmpty()) {
					dictType = (SysDictType) list.iterator().next();
				} else {
					dictType = new SysDictType();
					dictType.setDictName(Util.idNoName());
				}
				return dictType;
			}
		};

		SysDictType dictType = null;
		try {
			dictType = (SysDictType) getHibernateTemplate().execute(
					callback);
		} catch (Exception e) {
			throw new DictDAOException(e);
		}
		return dictType.getDictName();
	}

	/**
	 * 保存字典信息并返回dictId
	 * 
	 * @param SysDictType
	 * @return String dictId
	 * @author liqiuye 2007-11-14
	 */
	public String saveSysDictTypeReturnDictId(
			SysDictType SysDictType) {
		if ((SysDictType.getId() == null)
				|| (SysDictType.getId().equals("")))
			getHibernateTemplate().save(SysDictType);
		else
			getHibernateTemplate().saveOrUpdate(SysDictType);

		return SysDictType.getDictId();
	}

	/**
	 * 根据 parentDictId 和 dictCode获取 SysDictType
	 * 
	 * @param sysDictType
	 * @param hpinSystemDictCode
	 * @return String dictId
	 * @author liqiuye 2007-11-14
	 */
	public SysDictType getDictByDictType(final String dictCode,
			final String parentDictId) {
		final String hql = " from SysDictType dicttype where dicttype.dictCode = ? and dicttype.parentDictId = ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictCode).setParameter(1 , parentDictId) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType;
	}

	/**
	 * 根据 dictCode获取 SysDictType
	 * 
	 * @param hpinSystemDictCode
	 * @return String dictId
	 * @author liqiuye 2007-11-14
	 */
	public SysDictType getDictByDictType(final String dictCode) {
		final String hql = " from SysDictType dicttype where dicttype.dictCode = ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictCode) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType;
	}

	public boolean isCodeExist(final String dictCode, final String dictId) {
		boolean bool = false;
		final String hql = " from SysDictType dict where dict.dictCode = ? and dict.dictId <> ? ";
		List dictList = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictCode).setParameter(1 , dictId) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		if (dictList.size() > 0) {
			bool = true;
		}
		return bool;
	}

	public String getDictIdByDictCode(final String dictCode) {
		final String hql = " from SysDictType dicttype where dicttype.dictCode = ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictCode) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType.getDictId();
	}

	public SysDictType getDictType(final int dictId, String _dictType)
			throws SQLException {
		final String dictType = "" + _dictType;
		final String hql = "from SysDictType dicttype WHERE dicttype.dictId = ? and dicttype.parentDictId = ? ";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictId).setParameter(1 , dictType) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType;

	}

	public SysDictType getDictType(final int dictId , final int dictType)
			throws SQLException {
		final String hql = "from SysDictType dicttype WHERE dicttype.dictId = ? and dicttype.parentDictId = ? ";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictId).setParameter(1 , dictType) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType;

	}

	public String getDictIdByDictCode(String parentId, final String dictCode) {
		final String hql = " from SysDictType dicttype where dicttype.parentDictId like '"
				+ parentId
				+ "%' and dicttype.dictCode= ? order by dicttype.dictCode";
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , dictCode) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		SysDictType SysDictType = new SysDictType();
		if (list != null && list.size() > 0)
			SysDictType = (SysDictType) list.get(0);
		return SysDictType.getDictId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hpin.commons.system.dict.dao.ISysDictTypeDao#listDictInDictIds(java.lang.String,
	 *      java.lang.String)
	 */
	public List listDictInDictIds(final String parentDictId, String dictIds) {
		final String hql = " from SysDictType dicttype where dicttype.parentDictId = ? and dictId in (" + dictIds + ")" ;
		List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				Query query = session.createQuery(hql).setParameter(0 , parentDictId) ;
				query.setCacheable(true) ;
				return query.list() ;
			}
		}) ;
		return list ;
	}

	@Override
	public String id2Field(final String id,final String beanId, final String field) throws DictDAOException {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				String queryStr = "from SysDictType t where t.dictId=:dictId";
				Query query = session.createQuery(queryStr);
				query.setString("dictId", id);
				query.setFirstResult(0);
				query.setMaxResults(1);
				query.setCacheable(true) ;
				List list = query.list();
				SysDictType dictType = null;

				if (list != null && !list.isEmpty()) {
					dictType = (SysDictType) list.iterator().next();
				} else {
					dictType = new SysDictType();
					dictType.setDictName(Util.idNoName());
				}
				return dictType;
			}
		};

		SysDictType dictType = null;
		try {
			dictType = (SysDictType) getHibernateTemplate().execute(callback);
		} catch (Exception e) {
			throw new DictDAOException(e);
		}
		String result="";
		if(null!=dictType){
			try {
				Class clas = Class.forName(dictType.getClass().getName());
				Field[] fields = clas.getDeclaredFields();
				for (Field f:fields){
					if(field.equals(f.getName())) {  
						f.setAccessible( true );
						result = f.get(dictType).toString();
					}  
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
