package org.hpin.webservice.dao.ty;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * @author machuan
 * @date 2017年1月20日
 */
@Repository
public class ErpTYCustomerDao extends BaseDao{

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
						int n = value.split(",").length;
						idArr = new String[n] ;
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
	 * 根据条件查询相关场次
	 * @param params 传入的条件
	 * @return List
	 * @author DengYouming
	 * @since 2016-5-18 上午11:33:17
	 */
	public List<ErpEvents> listEventsByProps(Map<String,String> params)throws Exception{
		List<ErpEvents> list = null;
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ErpEvents.class);

		if(!CollectionUtils.isEmpty(params)){
			for (String key : params.keySet()) {
				String value = params.get(key);
				if(key.equalsIgnoreCase(ErpEvents.F_ID)){
					String[] idArr;
					if(value.indexOf(",")!=-1){
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpEvents.F_ID, idArr));
				}else if(key.equalsIgnoreCase(ErpEvents.F_ISDELETED)){
					criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
				}else{
					criteria.add(Restrictions.eq(key, value));
				}
			}
			//未删除，按创建日期倒序
			criteria.add(Restrictions.eq(ErpEvents.F_ISDELETED, 0)).addOrder(Order.desc(ErpEvents.F_CREATETIME));
			list = criteria.list();
		}
		return list;
	}
	
	/**
	 * 根据支公司名称或总公司名称查找
	 * @param company 支公司名称或总公司名称
	 * @return CustomerRelationShip
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-18 下午5:09:49
	 */
	public CustomerRelationShip findByCompanyName(String company) throws Exception{
		CustomerRelationShip entity = null;
		String hql = "from CustomerRelationShip c where c.branchCommany=? or c.ownedCompany=?";
		List<CustomerRelationShip> list = this.getHibernateTemplate().find(hql, new String[]{company, company});
		if(null!=list&&0<list.size()){
			entity = list.get(0);
		}
		return entity;
	}


	/**
	 *判断authID是否能使用  ture为可以使用
	 * @param authID
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public boolean findAuthIDForUsed(String authID) {
		String sql = "select count(*) from erp_valid_code_detail d where d.valid_code='"
				+ authID
				+ "' and (d.is_used is null or d.is_used='0')";
		return this.getJdbcTemplate().queryForInt(sql)>0;
	}


	/**
	 * 根据场次号，姓名，身份证判断在erp_pre_customer表中是否存在该客户
	 * @param eventNo
	 * @param name
	 * @param idNum
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public List<ErpPreCustomer> getPreCustomerByParams(String eventNo, String name, String idNum,String barCode) {
		String sql = "from ErpPreCustomer  where eventsNo=? and wereName=? and wereIdcard=? and code=? and (isDeleted is null or isDeleted=0)";
		return this.getHibernateTemplate().find(sql, eventNo,name,idNum,barCode);
	}


	/**
	 * 根据authID 查询支公司ID
	 * @param authID
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public Map<String, Object> findCompanyIdByAuthId(String authID) {
		String sql = "select d.branch_id,d.project_id from erp_valid_code_detail d where d.valid_code='"
				+ authID
				+ "' and (d.is_used is null or d.is_used =0)";
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		return list.get(0);
	}


	/**
	 * 根据场次号，姓名和唯一标识查询阳光的支公司ID和项目ID
	 * @param parentName
	 * @param relationship
	 * @return
	 * @author machuan
	 * @param eventNo 
	 * @date  2017年2月20日
	 */
	public Map<String, Object> getYGInFo(String eventNo, String parentName, String relationship) {
		String sql = "select rp.id,p.customer_relationship_id from ERP_BXCOMPANY_PRESET p "
					+"left join erp_application a on a.application_no=p.application_no and a.is_deleted=0 "
					+"left join hl_customer_relationship_pro rp on rp.customer_relationship_id=a.banny_company_id "
					+"and rp.project_code=a.project_code and rp.is_deleted='0' and rp.is_seal='0' "
					+"where p.customer_sku_num='"
					+relationship
					+ "' and p.customer_name='"
					+parentName
					+ "' and p.is_useable='0' and p.events_no='"
					+eventNo
					+ "'";
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
