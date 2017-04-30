package org.hpin.system.log.service;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseService;
import org.hpin.common.widget.pagination.Page;
import org.hpin.system.log.dao.LoginLogDao;
import org.hpin.system.log.entity.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>@desc : 登陆日志Service </p>
 * <p>@see : </p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Aug 30, 2012 10:17:46 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */

@Service(value = "org.hpin.system.log.service.LoginLogService")
@Transactional()
public class LoginLogService extends BaseService {

	@Autowired
	private LoginLogDao loginLogDao = null;

	/**
	 * 分页获取登录日志
	 * 
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage(Page page, Map searchMap) {
		return loginLogDao.findByPage(page, searchMap);
	}

	public void save(LoginLog loginLog) {
		loginLogDao.save(loginLog);
	}
}
