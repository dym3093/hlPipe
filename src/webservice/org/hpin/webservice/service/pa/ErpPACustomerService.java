package org.hpin.webservice.service.pa;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.dao.pa.ErpPACustomerDao;
import org.hpin.webservice.dao.ty.ErpTYCustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 平安健康基因检测业务处理service
 * @author machuan
 * @date 2017年2月9日
 */
@Service("org.hpin.webservice.service.pa.ErpPACustomerService")
@Transactional
public class ErpPACustomerService extends BaseService{
	
	@Autowired
	private ErpPACustomerDao dao;
	/**
	 * @param serviceName
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public String findYmComboByCheckCombo(String serviceName) {
		return this.dao.findYmComboByCheckCombo(serviceName);
	}
	/**
	 * @param orderId
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	public ErpPreCustomer getPreCustomerByOrderId(String orderId) {
		return this.dao.getPreCustomerByOrderId(orderId);
	}
	
	
}




































