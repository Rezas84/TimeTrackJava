/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import timetrackingexam.be.LoggedUser;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;
import timetrackingexam.help.Hash;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class LoginscrController implements Initializable {

    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtPassword;

    Hash hash = new Hash();
    IFacade bll = new BllFacade();
    SceneManager sm = new SceneManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void login(ActionEvent event) {
        String email = txtEmail.getText();
        String password = hash.hashPass(txtPassword.getText());

        if (isValid(email, password)) {
            mainwindow(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login");
            alert.setHeaderText("Wrong name or password");

            alert.showAndWait();
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
    }

    private boolean isValid(String email, String password) {
        return bll.isValidLogin(email, password);
    }

    private void mainwindow(ActionEvent event) {
        LoggedUser user = LoggedUser.getInstance();

        try {
            switch (user.rights) {
                case 1:
                    sm.changeScene(event, "userMain");
                    break;
                case 2:
                    sm.changeScene(event, "adminMain");
                    break;
                default:
                    throw new Exception("User rights unrecognized");
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginscrController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
