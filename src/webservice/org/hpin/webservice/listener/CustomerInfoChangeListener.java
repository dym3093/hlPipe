package org.hpin.webservice.listener;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.jdbc.driver.OracleDriver;
import oracle.sql.ROWID;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.Properties;

/**
 * Created by root on 17-2-16.
 */
public class CustomerInfoChangeListener implements DatabaseChangeListener{

    private TableChangeDescription[] tableChangeDescriptions;
    private OracleConnection conn = null;

    private static String sql = " select t.* from erp_customer t where t.is_deleted=0 and rowid = ? ";

    public static void main(String[] args){
        CustomerInfoChangeListener listener = new CustomerInfoChangeListener();
        listener.change();
    }

    public String change() {
        String changeMsg = null;
        try {
            System.out.println("change start ...");
            conn = DBUtils.connect();
            for (int i=0; i<tableChangeDescriptions.length; i++){
                TableChangeDescription tDesc = tableChangeDescriptions[i];
                String tableName = tDesc.getTableName();
                if (StringUtils.equalsIgnoreCase("ERP_CUSTOMER", tableName)){
                    RowChangeDescription[] rowChangeDescriptions = tDesc.getRowChangeDescription();
                    for (RowChangeDescription rowDesc : rowChangeDescriptions){
                        if (rowDesc.getRowOperation()==RowChangeDescription.RowOperation.INSERT
                                ||rowDesc.getRowOperation()==RowChangeDescription.RowOperation.UPDATE){
                            ROWID rowId = rowDesc.getRowid();
                            changeMsg = rowId.stringValue();
                            System.out.println("rowId: "+rowId.stringValue());
//                            Statement stmt = conn.createStatement();
                            //stmt.executeQuery(sq)

                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("change end ...");
        return changeMsg;
    }

    @Override
    public void onDatabaseChangeNotification(DatabaseChangeEvent databaseChangeEvent) {
        tableChangeDescriptions = databaseChangeEvent.getTableChangeDescription();
        if (tableChangeDescriptions!=null&&tableChangeDescriptions.length>0){
            change();
        }
    }

    public TableChangeDescription[] getTableChangeDescriptions() {
        return tableChangeDescriptions;
    }

    public void setTableChangeDescriptions(TableChangeDescription[] tableChangeDescriptions) {
        this.tableChangeDescriptions = tableChangeDescriptions;
    }
}
