package org.hpin.base.beanutils;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

public class HibernateConfigurationHelper {

	private static Configuration hibernateConf = new Configuration();

	private static PersistentClass getPersistentClass(Class clazz) throws Exception {

		synchronized (HibernateConfigurationHelper.class) {

			PersistentClass pc = hibernateConf.getClassMapping(clazz.getName());

			if (pc == null) {

				hibernateConf = hibernateConf.addClass(clazz);

				pc = hibernateConf.getClassMapping(clazz.getName());

			}

			return pc;

		}

	}

	public static String getTableName(Class clazz) throws Exception  {

		return getPersistentClass(clazz).getTable().getName();

	}

	public static String getPkColumnName(Class clazz) throws Exception {

		return getPersistentClass(clazz).getTable().getPrimaryKey()

		.getColumn(0).getName();

	}
	
	public static String getColumnNames(Class clazz) throws Exception {
		String columnNames="";
		Iterator  columnIterator=getPersistentClass(clazz).getTable().getColumnIterator();
		while(columnIterator.hasNext()){
			Column column=(Column)columnIterator.next();
			columnNames=columnNames+column.getName()+",";
		}
		if(columnNames.indexOf(",")>0) columnNames=columnNames.substring(0,columnNames.lastIndexOf(","));
	    return columnNames;
	}
	
	public static String getPropNamesWithoutPK(Class clazz) throws Exception {
		String propNames="";
		Iterator  columnIterator=getPersistentClass(clazz).getPropertyIterator();
		while(columnIterator.hasNext()){
			Property column=(Property)columnIterator.next();
			propNames=propNames+column.getName()+",";
		}
		if(propNames.indexOf(",")>0) propNames=propNames.substring(0,propNames.lastIndexOf(","));
	    return propNames;
	}
	
	public static String getPropNameByColumnName(Class clazz,String columnName) throws Exception  {
		String propName="";
		String pk=getPkColumnName(clazz);
		if(!columnName.equals(pk)){
		 Property property=getPersistentClass(clazz).getProperty(columnName);
		 if(property!=null){
			propName=property.getName();
		  }
		}
		else {
			propName=pk;	
		}
		return propName;		
	}
}
