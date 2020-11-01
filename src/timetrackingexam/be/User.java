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
public class User {
    int id;
    String name;
    String email;
    int rights;

    public User(int id, String name, String email, int rights) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rights = rights;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getRights() {
        return rights;
    }
    @Override
    public String toString() {
        return name;
    }
}
