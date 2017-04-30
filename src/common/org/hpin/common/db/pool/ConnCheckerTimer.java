package org.hpin.common.db.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>@desc : 定时检查</p>
 * <p>@see : </p>
 *
 * <p>@author : 胡五音</p>
 * <p>@createDate : May 31, 2012 11:23:43 AM</p>
 * <p>@version : v1.0 </p>
 * <p>All Rights Reserved By Acewill Infomation Technology(Beijing) Co.,Ltd</p> 
 */
public class ConnCheckerTimer extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnCheckerTimer.class) ;

    private DBConnectionPool pool;

    private long m_timer = 5000; // 定时器休眠时间

    private boolean ifExit = true;

    public ConnCheckerTimer(DBConnectionPool pool, long time) {
        this.pool = pool;
        this.m_timer = time;
    }

    public void run() {
        try {
            while (ifExit) {
            	LOGGER.debug(pool.getPoolName() + "定时器开始执行" + ifExit);

                try {
                    sleep(m_timer);
                }
                catch (Exception e) {
                    LOGGER.error(pool.getPoolName() + "定时器执行休眠出错: "
                            + e.getMessage());
                    ifExit = false;
                }
                pool.checkDBPool();
                LOGGER.debug(pool.getPoolName()
                        + "定时器调用连接池的checkDBPool()完成" + ifExit);
            }
        }
        catch (Exception ex) {
        	LOGGER.error(pool.getPoolName() + "定时器有误: " + ex.getMessage());
            ifExit = false;
        }
        ifExit = false;
    }

    public void exit() {
        this.ifExit = false;
    }

    public boolean getFlag() {
        return this.ifExit;
    }

}

