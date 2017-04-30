/**
 * @author DengYouming
 * @since 2016-10-14 上午11:22:33
 */
package org.hpin.webservice.service;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.ErpOrder;
import org.hpin.webservice.dao.ErpOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DengYouming
 * @since 2016-10-14 上午11:22:33
 */
@Service(value = "org.hpin.webservice.service.ErpOrderService")
@Transactional
public class ErpOrderService extends BaseService{

	@Autowired
	private ErpOrderDao dao;
	
	public List<ErpOrder> findByProps(Map<String,String> params) throws Exception{
		return dao.findByProps(params);
	}
}
