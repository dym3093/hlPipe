package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpCustomerTempWuChuang;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * @description: 客户信息临时表Dao
 * create by henry.xu 2016年12月2日
 */
@Repository
public class ErpCustomerTempWuChuangDao extends BaseDao {

	/**
	 * 保存客户信息临时数据;
	 * create by henry.xu 2016年12月2日
	 */
	public void saveObject(ErpCustomerTempWuChuang customer) {
		this.getHibernateTemplate().save(customer);
	}

	/**
	 * 根据Id获取数据;
	 * create by henry.xu 2016年12月2日
	 * @param id
	 * @return
	 */
	public ErpCustomerTempWuChuang findById(String id) {
		return this.getHibernateTemplate().get(ErpCustomerTempWuChuang.class, id);
	}

	/**
	 * 根据条形码和姓名获取;
	 * create by henry.xu 2016年12月5日
	 * @param code
	 * @param name
	 */
	public ErpCustomerTempWuChuang findByCodeAndName(String code, String name) {
		String sql = "select * from ERP_CUSTOMER_TEMP_WUCHUANG "+
				"where "+
				"code = '"+code+"' "+
				"and name = '"+name+"' ";
		BeanPropertyRowMapper<ErpCustomerTempWuChuang> rowMapper = new BeanPropertyRowMapper<ErpCustomerTempWuChuang>(ErpCustomerTempWuChuang.class);
		List<ErpCustomerTempWuChuang> lists = this.getJdbcTemplate().query(sql, rowMapper);
		return (null != lists && lists.size() > 0) ? lists.get(0) : null;
	}

	public ErpCustomerTempWuChuang findByNameBirthdaySex(String userName,
			String birthday, String sex) {
		String sql = "select * from ERP_CUSTOMER_TEMP_WUCHUANG "+
				"where "+
				"NAME = '"+userName+"' "+
				"and BIRTHDAY = '"+birthday+"' " +
				"and SEX = '"+sex+"' ";
		return this.getJdbcTemplate().queryForObject(sql, ErpCustomerTempWuChuang.class);
	}

	/**
	 * 修改状态
	 * create by henry.xu 2016年12月5日
	 * @param isMatch
	 */
	public void updateIsMatch(String isMatch, String id) throws Exception {
		String sql = "update ERP_CUSTOMER_TEMP_WUCHUANG set IS_MATCH='"+isMatch+"' where id='"+id+"'";
		this.getJdbcTemplate().update(sql);
	}

	/**
	 * 根据支公司Id+ 证件号查询无创缓存表中是否有存在数据;
	 * create by henry.xu 2016年12月13日
	 * 
	 * modified by henry.xu 20170417
	 * 添加如果存在了以上条件, 然后在后面在给一个验证,是否在erp_customer中已经出了报告了,
	 * 如果出了报告之后,可以重新进入;
	 * 
	 * @param shipId
	 * @param idCard
	 * @return
	 */
	public boolean findCustomerIsExits(String shipId, String idCard, String projectType) {

		String sql = "select code from ERP_CUSTOMER_TEMP_WUCHUANG where BRANCH_COMPANY_ID='"+shipId+"' and idno = '"+idCard+"'";
		if(StringUtils.isNotEmpty(projectType)) {
			sql += " and PROJECT_TYPE='"+projectType+"'";
		}
		Map<String, Object> mapObj = null;
		try {
			mapObj = this.getJdbcTemplate().queryForMap(sql); // 如果存在就获取code, 不存在返回false;
		} catch(Exception e) {
			return false;
		}	

		if(mapObj != null && !mapObj.isEmpty()) {
			String code = (String)mapObj.get("code");

			//当code为空时,表示没有对应的数据,说明还没有保存有过.
			if(StringUtils.isEmpty(code)) {
				return false;
			}

			/*
			 * 当code有数据时,在判断是否出了报告;如果有报告则返回false, 如果没有
			 * 报告返回true;
			 */
			String sqlCustomer = "select PDFFILEPATH pdfFilePath from erp_customer where code = '"+code+"' and is_deleted=0 ";
			Map<String, Object> mapCustomer = null;
			try {	
				mapCustomer = this.getJdbcTemplate().queryForMap(sqlCustomer);
			} catch(Exception e) {
				return true;
			}
			if(mapCustomer != null && !mapCustomer.isEmpty()) {
				String pdfFilePath = (String)mapCustomer.get("pdfFilePath");
				if(StringUtils.isEmpty(pdfFilePath)) {//报告路径为空的时候, 返回true验证不通过;
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}

		} else {
			return false;
		}

	}

}
