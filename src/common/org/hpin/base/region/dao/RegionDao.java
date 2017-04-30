package org.hpin.base.region.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.base.dict.dao.ID2NameDAO;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.region.entity.Region;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpQRCode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

/**
 * 行政区划管理
 * @author yifan
 *
 */
@Repository(value="org.hpin.base.region.dao.RegionDao")
public class RegionDao extends BaseDao implements ID2NameDAO {

	public List<Region> findRegionByParentId(String parentId){
		String hql = " from Region where parentId =? order by id asc " ;
		return super.getHibernateTemplate().find(hql,parentId); 
	}
	
	public String id2Name(String id) {
		// TODO Auto-generated method stub
		String hql = " from Region where id =? order by id asc " ;
		List<Region> regionList = super.getHibernateTemplate().find(hql , id) ;
		if(regionList != null && regionList.size() > 0){
			return regionList.get(0).getRegionName() ;
		}
		return null ;
	}
	
	public List<Region> findRegionsByParams(boolean isProvince){
		StringBuffer hqlBuffer = new StringBuffer(" from Region ") ;
		if(isProvince){
			hqlBuffer.append(" where id like '%0000' ") ;
		}else{
			hqlBuffer.append(" where id like '%00' and parentId != 0 ") ;
		}
		hqlBuffer.append(" order by nlssort(regionName , 'NLS_SORT=SCHINESE_PINYIN_M') ") ;
		
		return super.getHibernateTemplate().find(hqlBuffer.toString()) ;
	}
	
	public List<Region> findRegionByDeep(final int deep){
		final String hql = "from Region where deep = ? order by id asc" ;
		 List list = (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			 @Override
			public Object doInHibernate(Session session) throws HibernateException , SQLException {
				 Query query = null ;
				 query = session.createQuery(hql) ;
				 query.setInteger(0, deep);
				 query.setCacheable(true) ;
				 return query.list() ;
			}
		 }) ;
		return list;
	}

	@Override
	public String id2Field(String id, String beanId, String field) throws DictDAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据参数查找
	 * @param params 参数
	 * @param isExact 是否精确查找
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-19 上午10:56:07
	 */
	public List<Region> listByProps(Map<String,String> params, boolean isExact) throws Exception{
		List<Region> list = null;
		Session session = null;
		Criteria criteria = null;
		if(params!=null&&params.keySet().size()>0){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpQRCode.class);
			for (String key : params.keySet()) {
				String value = (String) params.get(key);
				if(isExact){
					criteria.add(Restrictions.eq(key, value));
				}else{
					criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据参数查找
	 * @param params 参数
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-19 上午10:56:48
	 */
	public List<Region> listByProps(Map<String,String> params) throws Exception{
		return this.listByProps(params, true);
	}
}
