package org.hpin.webservice.dao;

import java.util.List;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.OrderInfo;
import org.springframework.stereotype.Repository;

/**
 * 基因订单信息表数据库Dao层
 * @author ybc
 * @since 2016-06-06
 */
@Repository()
public class GeneOrderDao extends BaseDao {

	//根据订单号查找
	@SuppressWarnings("unchecked")
	public boolean getInfoByOrderNo(String orderNo){		
		String queryString = "select count(*) from ERP_ORDER_INFO where ORDERNO=?";
		int count=this.getJdbcTemplate().queryForInt(queryString, new Object[]{orderNo});
		return count==0?false:true;
	}

	public List<OrderInfo> getOrderId(String orderNo) {
		String sql = "from OrderInfo where orderNo = ?";
		return this.getHibernateTemplate().find(sql, new Object[]{orderNo});
	}
	
}
