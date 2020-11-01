/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import timetrackingexam.be.Project;
import timetrackingexam.be.User;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;
import timetrackingexam.bll.IReports;
import timetrackingexam.bll.ReportManager;
import timetrackingexam.gui.model.TaskModel;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminReportsController implements Initializable {

    @FXML
    private JFXButton btnMainPage;
    @FXML
    private JFXButton btnReport;
    @FXML
    private JFXButton btnNewEditProject;
    @FXML
    private JFXButton btnNewEditClient;
    @FXML
    private JFXButton btnNewEditUser;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXComboBox<Project> comboProject;

    @FXML
    private JFXComboBox<User> comboUser;
    @FXML
    private TreeTableView<?> tableReport;
    @FXML
    private TreeTableColumn<?, ?> columname;
    @FXML
    private TreeTableColumn<?, ?> columProject;
    @FXML
    private TreeTableColumn<?, ?> columTask;
    @FXML
    private TreeTableColumn<?, ?> columHour;
    @FXML
    private JFXRadioButton radioLastMonth;
    String periodType;
    int periodValue;
    @FXML
    private JFXRadioButton radioLastWeek;

    private TaskModel tsModel;
    @FXML
    private JFXDatePicker fromDatePicker;
    @FXML
    private JFXDatePicker toDatePicker;

    SceneManager sm = new SceneManager();
    IFacade bll = new BllFacade();
    IReports reportsbll = new ReportManager();
    List<User> users = new ArrayList();
    List<Project> projects = new ArrayList();
    private Date fromDate;
    private Date toDate;
    private LocalDate currentdate;
    @FXML
    private TreeTableColumn<?, ?> columdate;
    @FXML
    private JFXRadioButton radioCalender;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tsModel = TaskModel.getInstance();
        tsModel.getAllProjects();
        String str = "2020-05-06";
        Date date = Date.valueOf(str);//converting string into sql date  
        String str1 = "2020-05-08";
        Date date1 = Date.valueOf(str1);//converting string into sql date  

        tsModel.filterEverything(null, null, 2, -1);
        tsModel.filterEverything(null, null, 1, -1);
        tsModel.filterEverything(null, null, -1, 1);//null
        tsModel.filterEverything(null, null, -1, 2);
        tsModel.filterEverything(date, date1, -1, -1);//null
        //tsModel.filterEverything(null, null, -1, 2);
        users = bll.getAllUsers();
        comboUser.setItems(FXCollections.observableArrayList(users));
        projects = bll.getAllProject();
        comboProject.setItems(FXCollections.observableArrayList(projects));

    }

    @FXML
    private void btnMainPageAction(ActionEvent event) {
        sm.changeScene(event, "adminMain");
    }

    @FXML
    private void btnLogoutAction(ActionEvent event) {
        sm.logOut(event);
    }

    @FXML
    private void btnNewEditProjectAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditProject");
    }

    @FXML
    private void btnNewEditClientAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditClient");

    }

    @FXML
    private void btnNewEditUserAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditUser");

    }


    private void filterByLastWeek(ActionEvent event) {
        // The code of getting the start and end date from last week by current date was found on stackoverflow 

        if (radioLastMonth.isSelected() || fromDatePicker.getValue() != null || toDatePicker.getValue() != null) {
            currentdate = LocalDate.now();
            LocalDate startOfWeek = currentdate.minusDays(7 + currentdate.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = currentdate.minusDays(currentdate.getDayOfWeek().getValue());
            fromDate = Date.valueOf(startOfWeek);
            toDate = Date.valueOf(endOfWeek);
            System.out.println(fromDate + "  " + toDate);
            radioLastMonth.setSelected(false);
            fromDatePicker.getEditor().clear();
            toDatePicker.getEditor().clear();
        } else {
            currentdate = LocalDate.now();
            LocalDate startOfWeek = currentdate.minusDays(7 + currentdate.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = currentdate.minusDays(currentdate.getDayOfWeek().getValue());
            fromDate = Date.valueOf(startOfWeek);
            toDate = Date.valueOf(endOfWeek);
            System.out.println(fromDate + "  " + toDate);

        }

    }

    private void filterByLastMonth(ActionEvent event) {
        // The code of getting the start and end date from last month by current date was found on stackoverflow 

        if (radioLastWeek.isSelected() || fromDatePicker.getValue() != null || toDatePicker.getValue() != null) {
            currentdate = LocalDate.now();
            LocalDate previousMonth = currentdate.minusMonths(1);
            LocalDate startOfMonth = previousMonth.withDayOfMonth(1);
            LocalDate endOfMonth = previousMonth.withDayOfMonth(previousMonth.getMonth().maxLength());
            fromDate = Date.valueOf(startOfMonth);
            toDate = Date.valueOf(endOfMonth);
            System.out.println(fromDate + "  " + toDate);
            radioLastWeek.setSelected(false);
            fromDatePicker.getEditor().clear();
            toDatePicker.getEditor().clear();
        } else {
            currentdate = LocalDate.now();
            LocalDate previousMonth = currentdate.minusMonths(1);
            LocalDate startOfMonth = previousMonth.withDayOfMonth(1);
            LocalDate endOfMonth = previousMonth.withDayOfMonth(previousMonth.getMonth().maxLength());
            fromDate = Date.valueOf(startOfMonth);
            toDate = Date.valueOf(endOfMonth);
            System.out.println(fromDate + "  " + toDate);

        }

    }

    private void chooseProject(ActionEvent event) {
        if (comboUser.getValue() == null) {
            users = reportsbll.getAllUsersByProject(comboProject.getSelectionModel().getSelectedItem().getId());
            comboUser.setItems(FXCollections.observableArrayList(users));

        }
    }

    private void chooseUser(ActionEvent event) {
        // has a problem, doesnt select the proper project for a user
        if (comboProject.getValue() == null) {
            projects = reportsbll.getAllProjectsByUser(comboUser.getSelectionModel().getSelectedItem().getId());
            comboProject.setItems(FXCollections.observableArrayList(projects));
        }
    }

    private void setFromDateInCalendar(ActionEvent event) {
        if (radioLastWeek.isSelected() || radioLastMonth.isSelected()) {
            fromDate = Date.valueOf(fromDatePicker.getValue());
            System.out.println(fromDate);
            radioLastWeek.setSelected(false);
            radioLastMonth.setSelected(false);
        } else {
            fromDate = Date.valueOf(fromDatePicker.getValue());
            System.out.println(fromDate);
        }
    }

    private void setToDateInCalendar(ActionEvent event) {
        if (radioLastWeek.isSelected() || radioLastMonth.isSelected()) {
            toDate = Date.valueOf(toDatePicker.getValue());
            System.out.println(toDate);
            radioLastWeek.setSelected(false);
            radioLastMonth.setSelected(false);
        } else {
            toDate = Date.valueOf(toDatePicker.getValue());
            System.out.println(toDate);
        }
    }

    @FXML
    private void btnReportsAction(ActionEvent event) {
    }

    @FXML
    private void filter(ActionEvent event) {
    }

}
