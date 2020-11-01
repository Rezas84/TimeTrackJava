/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be.helperFilterEntities;

import java.util.ArrayList;
import java.util.List;
import timetrackingexam.be.User;

/**
 *
 * @author narma
 */
public class FTask {

    private List<FTaskTime> filteredTaskTime = new ArrayList();;
    private int id;
    private String taskname;
    private int workinghours;
    private int userID;
   
    private User us;

    public FTask(int id,User us, String taskname, int workinghours) {
        this.id = id;
        this.us = us;
        this.taskname = taskname;
        this.workinghours = workinghours;
    }

    public User getUs() {
        return us;
    }

    public void setUs(User us) {
        this.us = us;
    }
    
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getTaskname() {
        return taskname;
    }

    public List<FTaskTime> getFilteredTaskTime() {
        return filteredTaskTime;
    }

    public void setFilteredTaskTime(FTaskTime filteredTaskTime) {
        this.filteredTaskTime.add(filteredTaskTime);
    }

    public void setListFilteredTaskTime(List<FTaskTime> filteredTaskTime) {
        this.filteredTaskTime = filteredTaskTime;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public int getWorkinghours() {
        int seconds = workinghours % 60;
        int minutes = (workinghours / 60) % 60;
        int hours = (workinghours / 60) / 60;
        String secs;
        String mins;
        String hourse;

        if (seconds < 10) {
            secs = ":0" + seconds;
        } else {
            secs = ":" + seconds;
        }
        if (minutes < 10) {
            mins = ":0" + minutes;
        } else {
            mins = ":" + minutes;
        }
        if (hours < 10) {
            hourse = "0" + hours;
        } else {
            hourse = "" + hours;
        }
        return hours + minutes + seconds;

    }

    public void setWorkinghours(int workinghours) {
        this.workinghours = workinghours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  taskname + " , logs : =" + filteredTaskTime ;
    }
    
}
