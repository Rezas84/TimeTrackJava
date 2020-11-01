/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.model;

import java.util.ArrayList;
import timetrackingexam.be.Client;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;

/**
 *
 * @author narma
 */
public class FacadeModel {

    private static final FacadeModel facade = new FacadeModel();
    private final IFacade logiclayer;

    private FacadeModel() {
        logiclayer = new BllFacade();
    }

    public static FacadeModel getInstance() {
        return facade;
    }

    public boolean isValidLogin(String email, String password) {
        return logiclayer.isValidLogin(email, password);
    }

    public void addNewClient(int rate, String name) {
        logiclayer.addNewClient(rate, name);
    }

    public void deleteClient(Client client) {
        logiclayer.deleteClient(client);
    }

    public void editClient(Client client) {
        logiclayer.editClient(client);
    }

    public ArrayList<Client> getAllClient() {
        return logiclayer.getAllClient();
    }

    public ArrayList<Project> getAllProject() {
        return logiclayer.getAllProject();
    }

    public ArrayList<User> getAllUsers() {
        return logiclayer.getAllUsers();
    }

    public User addNewUser(String name, String email, int rights, String password) {
        return logiclayer.addNewUser(name, email, rights, password);
    }

    public void deleteUser(int id) {
        logiclayer.deleteUser(id);
    }

    public void editUser(int id, String name, String email, int rights) {
        logiclayer.editUser(id, name, email, rights);
    }

    public Project addNewProject(String name, int clientId, String clientName, int clientRate, int rate) {
        return logiclayer.addNewProject(name, clientId, clientName, clientRate, rate);
    }

    public void deleteProject(int id) {
        logiclayer.deleteProject(id);
    }

    public void editProject(int id, String name, int rate, int clientId) {
        logiclayer.editProject(id, name, rate, clientId);
    }

    public ArrayList<User> getUsersOnProject(int projectId) {
        return logiclayer.getUsersOnProject(projectId);
    }

    public void addUsersToProject(ArrayList<User> users, int projectId) {
        logiclayer.addUsersToProject(users, projectId);
    }

    public boolean isEmailValid(String email) {
        return logiclayer.isEmailValid(email);
    }

    public void resetPassword(String eamil) {
        logiclayer.resetPassword(eamil);
    }

    public void userSetPassword(int id, String newPassword) {
        logiclayer.userSetPassword(id, newPassword);
    }
}
