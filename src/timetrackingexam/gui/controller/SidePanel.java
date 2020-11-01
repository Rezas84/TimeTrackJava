/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.gui.controller;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author domin
 */
public class SidePanel {

    SceneManager sm = new SceneManager();

    public void init(VBox vbox) {
        VBox btnvb = new VBox();
        btnvb.setSpacing(5);

        Label lb = new Label(LoggedUser.getInstance().name);
        lb.setAlignment(Pos.TOP_CENTER);

        //main page button
        JFXButton mp = new JFXButton("Main Page");
        mp.setMinWidth(160.0);
        mp.setOnAction((event) -> {
            sm.changeScene(event, "userMain");
        });

        //report page button
        JFXButton rp = new JFXButton("Reports");
        rp.setMinWidth(160.0);
        rp.setOnAction((event) -> {
            sm.changeScene(event, "adminReports");
        });

        //project page button
        JFXButton pp = new JFXButton("Projects");
        pp.setMinWidth(160.0);
        pp.setOnAction((event) -> {
            sm.changeScene(event, "adminNewEditProject");
        });

        //client page button
        JFXButton cp = new JFXButton("Clients");
        cp.setMinWidth(160.0);
        cp.setOnAction((event) -> {
            sm.changeScene(event, "adminNewEditClient");
        });

        //user page button
        JFXButton up = new JFXButton("Users");
        up.setMinWidth(160.0);
        up.setOnAction((event) -> {
            sm.changeScene(event, "adminNewEditUser");
        });

        //set password button
        JFXButton np = new JFXButton("Password");
        np.setMinWidth(160.0);
        np.setOnAction((event) -> {
            sm.changeScene(event, "editPassword");
        });

        //logout button
        JFXButton lo = new JFXButton("Log out");
        lo.setMinWidth(160.0);
        lo.setOnAction((event) -> {
            sm.logOut(event);
        });

        //user side panel
        if (LoggedUser.getInstance().rights == 1) {
            lb.setMinHeight(400.0);

            btnvb.setAlignment(Pos.BOTTOM_CENTER);
            btnvb.getChildren().addAll(mp, np, lo);

        //admin side panel    
        } else {
            lb.setMinHeight(200.0);

            btnvb.setAlignment(Pos.BOTTOM_CENTER);
            btnvb.getChildren().addAll(mp, rp, pp, cp, up, np, lo);
        }

        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.getChildren().addAll(lb, btnvb);
    }
}
