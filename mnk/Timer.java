package mnk;

import javafx.scene.control.Label;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Timer {

    public Label clock;
    private long startTime;
    private long elapsedTime;
    private ScheduledExecutorService executorService;
    private boolean isRunning = false;
    private Integer timeControl;
    private long timeRemaining;

    private final Main game;
    public void setTimeControl(Integer timeControl) {
        this.timeControl = timeControl;
    }



    public Timer (Main game) {
        this.game = game;
        clock = new Label("00:00:000");
        clock.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
        this.elapsedTime = 0;
}
    public void flipTimer() {
        if(isRunning) stopTimer();
        else {
            startTime = System.currentTimeMillis() - elapsedTime;
            isRunning = true;
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(this::updateTimer, 0, 1, TimeUnit.MILLISECONDS);
        }
    }
    public void resetTimer(){ //assuming that timer os stopped
        startTime = System.currentTimeMillis();
        updateTimer();
    }

    public void stopTimer() {

        elapsedTime = System.currentTimeMillis() - startTime;

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            isRunning = false;
        }
    }

    private void updateTimer() {
        elapsedTime = System.currentTimeMillis() - startTime;
        Platform.runLater(() -> {
            timeRemaining = timeControl-elapsedTime;
            if(timeRemaining<=0) game.gameOver("Out of time! " +game.move + " lost!");
            String formattedTime = formatTime(timeRemaining); // system time to human-readable time
            clock.setText(formattedTime);
        });
    }
    private String formatTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        long millis = milliseconds % 1000;

        DecimalFormat format = new DecimalFormat("00");
        return String.format("%s:%s:%03d", format.format(minutes), format.format(seconds), millis);
    }

}








