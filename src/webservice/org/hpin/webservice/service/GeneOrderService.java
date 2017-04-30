package org.hpin.webservice.service;

import java.util.ArrayList;
import java.util.List;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.OrderInfo;
import org.hpin.webservice.dao.GeneOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service(value = "org.hpin.webservice.service.GeneOrderService")
@Transactional()
public class GeneOrderService extends BaseService{
	@Autowired
	private GeneOrderDao dao;
	
	public void save(OrderInfo obj) throws Exception{
		dao.save(obj);
	}
	
	/**
	 * 根据订单号查找是否重复
	 * @param orderNo
	 * @return true 重复;false 不重复
	 */
	public boolean isRepeat(String orderNo){		
		return dao.getInfoByOrderNo(orderNo);
	}

	/**
	 * 获取订单号对应的ID
	 * @param orderNo
	 * @param status
	 * @return
	 */
	public List<OrderInfo> getOrderInfo(String orderNo,String status) {
		List<OrderInfo> list = dao.getOrderId(orderNo);
		List<OrderInfo> orderList = new ArrayList<OrderInfo>();
		for(OrderInfo info:list){
			info.setStatus(status);
			orderList.add(info);
		}
		return orderList;
	}

	/**
	 * @param list
	 * 更新订单状态
	 */
	public void updateStatus(List<OrderInfo> list) {
		dao.getHibernateTemplate().saveOrUpdateAll(list);
	}
}
