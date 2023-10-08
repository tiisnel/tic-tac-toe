package mnk.controls;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ClockTime extends VBox{ // new game time control selection area
    private final Spinner<Integer> timeSpinner;
    private int min = 0;
    private int max = 6000;
    private int def = 300; // 5 min default
    private int step= 10;
    public Integer value;
    public ClockTime(Label label){
        super();
        setPrefWidth(100);
        timeSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, def, step);
        timeSpinner.setValueFactory(timeValueFactory);
        timeValueFactory.setWrapAround(true);
        timeSpinner.getEditor().setPrefWidth(100);
        timeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateText();
        });

        updateText();
        getChildren().addAll(label, timeSpinner);
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setDef(int def)
    {
        timeSpinner.getValueFactory().setValue(def);
        this.def = def;
    }

    public void setStep(int step) {
        this.step = step;
    }

    private void updateText() {
        value = timeSpinner.getValue();
        if (value != null) {
            int minutes = value / 60;
            int seconds = value % 60;
            String formattedTime = String.format("%02d:%02d", minutes, seconds);
            timeSpinner.getEditor().setText(formattedTime);

        }
    }
}
