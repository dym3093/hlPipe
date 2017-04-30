package org.hpin.webservice.dao.pa;

import java.util.List;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.springframework.stereotype.Repository;


/**
 * @author machuan
 * @date 2017年2月9日
 */
@Repository
public class ErpPACustomerDao extends BaseDao{

	/**
	 * @param serviceName
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public String findYmComboByCheckCombo(String serviceName) {
		/*String sql = "select eyt.ym_comboname from erp_ymcomboname_testcomboname eyt WHERE eyt.project='PA' and eyt.test_comboname like '%"
				+ serviceName
				+ "%'";*/
		String sql ="SELECT * FROM erp_relationshippro_combo t where t.customer_relationship_pro_id='ff8080815a211c39015a21a048fa0c28'  and t.combo_show_name LIKE'%"
					+ serviceName
					+ "%'";
		return (String) this.getJdbcTemplate().queryForList(sql).get(0).get("ym_comboname");
	}

	/**
	 * @param orderId
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	@SuppressWarnings("unchecked")
	public ErpPreCustomer getPreCustomerByOrderId(String orderId) {
		String sql = "from ErpPreCustomer  where performNo=? and (isDeleted is null or isDeleted=0) ";
		List<ErpPreCustomer> list = this.getHibernateTemplate().find(sql, orderId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
