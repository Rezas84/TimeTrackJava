/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author domin
 */
public class DBConnection {
    SQLServerDataSource ds = new SQLServerDataSource();

    public DBConnection() {
        try{
            ds = new SQLServerDataSource();
            ds.setServerName("10.176.111.31");
            ds.setPortNumber(1433);
            ds.setDatabaseName("TimeTrackingExam");
            ds.setUser("CSe19B_3");
            ds.setPassword("CSe19B_3");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public Connection getConnection() throws SQLServerException{
        return ds.getConnection();
    }
}
