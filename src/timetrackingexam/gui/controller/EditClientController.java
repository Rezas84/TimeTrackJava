/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import timetrackingexam.be.Client;
import timetrackingexam.gui.model.FacadeModel;

/**
 * FXML Controller class
 *
 * @author saraf
 */
public class EditClientController implements Initializable {

    @FXML
    private JFXButton btnClientCancelPanel;
    @FXML
    private JFXButton btnEditClientPanel;
    @FXML
    private JFXTextField txtClientEditNamePanel;
    @FXML
    private JFXTextField txtClientEditRatePanel;
    @FXML
    private Label labelErrorEditClient;
    Client client;
    FacadeModel model;
    //private ObservableList<Client> data = FXCollections.observableList(model.getAllClient());
     ArrayList<Client> data = new ArrayList();
    /**
     * initialize the model.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = FacadeModel.getInstance();
    }
/**
 * 
 * @param event 
 */
    @FXML
    private void btnEditClientPanelaction(ActionEvent event) {
        if (labeIsNotEmpty()) {
            client.setName(txtClientEditNamePanel.getText());
            client.setRate(Integer.parseInt(txtClientEditRatePanel.getText()));
            model.editClient(client);
            cancelAction(event);
        }
    }
/**
 * set the rate and the name of the client .
 * @param client .Business entity of the client.
 */
    public void acceptData(Client client) {
        this.client = client;
        txtClientEditNamePanel.setText(this.client.getName());
        txtClientEditRatePanel.setText(String.valueOf(this.client.getRate()));
    }
/**
 * Action for the button to to cancel the panel.
 * @param event .Action Event
 */
    @FXML
    private void btnCancelClientPanelAction(ActionEvent event) {
        cancelAction(event);
    }

    private boolean labeIsNotEmpty() {
        if (txtClientEditNamePanel.getText() == null || txtClientEditNamePanel.getText().trim().isEmpty()) {
            labelErrorEditClient.setText("Please enter the Client Name");
            return false;
        } else if (txtClientEditRatePanel.getText() == null || txtClientEditRatePanel.getText().trim().isEmpty()) {
            labelErrorEditClient.setText("Please enter the Rate");
            return false;
        }
        return true;
    }
    private void cancelAction (ActionEvent event){
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
