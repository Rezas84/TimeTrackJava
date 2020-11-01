/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.util.ArrayList;
import timetrackingexam.be.Client;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.dal.Authentication;
import timetrackingexam.dal.ClientDB;
import timetrackingexam.dal.ConnectionPool;
import timetrackingexam.dal.ProjectDB;
import timetrackingexam.dal.UserDB;

/**
 *
 * @author domin
 */
public class BllFacade implements IFacade {

    ConnectionPool conpool = ConnectionPool.getInstance();
    Authentication auth = new Authentication();
    ClientDB cdb = new ClientDB();
    UserDB udb = new UserDB();
    ProjectDB projectDB = new ProjectDB();
    CredentialManager cm = new CredentialManager();

    @Override
    public boolean isValidLogin(String email, String password) {
        Connection con = conpool.checkOut();
        boolean res = auth.isValidLogin(con, email, password);
        conpool.checkIn(con);
        return res;
    }

    @Override
    public void addNewClient(int rate, String name) {
        Connection con = conpool.checkOut();
        cdb.addNewClient(con, rate, name);
        conpool.checkIn(con);
    }

    @Override
    public ArrayList<Client> getAllClient() {
        Connection con = conpool.checkOut();
        ArrayList<Client> clients = cdb.getAllClient(con);
        conpool.checkIn(con);
        return clients;
    }

    @Override
    public void deleteClient(Client client) {
        Connection con = conpool.checkOut();
        cdb.deleteClient(client, con);
        conpool.checkIn(con);
    }

    @Override
    public void editClient(Client client) {
        Connection con = conpool.checkOut();
        cdb.editClient(client, con);
        conpool.checkIn(con);
    }

    @Override
    public User addNewUser(String name, String email, int rights, String password) {
        Connection con = conpool.checkOut();
        User newUser = udb.addNewUser(con, name, email, rights, password);
        conpool.checkIn(con);
        return newUser;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        Connection con = conpool.checkOut();
        ArrayList users = udb.getAllUsers(con);
        conpool.checkIn(con);
        return users;
    }

    @Override
    public void deleteUser(int id) {
        Connection con = conpool.checkOut();
        udb.deleteUser(con, id);
        conpool.checkIn(con);
    }

    @Override
    public void editUser(int id, String name, String email, int rights) {
        Connection con = conpool.checkOut();
        udb.editUser(con, id, name, email, rights);
        conpool.checkIn(con);
    }

    @Override
    public ArrayList<Project> getAllProject() {
        Connection con = conpool.checkOut();
        ArrayList<Project> project = projectDB.getAllProject(con);
        conpool.checkIn(con);
        return project;
    }

    @Override
    public Project addNewProject(String name, int clientId, String clientName, int clientRate, int rate) {
        Connection con = conpool.checkOut();
        Project p = projectDB.addNewProject(con, name, clientId, clientName, clientRate, rate);
        conpool.checkIn(con);
        return p;
    }

    @Override
    public void deleteProject(int id) {
        Connection con = conpool.checkOut();
        projectDB.deleteProject(con, id);
        conpool.checkIn(con);
    }

    @Override
    public void editProject(int id, String name, int rate, int clientId) {
        Connection con = conpool.checkOut();
        projectDB.editProject(con, id, name, rate, clientId);
        conpool.checkIn(con);
    }

    @Override
    public ArrayList<User> getUsersOnProject(int projectId) {
        Connection con = conpool.checkOut();
        ArrayList<User> users = projectDB.getUsersOnProject(con, projectId);
        conpool.checkIn(con);
        return users;
    }

    @Override
    public void addUsersToProject(ArrayList<User> users, int projectId) {
        Connection con = conpool.checkOut();
        projectDB.addUsersToProject(con, users, projectId);
        conpool.checkIn(con);
    }

    @Override
    public boolean isEmailValid(String email) {
        return cm.isEmailValid(email);
    }

    @Override
    public void resetPassword(String eamil) {
        cm.resetPassword(eamil);
    }

    @Override
    public void userSetPassword(int id, String newPassword) {
        cm.userSetPassword(id, newPassword);
    }
}
