/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import timetrackingexam.be.Client;
import timetrackingexam.be.Project;
import timetrackingexam.gui.model.FacadeModel;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class EditProjectController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtRate;
    @FXML
    private JFXComboBox<Client> comboboxClient;

    SceneManager sm = new SceneManager();
    FacadeModel model;
    Project project;
    ArrayList<Client> clients;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = FacadeModel.getInstance();
        try {
            clients = model.getAllClient();
            comboboxClient.setItems(FXCollections.observableList(clients));
        } catch (Exception e) {
            e.printStackTrace();
        }

        comboboxClient.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Client> options, Client oldValue, Client newValue) -> {
            txtRate.setText(comboboxClient.getSelectionModel().getSelectedItem().getRate() + "");
        });

    }

    @FXML
    private void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Edit project");

        if (txtName.getText().isEmpty() || txtName.getText() == null) {
            alert.setHeaderText("Name field cannot be empty");

            alert.showAndWait();
        } else if (txtRate.getText().isEmpty() || txtRate.getText() == null) {
            alert.setHeaderText("Rate field cannot be empty");

            alert.showAndWait();
        } else if (comboboxClient.getSelectionModel().getSelectedItem() == null) {
            alert.setHeaderText("Client needs to be selected");

            alert.showAndWait();
        } else {
            int rate;

            try {
                rate = Integer.parseInt(txtRate.getText());
            } catch (NumberFormatException e) {
                alert.setHeaderText("Rate needs to be number");

                alert.showAndWait();
                return;
            }

            model.editProject(project.getId(),
                    txtName.getText(),
                    rate,
                    comboboxClient.getSelectionModel().getSelectedItem().getId());
            sm.changeScene(event, "adminNewEditProject");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        sm.changeScene(event, "adminNewEditProject");
    }

    public void setProject(Project project) {
        this.project = project;
        txtName.setText(project.getName());

        clients.stream().filter((c) -> (project.getCLient().getId() == c.getId())).forEachOrdered((c) -> {
            comboboxClient.getSelectionModel().select(c);
        });

        //first time edit page is opend rate is set to value from db
        //this had to be added becasue change listener changed it on initializitaion
        txtRate.setText(project.getRate() + "");
    }
}
