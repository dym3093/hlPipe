package org.hpin.common.db.pool;

import java.util.Vector;

import org.hpin.common.db.datasource.HpinConnection;


/**
 * <p>@desc : </p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : May 31, 2012 11:48:05 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ConnCloseThread extends Thread {

    private HpinConnection conn = null;

    private Vector closeConnections;

    public ConnCloseThread(HpinConnection con, Vector closeConns) {
        this.conn = con;
        this.closeConnections = closeConns;
    }

    public void run() {
        closeConnections.add(conn);
        conn.release();
        closeConnections.remove(conn);
    }
}

