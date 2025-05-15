package org.example.thegreatests.Models;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

public class GlobalChrono {

    private static final int START_TIME_SECONDS = 25 * 60;
    private static final int FIFTEEN_MINUTES = 15 * 60;

    private static GlobalChrono instance;

    private int timeLeft = START_TIME_SECONDS;
    private Runnable onTick;
    private ScheduledExecutorService scheduler;

    private GlobalChrono() {}

    public static GlobalChrono getInstance() {
        if (instance == null) {
            instance = new GlobalChrono();
        }
        return instance;
    }

    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) return;

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            timeLeft--;

            if (onTick != null) {
                // Exécute sur le thread JavaFX
                Platform.runLater(onTick);
            }

            if (timeLeft <= 0) stop();

        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    public void reset() {
        timeLeft = START_TIME_SECONDS;
    }

    public int getTimeLeftInSeconds() {
        return timeLeft;
    }

    public String getFormattedTime() {
        int min = timeLeft / 60;
        int sec = timeLeft % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public boolean isUnder15Minutes() {
        return timeLeft < FIFTEEN_MINUTES;
    }

    public void setOnTick(Runnable onTick) {
        this.onTick = onTick;
    }

    public boolean isRunning() {
        return scheduler != null && !scheduler.isShutdown();
    }
}