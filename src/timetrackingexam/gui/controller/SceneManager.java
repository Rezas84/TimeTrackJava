/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author domin
 */
public class SceneManager {

    HashMap<String, String> scenes = new HashMap<>();

    public SceneManager() {
        scenes.put("userMain", "/timetrackingexam/gui/view/UserMain.fxml");
        scenes.put("adminMain", "/timetrackingexam/gui/view/AdminMain.fxml");
        scenes.put("adminNewEditClient", "/timetrackingexam/gui/view/AdminNewEditClient.fxml");
        scenes.put("adminNewEditProject", "/timetrackingexam/gui/view/AdminNewEditProject.fxml");
        scenes.put("adminNewEditUser", "/timetrackingexam/gui/view/AdminNewEditUser.fxml");
        scenes.put("adminReports", "/timetrackingexam/gui/view/AdminReports.fxml");
        scenes.put("loginscr", "/timetrackingexam/gui/view/Loginscr.fxml");
        scenes.put("editPassword", "/timetrackingexam/gui/view/EditPassword.fxml");
    }

    public void changeScene(ActionEvent event, String sceneName) {
        FXMLLoader loader;
        Parent root;

        try {
            loader = new FXMLLoader(getClass().getResource(scenes.get(sceneName)));
            root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SceneManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logOut(ActionEvent event) {
        FXMLLoader loader;
        Parent root;

        try {
            LoggedUser.removeInstance();
            loader = new FXMLLoader(getClass().getResource(scenes.get("loginscr")));
            root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SceneManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
