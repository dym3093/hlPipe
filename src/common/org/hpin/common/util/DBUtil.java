package org.hpin.common.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hpin.common.core.SpringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 连接数据库工具
 *
 * @author 陈林
 */
public class DBUtil {
	
	private static final Logger log = LoggerFactory.getLogger(DBUtil.class);

    /**
     * 获取系统的数据源
     *
     * @return DataSource
     */
    public static DataSource getDataSource(String dataSourceId) {
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) SpringTool.getBean(dataSourceId);
        } catch (Exception e) {
            log.error("获取数据源出错，请检查Spring数据源配置！");
        }
        return dataSource;
    }

    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static Connection makeConnection(String dataSourceId) {
        Connection conn = null;
        try {
        	DataSource dbSource = getDataSource(dataSourceId);
            conn = dbSource.getConnection();
        } catch (SQLException e) {
            log.error("通过数据源获取数据库连接发生异常！");
            e.printStackTrace();
        }
        return conn;
    }
    
    public static PreparedStatement createStatement(Connection conn,String sqlStr){
    	PreparedStatement cs = null;
    	try {
			if(conn != null && !conn.isClosed()){
				cs = conn.prepareStatement(sqlStr);    		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
    	return cs ;
    }

    /**
     * 执行SQL语句
     * @param dataSourceId dataSource的bean
     * @param sqlStr sql语句
     * @return boolean      返回存储过程执行的结果,true表示执行成功,false表示执行失败.
     */
    public static ResultSet executeSql(String dataSourceId, String sqlStr) {
        Statement cs;
        ResultSet rs = null;
        Connection conn = makeConnection(dataSourceId);
        try {
            cs = conn.createStatement();
            rs = cs.executeQuery(sqlStr);
        } catch (SQLException e) {
            log.error("执行" + sqlStr + "失败！");
            e.printStackTrace();
        }
        return rs;
    }
    
    
    public static void closeConnection( Connection conn, PreparedStatement pstatm, ResultSet rs){
		try {
			if( rs != null){
				rs.close();
			}
			if(pstatm != null){
				pstatm.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public static void closeConnection( Connection conn, Statement statm, ResultSet rs){
		try {
			if( rs != null){
				rs.close();
			}
			if(statm != null){
				statm.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 执行没有参数的SQL存储过程
     *
     * @param dataSourceId dataSource的bean
     * @param procedureName 存储过程名字
     * @return boolean      返回存储过程执行的结果,true表示执行成功,false表示执行失败.
     */
    public static boolean executeProcedure(String dataSourceId,String procedureName) {
        boolean flag = false;
        String sqlStr = "{call " + procedureName + "()}";
        CallableStatement cs;
        Connection conn = makeConnection(dataSourceId);
        try {
            cs = (CallableStatement) conn.prepareStatement(sqlStr);
            cs.executeUpdate(sqlStr);
            flag = true;
        } catch (SQLException e) {
            log.error("调用存储过程" + sqlStr + "失败！");
            e.printStackTrace();
        }
        return flag;
    }
}
