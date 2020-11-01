/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be;

import java.sql.Date;

/**
 *
 * @author narma
 */
public class TaskTime {

    private Date taskname;
    private int workinghours;

    public TaskTime(Date date, int hours) {
        this.taskname = date;
        this.workinghours = hours;
    }

    public String getTaskname() {
        return taskname.toString();
    }
    public Date getDateTaskname() {
        return taskname;
    }
    public void setTaskname(Date taskname) {
        this.taskname = taskname;
    }

    public String getWorkinghours() {
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
        return hourse + mins + secs;
    }

    public void setWorkinghours(int workinghours) {
        this.workinghours = workinghours;
    }

}
