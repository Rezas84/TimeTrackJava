/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.util.ArrayList;
import timetrackingexam.be.Client;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;

/**
 *
 * @author domin
 */
public interface IFacade {

    public boolean isValidLogin(String email, String password);

    public void addNewClient(int rate, String name);

    public void deleteClient(Client client);

    public void editClient(Client client);

    public ArrayList<Client> getAllClient();

    public ArrayList<Project> getAllProject();

    public ArrayList<User> getAllUsers();

    public User addNewUser(String name, String email, int rights, String password);

    public void deleteUser(int id);

    public void editUser(int id, String name, String email, int rights);

    public Project addNewProject(String name, int clientId, String clientName, int clientRate, int rate);

    public void deleteProject(int id);

    public void editProject(int id, String name, int rate, int clientId);

    public ArrayList<User> getUsersOnProject(int projectId);

    public void addUsersToProject(ArrayList<User> users, int projectId);

    public boolean isEmailValid(String email);

    public void resetPassword(String eamil);

    public void userSetPassword(int id, String newPassword);
}
