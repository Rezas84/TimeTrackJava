/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.sql.Time;
import java.util.List;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;
import timetrackingexam.dal.ConnectionPool;
import timetrackingexam.dal.TaskDB;

/**
 *
 * @author narma
 */
public class TaskManager implements ITask {

    ConnectionPool conpool = ConnectionPool.getInstance();
    TaskDB taskdb = new TaskDB();

    @Override
    public void createNewTask(String name, int project_id, int user_id) {
        Connection con = conpool.checkOut();

        taskdb.createNewTask(con, name, project_id,user_id);
        conpool.checkIn(con);
    }

    @Override
    public void addNewTimeToTask(int task_id, int user_id, int time) {
        Connection con = conpool.checkOut();

        taskdb.addNewTimeToTask(con, task_id, user_id, time);
        conpool.checkIn(con);
    }

    @Override
    public List<Task> getAllTasksByProject(int user_id, int project_id) {
        Connection con = conpool.checkOut();
        List<Task> tasks = taskdb.getAllTasksByProject(con, user_id, project_id);
        conpool.checkIn(con);
        return tasks;
    }

   /* @Override
    public void assignTaskToUser(int user_id, int task_id) {
        Connection con = conpool.checkOut();

        taskdb.assignTaskToUser(con, user_id, task_id);
        conpool.checkIn(con);
    }*/

    @Override
    public List<TaskTime> getTimeForTask(int user_id, int task_id) {
        Connection con = conpool.checkOut();
        List<TaskTime> tasks = taskdb.getTimeForTask(con, user_id, task_id);
        conpool.checkIn(con);
        return tasks;
    }

}
