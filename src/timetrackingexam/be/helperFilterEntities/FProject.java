package timetrackingexam.be.helperFilterEntities;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author narma
 */
public class FProject {

    private List<FTask> filterTasks = new ArrayList();
    private int id;

    private String name;
    private int user;
    private int rate;

    private int workinghours;
    private String userName;

    private double gottenPay = 0;

    public double getGottenPay() {
        return gottenPay;
    }

    public void setGottenPay(double gottenPay) {
        this.gottenPay += gottenPay;
    }

    public void setGottenPayToZero() {
        this.gottenPay = 0;
    }
    private int totalCountOfTasks;

    public FProject(int id, String name, int rate) {

        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getRate() {
        return rate;
    }

    public int getTotalCountOfTasks() {
        totalCountOfTasks = 0;
        for (FTask filterTask : filterTasks) {
            if (filterTask.getUs().getId() == user) {
                totalCountOfTasks++;
            }
        }
        return totalCountOfTasks;
    }

    public List<FTask> getFilterTasks() {
        return filterTasks;
    }

    public void setListFilterTasks(List<FTask> taskerino) {
        this.filterTasks = taskerino;
    }

    public void setFilterTasks(FTask taskerino) {
        this.filterTasks.add(taskerino);
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        for (FTask filterTask : filterTasks) {
            if (filterTask.getUs().getId() == user) {
                return filterTask.getUs().getName();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return name + "" + filterTasks;
    }

    /**
     * get working hours for a user, with adding total working hours on a task
     * @return working hours in 00:00:00 formula
     */ 
    public String getWorkinghours() {
        int totalWorkingHours = 0;
        for (FTask filterTask : filterTasks) {
            if (filterTask.getUs().getId() == user) {
                totalWorkingHours = totalWorkingHours + filterTask.getTotalWorkinghours();
            }
        }
        int seconds = totalWorkingHours % 60;
        int minutes = (totalWorkingHours / 60) % 60;
        int hours = (totalWorkingHours / 60) / 60;
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
}
