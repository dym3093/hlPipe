package org.hpin.common.core.orm.daoWrapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 数据库Dao层封装类.
 * 
 * @author thinkpad
 * @Apr 14, 2009
 */
public class DaoSupport {
	protected HibernateDaoSupportWrapper hibernateDaoSupport;

	protected JdbcDaoSupportWrapper jdbcDaoSupport;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateDaoSupport.getHibernateTemplate();
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcDaoSupport.getJdbcTemplate();
	}

	public HibernateDaoSupportWrapper getHibernateDaoSupport() {
		return hibernateDaoSupport;
	}

	public void setHibernateDaoSupport(HibernateDaoSupportWrapper hibernateDaoSupport) {
		this.hibernateDaoSupport = hibernateDaoSupport;
	}

	public JdbcDaoSupportWrapper getJdbcDaoSupport() {
		return jdbcDaoSupport;
	}

	public void setJdbcDaoSupport(JdbcDaoSupportWrapper jdbcDaoSupport) {
		this.jdbcDaoSupport = jdbcDaoSupport;
	}

}
