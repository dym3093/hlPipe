package org.hpin.common.db.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.hpin.common.db.datasource.HpinConnection;
import org.hpin.common.log.util.HpinLog;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : May 31, 2012 11:21:23 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class DBConnectionPool {
	private Vector connections = new Vector();

    private Vector closeConnections = new Vector();

    private boolean defaultAutoCommit = false;

    private int maxconn;

    private int minconn;

    private int maxusecount;

    private int maxidletime;

    private int maxalivetime;

    private String poolname;

    private String password;

    private String URL;

    private String user;

    private String charset;

    private ConnCheckerTimer connChk;

    private boolean checkdbable = true;

    private long mtime = 30000;

    /**
     * 构造函数
     * 
     * @param poolname,
     *            连接池名称
     * @param URL,
     *            数据库的JDBC URL
     * @param user,
     *            数据库帐户
     * @param password,
     *            数据库帐户密码
     * @param charset,
     *            连接的字符集
     * @param maxconn,
     *            此连接池允许建立的最大连接数
     * @param minconn,
     *            次连接池允许建立的最小连接数
     * @param maxusecount,
     *            数据库连接允许使用的最大次数
     * @param maxidletime,
     *            连接池允许的最大空闲时间（分钟）
     * @param maxalivetime,
     *            连接在一个请求中允许的最大持续时间
     */
    public DBConnectionPool(String poolname, String URL, String user,
            String password, String charset, int maxconn, int minconn,
            int maxusecount, int maxidletime, int maxalivetime) {
        this.poolname = poolname;
        this.URL = URL;
        this.user = user;
        this.password = password;
        this.charset = charset;
        this.maxconn = maxconn;
        this.minconn = minconn;
        this.maxusecount = maxusecount;
        this.maxidletime = maxidletime;
        this.maxalivetime = maxalivetime;

        for (int i = 0; i < minconn; i++) {
            newConnection();
        }
        connChk = new ConnCheckerTimer(this, mtime);
        connChk.start();
    }

    /**
     * @param defaultAutoCommit
     * @param maxconn
     * @param minconn
     * @param maxusecount
     * @param maxidletime
     * @param maxalivetime
     * @param poolname
     * @param password
     * @param url
     * @param user
     * @param charset
     */
    public DBConnectionPool(boolean defaultAutoCommit, int maxconn,
            int minconn, int maxusecount, int maxidletime, int maxalivetime,
            String poolname, String password, String url, String user,
            String charset) {
        super();
        this.defaultAutoCommit = defaultAutoCommit;
        this.maxconn = maxconn;
        this.minconn = minconn;
        this.maxusecount = maxusecount;
        this.maxidletime = maxidletime;
        this.maxalivetime = maxalivetime;
        this.poolname = poolname;
        this.password = password;
        URL = url;
        this.user = user;
        this.charset = charset;
    }

    public synchronized void freeConnection(HpinConnection conn) {
        if (conn == null) return;
        conn.close();
    }

    /**
     * 从连接池中获得一个可用的连接，如果没有空闲的连接且当前连接数小于最大连接数两限制，
     * 则创建新的连接。如果原来登记为可用的连接不再有效，则从池中删除该连接，然后循环尝试 新的可用连接。
     */
    public synchronized HpinConnection getConnection() {
        HpinConnection conn = null;
        Enumeration connList = connections.elements();
        int i = 1;
        while ((connList != null) && (connList.hasMoreElements())) {
            conn = (HpinConnection) connList.nextElement();
            if (conn.isRelease() && isUsable(conn)) {
                connections.removeElement(conn);
                connections.addElement(conn);
                conn.useConnection() ;
                return conn;
            }
            i++;
        }

        if (maxconn == 0 || connections.size() < maxconn || conn.isRelease()) {
            conn = newConnection();
            conn.useConnection();
        }
        return conn;
    }

    public synchronized HpinConnection getConnection(int timeout) {
        long startTime = new Date().getTime();
        HpinConnection conn;
        while ((conn = getConnection()) == null) {
            try {
                wait(500);
            }
            catch (InterruptedException e) {
            }
            if ((new Date().getTime() - startTime) >= timeout * 60 * 1000)
                return null;
        }
        return conn;
    }

    /**
     * 检查当前连接池中的连接
     */
    public synchronized void checkDBPool() {
        if (!checkdbable) return;
        long idletime = System.currentTimeMillis() - maxidletime * 60 * 1000;
        long activetime = System.currentTimeMillis() - maxalivetime * 60 * 1000;

        for (int i = connections.size() - 1; i >= 0; i--) {
            HpinConnection conn = (HpinConnection) connections.get(i);
            boolean flagUseLongActinveTime = conn.isFlagUseLongActiveTime();
            if (flagUseLongActinveTime)
                activetime = System.currentTimeMillis() - 30 * 60 * 1000;

            if (conn.isUse() && (activetime > conn.getTimeStamp())) {
                closeConnection(conn);
            }
            else if ((idletime > conn.getTimeStamp() || !conn.isValidate())
                    && conn.isRelease())
                closeConnection(conn);
            else if (conn.useCount() > maxusecount && conn.isRelease())
                closeConnection(conn);
        }
        for (int i = connections.size(); i < minconn; i++) {
            if (newConnection() == null) {
                break;
            }
        }
    }

    /**
     * 关闭当前连接池中所有的连接
     */
    public synchronized void release() {
        Enumeration connList = connections.elements();
        while (connList.hasMoreElements()) {
            HpinConnection conn = (HpinConnection) connList.nextElement();
            conn.release();
        }
        connections.removeAllElements();
    }

    /**
     * 返回当前池中所有连接
     */
    public Iterator getConnectionIterator() {
        return connections.iterator();
    }

    /**
     * 返回当前池中的连接数
     */
    public int getConnectionSize() {
        return connections.size();
    }

    /**
     * 返回可用连接数
     */
    public int getUsableConnSize() {
        int _iReturn = 0;

        HpinConnection _objConn = null;
        Iterator _objIterator = getConnectionIterator();
        while (_objIterator.hasNext()) {
            _objConn = (HpinConnection) _objIterator.next();
            if (_objConn.isValidate() && !_objConn.isUse()) {
                _iReturn++;
            }
        }
        return _iReturn;
    }

    /**
     * 返回关闭池中所有连接
     */
    public Iterator getCloseConnectionIterator() {
        return closeConnections.iterator();
    }

    /**
     * 返回关闭池中的连接数
     */
    public int getCloseConnectionSize() {
        return closeConnections.size();
    }

    /**
     * 重新启动当前数据库连接池
     */
    public boolean restartPool() {
        try {
            release();
            for (int i = 0; i < minconn; i++) {
                newConnection();
            }
            checkdbable = true;
            connChk.exit();
            connChk = null;
            HpinLog.debug(this, "重新启动连接池成功！" + checkdbable);

            connChk = new ConnCheckerTimer(this, mtime);
            connChk.start();
        }
        catch (Exception e) {
            HpinLog.error(this, "重新启动连接池失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 重置当前数据库连接池的检测器
     */
    public boolean resetCheckTimer() {
        try {
            connChk.exit();
            connChk = null;
            connChk = new ConnCheckerTimer(this, mtime);
            connChk.start();
        }
        catch (Exception e) {
            HpinLog.error(this, "重置检查器失败: " + checkdbable);
            return false;
        }

        HpinLog.debug(this, "重置检查器成功！" + checkdbable);
        return true;
    }

    /**
     * 创建新的数据库连接实例，放入当前数据库连接池中
     */
    private HpinConnection newConnection() {
        Connection conn = null;
        HpinConnection conns = null;
        try {
            if (user == null)
                conn = DriverManager.getConnection(URL);
            else {
                Properties props = new Properties();
                props.put("user", user);
                props.put("password", password);
                props.put("CHARSET", charset);
                conn = DriverManager.getConnection(URL, props);
            }
            if (URL.indexOf("oracle") > 0) {
                Statement stmt = conn.createStatement();
                stmt
                        .execute("alter session set NLS_DATE_FORMAT='YYYY-MM-DD HH24:MI:SS'");
                HpinLog.debug(this, "oracle NLS_DATE_FORMAT changed!");
                conn.commit();
            }
            if (URL.indexOf("informix") > 0) {
                conn.setTransactionIsolation(1);
            }
        }
        catch (SQLException e) {
            HpinLog.error(this, "无法创建下列URL的连接：" + URL);
            return null;
        }

        conns = new HpinConnection(defaultAutoCommit, conn);
        connections.addElement(conns);
        return conns;
    }

    /**
     * 检查连接是否可以使用，如果不可使用，则去掉
     */
    private boolean isUsable(HpinConnection conn) {
        long idletime = System.currentTimeMillis() - maxidletime * 60 * 1000;
        if (!conn.isValidate() || idletime > conn.getTimeStamp()
                || conn.useCount() > maxusecount) {
            closeConnection(conn);
            return false;
        }
        return true;
    }

    private void closeConnection(HpinConnection conn) {
        try {
            ConnCloseThread cct = new ConnCloseThread(conn, closeConnections);
            cct.start();
            connections.removeElement(conn);
        }
        catch (Exception e) {
            HpinLog.error(this, "关闭连接失败！");
        }
    }

    public String getPoolName() {
        return poolname;
    }

    public boolean getTimerExitFlag() {
        return connChk.getFlag();
    }

    public String getTimerAlive() {
        String active = "isAlive :" + connChk.isAlive() + "|| isInterrupted"
                + connChk.isInterrupted() + " ||" + connChk + "|| ExitFlag"
                + connChk.getFlag();
        return active;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset
     *            the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the maxalivetime
     */
    public int getMaxalivetime() {
        return maxalivetime;
    }

    /**
     * @param maxalivetime
     *            the maxalivetime to set
     */
    public void setMaxalivetime(int maxalivetime) {
        this.maxalivetime = maxalivetime;
    }

    /**
     * @return the maxconn
     */
    public int getMaxconn() {
        return maxconn;
    }

    /**
     * @param maxconn
     *            the maxconn to set
     */
    public void setMaxconn(int maxconn) {
        this.maxconn = maxconn;
    }

    /**
     * @return the maxidletime
     */
    public int getMaxidletime() {
        return maxidletime;
    }

    /**
     * @param maxidletime
     *            the maxidletime to set
     */
    public void setMaxidletime(int maxidletime) {
        this.maxidletime = maxidletime;
    }

    /**
     * @return the maxusecount
     */
    public int getMaxusecount() {
        return maxusecount;
    }

    /**
     * @param maxusecount
     *            the maxusecount to set
     */
    public void setMaxusecount(int maxusecount) {
        this.maxusecount = maxusecount;
    }

    /**
     * @return the minconn
     */
    public int getMinconn() {
        return minconn;
    }

    /**
     * @param minconn
     *            the minconn to set
     */
    public void setMinconn(int minconn) {
        this.minconn = minconn;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the poolname
     */
    public String getPoolname() {
        return poolname;
    }

    /**
     * @param poolname
     *            the poolname to set
     */
    public void setPoolname(String poolname) {
        this.poolname = poolname;
    }

    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param url
     *            the uRL to set
     */
    public void setURL(String url) {
        URL = url;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the checkdbable
     */
    public boolean isCheckdbable() {
        return checkdbable;
    }

    /**
     * @param checkdbable
     *            the checkdbable to set
     */
    public void setCheckdbable(boolean checkdbable) {
        this.checkdbable = checkdbable;
    }

    /**
     * @return the defaultAutoCommit
     */
    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    /**
     * @param defaultAutoCommit
     *            the defaultAutoCommit to set
     */
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }
}

