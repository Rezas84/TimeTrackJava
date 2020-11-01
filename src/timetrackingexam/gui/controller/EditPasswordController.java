/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import timetrackingexam.be.LoggedUser;
import timetrackingexam.gui.model.FacadeModel;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class EditPasswordController implements Initializable {

    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXPasswordField txtPasswordRepeat;

    SceneManager sm = new SceneManager();
    FacadeModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = FacadeModel.getInstance();
    }

    @FXML
    private void savePass(ActionEvent event) {
        if (check()) {
            model.userSetPassword(LoggedUser.getInstance().id, txtPassword.getText());
        }

        sm.changeScene(event, "userMain");
    }

    @FXML
    private void cancel(ActionEvent event) {
        sm.changeScene(event, "userMain");
    }

    private boolean check() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("New password");

        if (txtPassword.getText().isEmpty() || txtPassword.getText() == null) {
            alert.setHeaderText("New password field needs to be fiiled");
            alert.showAndWait();
            return false;
        } else if (txtPasswordRepeat.getText().isEmpty() || txtPasswordRepeat.getText() == null) {
            alert.setHeaderText("Repeat password field needs to be fiiled");
            alert.showAndWait();
            return false;
        } else if (!txtPassword.getText().equals(txtPasswordRepeat.getText())) {
            alert.setHeaderText("Passwords doesnt match");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
