/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import timetrackingexam.be.Task;

/**
 *
 * @author narma
 */
public class TaskFactory {
    
    public Task makeTask(int id, String name, int hours,String state)
    {
        return new Task(id,name,hours,state);
    }
}
