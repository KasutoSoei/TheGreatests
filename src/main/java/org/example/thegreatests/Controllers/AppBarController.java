package org.example.thegreatests.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.thegreatests.Models.GlobalChrono;

public class AppBarController {

    @FXML
    private Label timerLabel;

    @FXML
    private Button startButton;

    private boolean alreadyStarted = false;

    public void initialize() {
        GlobalChrono chrono = GlobalChrono.getInstance();

        chrono.setOnTick(() -> Platform.runLater(() -> {
            timerLabel.setText(chrono.getFormattedTime());

            if (chrono.getTimeLeftInSeconds() <= 0) {
                startButton.setDisable(false);
                alreadyStarted = false;
            }
        }));
    }

    @FXML
    private void handleStart() {
        if (!alreadyStarted) {
            GlobalChrono chrono = GlobalChrono.getInstance();
            chrono.reset();
            chrono.start();
            startButton.setDisable(true);
            alreadyStarted = true;
        }
    }
}