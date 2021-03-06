/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.util.List;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;

/**
 *
 * @author narma
 */
public interface ITask {

    public void createNewTask(String name, int project_id, int user_id, boolean isbillable);

    public void addNewTimeToTask(int task_id, int user_id, int time);

    public List<Task> getAllTasksByProject(int user_id, int project_id);

    public List<TaskTime> getTimeForTask(int user_id, int task_id);

    public void updateTask(int user_id, int task_id, String name, int time, boolean billable);

}
