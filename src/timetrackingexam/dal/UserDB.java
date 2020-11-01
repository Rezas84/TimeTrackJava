/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import timetrackingexam.be.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author domin
 */
public class UserDB {

    public ArrayList<User> getAllUsers(Connection con) {
        try {
            ArrayList<User> users = new ArrayList();
            String sql = "SELECT id, name, email, access_level FROM Person";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int rights = rs.getInt("access_level");

                users.add(new User(id, name, email, rights));
            }

            return users;
        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public User addNewUser(Connection con, String name, String email, int rights, String password) {
        try {
            int userId = -1;
            String sql = "INSERT INTO Person (name, email, access_level, password) VALUES (?,?,?,?)";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, rights);
            pstmt.setString(4, password);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                userId = rs.getInt(1);
            }

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Created new user with id " + userId + " " + "and name " + name);
            pstmt2.executeUpdate();

            return new User(userId, name, email, rights);

        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteUser(Connection con, int id) {
        try {
            String sql = "DELETE FROM Person WHERE id = ?";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Deleted user with id " + id);
            pstmt2.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editUser(Connection con, int id, String name, String email, int rights) {
        try {
            String sql = "UPDATE Person SET name = ?, email = ?, access_level = ? WHERE id = ? ";
            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, rights);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Edited user with id " + id);
            pstmt2.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
