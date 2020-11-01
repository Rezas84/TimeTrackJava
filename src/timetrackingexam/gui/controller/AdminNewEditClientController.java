/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import timetrackingexam.be.Client;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;

/**
 * FXML Controller class
 *
 * @author narma
 */
public class AdminNewEditClientController implements Initializable {

    @FXML
    private JFXButton btnMainPage;
    @FXML
    private JFXButton btnReports;
    @FXML
    private JFXButton BtnNewEditProject;
    @FXML
    private JFXButton BtnNewEditUser;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnAddClient;
    @FXML
    private JFXTextField txtClientName;
    @FXML
    private JFXTextField txtClientRate;
    @FXML
    private Label labelError;
    @FXML
    private TableView<Client> tableviewClient;
    @FXML
    private TableColumn<Client, String> ColumClientName;
    @FXML
    private TableColumn<Client, Integer> columnRate;

    SceneManager sm = new SceneManager();
    IFacade bll = new BllFacade();
    private ObservableList<Client> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableViewClient();
        addEditButtonToTable();
        addDeleteButtonToTable();
    }

    private void initTableViewClient() {
       ObservableList<Client> data = FXCollections.observableList(bll.getAllClient());

        tableviewClient.setItems(data);
        ColumClientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

    }

    @FXML
    private void btnMainPageAction(ActionEvent event) {
        sm.changeScene(event, "adminMain");
    }

    @FXML
    private void btnReportsAction(ActionEvent event) {
        sm.changeScene(event, "adminReports");
    }

    @FXML
    private void BtnNewEditProjectAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditProject");
    }

    @FXML
    private void BtnNewEditUserAction(ActionEvent event) {
        sm.changeScene(event, "adminNewEditUser");
    }

    @FXML
    private void btnLogoutAction(ActionEvent event) {
        sm.logOut(event);
    }

    @FXML
    private void btnAddClientAction(ActionEvent event) {
        if (labeIsNotEmpty()) {
            String name = txtClientName.getText();
            int rate = Integer.parseInt(txtClientRate.getText());
            bll.addNewClient(rate, name);
            txtClientName.setText("");
            txtClientRate.setText("");
            initTableViewClient();

        }

    }

    private boolean labeIsNotEmpty() {
        if (txtClientName.getText() == null || txtClientName.getText().trim().isEmpty()) {
            labelError.setText("Please enter the Client Name");
            return false;
        } else if (txtClientRate.getText() == null || txtClientRate.getText().trim().isEmpty()) {
            labelError.setText("Please enter the Rate");
            return false;
        }
        return true;
    }

    private void addDeleteButtonToTable() {
        TableColumn<Client, Void> colBtnDel = new TableColumn("Delete");

        Callback<TableColumn<Client, Void>, TableCell<Client, Void>> cellFactory = new Callback<TableColumn<Client, Void>, TableCell<Client, Void>>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                final TableCell<Client, Void> cell = new TableCell<Client, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Client client = getTableView().getItems().get(getIndex());
                            bll.deleteClient(client);
                            initTableViewClient();

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

        tableviewClient.getColumns().add(colBtnDel);
    }

    private void addEditButtonToTable() {
        TableColumn<Client, Void> colBtnEdit = new TableColumn("Edit");

        Callback<TableColumn<Client, Void>, TableCell<Client, Void>> cellFactory = new Callback<TableColumn<Client, Void>, TableCell<Client, Void>>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                final TableCell<Client, Void> cell = new TableCell<Client, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try {
                                Client client = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + client.getName());
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timetrackingexam/gui/view/EditClient.fxml"));
                                Parent root = loader.load();
                                Stage stage = new Stage();

                                EditClientController emc = loader.getController();
                                emc.acceptData(client);

                                Scene scene = new Scene(root);

                                stage.setScene(scene);
                                stage.show();

                                stage.setOnHiding(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        init();
                                    }
                                });
                            } catch (IOException ex) {
                                Logger.getLogger(AdminNewEditClientController.class.getName()).log(Level.SEVERE, null, ex);
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

        colBtnEdit.setCellFactory(cellFactory);

        tableviewClient.getColumns().add(colBtnEdit);

    }

    public void init() {
        data = FXCollections.observableArrayList(bll.getAllClient());
        /* categoryList.setItems(obsCategories);
        categoryList.getSelectionModel().select(0);
        obsMovie = FXCollections.observableArrayList(categoryList.getSelectionModel().getSelectedItem().getAllMovies());*/
        tableviewClient.setItems(data);

    }
}
