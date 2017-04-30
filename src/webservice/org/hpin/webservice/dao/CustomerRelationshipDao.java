package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hpin.base.dict.dao.ID2NameDAO;
import org.hpin.base.dict.exceptions.DictDAOException;
import org.hpin.base.region.entity.Region;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.springframework.stereotype.Repository;

@Repository(value="org.hpin.webservice.dao.CustomerRelationshipDao")
public class CustomerRelationshipDao extends BaseDao implements ID2NameDAO{

	@SuppressWarnings("unchecked")
	@Override
	public String id2Name(String id) throws DictDAOException {
		String queryString = " from CustomerRelationShip customer where customer.id=?";
		List<CustomerRelationShip> list = this.getHibernateTemplate().find(queryString,id);
		String customerName = "";
		if( list != null && list.size() >0){
			CustomerRelationShip customerRelationShip = list.get(0);
			customerName = customerRelationShip.getBranchCommany();
		}
		return customerName;
	}

	@Override
	public String id2Field(String id, String beanId, String field) throws DictDAOException {
		

		String result="";
		CustomerRelationShip customer=  this.getHibernateTemplate().get(CustomerRelationShip.class, id);
		if("province".equals(field)) {
			String hql = "from Region t where t.id=?";
			List<Region> regin = this.getHibernateTemplate().find(hql, customer.getProvince());
			result  = regin.get(0).getRegionName();
		}else if("city".equals(field)) {
			String hql = "from Region t where t.id=?";
			List<Region> region = this.getHibernateTemplate().find(hql, customer.getCity());
			result  = region.get(0).getRegionName();
		}
			return result;
    }
	
	/**
	 * 根据公司ID查找套餐
	 * @param companyId 公司ID
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-5-5 下午2:49:28
	 */
	public String findComboByCompanyId(String companyId) throws Exception{
		String combo = null;
		String hql = "from CustomerRelationShip customer where customer.id=?";
		CustomerRelationShip entity = (CustomerRelationShip)this.getHibernateTemplate().find(hql, companyId).get(0);
		combo = entity.getCombo();
		return combo;
	}
	
	/**
	 * 根据公司ID查找对象
	 */
	public CustomerRelationShip findShipByCompanyId(String companyId) throws Exception{
		String hql = "from CustomerRelationShip customer where customer.id=?";
		List<CustomerRelationShip> list = this.getHibernateTemplate().find(hql, companyId);
		if(null!=list&&0<list.size()){
			CustomerRelationShip entity = list.get(0);
			return entity;
		}else{
			return null;
		}
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
	 * @param companyName
	 * @param companyId
	 * @return 获取的该支公司所有套餐
	 * @throws Exception
	 */
	public List getCompanyPackagePrice1(String companyName,String companyId) throws Exception{

		String getStringComboSql="select a.combo from hl_customer_relationship a where a.branch_commany='"+companyName+"' and a.id='"+companyId+"'";
		return this.getJdbcTemplate().queryForList(getStringComboSql);		
	}
	
	/**
	 * @param companyName
	 * @param companyId
	 * @return 获取的该支公司所有套餐价格
	 * @throws Exception
	 */
	public List getCompanyPackagePrice2(String companyName,String companyId) throws Exception{
		String getName_PriceSql="select a.combo,a.combo_price from erp_company_combo_price a where a.company='"+companyName+"'";
		return this.getJdbcTemplate().queryForList(getName_PriceSql);	
	}
	
	//TODO
	public List listByProps(Map<String,String> params) throws Exception{
		List<CustomerRelationShip> list = null;
		if(params!=null&&params.keySet().size()>0){
		}
		return list;
	}

	/**
	 * 根据支公司id查询对应的数据信息;
	 * create by henry.xu 2016年12月5日
	 * @param branchCompanyId
	 */
	public Map<String, Object> findMapByShipId(String branchCompanyId) {
		String sql = "select pro.PROJECT_OWNER projectOwner, pro.id proId from HL_CUSTOMER_RELATIONSHIP ship " +
				"left join HL_CUSTOMER_RELATIONSHIP_PRO pro on pro.CUSTOMER_RELATIONSHIP_ID = ship.id " +
				"left join T_PROJECT_TYPE ty on ty.id = pro.project_type " +
				"where " +
				"ship.id = '"+branchCompanyId+"' " +
				"and ty.PROJECT_TYPE = 'PCT_004' " + //comboName默认为无创生物电套餐一;
				"and pro.IS_SEAL = '0' " ;  //未封存状态;
		return this.getJdbcTemplate().queryForMap(sql);
	}
	
	/**
	 * 根据支公司id查询对应的数据信息;
	 * create by henry.xu 2016年12月5日
	 * @param branchCompanyId
	 */
	public Map<String, Object> findTYMapByShipId(String branchCompanyId, String projectType) {
		String sql = "select pro.PROJECT_OWNER projectOwner, pro.id proId from HL_CUSTOMER_RELATIONSHIP ship " +
				"left join HL_CUSTOMER_RELATIONSHIP_PRO pro on pro.CUSTOMER_RELATIONSHIP_ID = ship.id " +
				"left join T_PROJECT_TYPE ty on ty.id = pro.project_type " +
				"where " +
				"ship.id = '"+branchCompanyId+"' " +
				"and ty.PROJECT_TYPE = '"+projectType+"' " + //comboName默认为无创生物电套餐一;
				"and pro.IS_SEAL = '0' " ;  //未封存状态;
		return this.getJdbcTemplate().queryForMap(sql);
	}

	/**
	 * 根据总公司id查询名称;
	 * create by henry.xu 2016年12月7日
	 * @param id
	 * @return
	 */
	public String findOwnedCompanyName(String id) {
		String sql = "select dept_name deptName from um_dept where id = '"+id+"' ";
		Map<String, Object> mapObject = this.getJdbcTemplate().queryForMap(sql);
		return null != mapObject ? (String)mapObject.get("deptName") : "";
	}

}