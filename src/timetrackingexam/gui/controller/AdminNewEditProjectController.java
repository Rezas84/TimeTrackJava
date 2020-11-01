/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetrackingexam.be.Client;
import timetrackingexam.be.Project;
import timetrackingexam.gui.model.FacadeModel;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminNewEditProjectController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtRate;
    @FXML
    private JFXComboBox<Client> comboboxClient;
    @FXML
    private TableView<Project> tableProjects;
    @FXML
    private TableColumn<Project, String> columnName;
    @FXML
    private TableColumn<Project, Integer> columnRate;
    @FXML
    private TableColumn<Project, String> columnClient;
    @FXML
    private VBox panelVbox;

    SceneManager sm = new SceneManager();
    FacadeModel model;
    SidePanel sp = new SidePanel();

    ArrayList<Project> projects = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp.init(panelVbox);
        model = FacadeModel.getInstance();
        addEditButtonToTable();
        addDeleteButtonToTable();
        addManageButtonToTable();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        columnRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

        try {
            comboboxClient.setItems(FXCollections.observableList(model.getAllClient()));
            comboboxClient.getSelectionModel().selectFirst();

            projects = model.getAllProject();
            tableProjects.setItems(FXCollections.observableList(projects));
        } catch (Exception e) {
            e.printStackTrace();
        }

        comboboxClient.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Client> options, Client oldValue, Client newValue) -> {
            txtRate.setText(comboboxClient.getSelectionModel().getSelectedItem().getRate() + "");
        });
    }

    @FXML
    private void addProject(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("New project");

        if (txtName.getText().isEmpty() || txtName.getText() == null) {
            alert.setHeaderText("Name field cannot be empty");

            alert.showAndWait();
        } else if (txtRate.getText().isEmpty() || txtRate.getText() == null) {
            alert.setHeaderText("Rate field cannot be empty");

            alert.showAndWait();
        } else if (comboboxClient.getSelectionModel().getSelectedItem() == null) {
            alert.setHeaderText("Client needs to be selected");

            alert.showAndWait();
        } else {
            int rate;

            try {
                rate = Integer.parseInt(txtRate.getText());
            } catch (NumberFormatException e) {
                alert.setHeaderText("Rate needs to be number");

                alert.showAndWait();
                return;
            }

            Project p = model.addNewProject(txtName.getText(),
                    comboboxClient.getSelectionModel().getSelectedItem().getId(),
                    comboboxClient.getSelectionModel().getSelectedItem().getName(),
                    comboboxClient.getSelectionModel().getSelectedItem().getRate(),
                    rate
            );

            projects.add(p);
            tableProjects.setItems(FXCollections.observableList(projects));
        }
    }

    private void addDeleteButtonToTable() {
        TableColumn<Project, Void> colBtnDel = new TableColumn("Delete");

        Callback<TableColumn<Project, Void>, TableCell<Project, Void>> cellFactory = new Callback<TableColumn<Project, Void>, TableCell<Project, Void>>() {
            @Override
            public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                final TableCell<Project, Void> cell = new TableCell<Project, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Project p = getTableView().getItems().get(getIndex());

                            model.deleteProject(p.getId());

                            projects.remove(p);
                            tableProjects.setItems(FXCollections.observableList(projects));
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

        tableProjects.getColumns().add(colBtnDel);
    }

    private void addEditButtonToTable() {
        TableColumn<Project, Void> colBtnDel = new TableColumn("Edit");

        Callback<TableColumn<Project, Void>, TableCell<Project, Void>> cellFactory = new Callback<TableColumn<Project, Void>, TableCell<Project, Void>>() {
            @Override
            public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                final TableCell<Project, Void> cell = new TableCell<Project, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Project p = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetrackingexam/gui/view/EditProject.fxml"));
                                Parent root = loader.load();

                                EditProjectController epc = loader.getController();
                                epc.setProject(p);

                                Scene scene = new Scene(root);

                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
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

        tableProjects.getColumns().add(colBtnDel);
    }

    private void addManageButtonToTable() {
        TableColumn<Project, Void> colBtnDel = new TableColumn("Manage");

        Callback<TableColumn<Project, Void>, TableCell<Project, Void>> cellFactory = new Callback<TableColumn<Project, Void>, TableCell<Project, Void>>() {
            @Override
            public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                final TableCell<Project, Void> cell = new TableCell<Project, Void>() {

                    private final Button btn = new Button("Manage");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Project p = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetrackingexam/gui/view/EditDeveloperProject.fxml"));
                                Parent root = loader.load();

                                EditDeveloperProjectController edpc = loader.getController();
                                edpc.setProject(p);

                                Scene scene = new Scene(root);

                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();

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

        tableProjects.getColumns().add(colBtnDel);
    }

    private void emptyForm() {
        txtName.clear();
        txtRate.clear();
        comboboxClient.getSelectionModel().selectFirst();
    }
}
