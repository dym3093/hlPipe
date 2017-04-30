package org.hpin.webservice.dao;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ReportCustomerInfo;
import org.springframework.stereotype.Repository;

@Repository
public class ReportCustomerInfoDao extends BaseDao {
	
	public void save(ReportCustomerInfo reCusInfo) throws Exception {
		this.getHibernateTemplate().save(reCusInfo);
	}

	/**
	 * 当通过条件查询时,已存在数据返回true,否则false
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年2月28日
	 */
	public boolean findIsExcitByCondtions(String reportId, String reportNum) {
		
		String sql = "select count(1) from ERP_REPORT_CUSTOMER_INFO where REPORT_ID= '" + reportId + "' and REPORT_NUM='" + reportNum + "'";
		int count = this.getJdbcTemplate().queryForInt(sql);
		if(count > 0) {
			return true;
		}
		return false;
	}
}
