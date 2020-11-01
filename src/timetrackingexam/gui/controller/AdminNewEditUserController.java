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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetrackingexam.be.Role;
import timetrackingexam.be.User;
import timetrackingexam.gui.model.FacadeModel;
import timetrackingexam.help.Hash;
import timetrackingexam.help.Validator;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminNewEditUserController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXComboBox<Role> comboboxRole;
    @FXML
    private JFXTextField txtPassword;
    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableColumn<User, String> columnName;
    @FXML
    private TableColumn<User, String> columnEmail;
    @FXML
    private SplitPane splitpane;
    @FXML
    private VBox panelVbox;

    FacadeModel model;
    SceneManager sm = new SceneManager();
    Hash hash = new Hash();
    SidePanel sp = new SidePanel();
    ArrayList<User> users = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp.init(panelVbox);
        model = FacadeModel.getInstance();
        comboboxRole.getItems().add(new Role(1, "Developer"));
        comboboxRole.getItems().add(new Role(2, "Admin"));
        comboboxRole.getSelectionModel().selectFirst();

        users = model.getAllUsers();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        addEditButtonToTable();
        addDeleteButtonToTable();

        tableUsers.setItems(FXCollections.observableList(users));
    }

    @FXML
    private void addNewUser(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("New User");

        if (txtName.getText().isEmpty() || txtName.getText() == null) {
            alert.setHeaderText("Name field cannot be empty");

            alert.showAndWait();
        } else if (txtEmail.getText().isEmpty() || txtEmail.getText() == null) {
            alert.setHeaderText("Email field cannot be empty");

            alert.showAndWait();
        } else if (txtPassword.getText().isEmpty() || txtPassword.getText() == null) {
            alert.setHeaderText("Password field cannot be empty");

            alert.showAndWait();
        } else if (!Validator.validateEmail(txtEmail.getText())) {
            alert.setHeaderText("Invalid email");

            alert.showAndWait();
        } else {
            User newUser = model.addNewUser(txtName.getText(),
                    txtEmail.getText(),
                    comboboxRole.getSelectionModel().getSelectedItem().getId(),
                    hash.hashPass(txtPassword.getText()));

            users.add(newUser);
            tableUsers.setItems(FXCollections.observableList(users));
            emptyForm();
        }
    }

    private void addDeleteButtonToTable() {
        TableColumn<User, Void> colBtnDel = new TableColumn("Delete");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            User u = getTableView().getItems().get(getIndex());
                            model.deleteUser(u.getId());
                            users.remove(u);
                            tableUsers.setItems(FXCollections.observableList(users));
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

        tableUsers.getColumns().add(colBtnDel);
    }

    private void addEditButtonToTable() {
        TableColumn<User, Void> colBtnDel = new TableColumn("Edit");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetrackingexam/gui/view/EditUser.fxml"));
                                Parent root = loader.load();

                                EditUserController euc = loader.getController();
                                euc.setUser(getTableView().getItems().get(getIndex()));

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

        tableUsers.getColumns().add(colBtnDel);
    }

    private void emptyForm() {
        txtName.clear();
        txtEmail.clear();
        txtPassword.setText("pass");
    }
}
