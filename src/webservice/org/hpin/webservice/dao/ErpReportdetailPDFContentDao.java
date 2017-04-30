package org.hpin.webservice.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpReportdetailPDFContent;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ErpReportdetailPDFContentDao extends BaseDao {
	
	/**
	 * 根据code和username查询对应的报告数据;
	 * 查询中只能查询报告状态为基因的及reportType='0'
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年4月11日
	 */
	public List<ErpReportdetailPDFContent> findByCodeAndUserName(String code, String username) {
		List<ErpReportdetailPDFContent> list = null;
		String sql = "select pdfc.* from ERP_REPORTDETAIL_PDFCONTENT pdfc " +
		"inner join erp_customer cus on cus.code =pdfc.code and cus.name=pdfc.username " +
		"inner join erp_events event on event.EVENTS_NO = cus.EVENTS_NO " +
		"inner join hl_customer_relationship_pro pro on pro.id = event.CUSTOMERRELATIONSHIPPRO_ID " +
		"inner join T_PROJECT_TYPE ty on ty.id = pro.PROJECT_TYPE " +
		"where " +
		"pdfc.reportType='0' and pdfc.code = '" + code + "' and pdfc.username = '" + username + "' and pdfc.matchstate in (2, 12)  " +
		"and ty.PROJECT_TYPE = 'PCT_001'" ;
		
		BeanPropertyRowMapper<ErpReportdetailPDFContent> rowMapper = new BeanPropertyRowMapper<ErpReportdetailPDFContent>(ErpReportdetailPDFContent.class);
		try {
			list = this.getJdbcTemplate().query(sql, rowMapper);
		} catch(DataAccessException e) {
			list = null;
		}
		
		return list;
	}
	
	public void save(ErpReportdetailPDFContent pdfContent){
		this.getHibernateTemplate().save(pdfContent);
	}
	
	public void save(List<ErpReportdetailPDFContent> pdfContentList){
		this.getHibernateTemplate().saveOrUpdateAll(pdfContentList);
	}
	
	public int saveList(List<ErpReportdetailPDFContent> list){
		Integer count = 0;
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				this.getHibernateTemplate().save(list.get(i));
				count++;
			}
		}
		return count;
	}
	
	public List<ErpReportdetailPDFContent> findByProps(String code, String pdfName) throws Exception{
		List<ErpReportdetailPDFContent> list = null;
		if(StringUtils.isNotEmpty(code)){
			String hql = " from ErpReportdetailPDFContent where 1=1 and code = ? and pdfName = ?";
			list  = this.getHibernateTemplate().find(hql,new Object[]{code, pdfName});
		}
		return list;
	}
}
