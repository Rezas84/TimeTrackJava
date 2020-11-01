/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.util.List;
import timetrackingexam.be.Project;
import timetrackingexam.bll.IProject;
import timetrackingexam.bll.ProjectManager;

/**
 *
 * @author narma
 */
public class ProjectModel {
     private static final ProjectModel ProjectModel = new ProjectModel();
     private final IProject projectlayer;
    
     private ProjectModel() {
        projectlayer = new ProjectManager();
    }

    /* Static 'instance' method */
    public static ProjectModel getInstance() {
        return ProjectModel;
    }
    public List<Project> getAllProjectByUser(int user_id)
    {
        return projectlayer.getAllProjectByUser(user_id);
    }
}
