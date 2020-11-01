/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Time;
import timetrackingexam.be.Task;

/**
 *
 * @author narma
 */
public class TaskFactory {
    
    public Task makeTask(int id, String name, int hours)
    {
        return new Task(id,name,hours);
    }
}
