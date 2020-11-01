/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import timetrackingexam.be.LoggedUser;
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

    /**
     * @param con
     * @param name
     * @param project_id
     * @param user_id
     * @param isbillable First query - Makes a new task in the Task table Second
     * query - Assigns the newly created task to a User in User_have_task table
     * Third query - Assigns a new task time, which is 0 for first to the new
     * task
     */
    public void createNewTask(Connection con, String name, int project_id, int user_id, boolean isbillable) {
        int taskid = 0;
        try {
            String sql = "INSERT INTO Task VALUES (?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setInt(2, project_id);
            pstmt.setBoolean(3, isbillable);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                taskid = rs.getInt(1);
            }
            String sql2 = "INSERT INTO User_have_task VALUES (?,?)";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, user_id);
            pstmt2.setInt(2, taskid);
            pstmt2.executeUpdate();

            String sql3 = "INSERT INTO Task_time VALUES (?,?,Convert(date, getdate()),0)";
            PreparedStatement pstmt3 = con.prepareStatement(sql3);
            pstmt3.setInt(1, taskid);
            pstmt3.setInt(2, user_id);
            pstmt3.executeUpdate();

            String sql4 = "INSERT INTO Log(user_id, action) VALUES(?,?)";
            PreparedStatement pstmt4 = con.prepareStatement(sql4);
            pstmt4.setInt(1, LoggedUser.getInstance().id);
            pstmt4.setString(2, "Created new task with id " + taskid + " " + "and name " + name);
            pstmt4.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTask(Connection con, int user_id, int task_id, String name, int time, boolean billable) {
        try {
            String sql = "UPDATE Task SET name = ? , billable = ? WHERE Task.id = ?\n"
                    + "DELETE FROM Task_time WHERE task_id = ?\n"
                    + "Insert INTO Task_time (task_id,user_id,date,time) VALUES (?,?,Convert(date, getdate()),?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setBoolean(2, billable);
            pstmt.setInt(3, task_id);
            pstmt.setInt(4, task_id);
            pstmt.setInt(5, task_id);
            pstmt.setInt(6, user_id);

            pstmt.setInt(7, time);

            pstmt.executeUpdate();

            String sql2 = "INSERT INTO Log(user_id, action) VALUES(?,?)";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, LoggedUser.getInstance().id);
            pstmt2.setString(2, "Updated task with id " + task_id);
            pstmt2.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TaskTime> getTimeForTask(Connection con, int user_id, int task_id) {
        List<TaskTime> times = new ArrayList();
        try {
            String sql = "SELECT Task_time.date , Task_time.time FROM Task_time\n"
                    + "WHERE Task_time.task_id = ? AND Task_time.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, task_id);
            pstmt.setInt(2, user_id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                int time = rs.getInt("time");

                TaskTime newtime = new TaskTime(date, time);

                times.add(newtime);

            }
            return times;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param con
     * @param task_id
     * @param user_id
     * @param time Each time a user chooses to work on an existing tasks, this
     * method will create a new time frame for the task in the table. So later
     * we can fetch the SUM working hours for every task.
     */
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

    /**
     *
     * @param con
     * @param user_id
     * @param project_id
     * @return List of tasks Gets all the existing tasks ,which a user has under
     * a project. The user has the ability to continue working on an existing
     * tasks.
     */
    public List<Task> getAllTasksByProject(Connection con, int user_id, int project_id) {
        try {
            List<Task> tasks = new ArrayList();
            String sql = "SELECT Task.id, Task.name,SUM(Task_time.time) as TotalTime, Task.billable FROM Task JOIN Task_time ON Task.id = Task_time.task_id JOIN Project ON Project.id = Task.project_id  WHERE project_id = ? AND Task_time.user_id = ? GROUP BY Task.id,Task.name, Task.billable";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, project_id);
            pstmt.setInt(2, user_id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer taskid = rs.getInt("id");
                String taskname = rs.getString("name");
                int taskhours = rs.getInt("TotalTime");
                boolean isbillable = rs.getBoolean("billable");
                String state = "";
                if (isbillable == true) {
                    state = "Billable";
                }
                if (isbillable == false) {
                    state = "";
                }
                tasks.add(taskmaker.makeTask(taskid, taskname, taskhours, state));
            }
            return tasks;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
