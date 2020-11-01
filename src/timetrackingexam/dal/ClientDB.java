/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.Client;
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author saraf
 */
public class ClientDB {

    public void addNewClient(Connection con, int rate, String name) {
        try {
            String sql = "INSERT INTO CLIENT VALUES (?,?)";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, rate);
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Created new client with name " + name);
            pstmt2.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Client> getAllClient(Connection con) {
        try {
            String sql = "SELECT * FROM CLIENT";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Client> clientList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rate = rs.getInt("rate");
                Client cal = new Client(id, name, rate);
                clientList.add(cal);
            }
            return clientList;
        } catch (SQLServerException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteClient(Client client, Connection con) {
        try {
            String sql = "DELETE FROM CLIENT WHERE ID = ?";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, client.getId());
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Deleted client with id " + client.getId());
            pstmt2.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editClient(Client client, Connection con) {
        try {
            String sql = "UPDATE CLIENT SET NAME = ?, RATE = ? WHERE ID = ?";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setInt(2, client.getRate());
            pstmt.setInt(3, client.getId());
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Edited client with id " + client.getId());
            pstmt2.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
