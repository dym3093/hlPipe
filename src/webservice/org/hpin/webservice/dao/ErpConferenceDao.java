package org.hpin.webservice.dao;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpConference;
import org.springframework.stereotype.Repository;
/**
 * 会议管理dao
 * @description: 
 * create by henry.xu 2016年12月5日
 */
@Repository
public class ErpConferenceDao extends BaseDao{
	
	/**
	 * 保存会议管理对象;
	 * create by henry.xu 2016年12月5日
	 * @param conference
	 */
	public void saveErpConference(ErpConference conference) {
		this.getHibernateTemplate().save(conference);
	}
}
