/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableView;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminMainController implements Initializable {

    @FXML
    private JFXButton btnLogOut;
    @FXML
    private JFXButton btnNewEditClient;
    @FXML
    private JFXButton btnNewEditUser;
    @FXML
    private JFXButton btnNewEditProject;
    @FXML
    private JFXButton btnReports;
    @FXML
    private JFXTextField TxtTaskname;
    @FXML
    private JFXComboBox<?> Comboprojects;
    @FXML
    private JFXButton BtnSartstop;
    @FXML
    private JFXButton BtnAddtask;
    @FXML
    private Label Lbltimer;
    @FXML
    private TreeTableView<?> tableview;
    @FXML
    private Label LblCurrenttask;

    SceneManager sm = new SceneManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void actionLogout(ActionEvent event) {
        sm.logOut(event);
    }

    @FXML
    private void btnNewEditClientAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditClient");
    }

    @FXML
    private void btnNewEditUserAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditUser");
    }

    @FXML
    private void btnNewEditProjectAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditProject");
    }

    @FXML
    private void btnReportsAction(ActionEvent event) {
        sm.changeScene(event, "adminReports");
    }
}
