package org.hpin.webservice.dao.yg;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.yg.ErpBxCompanyPreSet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author machuan
 * @date 2017年2月4日
 */
@Repository
public class ErpBxcompanyPreSetDao extends BaseDao{

	/**
	 * @param filterMap
	 * @return
	 * @author machuan
	 * @date  2017年2月4日
	 */
	public List<ErpBxCompanyPreSet> getComboInfo(Map<String, String> filterMap) {
		String sql = "SELECT combo_name comboName,combo_show_name comboShowName,events_no eventsNo,application_no applicationNo,customer_relationship_id customerRelationshipId "
				+ "FROM ERP_BXCOMPANY_PRESET WHERE CUSTOMER_SKU_NUM=? AND CUSTOMER_NAME=? AND IS_USEABLE='0'";
		BeanPropertyRowMapper<ErpBxCompanyPreSet> rowMapper = new BeanPropertyRowMapper<ErpBxCompanyPreSet>(ErpBxCompanyPreSet.class);
		return this.getJdbcTemplate().query(sql,new Object[]{filterMap.get("uniqueCode"),filterMap.get("userName")} , rowMapper);
	}

}
