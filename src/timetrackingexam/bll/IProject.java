/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.util.List;
import timetrackingexam.be.Project;

/**
 *
 * @author narma
 */
public interface IProject {
    public List<Project> getAllProjectByUser( int user_id);
}
