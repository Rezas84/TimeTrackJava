/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import timetrackingexam.gui.model.FacadeModel;

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
    FacadeModel model;
    Project project;
    List<User> freeUsers;
    ArrayList<User> inProjectUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        model = FacadeModel.getInstance();
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
        model.addUsersToProject(inProjectUsers, project.getId());
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

    private void initUsers() {
        inProjectUsers = model.getUsersOnProject(project.getId());
        ArrayList<User> toFilter = model.getAllUsers();

        if (inProjectUsers.isEmpty()) {
            freeUsers = toFilter;
            return;
        }
        
    freeUsers = toFilter.stream()
    .filter(two -> inProjectUsers.stream()
    .noneMatch(one -> one.getId()==two.getId()))
    .collect(Collectors.toList());
    
    }

    private void setViews() {
        tableviewUsers.setItems(FXCollections.observableList(inProjectUsers));
        comboboxUser.setItems(FXCollections.observableList(freeUsers));
        comboboxUser.getSelectionModel().selectFirst();
    }

    @FXML
    private void comboboxAction(ActionEvent event) {
        if (comboboxUser.getSelectionModel().isEmpty()) {
            comboboxUser.setDisable(true);
        } else {
            comboboxUser.setDisable(false);
        }
    }
}
