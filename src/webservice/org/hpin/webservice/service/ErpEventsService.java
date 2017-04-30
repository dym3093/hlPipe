package org.hpin.webservice.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.BeanUtils;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.dao.ErpEventsDao;
import org.hpin.webservice.dao.ErpCustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "org.hpin.webservice.service.ErpEventsService")
@Transactional()
public class ErpEventsService extends BaseService {
	private Logger log = Logger.getLogger(ErpEventsService.class);
	
	@Autowired
	ErpCustomerDao customerDao;
	@Autowired
	ErpEventsDao eventsDao;
	
	/**
	 * 保存
	 * 
	 * @param erpCustomer
	 */
	public void save(ErpCustomer erpCustomer) {
		customerDao.save(erpCustomer);
	}

	/* 非物理删除 */
	public void delete(List<ErpCustomer> list) {
		for (ErpCustomer customer : list) {
			customerDao.update(customer);
		}
	}
	

	/**
	 * 更新
	 *
     * @param customer
	 */
	public void updateInfo(ErpCustomer customer) {
		ErpCustomer _erpCustomer = (ErpCustomer) customerDao.findById(ErpCustomer.class, customer.getId());
		BeanUtils.copyProperties(_erpCustomer, customer);
		_erpCustomer.setUpdateTime(new Date());
		customerDao.update(_erpCustomer);
	}
	
	/**
	 * 根据参数查询对应的场次信息
	 * @param params Map
	 * @return List 场次列表
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-4 下午12:36:24
	 */
	public List<ErpEvents> listEventsByProps(Map<String, String> params) throws Exception{
		List<ErpEvents> list = null;
		if(!params.isEmpty()){
			list = eventsDao.listEventsByProps(params);
		}
		return list;
	}
	
	/**
	 * 可以根据 
	 * 1）info: 会员姓名 , infoType: name 
	 * 2）info: 会员条码, infoType: code 
	 * 3）info: 批次号,  infoType: batchNo
	 * 4）info: 场次号,  infoType: eventsNo
	 * 5）info: 团单号 ,  infoType: groupOrderNo
	 * 等信息，查找到相关场次
	 * @param info 传入的信息
	 * @param infoType 信息类型
	 * @return List
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-25 下午5:03:39
	 */
	public List<ErpEvents> listEventsByInfo(String info, String infoType)throws Exception{
		List<ErpEvents> list = null;
		if(StringUtils.isNotEmpty(info)&&StringUtils.isNotEmpty(infoType)){
			list = eventsDao.listEventsByInfo(info,infoType);
		}
		return list;
	}
	
}
