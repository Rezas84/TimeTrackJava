/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import timetrackingexam.be.User;

/**
 *
 * @author narma
 */
public class UserFactory {
    
     public User makeUser(int id, String name, String email, int rights)
    {
        return new User(id,name,email,rights);
    }
}
