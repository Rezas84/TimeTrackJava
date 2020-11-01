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
public class LoggedUser {

    private static LoggedUser loggedUser = null;
    public int id;
    public String name;
    public String email;
    public int rights;

    private LoggedUser(int id, String name, String email, int rights) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rights = rights;
    }

    public synchronized static LoggedUser init(int id, String name, String email, int rights) {
        if (loggedUser != null) {
            throw new AssertionError("Already initialized");
        }

        loggedUser = new LoggedUser(id, name, email, rights);
        return loggedUser;
    }

    public static LoggedUser getInstance() {
        if (loggedUser == null) {
            throw new AssertionError("You have to call init first");
        }

        return loggedUser;
    }

    public static void removeInstance() {
        loggedUser = null;
    }
}
