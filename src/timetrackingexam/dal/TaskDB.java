/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;
import timetrackingexam.bll.TaskFactory;

/**
 *
 * @author narma
 */
public class TaskDB {

    DBConnection db = new DBConnection();
    TaskFactory taskmaker = new TaskFactory();

    public void createNewTask(Connection con, String name, int project_id, int user_id) {
        int taskid = 0;
        try {
            String sql = "INSERT INTO Task VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setInt(2, project_id);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                taskid = rs.getInt(1);
            }
            String sql2 = "INSERT INTO User_have_task VALUES (?,?)";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1,user_id);
            pstmt2.setInt(2,taskid);
            pstmt2.executeUpdate();
            
            String sql3 = "INSERT INTO Task_time VALUES (?,?,Convert(date, getdate()),0)";
            PreparedStatement pstmt3 = con.prepareStatement(sql3);
            pstmt3.setInt(1,taskid);
            pstmt3.setInt(2, user_id);
            pstmt3.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TaskTime> getTimeForTask(Connection con, int user_id, int task_id)
    {
        List<TaskTime> times = new ArrayList();
        try {
            String sql = "SELECT Task_time.date , Task_time.time FROM Task_time\n" +
                          "WHERE Task_time.task_id = ? AND Task_time.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, task_id);
            pstmt.setInt(2, user_id);
            

            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                Date date = rs.getDate("date");
                int time = rs.getInt("time");
                
                TaskTime newtime = new TaskTime(date,time);
                
                times.add(newtime);
                
            }
            return times;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   /* public void assignTaskToUser(Connection con, int user_id, int task_id) {
        try {
            String sql2 = "INSERT INTO User_have_task VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, task_id);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    // Each time a user chooses to work on an existing tasks, this method will create a new time frame for the task in the table.
    // So later we can fetch the SUM working hours for every task.
    public void addNewTimeToTask(Connection con, int task_id, int user_id, int time) {
        try {
            String sql = "INSERT INTO Task_time VALUES (?,?,Convert(date, getdate()),?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, task_id);
            pstmt.setInt(2, user_id);
            pstmt.setInt(3, time);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Gets all the existing tasks ,which a user has under a project.
    // The user has the ability to continue working on an existing taks.
    public List<Task> getAllTasksByProject(Connection con, int user_id, int project_id) {
        try {
            List<Task> tasks = new ArrayList();
            String sql = "SELECT Task.id, Task.name,SUM(Task_time.time) as TotalTime FROM Task JOIN Task_time ON Task.id = Task_time.task_id JOIN Project ON Project.id = Task.project_id  WHERE project_id = ? AND Task_time.user_id = ? GROUP BY Task.id,Task.name";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, user_id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer taskid = rs.getInt("id");
                String taskname = rs.getString("name");
                int taskhours = rs.getInt("TotalTime");

                tasks.add(taskmaker.makeTask(taskid, taskname, taskhours));
            }
            return tasks;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
