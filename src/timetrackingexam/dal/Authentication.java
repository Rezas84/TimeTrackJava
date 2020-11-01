/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author domin
 */
public class Authentication {

    public boolean isValidLogin(Connection con, String email, String password) {
        try {
            String sql = "SELECT * FROM Person\n"
                    + "WHERE email = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mail = rs.getString("email");
                int rights = rs.getInt("access_level");

                LoggedUser.init(id, name, mail, rights);
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean isEmailValid(Connection con, String email) {
        try {
            String sql = "SELECT id FROM Person WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void resetPassword(Connection con, String email, String newPassword) {
        try {
            String sql = "UPDATE Person SET password = ? WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void userSetPassword(Connection con, int id, String newPassword) {
        try {
            String sql = "UPDATE Person SET password = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
