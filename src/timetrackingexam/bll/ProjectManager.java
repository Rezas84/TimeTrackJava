/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import timetrackingexam.be.Project;
import timetrackingexam.dal.ConnectionPool;
import timetrackingexam.dal.ProjectDB;

/**
 *
 * @author narma
 */
public class ProjectManager implements IProject {

    ConnectionPool conpool = ConnectionPool.getInstance();
    ProjectDB projectDB = new ProjectDB();

    @Override
    public List<Project> getAllProjectByUser(int user_id) {
        Connection con = conpool.checkOut();
        List<Project> projects = projectDB.getAllProjectByUser(con, user_id);
        conpool.checkIn(con);
        return projects;
    }

}
