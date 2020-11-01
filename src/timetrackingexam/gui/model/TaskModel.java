/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.util.List;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;
import timetrackingexam.bll.IReports;
import timetrackingexam.bll.ITask;
import timetrackingexam.bll.ReportManager;
import timetrackingexam.bll.TaskManager;

/**
 *
 * @author narma
 */
public class TaskModel {
    private static final TaskModel TaskModel = new TaskModel();
     private final IReports logiclayer;
    private final ITask taskLayer;
    
     private TaskModel() {
        logiclayer = new ReportManager();
        taskLayer = new TaskManager();
    }

    /* Static 'instance' method */
    public static TaskModel getInstance() {
        return TaskModel;
    }
    
    public void createNewTask(String text, int id, int id0, boolean selected) {
        taskLayer.createNewTask(text, id, id0, selected);
    }

    public void addNewTimeToTask(int task_id, int user_id, int totalTime) {
        taskLayer.addNewTimeToTask(task_id, user_id, totalTime);
    }
    public List<Task> getAllTasksByProject(int user_id, int project_id)
    {
       return taskLayer.getAllTasksByProject(user_id, project_id);
    }
    public List<TaskTime> getTimeForTask(int user_id, int task_id)
    {
       return taskLayer.getTimeForTask(user_id, task_id);
    }
    public void updateTask(int user_id, int task_id ,String name, int time, boolean billable)
    {
        taskLayer.updateTask(user_id, task_id, name, time, billable);
    }
}
