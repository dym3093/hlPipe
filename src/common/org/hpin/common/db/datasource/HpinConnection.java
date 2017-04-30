package org.hpin.common.db.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : May 31, 2012 11:08:51 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class HpinConnection {


    /* Identify one physical database connection */
    private Connection conn;

    /* Identify current database connection wether or not be using */
    private boolean inuse;

    /* Identify the time of last connection on conn(private property) */
    private long timestamp;

    /* Identify how many times user have connected to conn(private property) */
    private int ConnectionUseCount;

    /* Identify SQL clause will or have be executed */
    private String tempsql = "";

    /* Identify current database connection wether or not need time check */
    private boolean flagUseLongActiveTime = false;

    /**
     * minimal constructor
     * 
     * @param Connection,
     *            one physical database connection
     */
    public HpinConnection(Connection conn) {
        this.conn = conn;
        this.timestamp = System.currentTimeMillis();
        this.ConnectionUseCount = 0;
        this.inuse = false;
        try {
            this.setAutoCommit(false); // 锟斤拷璁剧疆鏄剧ず浜嬪姟鎺у埗
        }
        catch (Exception e) {
           
        }
    }

    /**
     * Constructor
     * 
     * @param boolean
     *            _bAutoCommit
     * @param Connection,
     *            one physical database connection
     */
    public HpinConnection(boolean _bAutoCommit, Connection conn) {
        this.conn = conn;
        this.timestamp = System.currentTimeMillis();
        this.ConnectionUseCount = 0;
        this.inuse = false;
        try {
            this.setAutoCommit(_bAutoCommit);
        }
        catch (Exception e) {
           
        }
    }

    /**
     * Set current database connection's status, including wether or not be
     * using, last connecting time and incremnt times be used. This function is
     * synchronized one, to add one object type lock.
     */
    public synchronized void useConnection() {
        this.inuse = true;
        timestamp = System.currentTimeMillis();
        ConnectionUseCount++;
    }

    public synchronized boolean isRelease() {
        boolean _bReturn = true;
        if (inuse) {
            _bReturn = false;
        }

        return _bReturn;
    }

    public boolean isUse() {
        return inuse;
    }

    public int useCount() {
        return ConnectionUseCount;
    }

    public boolean isValidate() {
        boolean _bReturn = false;
        try {
            conn.getMetaData();
            _bReturn = true;
        }
        catch (SQLException e) {
            
        }

        return _bReturn;
    }

    public long getTimeStamp() {
        return timestamp;
    }

    public void release() {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e) {
           
        }
    }

  
    public void close() {
        timestamp = System.currentTimeMillis();
        this.flagUseLongActiveTime = false;
        this.inuse = false;
    }

    protected Connection getConnection() {
        return conn;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        this.tempsql = sql;
        return conn.prepareStatement(sql);
    }

    public java.util.Map getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    public void setTypeMap(Map map) throws SQLException {
        conn.setTypeMap(map);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        this.tempsql = sql;
        return conn.prepareCall(sql);
    }

    public CallableStatement prepareCall(java.lang.String sql,
            int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    public String getSQL() {
        return this.tempsql;
    }

    public void setHoldability(int holdability) throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method setHoldability() not yet implemented.");
    }

    public int getHoldability() throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method getHoldability() not yet implemented.");
    }

    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method createStatement() not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method prepareStatement() not yet implemented.");
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method prepareCall() not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method prepareStatement() not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method prepareStatement() not yet implemented.");
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        /** @todo: Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method prepareStatement() not yet implemented.");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        /** @todo Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method releaseSavepoint() not yet implemented.");
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        /** @todo Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method rollback() not yet implemented.");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        /** @todo Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method setSavepoint() not yet implemented.");
    }

    public Savepoint setSavepoint() throws SQLException {
        /** @todo Implement this java.sql.Connection method */
        throw new java.lang.UnsupportedOperationException(
                "Method setSavepoint() not yet implemented.");
    }

    public boolean isFlagUseLongActiveTime() {
        return flagUseLongActiveTime;
    }

    public void setFlagUseLongActiveTime(boolean flagUseLongActiveTime) {
        this.flagUseLongActiveTime = flagUseLongActiveTime;
    }

    /**
     * @return the connectionUseCount
     */
    public int getConnectionUseCount() {
        return ConnectionUseCount;
    }

    /**
     * @param connectionUseCount
     *            the connectionUseCount to set
     */
    public void setConnectionUseCount(int connectionUseCount) {
        ConnectionUseCount = connectionUseCount;
    }

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

