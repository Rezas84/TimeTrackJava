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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import timetrackingexam.be.Client;
import timetrackingexam.gui.model.FacadeModel;

/**
 * FXML Controller class
 *
 * @author Reza
 */
public class AdminNewEditClientController implements Initializable {

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
    @FXML
    private VBox panelVbox;

    SceneManager sm = new SceneManager();
    FacadeModel model;
    SidePanel sp = new SidePanel();
    private ObservableList<Client> data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp.init(panelVbox);
        model = FacadeModel.getInstance();
        initTableViewClient();
        addEditButtonToTable();
        addDeleteButtonToTable();
    }

    /**
     * Initializes the Table View of the ClientControler class.Set client's
     * rates and names columns.
     */
    private void initTableViewClient() {
        initTableView();
        ColumClientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnRate.setCellValueFactory(new PropertyValueFactory<>("rate"));

    }

    /**
     * Action for the button to add a new Client.
     *
     * @param event .Action Event
     */
    @FXML
    private void btnAddClientAction(ActionEvent event) {
        if (labeIsNotEmpty()) {
            String name = txtClientName.getText();
            int rate = Integer.parseInt(txtClientRate.getText());
            model.addNewClient(rate, name);
            txtClientName.setText("");
            txtClientRate.setText("");
            initTableViewClient();

        }

    }

    /**
     * Check validation of
     *
     * @return
     */
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

    /**
     * Add a Dynamic column and delete button for each Client.
     */
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
                            model.deleteClient(client);
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

    /**
     * Add a Dynamic column and Edit button for each Client.
     */
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
                                //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow()
                                Stage stage = new Stage();

                                EditClientController ecc = loader.getController();
                                ecc.acceptData(client);

                                Scene scene = new Scene(root);

                                stage.setScene(scene);
                                stage.show();

                                stage.setOnHiding(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        initTableView();
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

    /**
     * Initializes the Table View of the ClientControler class. Refresh the
     * table to update it.
     */
    public void initTableView() {
        data = FXCollections.observableArrayList(model.getAllClient());
        tableviewClient.setItems(data);

    }
}
