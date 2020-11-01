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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.bll.ProjectFactory;

/**
 *
 * @author narma
 */
public class ProjectDB {

    ProjectFactory projectmaker = new ProjectFactory();

    public List<Project> getAllProjectByUser(Connection con, int user_id) {
        try {
            List<Project> projects = new ArrayList();
            String sql = "SELECT Project.id, Project.name, Project.rate FROM Project\n"
                    + "JOIN User_have_project ON Project.id = User_have_project.project_id\n"
                    + "WHERE User_have_project.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, user_id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer project_id = rs.getInt("id");
                String projectname = rs.getString("name");
                Integer rate = rs.getInt("rate");
                projects.add(projectmaker.makeProject(project_id, projectname, rate));
            }
            return projects;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Project> getAllProject(Connection con) {
        try {
            String sql = "SELECT p.*, c.name AS clientName, c.id AS clientId, c.rate AS clientRate FROM PROJECT AS p "
                    + "JOIN Client AS c ON p.client_id = c.id";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Project> projectList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rate = rs.getInt("rate");
                String clientName = rs.getString("clientName");
                int clientId = rs.getInt("clientId");
                int clientRate = rs.getInt("clientRate");
                Project project = new Project(id, name, rate, clientName, clientId, clientRate);
                projectList.add(project);
            }
            return projectList;
        } catch (SQLServerException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClientDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Project addNewProject(Connection con, String name, int clientId, String clientName, int clientRate, int rate) {
        int projectId = -1;
        try {
            String sql = "Insert INTO Project (name, rate, client_id) VALUES (?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setInt(2, rate);
            pstmt.setInt(3, clientId);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                projectId = rs.getInt(1);
            }

            return new Project(projectId, name, rate, clientName, clientId, clientRate);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteProject(Connection con, int id) {
        try {
            String sql = "DELETE FROM Project WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editProject(Connection con, int id, String name, int rate, int clientId) {
        try {
            String sql = "UPDATE Project SET name = ?, rate = ?, client_id WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setInt(2, rate);
            pstmt.setInt(3, clientId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<User> getUsersOnProject(Connection con, int projectId) {
        try {
            ArrayList<User> users = new ArrayList();
            String sql = "SELECT u.*, p.id, p.name, p.email, p.access_level FROM User_have_project AS u\n"
                    + "JOIN Person AS p ON u.user_id = p.id\n"
                    + "WHERE u.project_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mail = rs.getString("email");
                int rights = rs.getInt("access_level");
                users.add(new User(id, name, mail, rights));
            }

            return users;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addUsersToProject(Connection con, ArrayList<User> users, int projectId) {
        try {
            String sql = "DELETE FROM User_have_project WHERE project_id = ?";
            String sql2 = "INSERT INTO User_have_project (user_id, project_id) VALUES (?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt2 = con.prepareStatement(sql2);

            pstmt.setInt(1, projectId);

            for (User user : users) {
                pstmt2.setInt(1, user.getId());
                pstmt2.setInt(2, projectId);
                pstmt2.addBatch();
            }

            pstmt.executeUpdate();
            pstmt2.executeBatch();

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
