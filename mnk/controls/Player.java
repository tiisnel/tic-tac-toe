package mnk.controls;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Player extends VBox {//choose players

    private String[] opponents = {"Human","Random","Simple"};
    private final ComboBox<String> choice;

    public Player(Label label){
        super();
        setPrefWidth(100);

        choice = new ComboBox(FXCollections.observableArrayList(opponents));
        choice.getSelectionModel().select(0);

        getChildren().addAll(label, choice);
    }
    public String value(){
        return choice.getValue();
    }

    public void setOpponent(String value){
        choice.getSelectionModel().select(value);
    }

}
