/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be;

/**
 *
 * @author narma
 */
public class FilterResult {
    
    private String username;
    
    private String projectname;
    
    private String date;
    
    private int numberoftasks;
    
    private int totalhours;

    public FilterResult(String username, String projectname, String date, int numberoftasks, int totalhours) {
        this.username = username;
        this.projectname = projectname;
        this.date = date;
        this.numberoftasks = numberoftasks;
        this.totalhours = totalhours;
    }

    
    
    public int getTotalhours() {
        return totalhours;
    }

    public void setTotalhours(int totalhours) {
        this.totalhours = totalhours;
    }

    public int getNumberoftasks() {
        return numberoftasks;
    }

    public void setNumberoftasks(int numberoftasks) {
        this.numberoftasks = numberoftasks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
