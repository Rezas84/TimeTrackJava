/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class EditDeveloperProjectController implements Initializable {

    @FXML
    private JFXComboBox<User> comboboxUser;
    @FXML
    private TableView<User> tableviewUsers;
    @FXML
    private TableColumn<User, String> columnName;
    @FXML
    private TableColumn<User, String> columnEmail;
    @FXML
    private Label lblProjectName;

    SceneManager sm = new SceneManager();
    IFacade bll = new BllFacade();

    Project project;
    ArrayList<User> freeUsers = new ArrayList();
    ArrayList<User> inProjectUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    @FXML
    private void addUser(ActionEvent event) {
        User selected = comboboxUser.getSelectionModel().getSelectedItem();
        inProjectUsers.add(selected);
        freeUsers.remove(selected);
        setViews();
    }

    @FXML
    private void removeUser(ActionEvent event) {
        User selected = tableviewUsers.getSelectionModel().getSelectedItem();
        freeUsers.add(selected);
        inProjectUsers.remove(selected);
        setViews();
    }

    @FXML
    private void save(ActionEvent event) {
        bll.addUsersToProject(inProjectUsers, project.getId());
        sm.changeScene(event, "adminNewEditProject");
    }

    @FXML
    private void cancel(ActionEvent event) {
        sm.changeScene(event, "adminNewEditProject");
    }

    public void setProject(Project project) {
        this.project = project;
        lblProjectName.setText(project.getName());
        
        initUsers();
        setViews();
    }
    
    private void initUsers(){
        inProjectUsers = bll.getUsersOnProject(project.getId());
        ArrayList<User> toFilter = bll.getAllUsers();
        
        
        for (User i : inProjectUsers) {
            for (User f : toFilter) {
                if(i.getId() != f.getId()){
                    freeUsers.add(f);
                }
            }
        }
    }
    
    private void setViews(){
        tableviewUsers.setItems(FXCollections.observableList(inProjectUsers));
        comboboxUser.setItems(FXCollections.observableList(freeUsers));
        comboboxUser.getSelectionModel().selectFirst();
    }
}
