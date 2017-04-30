package org.hpin.webservice.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.common.util.DateUtils;
import org.hpin.webservice.bean.ErpCustomerReceive;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * @description: 客户信息临时表Dao
 * create by henry.xu 2016年12月2日
 */
@Repository
public class CustomerReceivesDao extends BaseDao{
	
	/**
	 * 保存客户信息临时数据;
	 * create by henry.xu 2016年12月2日
	 */
	public void saveObject(ErpCustomerReceive customer) {
		this.getHibernateTemplate().save(customer);
	}
	
	/**
	 * 根据Id获取数据;
	 * create by henry.xu 2016年12月2日
	 * @param id
	 * @return
	 */
	public ErpCustomerReceive findById(String id) {
		return this.getHibernateTemplate().get(ErpCustomerReceive.class, id);
	
	}

	/**
	 * 
	 * 查询当天进入数据库数据,并根据(支公司Id+检测日期+场次类型 分为同一个场次)分组;
	 * create by henry.xu 2016年12月5日
	 * 
	 * 增加了项目类型作为区别;当projectType为空时则为无创知康;
	 * modified by henry.xu 20170122
	 * @return
	 */
	public List<Map<String, Object>> findGroupByTime() {
		
		String date = DateUtils.dateToStr(new Date(), "yyyy-MM-dd");
		
		String sql = "select  " +
        		"receive.BRANCHCOMPANYID branchCompanyId, " +
				"receive.EXAMDATE examDate, " +
				"receive.EVENTSTYPE eventsType, count(1) eventsCount, receive.BATCHNO batchNo, receive.project_type projectType " +
				"from erp_customer_receive receive " +
				"where receive.create_time < to_date('"+date+"', 'yyyy-mm-dd') + 1 " +
				"and receive.create_time >= to_date('"+date+"', 'yyyy-mm-dd') and receive.RETURNFLAG = 'Yes' and receive.ismatch='-1' and receive.project_type = 'PCT_004' " +
				"group by " +
				"receive.BRANCHCOMPANYID,  " +
				"receive.EXAMDATE,  " +
				"receive.eventsType, receive.BATCHNO, receive.project_type";
		
		return this.getJdbcTemplate().queryForList(sql);
		
	}
	
	/**
	 * 查询当天进入数据库数据,并根据(支公司Id+检测日期+场次类型+项目类别编码 分为同一个场次)分组;
	 * create by henry.xu 2017年01月22日
	 * @return
	 */
	public List<Map<String, Object>> findTYGroupByTime() {
		
		String date = DateUtils.dateToStr(new Date(), "yyyy-MM-dd");
		
		String sql = "select  " +
        		"receive.BRANCHCOMPANYID branchCompanyId, " +
				"receive.EXAMDATE examDate, " +
				"receive.EVENTSTYPE eventsType, count(1) eventsCount, receive.BATCHNO batchNo, receive.project_type projectType " +
				"from erp_customer_receive receive " +
				"where receive.create_time < to_date('"+date+"', 'yyyy-mm-dd') + 1 " +
				"and receive.create_time >= to_date('"+date+"', 'yyyy-mm-dd') and receive.RETURNFLAG = 'Yes' and receive.ismatch='-1' and receive.project_type = 'PCT_005' " +
				"group by " +
				"receive.BRANCHCOMPANYID,  " +
				"receive.EXAMDATE,  " +
				"receive.eventsType, receive.BATCHNO, receive.project_type";
		
		
		return this.getJdbcTemplate().queryForList(sql);
		
	}

	/**
	 * 根据条件查询customer_receive表中数据;
	 * create by henry.xu 2016年12月5日
	 * 
	 * 知康添加了条件project_type is null; 知康默认为空;
	 * @param branchCompanyId
	 * @param examDate
	 * @param eventsType
	 * @return
	 */
	public List<ErpCustomerReceive> findObjectsByConditions(
			String branchCompanyId, String examDate, String eventsType) {
		String sql = "select id, "
				+ "batchNo, "
				+ "serviceCode, "
				+ "userName, "
				+ "sex, "
				+ "birthday, "
				+ "branchCompanyId companyId, "
				+ "branchCompanyName companyName, "
				+ "ownerCompanyId, ownerCompanyName, "
				+ "examdate, "
				+ "examTime, "
				+ "eventsType, "
				+ "isMatch, "
				+ "create_time createTime, "
				+ "update_time updateTime, "
				+ "returnFlag, "
				+ "other, "
				+ "project_type projectType, "
				+ "source_from sourceFrom "
				+ "from ERP_CUSTOMER_RECEIVE  " +
			"where  " +
			"BRANCHCOMPANYID='" + branchCompanyId + "'  " +
			"and EXAMDATE = '" + examDate.substring(0, 10) + "' " +
			"and EVENTSTYPE='" + eventsType + "' and returnflag='Yes' and ismatch='-1' and project_type = 'PCT_004' ";
		BeanPropertyRowMapper<ErpCustomerReceive> rowMapper = new BeanPropertyRowMapper<ErpCustomerReceive>(ErpCustomerReceive.class);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}
	
	/**
	 * 根据条件查询customer_receive表中数据;
	 * create by henry.xu 2016年12月5日
	 * @param branchCompanyId
	 * @param examDate
	 * @param eventsType
	 * @return
	 */
	public List<ErpCustomerReceive> findTYObjectsByConditions(
			String branchCompanyId, String examDate, String eventsType, String projectType) {
		String sql = "select id, "
				+ "batchNo, "
				+ "serviceCode, "
				+ "userName, "
				+ "sex, "
				+ "birthday, "
				+ "branchCompanyId companyId, "
				+ "branchCompanyName companyName, "
				+ "ownerCompanyId, ownerCompanyName, "
				+ "examdate, "
				+ "examTime, "
				+ "eventsType, "
				+ "isMatch, "
				+ "create_time createTime, "
				+ "update_time updateTime, "
				+ "returnFlag, "
				+ "other, "
				+ "project_type projectType, "
				+ "source_from sourceFrom, "
				+ "report_type reportType " 
				+ "from ERP_CUSTOMER_RECEIVE " +
			"where  " +
			"BRANCHCOMPANYID='" + branchCompanyId + "'  " +
			"and EXAMDATE = '" + examDate.substring(0, 10) + "' " +
			"and EVENTSTYPE='" + eventsType + "' and returnflag='Yes' and ismatch='-1' and project_type='"+projectType+"' ";
		BeanPropertyRowMapper<ErpCustomerReceive> rowMapper = new BeanPropertyRowMapper<ErpCustomerReceive>(ErpCustomerReceive.class);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	/**
	 * 修改是否匹配;
	 * create by henry.xu 2016年12月5日
	 * @param isMatch
	 * @param id
	 */
	public void updateIsMatch(String isMatch, String receiveId) throws Exception {
		String sql = "update ERP_CUSTOMER_RECEIVE set ISMATCH='"+isMatch+"' where id='"+receiveId+"'";
		this.getJdbcTemplate().update(sql);
	}

	public boolean findCountByCondition(String serviceCode) {
		String sql = "select count(1) from ERP_CUSTOMER_RECEIVE where SERVICECODE='"+serviceCode+"' ";
		return this.getJdbcTemplate().queryForInt(sql) > 0;
		
	}
}
