package org.hpin.common.db.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.DataSource;

import org.hpin.common.core.SpringTool;
import org.hpin.common.db.pool.DBConnectionPool;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : May 31, 2012 11:00:52 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ConnectionPool {
	
	static private ConnectionPool instance; 

	static private int clients;

	static private String defaultpoolname;
	

	private Vector drivers = new Vector();

	private Hashtable pools = new Hashtable();

	static synchronized public ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		clients++;
		return instance;
	}

	private ConnectionPool() {
	}

	public void returnConnection(HpinConnection con) {
	}

	public void returnConnection(String name, HpinConnection con) {
	}

	public HpinConnection getConnection() {
		DataSource ds = (DataSource) SpringTool.getBean("dataSource");
		try {

			return new HpinConnection(ds.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection(String name) {
		return null;
	}

	public DBConnectionPool getPool(
			String name) {
		return null;
	}

	public HpinConnection getConnection(String name, int time) {
		return null;
	}


	public synchronized void closeAllConnection() {

	}

	private void log(String msg) {
	}
	private void log(Throwable e, String msg) {
	}
	
}

