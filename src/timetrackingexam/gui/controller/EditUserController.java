/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import timetrackingexam.be.Role;
import timetrackingexam.be.User;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;
import timetrackingexam.help.Validator;

/**
 * FXML Controller class
 *
 * @author domin
 */
public class EditUserController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXComboBox<Role> comboboxRole;

    SceneManager sm = new SceneManager();
    IFacade bll = new BllFacade();

    User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboboxRole.getItems().add(new Role(1, "Admin"));
        comboboxRole.getItems().add(new Role(2, "Developer"));
    }

    @FXML
    private void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("New User");

        if (txtName.getText().isEmpty() || txtName.getText() == null) {
            alert.setHeaderText("Name field cannot be empty");

            alert.showAndWait();
        } else if (txtEmail.getText().isEmpty() || txtEmail.getText() == null) {
            alert.setHeaderText("Email field cannot be empty");

            alert.showAndWait();
        } else if (!Validator.validateEmail(txtEmail.getText())) {
            alert.setHeaderText("Invalid email");

            alert.showAndWait();
        } else {
            bll.editUser(user.getId(),
                    txtName.getText(),
                    txtEmail.getText(),
                    comboboxRole.getSelectionModel().getSelectedItem().getId());

            sm.changeScene(event, "adminNewEditUser");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        sm.changeScene(event, "adminNewEditUser");
    }

    public void setUser(User user) {
        this.user = user;

        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        comboboxRole.getSelectionModel().select(user.getRights() - 1);
    }
}
