/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import timetrackingexam.be.Client;
import timetrackingexam.bll.BllFacade;
import timetrackingexam.bll.IFacade;

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
    Client client;
    IFacade bll = new BllFacade();
    private ObservableList<Client> data = FXCollections.observableList(bll.getAllClient());
    @FXML
    private Label labelErrorEditClient;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnEditClientPanelaction(ActionEvent event) {
        if (labeIsNotEmpty()) {
            client.setName(txtClientEditNamePanel.getText());
            client.setRate(Integer.parseInt(txtClientEditRatePanel.getText()));
            bll.editClient(client);
            cancelAction(event);
        }
    }

    public void acceptData(Client client) {
        this.client = client;
        txtClientEditNamePanel.setText(this.client.getName());
        txtClientEditRatePanel.setText(String.valueOf(this.client.getRate()));
    }

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
