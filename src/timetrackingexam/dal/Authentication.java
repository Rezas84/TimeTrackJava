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

}
