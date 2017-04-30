/**
 * @author DengYouming
 * @since 2016-10-26 下午6:20:42
 */
package org.hpin.webservice.service;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.ErpReportDetail;
import org.hpin.webservice.dao.ErpReportDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author DengYouming
 * @since 2016-10-26 下午6:20:42
 */
@Service(value = "org.hpin.webservice.service.ErpReportDetailService")
@Transactional
public class ErpReportDetailService extends BaseService{
	
	@Autowired
	private ErpReportDetailDao dao;
	
	public void saveReportDetail(ErpReportDetail obj){
		dao.save(obj);
	}
	
	/**
	 * 根据条件精确查找
	 * @param params 查询条件
	 * @return List
	 * @author DengYouming
	 * @since 2016-10-26 下午6:29:50
	 */
	public List<ErpReportDetail> listRerortDetailByProps(Map<String,String> params){
		return dao.listRerortDetailByProps(params, true);
	}
	
	/**
	 * 根据条件是否精确查找 
	 * @param params 查询条件
	 * @param isExact 是否精确查找，true:精确  false:模糊
	 * @return List
	 * @author DengYouming
	 * @since 2016-10-26 下午6:30:31
	 */
	public List<ErpReportDetail> listRerortDetailByProps(Map<String,String> params, boolean isExact){
		return dao.listRerortDetailByProps(params, isExact);
	}
	
}
