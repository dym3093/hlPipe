package org.hpin.webservice.listener;

import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.TableChangeDescription;

/**
 * Created by damian on 16-12-31.
 */
public class DCNDemoListener implements DatabaseChangeListener {

    DBChangeNotification demo;
    DCNDemoListener(DBChangeNotification dem)
    {
        demo = dem;
    }

    public void onDatabaseChangeNotification(DatabaseChangeEvent e) {
        Thread t = Thread.currentThread();
        System.out.println("DCNDemoListener: got an event ("+this+" running on thread "+t+")");
        System.out.println(e.toString());
        TableChangeDescription[] tableChangeDescriptions = e.getTableChangeDescription();
        for (TableChangeDescription desc: tableChangeDescriptions) {
           desc.getObjectNumber();
        }
        synchronized( demo ){
            demo.notify();
        }
    }
}
