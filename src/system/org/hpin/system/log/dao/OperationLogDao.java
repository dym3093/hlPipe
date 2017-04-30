package org.hpin.system.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.common.core.orm.OrmConverter;
import org.hpin.common.widget.pagination.Page;
import org.springframework.stereotype.Repository;

/**
 * <p>@desc : 操作日志处理Dao</p>
 * <p>@see : </p>
 *
 * <p>@author : sky</p>
 * <p>@createDate : Aug 29, 2012 9:45:50 PM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p>
 */

@Repository()
public class OperationLogDao extends BaseDao {

	/**
	 * 分页方式查询操作日志
	 * @param page
	 * @param searchMap
	 * @return
	 */
	public List findByPage(Page page, Map searchMap) {
		StringBuffer query = new StringBuffer(" from OperationLog where 1=1");
		List valueList = new ArrayList();
		OrmConverter.getQuery(query, searchMap, "filter_and_loginLogId_EQ_L",
				valueList);
		OrmConverter.getQuery(query, searchMap, "filter_and_userName_LIKE_S",
				valueList);
		OrmConverter.getQuery(query, searchMap,
				"filter_and_operationDate_GE_T", valueList);
		OrmConverter.getQuery(query, searchMap,
				"filter_and_operationDate_LE_T", valueList);
		OrmConverter.getQuery(query, searchMap,
				"filter_and_businessName_LIKE_S", valueList);
		query.append(" order by operationTime desc");
		return super.findByHql(page, query, valueList);
	}

}
