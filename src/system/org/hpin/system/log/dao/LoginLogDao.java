package org.hpin.system.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.common.core.orm.OrmConverter;
import org.hpin.common.widget.pagination.Page;
import org.hpin.system.log.entity.LoginLog;
import org.springframework.stereotype.Repository;

/**
 * <p>@desc : 登陆日志Dao</p>
 * <p>@see : </p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Aug 29, 2012 9:22:10 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */
@Repository()
public class LoginLogDao extends BaseDao {

	/**
	 * 分页查询登陆日志
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage(Page page, Map searchMap) {
		StringBuffer query = new StringBuffer(" from LoginLog where 1=1");
		List valueList = new ArrayList();
		OrmConverter.getQuery(query, searchMap, "filter_and_userName_LIKE_S",
				valueList);
		OrmConverter.getQuery(query, searchMap, "filter_and_orgName_LIKE_S",
				valueList);
		OrmConverter.getQuery(query, searchMap, "filter_and_loginTime_GE_T",
				valueList);
		OrmConverter.getQuery(query, searchMap, "filter_and_loginTime_LE_T",
				valueList);
		query.append(" order by loginTime desc");
		return super.findByHql(page, query, valueList);
	}

	/**
	 * 同步方法，JDBC方式保存日志信息
	 * @param loginLog
	 */
	public void saveForJdbc(LoginLog loginLog) {
//		String id = this.getJdbcTemplate().queryForLong(
//				"select max(id) from sys_login_log") + 1;
//		loginLog.setId(id);
		String sql = "insert into sys_login_log(id,user_name,login_ip,org_name,login_time)values(?,?,?,?,?)";
		this.getJdbcTemplate().update(
				sql,
				new Object[] { loginLog.getId(), loginLog.getUserName(),
						loginLog.getLoginIp(), loginLog.getOrgName(),
						loginLog.getLoginTime() });
	}
}
