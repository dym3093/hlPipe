package org.hpin.fg.system.util;

import java.sql.Types;

import org.hibernate.Hibernate;

/**
 * @author YueJQ
 * 解决sql中有变量只放回第一个字的问题
 */
public class MyOracleDialect extends org.hibernate.dialect.Oracle10gDialect
{
	public MyOracleDialect()
	{
		super();
		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}
}