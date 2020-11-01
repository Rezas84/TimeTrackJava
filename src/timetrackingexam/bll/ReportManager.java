/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import timetrackingexam.be.Project;
import timetrackingexam.be.TaskTime;
import timetrackingexam.be.User;
import timetrackingexam.be.helperFilterEntities.FProject;
import timetrackingexam.dal.ConnectionPool;
import timetrackingexam.dal.ReportDB;

/**
 *
 * @author narma
 */
public class ReportManager implements IReports {

    ConnectionPool conpool = ConnectionPool.getInstance();
    ReportDB reportdb = new ReportDB();

    @Override
    public List<Project> getAllProjectsByUser(int user_id) {
        Connection con = conpool.checkOut();
        List<Project> projects = reportdb.getAllProjectsByUser(con, user_id);
        conpool.checkIn(con);
        return projects;
    }

    @Override
    public List<User> getAllUsersByProject(int project_id) {
        Connection con = conpool.checkOut();
        List<User> users = reportdb.getAllUsersByProject(con, project_id);
        conpool.checkIn(con);
        return users;
    }

    @Override
    public List<FProject> getAllProjects() {
        Connection con = conpool.checkOut();
        List<FProject> filteredProjects = reportdb.getAllProjects(con);
        conpool.checkIn(con);
        return filteredProjects;
    }

    @Override
    public List<TaskTime> getAllTaskTimeForUserByDate(int userID, Date fromDate) {
        Connection con = conpool.checkOut();
        List<TaskTime> taskTimes = reportdb.getAllTaskTimeForUserByDate(con, userID, fromDate);
        conpool.checkIn(con);
        return taskTimes;

    }
}
