/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import timetrackingexam.be.LoggedUser;
import timetrackingexam.be.Task;
import timetrackingexam.gui.model.TaskModel;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class EditTaskController implements Initializable {

    @FXML
    private JFXTextField txttaskname;
    @FXML
    private JFXRadioButton radioBillable;
    @FXML
    private JFXTextField txtMinute;
    @FXML
    private JFXTextField txtHour;

    TaskModel taskModel;
    Task task;
    LoggedUser user;
    SceneManager sm = new SceneManager();
    private int newtime;
    private boolean isbillable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        taskModel = TaskModel.getInstance();
        user = LoggedUser.getInstance();
    }

    @FXML
    private void save(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Edit Task");
        if (txttaskname.getText().isEmpty() || txttaskname.getText() == null) {
            alert.setHeaderText("Name field cannot be empty");

            alert.showAndWait();
        }
        if (txtHour.getText().isEmpty() || txtHour.getText() == null && txtMinute.getText().isEmpty() || txtMinute.getText() == null) {
            alert.setHeaderText("Time field cannot be empty");

            alert.showAndWait();
        } else {
            newtime = 0;
            int hour = Integer.parseInt(txtHour.getText()) * 60 * 60;
            System.out.println("hour " + hour);
            int minute = Integer.parseInt(txtMinute.getText()) * 60;
            System.out.println("minute " + minute);
            newtime = hour + minute;
            System.out.println(newtime);
            if (radioBillable.isSelected() == true) {
                isbillable = true;
            } else {
                isbillable = false;
            }

            taskModel.updateTask(user.id, task.getId(), txttaskname.getText(), newtime, isbillable);
            sm.changeScene(event, "userMain");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        sm.changeScene(event, "userMain");
    }

    public void setTask(Task task) {
        this.task = task;

        txttaskname.setText(task.getTaskname());

        if (task.getState() == "Billable") {
            radioBillable.setSelected(true);
        }

        if (task.getState() == "") {
            radioBillable.setSelected(false);
        }
    }
}
