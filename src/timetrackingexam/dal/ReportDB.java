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
import timetrackingexam.be.Project;
import timetrackingexam.be.TaskTime;
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

    /**
     * 
     * @param con
     * @param user_id
     * @return Returns a List of projects, which a user is assigned to
     */  
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
    /**
     * 
     * @param con
     * @param project_id
     * @return Returns all the users, who are assigned to a specific project
     */
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
    
    /**
     * 
     * @param con
     *   This method calls the getAllTasksForProject method
     Creates a new project object 
     With the setListFilterTasks method it puts all the tasks which are connected to the project into it 
     The FProject has a property called filtertasks which will get all the filtered tasks for a project by project id
     * @return List of FProject
     */
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
                // 
                newproject.setListFilterTasks(getAllTasksForProject(con, id));
                allProjects.add(newproject);
            }
            return allProjects;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   /**
    * 
    * @param con
    * @param projectID
    *   Gets every tasks by a project id 
     Calls the getAllTaskLogsForProject method, which will return all the logged time entries for a task from Task_time table
     With these 3 methods : getAllProjects() , getAllTasksForProject() , getAllTaskLogsForProject() ->
     We get a hierarchy of everything that is needed for the report page like, List<Project>(List<Task>(List<Task_Time>))
     All the project objects get the filtered tasks for themselves and all the filtered tasks get the filtered task_time logs for themselves
     * @return List of FTask
    */
    public List<FTask> getAllTasksForProject(Connection con, int projectID) {
        List<FTask> allTasks = new ArrayList();
        try {

            String sql = "SELECT Task.id , Task.name as TName, SUM(Task_time.time) as TotalTime,  Person.id  userID, Person.name nameOfUser, Person.email,Person.access_level, Task.billable From Task\n" +
"                                        JOIN Task_time ON Task_time.task_id = Task.id\n" +
"                                       JOIN User_have_task ON User_have_task.task_id = Task.id\n" +
"                                        JOIN Person ON Person.id = User_have_task.user_id\n" +
"                                       JOIN User_have_project ON User_have_project.user_id = Person.id\n" +
"                                        WHERE Task.project_id = ?\n" +
"                                  GROUP BY Task.id,Task.name,Person.id,Person.name,Person.email,Person.access_level , Task.billable";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, projectID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("TName");
                int workhours = rs.getInt("TotalTime");
                boolean isbillable = rs.getBoolean("billable");
                String state = "";
                if(isbillable == true)
                {
                    state = "Billable";
                }
                if(isbillable == false)
                {
                    state = "";
                }
                User us = new User(rs.getInt("userID"), rs.getString("nameOfUser"), rs.getString("email"), rs.getInt("access_level"));
                FTask newTask = new FTask(id,state, us, name, workhours);
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
    public List<TaskTime> getAllTaskTimeForUserByDate(Connection con, int userID, Date fromDate) {
        List<TaskTime> allTaskLogs = new ArrayList();
        try {
            String sql = "SELECT Task_time.date, Task_time.time FROM Task_time\n"
                    + "WHERE Task_time.user_id =? AND Task_time.date>=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userID);
            pstmt.setDate(2, fromDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                int hours = rs.getInt("time");
                TaskTime newLog = new TaskTime(date, hours);
                allTaskLogs.add(newLog);
            }
            return allTaskLogs;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
