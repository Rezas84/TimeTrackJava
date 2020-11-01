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
    
    private int rate;

    private String clientName;

    public FProject(int id, String name, int rate) {

        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public int getRate() {
        return rate;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return name + "" + filterTasks;
    }

}
