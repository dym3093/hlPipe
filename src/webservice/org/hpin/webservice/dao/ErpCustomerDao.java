package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpCustomer;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
/**
 * 会员信息DAO
 * @author DengYouming
 * @since 2016-6-23 下午3:31:10
 */
@Repository
public class ErpCustomerDao extends BaseDao{
	
	/**
	 * 根据code和name 查询对应的客户信息;
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年4月11日
	 */
	@SuppressWarnings("unchecked")
	public ErpCustomer findByCodeAndName(String idcard, String name, String tel) {
		String hql = " from ErpCustomer where isDeleted=0 and idno=? and name=? and phone=?";
		List<ErpCustomer> list = this.getHibernateTemplate().find(hql, new Object[]{idcard, name, tel});
		return list!=null && list.size()>0 ? list.get(0) : null ;
	}

	/**
	 * 根据条件查询
	 * @param params 传入的Map键值对
	 * @param isExact 是否精确查找（只对字符串类型有效，ID为in查找，不用like）
	 * @return List ErpCustomer对象集
	 * @author DengYouming
	 * @since 2016-8-18 上午11:57:59
	 */
	public List<ErpCustomer> listCustomerByProps(Map<String,String> params, boolean isExact){
		List<ErpCustomer> list = null;
		Session session = null;
		Criteria criteria = null;
		if(!CollectionUtils.isEmpty(params)){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpCustomer.class);
		
			for (String key : params.keySet()) {
				String value = params.get(key);
				System.out.println(key+" : "+value);
				if(key.equalsIgnoreCase(ErpCustomer.F_ID)){
					String[] idArr = null ;
					if(value.indexOf(",")!=-1){
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpCustomer.F_ID, idArr));
				}else if(key.equalsIgnoreCase(ErpCustomer.F_ISDELETED)){
					criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
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


	/**
	 * @description
	 * @param params 传入的参数
	 * @return List
	 * @author YoumingDeng
	 * @since: 2016/12/6 14:51
	 */
	public List<ErpCustomer> listCustomerByProps(Map<String,String> params) throws Exception{
		List<ErpCustomer> list = null;
		if(!CollectionUtils.isEmpty(params)){
			list = this.listCustomerByProps(params, true);
		}
		return list;
	}

	public List<ErpCustomer> findByProps(String name, String code) throws Exception{
		String hql = " from ErpCustomer where 1=1 and isDeleted = 0 and name = ? and code = ? ";
		List<ErpCustomer> list  = this.getHibernateTemplate().find(hql,new Object[]{name, code});
		return list;
	}

	/**
	 * @description 根据客户信息查询其所在的支公司项目信息
	 * @param params 客户信息
	 * @return List
	 * @author YoumingDeng
	 * @since: 2016/12/6 14:05
	 */
	public List<CustomerRelationShipPro> findProjectNoByProps(Map<String,String> params) throws Exception{
		List<CustomerRelationShipPro> shipProList = null;
		if(!params.isEmpty()){

		}
		return shipProList;
	}

	/**
	 * 根据场次号+证件号查询;是否存在;
	 * create by henry.xu 2016年12月13日
	 * @param eventsNo
	 * @param cardId
	 * @return true有存在的客户信息,false没有存在的信息;
	 */
	public boolean findCustomerIsExits(String eventsNo, String cardId) {
		String sql = "select count(1) from erp_customer where events_no = '"+eventsNo+"' and idNo='"+cardId+"' ";
		int countNum = this.getJdbcTemplate().queryForInt(sql);
		if(countNum > 0) {
			return true;
		}
		return false;
	}

	/**
	* @description 根据会员条码修改其报告状态
	* @author YoumingDeng
	* @since: 2016/12/16 2:32
	 */
	public void updateStatusYmByCode(Integer statusYm, String code){
		String sql = " update erp_customer set status_ym = ? where is_deleted = 0 and code = ? ";
		this.getJdbcTemplate().update(sql, new Object[]{statusYm, code});
	}

}
