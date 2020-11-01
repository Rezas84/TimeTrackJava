/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import timetrackingexam.be.Project;

/**
 *
 * @author narma
 */
public class ProjectFactory {
    
    public Project makeProject(int id, String name, int rate)
    {
       
        return new Project(id,name,rate);
    }
}
