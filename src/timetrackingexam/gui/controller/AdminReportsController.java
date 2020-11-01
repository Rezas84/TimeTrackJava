/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.VBox;
import timetrackingexam.be.Project;
import timetrackingexam.be.TaskTime;
import timetrackingexam.be.User;
import timetrackingexam.be.helperFilterEntities.FProject;
import timetrackingexam.be.helperFilterEntities.FTask;
import timetrackingexam.gui.model.FacadeModel;
import timetrackingexam.gui.model.FilterModel;
import timetrackingexam.gui.model.ReportModel;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminReportsController implements Initializable {

    @FXML
    private JFXComboBox<Project> comboProject;
    @FXML
    private JFXComboBox<User> comboUser;
    @FXML
    private TreeTableView<FProject> tableReport;
    @FXML
    private TreeTableColumn<User, String> columname;
    @FXML
    private TreeTableColumn<FProject, String> columProject;
    @FXML
    private TreeTableColumn<FTask, Integer> columTask;
    @FXML
    private TreeTableColumn<FTask, String> columHour;
    @FXML
    private JFXRadioButton radioLastMonth;
    @FXML
    private JFXRadioButton radioLastWeek;
    private FilterModel tsModel;
    @FXML
    private JFXDatePicker fromDatePicker;
    @FXML
    private JFXDatePicker toDatePicker;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private NumberAxis barChartY;
    @FXML
    private CategoryAxis barChartX;
    @FXML
    private JFXComboBox<User> comboUser1;
    @FXML
    private VBox panelVbox;
    @FXML
    private TreeTableColumn<FProject, Integer> col_salary;
    XYChart.Series<String, Number> set1;
    SceneManager sm = new SceneManager();
    FacadeModel model;
    ReportModel reportmodel;
    SidePanel sp = new SidePanel();
    List<User> users = new ArrayList();
    List<Project> projects = new ArrayList();
    private Date fromDate;
    private Date toDate;
    private LocalDate currentdate;
    private ObservableList<FProject> results;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp.init(panelVbox);
        model = FacadeModel.getInstance();
        reportmodel = ReportModel.getInstance();
        tsModel = FilterModel.getInstance();
        tsModel.getAllProjects();
        set1 = new XYChart.Series<String, Number>();
        columname.setCellValueFactory(new TreeItemPropertyValueFactory<>("userName"));
        columProject.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        columTask.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalCountOfTasks"));
        columHour.setCellValueFactory(new TreeItemPropertyValueFactory<>("workinghours"));
        col_salary.setCellValueFactory(new TreeItemPropertyValueFactory<>("gottenPay"));
        users = model.getAllUsers();
        comboUser.setItems(FXCollections.observableArrayList(users));
        comboUser.getItems().add(new User(-1, "All users", "", -1));
        comboUser1.setItems(FXCollections.observableArrayList(users));
        
        comboUser.getSelectionModel().selectLast();
        comboUser1.getSelectionModel().clearSelection();

        projects = model.getAllProject();

        comboProject.setItems(FXCollections.observableArrayList(projects));
        comboProject.getItems().add(new Project(-1, "All projects", -1));
        comboProject.getSelectionModel().selectLast();

    }

    @FXML
    private void filter(ActionEvent event) {

        setResultsToTable();
    }

    @FXML
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

    @FXML
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

    @FXML
    private void chooseProject(ActionEvent event) {
        if (comboUser.getValue() == null) {
            users = reportmodel.getAllUsersByProject(comboProject.getSelectionModel().getSelectedItem().getId());

            comboUser.setItems(FXCollections.observableArrayList(users));
            comboUser.getItems().add(new User(-1, "All users", "", -1));

        }
    }

    @FXML
    private void chooseUser(ActionEvent event) {
        // has a problem, doesnt select the proper project for a user
        if (comboProject.getValue() == null) {
            projects = reportmodel.getAllProjectsByUser(comboUser.getSelectionModel().getSelectedItem().getId());

            comboProject.setItems(FXCollections.observableArrayList(projects));

            comboProject.getItems().add(new Project(-1, "All projects", -1));
        }
    }

    @FXML
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

    @FXML
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

    private void setResultsToTable() {
        results = FXCollections.observableArrayList(tsModel.filterEverything(fromDate, toDate, comboUser.getSelectionModel().getSelectedItem().getId(), comboProject.getSelectionModel().getSelectedItem().getId()));

        TreeItem items = new TreeItem(results.toArray());

        for (FProject result : results) {
            TreeItem child = new TreeItem(result);
            items.getChildren().add(child);
            
        }
        tableReport.setRoot(items);
        tableReport.setShowRoot(false);
    }
