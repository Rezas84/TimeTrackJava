/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author narma
 */
public class TimeCounter {

    // The creation of this class and also the solution for time tracking with thread was inspired by the compulsory Imageviewer app for DBOS.
    private int totalseconds = 0;
    private int seconds = 1;
    private int minutes = 0;
    private int hours = 0;
    private Label time;
    private String lblhours;
    private String lblminutes;
    private String lblseconds;

    public TimeCounter(Label times, int totalseconds) {
        this.time = times;

        this.totalseconds = totalseconds;
    }

    private ScheduledExecutorService absenceThreadExecutor;
    // counts up in every second 
    public void counter() {
        absenceThreadExecutor = Executors.newSingleThreadScheduledExecutor();
        absenceThreadExecutor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                // The selection solves the problem of splitting up the time into hours, minutes, seconds
                if (seconds < 10) {
                    lblseconds = ":0" + seconds;
                } else {
                    lblseconds = ":" + seconds;
                }
                if (minutes < 10) {
                    lblminutes = ":0" + minutes;
                } else {
                    lblminutes = ":" + minutes;
                }
                if (hours < 10) {
                    lblhours = "0" + hours;
                } else {
                    lblhours = "" + hours;
                }
                String stringero = lblhours + lblminutes + lblseconds;
                time.setText(stringero);
                seconds++;
                totalseconds++;
                if (seconds >= 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes >= 60) {
                    seconds = 0;
                    minutes = 0;
                    hours++;
                }
            });
        }, 0, 1, TimeUnit.SECONDS);
    }
    // Shuts down the thread and resets the counter values to 0
    public int stopCounter() {
        absenceThreadExecutor.shutdown();
        seconds = 0;
        minutes = 0;
        hours = 0;
        int total = totalseconds;
        totalseconds = 0;
        return total;

    }
}
