/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be;

/**
 *
 * @author domin
 */
public class Role {
    int id;
    String Name;
    
    public Role(int id, String name){
        this.id = id;
        this.Name = name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return Name;
    }
}
