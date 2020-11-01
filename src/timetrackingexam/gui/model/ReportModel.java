/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.sql.Date;
import java.util.List;
import timetrackingexam.be.Project;
import timetrackingexam.be.TaskTime;
import timetrackingexam.be.User;
import timetrackingexam.be.helperFilterEntities.FProject;
import timetrackingexam.bll.IReports;
import timetrackingexam.bll.ReportManager;

/**
 *
 * @author narma
 */
public class ReportModel {

    private static final ReportModel Report = new ReportModel();
    private final IReports logiclayer;

    private ReportModel() {
        logiclayer = new ReportManager();
    }

    public static ReportModel getInstance() {
        return Report;
    }

    public List<Project> getAllProjectsByUser(int user_id) {
        return logiclayer.getAllProjectsByUser(user_id);
    }

    public List<User> getAllUsersByProject(int project_id) {
        return logiclayer.getAllUsersByProject(project_id);
    }

    public List<TaskTime> getAllTaskTimeForUserByDate(int userID, Date fromDate) {
        return logiclayer.getAllTaskTimeForUserByDate(userID, fromDate);
    }

    public List<FProject> getAllProjects() {
        return logiclayer.getAllProjects();
    }
}
