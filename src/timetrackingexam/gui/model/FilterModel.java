/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import timetrackingexam.be.helperFilterEntities.FProject;
import timetrackingexam.be.helperFilterEntities.FTask;
import timetrackingexam.be.helperFilterEntities.FTaskTime;
import timetrackingexam.bll.IReports;
import timetrackingexam.bll.ITask;
import timetrackingexam.bll.ReportManager;
import timetrackingexam.bll.TaskManager;

/**
 *
 * @author narma
 */
public class FilterModel {

    private static final FilterModel FilterModel = new FilterModel();
    private final IReports logiclayer;
    private final ITask taskLayer;
    private List<FProject> allProjects = new ArrayList();

    /*
    Initialises the logic layer manager
     */
    private FilterModel() {
        logiclayer = new ReportManager();
        taskLayer = new TaskManager();
    }

    /* Static 'instance' method */
    public static FilterModel getInstance() {
        return FilterModel;
    }

    public List<FProject> getAllProjects() {
        allProjects = logiclayer.getAllProjects();
        return allProjects;
    }
    // Filters everything by only user
    private List<FProject> filteredByUser(List<FProject> filteredABitList, int userID) {
        HashMap<Integer, Integer> allusersPresentInTasks = new HashMap<Integer, Integer>(); //First will be ID of project second id of user
        List<FProject> filteredProjects = new ArrayList();
        for (FProject singleProject : filteredABitList) {
            //prevents nullpointer and checks if we need to repeat the project with different userid
            if (allusersPresentInTasks.get(singleProject.getId()) == null || allusersPresentInTasks.get(singleProject.getId()) != userID) {
                // deep copy of project
                FProject newProject = new FProject(singleProject.getId(), singleProject.getName(), singleProject.getRate());
                for (FTask singleTask : singleProject.getFilterTasks()) { //Fore ( IndiviualObject - AssignedName : fromList)
                    if (singleTask.getUs().getId() == userID) { //
                        newProject.setFilterTasks(singleTask);
                    }
                }
                newProject.setUser(userID);
                if (newProject.getFilterTasks().size() > 0) {
                    filteredProjects.add(newProject);
                }

                allusersPresentInTasks.put(singleProject.getId(), userID);
            }
        }
        return filteredProjects;
    }
    // The main filter method which calls individual filter methods in the selections
    public List<FProject> filterEverything(Date date1, Date date2, int user, int project) {
        List<FProject> filteredProjects = allProjects;
        if (date1 != null && date2 != null) {
            filteredProjects = filterByDate(filteredProjects, date1, date2);
        }
        if (project > 0) {
            filteredProjects = filteredByProject(filteredProjects, project);
        }
        if (user > 0) {
            filteredProjects = filteredByUser(filteredProjects, user);

        }
        filteredProjects = calculatePay(filteredProjects);
       
        return filteredProjects;

    }
    // Calculates the money , a user will get 
    private List<FProject> calculatePay(List<FProject> filteredProjects) {
        for (FProject filteredProject : filteredProjects) {

            filteredProject.setGottenPayToZero();

            int rate = filteredProject.getRate();
            int user = filteredProject.getUser();
            for (FTask filterTask : filteredProject.getFilterTasks()) {
                // only billable tasks are needed
                if (filterTask.getState().equals("Billable")) {
                    if (filterTask.getUs().getId() == user) {

                        double TotalTime = filterTask.getTotalWorkinghours();
                        // project's hourly rate is multiplied by total working time by a user
                        double pay = Math.round((((TotalTime / 60) / 60) * rate) * 100d) / 100d;

                        filteredProject.setGottenPay(pay);
                    }

                }

            }

        }
        return filteredProjects;
    }
    // Filters everything by only project
    private List<FProject> filteredByProject(List<FProject> filteredABitList, int projectID) {
        HashMap<Integer, Integer> allusersPresentInTasks = new HashMap<Integer, Integer>();
        List<FProject> filteredProjects = new ArrayList();
        for (FProject singleProject : filteredABitList) {
            if (singleProject.getId() == projectID) {
                for (FTask filterTask : singleProject.getFilterTasks()) {
                    if (allusersPresentInTasks.get(filterTask.getUs().getId()) == null || allusersPresentInTasks.get(filterTask.getUs().getId()) != singleProject.getId()) {
                        allusersPresentInTasks.put(filterTask.getUs().getId(), filterTask.getId());
                    }
                }
                // for each entry in the hashmap
                for (Map.Entry<Integer, Integer> entry : allusersPresentInTasks.entrySet()) {
                    FProject toBeAddedProject = new FProject(singleProject.getId(), singleProject.getName(), singleProject.getRate());
                    toBeAddedProject.setListFilterTasks(singleProject.getFilterTasks());
                    toBeAddedProject.setUser(entry.getKey());
                    filteredProjects.add(toBeAddedProject);
                }
                return filteredProjects;
            }
        }
        return filteredProjects;
    }
    // Filters everthing by date
    private List<FProject> filterByDate(List<FProject> filteredABitList, Date date1, Date date2) {
        HashMap<Integer, Integer> allusersPresentInTasks = new HashMap<Integer, Integer>(); // to get unique values, otherwise I got duplicated 

        List<FProject> filteredProjects = new ArrayList();

        for (FProject singleproject : filteredABitList) {
            FProject newProject = new FProject(singleproject.getId(), singleproject.getName(), singleproject.getRate());
            for (FTask singleTask : singleproject.getFilterTasks()) {
                FTask newTask = new FTask(singleTask.getId(), singleTask.getState(), singleTask.getUs(), singleTask.getTaskname(), singleTask.getWorkinghours());
                for (FTaskTime singleTaskTime : singleTask.getFilteredTaskTime()) {
                    LocalDate localDateBefore = date1.toLocalDate();
                    LocalDate compare = singleTaskTime.getDateTaskname().toLocalDate();
                    LocalDate localDateAfter = date2.toLocalDate();

                    if ((compare.isAfter(localDateBefore) || compare.isEqual(localDateBefore)) && (compare.isBefore(localDateAfter) || compare.isEqual(localDateAfter))) {
                        newTask.setFilteredTaskTime(singleTaskTime);
                    }
                }
                if (newTask.getFilteredTaskTime().size() > 0) {
                    if (allusersPresentInTasks.get(singleTask.getUs().getId()) == null || allusersPresentInTasks.get(singleTask.getUs().getId()) != newProject.getId()) {
                        allusersPresentInTasks.put(singleTask.getUs().getId(), newProject.getId());
                    }
                    newProject.setFilterTasks(newTask);
                }
            }
            if (newProject.getFilterTasks().size() > 0) {
                for (Map.Entry<Integer, Integer> entry : allusersPresentInTasks.entrySet()) {
                    FProject toBeAddedProject = new FProject(singleproject.getId(), singleproject.getName(), singleproject.getRate());
                    toBeAddedProject.setListFilterTasks(newProject.getFilterTasks());
                    if (entry.getValue() == newProject.getId()) {
                        toBeAddedProject.setUser(entry.getKey());
                        filteredProjects.add(toBeAddedProject);
                    }
                }

            }
        }
        return filteredProjects;
    }

    
}
