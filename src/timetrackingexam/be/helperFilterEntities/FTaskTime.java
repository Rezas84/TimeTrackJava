/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be.helperFilterEntities;

import java.sql.Date;

/**
 *
 * @author narma
 */
public class FTaskTime {

    private Date taskname;
    private int workinghours;

    public FTaskTime(Date date, int hours) {
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
    /**
     * formula for printing out the time 00:00:00
     * @return String 
     */
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
    public int getTotalWorkinghours() {
        return workinghours;
    }
    @Override
    public String toString() {
        return  "date: " + taskname + " time worked : " + getWorkinghours() ;
    }
}
