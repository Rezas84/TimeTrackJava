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
import java.sql.Time;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import timetrackingexam.be.LoggedUser;
import timetrackingexam.be.Project;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;
import timetrackingexam.bll.IProject;
import timetrackingexam.bll.ITask;
import timetrackingexam.bll.ProjectManager;
import timetrackingexam.bll.TaskManager;
import timetrackingexam.bll.TimeCounter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class UserMainController implements Initializable {

    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXTextField TxtTaskname;
    @FXML
    private JFXComboBox<Project> Comboprojects;
    @FXML
    private JFXButton BtnAddtask;

    @FXML
    private TreeTableView<Task> tableview;
    @FXML
    private Label LblCurrenttask;
    @FXML
    private Label LblUserInfo;
    private Label Lblhour;
    private Label Lblminute;
    private Label Lblsecond;
    @FXML
    private JFXButton BtnStart;
    @FXML
    private JFXButton BtnStop;
    @FXML
    private Label Lblerror;
    SceneManager sm = new SceneManager();
    private ObservableList<Project> projects;
    private ObservableList<Task> tasks;
    private ObservableList<TaskTime> tasksTime;
    private boolean x = true;
    private int time;
    ITask taskmanager = new TaskManager();
    IProject projectmanager = new ProjectManager();
    LoggedUser user;

    TimeCounter timecounter;
    @FXML
    private TreeTableColumn<Task, String> task_clm;
    @FXML
    private TreeTableColumn<Task, String> hour_clm;
    @FXML
    private Label lblTime;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        user = LoggedUser.getInstance();

        task_clm.setCellValueFactory(new TreeItemPropertyValueFactory<>("taskname"));
        hour_clm.setCellValueFactory(new TreeItemPropertyValueFactory<>("workinghours"));
        TreeItem<String> item = new TreeItem<>();

        setProjectsToCombobox();

        setCurrentTaskToLabel();
        System.out.println(user.name + " " + user.email);
        timecounter = new TimeCounter(lblTime, time);
        BtnStop.setVisible(false);
    }

    @FXML
    private void btnLogoutAction(ActionEvent event) {
        sm.logOut(event);
    }

    @FXML
    private void startStopTask(ActionEvent event) throws InterruptedException {
        if (tableview.getSelectionModel().getSelectedItem() == null || LblCurrenttask.getText() == null || LblCurrenttask.getText().trim().isEmpty()) {
            Lblerror.setText("Please select a task from the table");
        } else {
            timecounter.counter();
            BtnStart.setDisable(true);
            Lblerror.setText("");
            BtnStart.setVisible(false);
            BtnStop.setVisible(true);
        }
    }

    @FXML
    private void addNewTask(ActionEvent event) {

        if (TxtTaskname.getText() == null || TxtTaskname.getText().trim().isEmpty()) {
            Lblerror.setText("Please enter a task name");
        } else {

            addCreateNewTask();
            setTasksToTable();
            TxtTaskname.clear();
            Lblerror.setText("");

        }

    }

    private void setTasksToTable() {
        tasks = FXCollections.observableArrayList(taskmanager.getAllTasksByProject(user.id, Comboprojects.getSelectionModel().getSelectedItem().getId()));

        TreeItem itemas = new TreeItem(tasks.toArray());

        for (Task task : tasks) {
            TreeItem child = new TreeItem(task);
            itemas.getChildren().add(child);
            tasksTime = FXCollections.observableArrayList(taskmanager.getTimeForTask(user.id, task.getId()));
            for (TaskTime taskTime : tasksTime) {
                child.getChildren().add(new TreeItem(taskTime));
            }
        }
        tableview.setRoot(itemas);
        tableview.setShowRoot(false);

    }

    private void setProjectsToCombobox() {
        projects = FXCollections.observableArrayList(projectmanager.getAllProjectByUser(user.id));
        Comboprojects.setItems(projects);
    }

    @FXML
    private void stopTask(ActionEvent event) {
        int totalTime = timecounter.stopCounter();
        BtnStart.setDisable(false);

        taskmanager.addNewTimeToTask(tableview.getSelectionModel().getSelectedItem().getValue().getId(), user.id, totalTime);
        setTasksToTable();
                            BtnStart.setVisible(true);
        BtnStop.setVisible(false);

    }

    @FXML
    private void setCurrentTaskToLabel() {

        if (tableview.getSelectionModel().getSelectedItem() != null) {
            LblCurrenttask.setText(tableview.getSelectionModel().getSelectedItem().getValue().getTaskname());
        }

    }

    private void addCreateNewTask() {
        taskmanager.createNewTask(TxtTaskname.getText(), Comboprojects.getSelectionModel().getSelectedItem().getId(), user.id);
    }

    private ExecutorService absenceThreadExecutor;
    private ScheduledExecutorService scedExec;

    @FXML
    private void setUpTableView(ActionEvent event) {
        if (Comboprojects.getSelectionModel().getSelectedItem() != null) {
            //Start here animu
            absenceThreadExecutor = Executors.newSingleThreadExecutor();
            absenceThreadExecutor.execute(() -> {
                Platform.runLater(() -> {
                    setTasksToTable();
                });
            });

            absenceThreadExecutor.shutdown();

            //Stop load gif here
            scedExec = Executors.newSingleThreadScheduledExecutor();
            scedExec.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> {
                    if (!this.absenceThreadExecutor.isTerminated()) {
                        System.out.println("load");
                        //Continue
                    } else {
                        System.out.println("DONE");
                        scedExec.shutdownNow();
                    }
                    /* option 2
                    if (this.absenceThreadExecutor.isTerminated()) {
                              System.out.println("DONE");
                        scedExec.shutdownNow();
                    }
                     */
                });
            }, 1//Delay before start 
                    ,
                     3 //Repeat after execution
                    ,
                     TimeUnit.SECONDS);

        }
    }

}
