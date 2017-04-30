/**
 * @author DengYouming
 * @since 2016-10-31 下午3:55:16
 */
package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpScheduleJob;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * @author DengYouming
 * @since 2016-10-31 下午3:55:16
 */
@Repository
public class ErpScheduleJobDao extends BaseDao{

	/**
	 * 
	 * @param params
	 * @param isExact true:精确查找，false:模糊查找
	 * @return List
	 * @author DengYouming
	 * @since 2016-10-31 下午4:52:02
	 */
	public List<ErpScheduleJob> listScheduleJobByProps(Map<String,String> params, boolean isExact){
		List<ErpScheduleJob> list = null;
		Session session;
		Criteria criteria;
		if(!CollectionUtils.isEmpty(params)){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpScheduleJob.class);
		
			for (String key : params.keySet()) {
				String value = params.get(key);
				System.out.println(key+" : "+value);
				if(key.equalsIgnoreCase(ErpScheduleJob.F_ID)){
					String[] idArr;
					if(value.indexOf(",")!=-1){
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpScheduleJob.F_ID, idArr));
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
	
	public List<ErpScheduleJob> listScheduleJobByProps(Map<String,String> params){
		return this.listScheduleJobByProps(params,true);
	}
}
