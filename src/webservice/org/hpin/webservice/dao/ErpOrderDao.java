/**
 * @author DengYouming
 * @since 2016-10-12 上午11:51:30
 */
package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpOrder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * @author DengYouming
 * @since 2016-10-12 上午11:51:30
 */
@Repository
public class ErpOrderDao extends BaseDao{

	public List<ErpOrder> findByProps(Map<String,String> params, boolean isExact) throws Exception{
		List<ErpOrder> list = null;
		Session session = null;
		Criteria criteria = null;
		if(!CollectionUtils.isEmpty(params)){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpOrder.class);
		
			for (String key : params.keySet()) {
				String value = (String) params.get(key);
				System.out.println(key+" : "+value);
				if(key.equalsIgnoreCase(ErpOrder.F_ID)){
					String[] idArr = null ;
					if(value.indexOf(",")!=-1){
						int n = value.split(",").length;
						idArr = new String[n] ;
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpOrder.F_ID, idArr));
				}else{
					if(isExact){
						criteria.add(Restrictions.eq(key, value));
					}else{
						criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
					}
				}
			}				
			list = criteria.list();
		}
		return list;
	}
	
	public List<ErpOrder> findByProps(Map<String,String> params) throws Exception{
		return this.findByProps(params, true);
	}
}
