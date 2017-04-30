package org.hpin.webservice.listener;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.OracleDriver;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by root on 17-2-16.
 */
public class DBUtils {

    private static Properties prop = new Properties();

    /**
     * Creates a connection the database.
     */
    public static OracleConnection connect() throws SQLException {
        OracleDriver dr = new OracleDriver();
        prop.setProperty("user",DBChangeNotification.USERNAME);
        prop.setProperty("password",DBChangeNotification.PASSWORD);
        return (OracleConnection)dr.connect(DBChangeNotification.URL,prop);
    }

}
