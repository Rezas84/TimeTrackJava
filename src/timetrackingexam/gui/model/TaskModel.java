/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import timetrackingexam.be.Task;
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
public class TaskModel {

    private static final TaskModel TaskSingle = new TaskModel();
    private final IReports logiclayer;

    private List<FProject> allProjects = new ArrayList();

    /*
    Initialises the logic layer manager
     */
    private TaskModel() {
        logiclayer = new ReportManager();
    }

    /* Static 'instance' method */
    public static TaskModel getInstance() {
        return TaskSingle;
    }

    public List<FProject> getAllProjects() {
        allProjects = logiclayer.getAllProjects();
        return allProjects;
    }

    // filter by one user
    private List<FProject> filteredByUser(List<FProject> filteredABitList, int userID) {
        List<FProject> filteredProjects = new ArrayList();
        for (FProject singleProject : filteredABitList) {
            FProject newProject = new FProject(singleProject.getId(), singleProject.getName(), singleProject.getRate());
            for (FTask singleTask : singleProject.getFilterTasks()) { //Fore ( IndiviualObject - AssignedName : fromList)
                if (singleTask.getUs().getId() == userID) { //
                    newProject.setFilterTasks(singleTask);
                }
            }
            if (newProject.getFilterTasks().size() > 0) {
                filteredProjects.add(newProject);
            }

        }
        return filteredProjects;
    }

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
        System.out.println(filteredProjects);
        return filteredProjects;

    }

    // filter by one project
    private List<FProject> filteredByProject(List<FProject> filteredABitList, int projectID) {
        List<FProject> filteredProjects = new ArrayList();
        for (FProject singleProject : filteredABitList) {
            if (singleProject.getId() == projectID) {
                filteredProjects.add(singleProject);
                return filteredProjects;
            }
        }
        return filteredProjects;
    }

    // filter by date
    private List<FProject> filterByDate(List<FProject> filteredABitList, Date date1, Date date2) {

        //Do filter stuff
        List<FProject> filteredProjects = new ArrayList();
        //Filter by date 
        for (FProject singleproject : filteredABitList) {
            FProject newProject = new FProject(singleproject.getId(), singleproject.getName(), singleproject.getRate());
            for (FTask singleTask : singleproject.getFilterTasks()) {
                FTask newTask = new FTask(singleTask.getId(), singleTask.getUs(), singleTask.getTaskname(), singleTask.getWorkinghours());
                for (FTaskTime singleTaskTime : singleTask.getFilteredTaskTime()) {
                    LocalDate localDateBefore = date1.toLocalDate();
                    LocalDate compare = singleTaskTime.getDateTaskname().toLocalDate();
                    LocalDate localDateAfter = date2.toLocalDate();
                    // problem with date types
                    if ((compare.isAfter(localDateBefore) || compare.isEqual(localDateBefore)) && (compare.isBefore(localDateAfter) || compare.isEqual(localDateAfter))) {
                        newTask.setFilteredTaskTime(singleTaskTime);
                    }
                }
                if (newTask.getFilteredTaskTime().size() > 0) {
                    newProject.setFilterTasks(newTask);
                }
            }
            if (newProject.getFilterTasks().size() > 0) {
                filteredProjects.add(newProject);
            }
        }
        return filteredProjects;
    }

}
