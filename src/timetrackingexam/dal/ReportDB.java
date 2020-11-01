/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.FilterResult;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.be.helperFilterEntities.FProject;
import timetrackingexam.be.helperFilterEntities.FTask;
import timetrackingexam.be.helperFilterEntities.FTaskTime;
import timetrackingexam.bll.ProjectFactory;
import timetrackingexam.bll.UserFactory;

/**
 *
 * @author saraf
 */
public class ReportDB {

    DBConnection db = new DBConnection();
    ProjectFactory projectfactory = new ProjectFactory();
    UserFactory userfactory = new UserFactory();

    public ReportDB() {
    }

    public List<Project> getAllProjectsByUser(Connection con, int user_id) {
        List<Project> projects = new ArrayList();
        try {
            String sql = "SELECT * FROM Project\n"
                    + "JOIN User_have_project ON Project.id = User_have_project.project_id\n"
                    + "JOIN Client ON Client.id = Project.client_id\n"
                    + "WHERE User_have_project.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rate = rs.getInt("rate");
                String cname = rs.getString("name");

                projects.add(projectfactory.makeProject(id, name, rate));

            }
            return projects;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<User> getAllUsersByProject(Connection con, int project_id) {
        List<User> users = new ArrayList();
        try {
            String sql = "SELECT Person.id,Person.name,Person.email,Person.access_level FROM Person\n"
                    + "JOIN User_have_project ON Person.id = User_have_project.user_id\n"
                    + "WHERE User_have_project.project_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, project_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int right = rs.getInt("access_level");

                users.add(userfactory.makeUser(id, name, email, right));

            }
            return users;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<FProject> getAllProjects(Connection con) {
        List<FProject> allProjects = new ArrayList();
        try {
            String sql = "SELECT * FROM Project";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rate = rs.getInt("rate");

                FProject newproject = new FProject(id, name, rate);
                newproject.setListFilterTasks(getAllTasksForProject(con, id));
                allProjects.add(newproject);
            }
            return allProjects;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<FTask> getAllTasksForProject(Connection con, int projectID) {
        List<FTask> allTasks = new ArrayList();
        try {

            String sql = "SELECT Task.id , Task.name as TName, SUM(Task_time.time) as TotalTime,  Person.id  userID, Person.name nameOfUser, Person.email,Person.access_level From Task\n"
                    + "                    JOIN Task_time ON Task_time.task_id = Task.id\n"
                    + "                    JOIN User_have_task ON User_have_task.task_id = Task.id\n"
                    + "                    JOIN Person ON Person.id = User_have_task.user_id\n"
                    + "					JOIN User_have_project ON User_have_project.user_id = Person.id\n"
                    + "                    WHERE Task.project_id = ?\n"
                    + "                 GROUP BY Task.id,Task.name,Person.id,Person.name,Person.email,Person.access_level";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, projectID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("TName");
                int workhours = rs.getInt("TotalTime");
                User us = new User(rs.getInt("userID"), rs.getString("nameOfUser"), rs.getString("email"), rs.getInt("access_level"));
                FTask newTask = new FTask(id, us, name, workhours);
                newTask.setListFilteredTaskTime(getAllTaskLogsForProject(con, id));
                allTasks.add(newTask);
            }
            return allTasks;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<FTaskTime> getAllTaskLogsForProject(Connection con, int taskID) {
        List<FTaskTime> allTaskLogs = new ArrayList();
        try {
            String sql = "SELECT Task_time.date, Task_time.time FROM Task_time\n"
                    + "WHERE Task_time.task_id =?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                int hours = rs.getInt("time");
                FTaskTime newLog = new FTaskTime(date, hours);
                allTaskLogs.add(newLog);
            }
            return allTaskLogs;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
