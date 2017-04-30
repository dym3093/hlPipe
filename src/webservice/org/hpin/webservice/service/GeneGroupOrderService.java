package org.hpin.webservice.service;

import java.util.List;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.GroupOrderCombo;
import org.hpin.webservice.bean.GroupOrderInfo;
import org.hpin.webservice.dao.GeneGroupOrderComboDao;
import org.hpin.webservice.dao.GeneGroupOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "org.hpin.webservice.service.GeneGroupOrderService")
@Transactional()
public class GeneGroupOrderService extends BaseService {

	@Autowired
	private GeneGroupOrderDao dao;
	
	@Autowired
	private GeneGroupOrderComboDao comboDao;
	
	/**
	 * 保存团购订单
	 * @param obj GroupOrderInfo
	 * @return boolean 
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-6-23 上午10:38:29
	 */
	public boolean saveGroupOrderInfo(GroupOrderInfo obj) throws Exception{
		boolean flag = false;
		List<GroupOrderCombo> comboList = obj.getComboList();
		if(obj!=null&&comboList!=null&&comboList.size()>0){
			for (GroupOrderCombo entity : comboList) {
				entity.setOrderNo(obj.getOrderNo());
				comboDao.save(entity);
			}
			dao.save(obj);
			flag = true;
		}
		return flag;
	}
	
}
