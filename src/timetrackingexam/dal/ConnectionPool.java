/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author domin
 */
public class ConnectionPool extends ObjectPool<Connection> {

    private static ConnectionPool INSTANCE;
    DBConnection connection;

    public static synchronized ConnectionPool getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionPool();
        }
        return INSTANCE;
    }

    private ConnectionPool() {
        super();
        try {
            connection = new DBConnection();
        } catch (Exception e) {
            System.out.println("Error when trying to get instance of database connection");
            System.out.println(e);
        }

    }

    @Override
    protected Connection create() {
        try {
            return connection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return (null);
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            o.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return o.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
