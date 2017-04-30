package org.hpin.webservice.dao;

import java.util.List;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.PreSalesMan;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * @description: 预留营销员信息表对应dao
 * create by henry.xu 2017年2月8日
 */
@Repository
public class PreSalesManDao extends BaseDao{
	
	/**
	 * 根据参数查询对应的预留营销员信息数据;
	 * create by henry.xu 2017年2月8日
	 * @param salesManName
	 * @param salesManNo
	 * @param salesChannel
	 * @return
	 */
	public PreSalesMan findSalesManInfoByParams(String salesManName, String salesManNo, String salesChannel) {
		String sql = "select " +
		"salePre.ID, " +
		"salePre.SALESMAN salesman, " +
		"salePre.EMPLOYEE_NO employeeNo, " +
		"salePre.EMPLOYEE_PHONE employeePhone, " +
		"salePre.EMPLOYEE_COMPANY employeeCompany, " +
		"salePre.EMPLOYEE_CITY_COMPANY employeeCityCompany, " +
		"salePre.EMPLOYEE_HEAD_OFFICE employeeHeadOffice, " +
		"salePre.YM_COMPANY_ID ymCompanyId, " +
		"ship.BRANCH_COMMANY ymCompany, " +
		"salePre.YM_OWNCOMPANY_ID ymOwncompanyId, " +
		"dep.DEPT_NAME ymOwncompany, " +
		"salePre.IS_DELETED isDeleted, " +
		"salePre.MARK mark " +
		"from ERP_SALEMANNUM_INFO_PRE salePre " +
		"left join HL_CUSTOMER_RELATIONSHIP ship on salePre.YM_COMPANY_ID = ship.id " +
		"left join um_dept dep on dep.id = salePre.YM_OWNCOMPANY_ID " +
		"where " +
		"salePre.SALESMAN= '"+salesManName+"' " +
		"and salePre.EMPLOYEE_NO = '"+salesManNo+"' " +
		//"and salePre.MARK = '"+salesChannel+"' " + 
		"and salePre.IS_DELETED =0 ";
		
		BeanPropertyRowMapper<PreSalesMan> rowMapper = new BeanPropertyRowMapper<PreSalesMan>(PreSalesMan.class);
		List<PreSalesMan> list =this.getJdbcTemplate().query(sql, rowMapper);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
}
