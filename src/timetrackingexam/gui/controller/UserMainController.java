/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import timetrackingexam.be.LoggedUser;
import timetrackingexam.be.Project;
import timetrackingexam.be.Task;
import timetrackingexam.be.TaskTime;
import timetrackingexam.bll.TimeCounter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetrackingexam.gui.model.ProjectModel;
import timetrackingexam.gui.model.TaskModel;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class UserMainController implements Initializable {

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
    private JFXButton BtnStart;
    @FXML
    private JFXButton BtnStop;
    @FXML
    private Label Lblerror;
    @FXML
    private TreeTableColumn<Task, String> task_clm;
    @FXML
    private TreeTableColumn<Task, String> hour_clm;
    @FXML
    private Label lblTime;
    @FXML
    private JFXRadioButton radioBillable;
    @FXML
    private VBox panelVbox;
    @FXML
    private TreeTableColumn<Task, String> col_state;

    SceneManager sm = new SceneManager();
    private ObservableList<Project> projects;
    private ObservableList<Task> tasks;
    private ObservableList<TaskTime> tasksTime;
    private int time;

    TaskModel taskModel;
    ProjectModel projectModel;
    LoggedUser user;
    TimeCounter timecounter;
    SidePanel sp = new SidePanel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp.init(panelVbox);
        user = LoggedUser.getInstance();
        projectModel = ProjectModel.getInstance();
        taskModel = TaskModel.getInstance();
        task_clm.setCellValueFactory(new TreeItemPropertyValueFactory<>("taskname"));
        hour_clm.setCellValueFactory(new TreeItemPropertyValueFactory<>("workinghours"));
        col_state.setCellValueFactory(new TreeItemPropertyValueFactory<>("state"));

        setProjectsToCombobox();
        setUpTableAfterLogin();

        setCurrentTaskToLabel();
        System.out.println(user.name + " " + user.email);
        timecounter = new TimeCounter(lblTime, time);
        addEditButtonToTable();
        radioBillable.setSelected(true);
    }

    @FXML
    private void startStopTask(ActionEvent event) throws InterruptedException {
        if (tableview.getSelectionModel().getSelectedItem() == null || LblCurrenttask.getText() == null || LblCurrenttask.getText().trim().isEmpty()) {
            Lblerror.setText("Please select a task from the table");
        } else {
            timecounter.counter();
            BtnStart.setDisable(true);
            Lblerror.setText("");
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
            if (radioBillable.isSelected() == false) {
                radioBillable.setSelected(true);
            }
        }

    }

    private void setUpTableAfterLogin() {
        if (!Comboprojects.getItems().isEmpty()) {
            Comboprojects.getSelectionModel().selectFirst();
            setTasksToTable();
        }

    }

    private void setTasksToTable() {

        tasks = FXCollections.observableArrayList(taskModel.getAllTasksByProject(user.id, Comboprojects.getSelectionModel().getSelectedItem().getId()));
        if (!tasks.isEmpty()) {
            TreeItem itemas = new TreeItem(tasks.toArray());

            for (Task task : tasks) {
                TreeItem child = new TreeItem(task);
                itemas.getChildren().add(child);
                tasksTime = FXCollections.observableArrayList(taskModel.getTimeForTask(user.id, task.getId())); //For each task time task.getWorkingHours
                for (TaskTime taskTime : tasksTime) {
                    child.getChildren().add(new TreeItem(taskTime));
                }
            }
            tableview.setRoot(itemas);
            tableview.setShowRoot(false);
        }

    }

    private void setProjectsToCombobox() {
        projects = FXCollections.observableArrayList(projectModel.getAllProjectByUser(user.id));
        Comboprojects.setItems(projects);
    }

    @FXML
    private void stopTask(ActionEvent event) {
        int totalTime = timecounter.stopCounter();
        BtnStart.setDisable(false);

        taskModel.addNewTimeToTask(tableview.getSelectionModel().getSelectedItem().getValue().getId(), user.id, totalTime);
        setTasksToTable();

    }

    @FXML
    private void setCurrentTaskToLabel() {

        if (tableview.getSelectionModel().getSelectedItem() != null) {
            LblCurrenttask.setText(tableview.getSelectionModel().getSelectedItem().getValue().getTaskname());
        }

    }

    private void addCreateNewTask() {
        taskModel.createNewTask(TxtTaskname.getText(), Comboprojects.getSelectionModel().getSelectedItem().getId(), user.id, radioBillable.isSelected());
    }

    private ExecutorService absenceThreadExecutor;
    private ScheduledExecutorService scedExec;

    @FXML
    private void setUpTableView(ActionEvent event) {

        if (Comboprojects.getSelectionModel().getSelectedItem() != null) {
            absenceThreadExecutor = Executors.newSingleThreadExecutor();
            absenceThreadExecutor.execute(() -> {
                Platform.runLater(() -> {
                    setTasksToTable();
                });
            });
            absenceThreadExecutor.shutdown();
            scedExec = Executors.newSingleThreadScheduledExecutor();
            scedExec.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> {
                    if (!this.absenceThreadExecutor.isTerminated()) {
                        System.out.println("load");

                    } else {
                        System.out.println("DONE");

                        scedExec.shutdownNow();
                    }

                });
            }, 1//Delay  
                    ,
                     3 //Repeat after execution
                    ,
                     TimeUnit.SECONDS);

        }
    }

    private void addEditButtonToTable() {
        TreeTableColumn<Task, Void> colBtnDel = new TreeTableColumn("Edit");

        Callback<TreeTableColumn<Task, Void>, TreeTableCell<Task, Void>> cellFactory = new Callback<TreeTableColumn<Task, Void>, TreeTableCell<Task, Void>>() {
            @Override
            public TreeTableCell<Task, Void> call(final TreeTableColumn<Task, Void> param) {
                final TreeTableCell<Task, Void> cell = new TreeTableCell<Task, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try {
                                if (tableview.getSelectionModel().getSelectedItem() == null) {
                                    Lblerror.setText("Please select a task");
                                } else {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetrackingexam/gui/view/EditTask.fxml"));
                                    Parent root = loader.load();

                                    EditTaskController euc = loader.getController();

                                    // if you only click the edit button without selecting a task before, it will crash
                                    euc.setTask(tableview.getSelectionModel().getSelectedItem().getValue());

                                    Scene scene = new Scene(root);

                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            } catch (IOException ex) {
                                System.out.println(ex);
                            }
                        });

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtnDel.setCellFactory(cellFactory);

        tableview.getColumns().add(colBtnDel);
    }
}
