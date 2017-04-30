package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpQRCode;
import org.springframework.stereotype.Repository;

@Repository
public class ErpQRCodeDao extends BaseDao {

	/**
	 * 根据属性是否精确查找
	 * @param params 传入的参数
	 * @param isExact 是否精确查找 true：精确， false:模糊
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-19 上午10:03:31
	 */
	public List<ErpQRCode> listByProp(Map<String,String> params, boolean isExact) throws Exception{
		List<ErpQRCode> list = null;
		Session session = null;
		Criteria criteria = null;
		if(params!=null&&params.keySet().size()>0){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpQRCode.class);
			for (String key : params.keySet()) {
				String value = (String) params.get(key);
				System.out.println(key+" : "+value);
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
	 * 根据参数精确查找
	 * @param params 传入的参数
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-19 上午10:07:49
	 */
	public List<ErpQRCode> listByProp(Map<String,String> params) throws Exception{
		return this.listByProp(params, true);
	}
	
}
