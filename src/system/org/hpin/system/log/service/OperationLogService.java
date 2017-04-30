package org.hpin.system.log.service;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseService;
import org.hpin.common.widget.pagination.Page;
import org.hpin.system.log.dao.OperationLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "org.hpin.system.log.service.OperationLogService")
@Transactional()
public class OperationLogService extends BaseService {

	@Autowired
	private OperationLogDao operationLogDao = null;

	/**
	 * 分页获取操作日志
	 * 
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage(Page page, Map searchMap) {
		return operationLogDao.findByPage(page, searchMap);
	}
}
