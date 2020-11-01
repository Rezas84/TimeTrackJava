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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import timetrackingexam.gui.model.FacadeModel;
import timetrackingexam.help.Hash;
import timetrackingexam.help.Validator;

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
    FacadeModel model;
    SceneManager sm = new SceneManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = FacadeModel.getInstance();
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
            alert.setHeaderText("Wrong email or password");

            alert.showAndWait();
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (txtEmail.getText().isEmpty() || txtEmail.getText() == null) {
            alert.setTitle("Reset password");
            alert.setHeaderText("Email field needs to be fiiled");
            return;
        }

        String email = txtEmail.getText();
        if (Validator.validateEmail(email) && model.isEmailValid(email)) {
            model.resetPassword(email);
            alert.setTitle("Reset password");
            alert.setHeaderText("New password was sent to email");
        }
    }

    private boolean isValid(String email, String password) {
        return model.isValidLogin(email, password);
    }

    private void mainwindow(ActionEvent event) {
        sm.changeScene(event, "userMain");
    }
}
