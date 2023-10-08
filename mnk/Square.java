package mnk;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mnk.controls.Player;

import static javafx.scene.paint.Color.*;

public class Square extends Button {
    int x, y;
    Main page;
    Square(int x, int y, Main page){ //init
        this.x=x;
        this.y=y;
        this.page=page;

        this.setStyle("-fx-border-color: black");
        this.setPrefSize(5000,5000);
        this.widthProperty().addListener((obs, oldWidth, newWidth) -> fontSize());
        this.heightProperty().addListener((obs, oldHeight, newHeight) -> fontSize());


        this.setOnMouseClicked(e -> click());
    }
    private void fontSize(){
        double fontSize = Math.min(this.getWidth(), this.getHeight()) * 0.3;
        this.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,fontSize));

    }
    public void click(){
        Color paint;
        Opponent current;
        page.move = page.move.equals("X") ? "0" : "X";

        this.setText(page.move);
        if(page.move.equals("X")){
            paint =GREEN;
            current =page.xPlayer;
        }
        else{
            paint = RED;
            current = page.oPlayer;
        }
        this.setTextFill(paint);
        if (current.won(x,y, true)) return;

        this.setDisable(true);
        page.newMove(current); //moves counter is located on main page/application

    }
}
