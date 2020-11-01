/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Date;
import java.util.List;
import timetrackingexam.be.Project;
import timetrackingexam.be.TaskTime;
import timetrackingexam.be.User;
import timetrackingexam.be.helperFilterEntities.FProject;

/**
 *
 * @author narma
 */
public interface IReports {
     public List<Project> getAllProjectsByUser( int user_id);
     public List<User> getAllUsersByProject( int project_id);
     public List<TaskTime> getAllTaskTimeForUserByDate(int userID, Date fromDate);
     public List<FProject> getAllProjects();
}