/**
 * Action to connect user combo box to the bar chart for showing total hours working per hours of 
 * each User in the bar chart .
 * @param event .Action event.
 */
    @FXML
    private void comboUserForChart(ActionEvent event) {
        set1.getData().clear();
        int userId = comboUser1.getSelectionModel().getSelectedItem().getId();
        LocalDate currentMonth = LocalDate.now();
        List<TaskTime> allTaskTimeForUserByDate = reportmodel.getAllTaskTimeForUserByDate(userId, Date.valueOf(currentMonth.minusMonths(5)));
        int[] hours = new int[4];
        for (TaskTime taskTime : allTaskTimeForUserByDate) {
            LocalDate startOfMonth1 = currentMonth.withDayOfMonth(1);
            LocalDate endOfMonth1 = currentMonth.withDayOfMonth(currentMonth.getMonth().maxLength());
            Date fromDate1 = Date.valueOf(startOfMonth1);
            Date toDate1 = Date.valueOf(endOfMonth1);
            LocalDate startOfMonth2 = currentMonth.minusMonths(1).withDayOfMonth(1);
            LocalDate endOfMonth2 = currentMonth.minusMonths(1).withDayOfMonth(currentMonth.minusMonths(1).getMonth().maxLength());
            Date fromDate2 = Date.valueOf(startOfMonth2);
            Date toDate2 = Date.valueOf(endOfMonth2);
            LocalDate startOfMonth3 = currentMonth.minusMonths(2).withDayOfMonth(1);
            LocalDate endOfMonth3 = currentMonth.minusMonths(2).withDayOfMonth(currentMonth.minusMonths(2).getMonth().maxLength());
            Date fromDate3 = Date.valueOf(startOfMonth3);
            Date toDate3 = Date.valueOf(endOfMonth3);
            LocalDate startOfMonth4 = currentMonth.minusMonths(3).withDayOfMonth(1);
            LocalDate endOfMonth4 = currentMonth.minusMonths(3).withDayOfMonth(currentMonth.minusMonths(3).getMonth().maxLength());
            Date fromDate4 = Date.valueOf(startOfMonth4);
            Date toDate4 = Date.valueOf(endOfMonth4);

            if (taskTime.getDateTaskname().getTime() >= fromDate4.getTime() && taskTime.getDateTaskname().getTime() <= toDate4.getTime()) {
                hours[3] += taskTime.getWorkingHours1();
            } else if (taskTime.getDateTaskname().getTime() >= fromDate3.getTime() && taskTime.getDateTaskname().getTime() <= toDate3.getTime()) {
                hours[2] += taskTime.getWorkingHours1();
            } else if (taskTime.getDateTaskname().getTime() >= fromDate2.getTime() && taskTime.getDateTaskname().getTime() <= toDate2.getTime()) {
                hours[1] += taskTime.getWorkingHours1();
            } else {
                hours[0] += taskTime.getWorkingHours1();
            }
        }
        TaskTime taskTime = new TaskTime();
        set1.getData().add(new XYChart.Data(currentMonth.minusMonths(0).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), taskTime.getWorkinghoursByHour(hours[0])));
        set1.getData().add(new XYChart.Data(currentMonth.minusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), taskTime.getWorkinghoursByHour(hours[1])));
        set1.getData().add(new XYChart.Data(currentMonth.minusMonths(2).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), taskTime.getWorkinghoursByHour(hours[2])));
        set1.getData().add(new XYChart.Data(currentMonth.minusMonths(3).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), taskTime.getWorkinghoursByHour(hours[3])));
        barChart.setData(FXCollections.observableArrayList(set1));
        barChart.setAnimated(false);
    }
}
