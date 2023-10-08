package mnk;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.abs;

public class Opponent {
    Main page;
    Square[] markup;
    AI opponent;
    HashMap<Square, Double> potential = new HashMap<Square, Double>();
    int score;

    public Opponent(Main page, String type){
        this.page =page;
        markup = new Square[page.won];

        switch (type){
            case "Human":
                opponent = new Human();
                break;
            case "Random":
                opponent = new Aimless(); //using synonym to avoid confusion with Java build-in random function
                break;
            case "Simple":
                opponent = new Simple();
                break;
            default:
                System.out.println(type + " Opponent not implemented yet, defaulting to human opponent");
                opponent = new Human();
                break;

        }
}


    public interface AI {

        double evaluate(Square move);
    }
    public Boolean won(int x, int y, boolean real){

        String player = page.move;

        int j,k;
        boolean test;
        markup[0]=page.board[x][y];
        score =1; // start with last move made: guaranteed to be non-empty

        for (k = x-1; k>=0;k--) { //left-
            test = scores(page.board[k][y], player, score);
            if(!test) break;
        }
        for (k = x+1; k<page.rows;k++){//-right
            test = scores(page.board[k][y], player, score);
            if(!test) break;
        }

        if(score == page.won){
            if(real) paint();
            return true;
        }
        score = 1;

        for (k = y-1; k>=0;k--) {//down-
            test = scores(page.board[x][k], player, score);
            if(!test) break;
        }
        for (k = y+1; k<page.columns;k++){//-up
            test = scores(page.board[x][k], player, score);
            if(!test) break;
        }
        if(score == page.won){
            if(real) paint();
            return true;
        }
        score =1;

        for (k=x+1, j=y+1;k< page.rows && j< page.columns; k++, j++){
            test = scores(page.board[k][j],player,score);
            if(!test) break;
        }
        for (k=x-1, j=y-1;k>=0 && j>=0; k--, j--){
            test = scores(page.board[k][j],player,score);
            if(!test) break;
        }
        if(score == page.won){
            if(real) paint();
            return true;
        }
        score =1;

        for (k=x-1, j=y+1;k>=0 && j< page.columns; k--, j++){
            test = scores(page.board[k][j],player,score);
            if(!test) break;
        }
        for (k=x+1, j=y-1;k<page.rows && j>=0; k++, j--){
            test = scores(page.board[k][j],player,score);
            if(!test) break;
        }
        if(score == page.won){
            if(real) paint();
            return true;
        }
        score =1;

        return false;
    }
    public boolean scores(Square square, String player, int index){
        if(!square.getText().equals(player)) return false;
        this.score++;
        if(score>page.won)return false;
        markup[index]=square;
        return true;
    }

    public void paint(){ //marks winning sequence
        String player = page.move;
        Background background = null;
        BackgroundFill backgroundFill = page.move.equals("X") ?
                new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY) :
                new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
        background = new Background(backgroundFill);
            for(Square square : markup){
                square.setBackground(background);
        }
            page.gameOver(player + " won");
    }

    public void find() { //finds all candidate moves
        if(opponent.getClass().getName().equals("mnk.Opponent$Human"))return; // human makes his own moves :)
        potential.clear();

        for (Square[] squares : page.board) {
            for (Square choice : squares){
                if(!choice.isDisabled()) potential.put(choice, opponent.evaluate(choice));
            }
        }
        make();

    }
    public void make(){ // makes random move based on calculated values
                        //if engine wants to be 'perfect', it should return 0 for never to be played moves
        double sum = potential.values().stream().mapToDouble(Double::doubleValue).sum();
        //System.out.println(potential.keySet());
        Random random = new Random();
        double randomSquare = random.nextDouble(sum);
        int chosen =0;

        for(Square key : potential.keySet()){
            double value = potential.get(key);
            chosen+=value;
            if(randomSquare<=chosen){
                key.click();
                break;
            }
        }
    }
    public class Human implements AI {

        @Override
        public double evaluate(Square move) {
            return 0;
        } // never called
    }
    public class Aimless implements AI{ // Just Random

        @Override
        public double evaluate(Square move) {// random gives equal changes to all moves
            return 1;
        }
    }
    public class Simple implements AI{

        @Override
        public double evaluate(Square move) {
            double score = abs(page.columns/2- move.y) + abs(page.rows/2-move.x);
            score = page.columns + page.rows -score;//reverse so that central squares gave higher value

            if(won(move.x, move.y, false)) score +=50;
            page.move = page.move.equals("X") ? "0" : "X";
            if(won(move.x, move.y, false)) score +=25;
            page.move = page.move.equals("X") ? "0" : "X";

            return score;
        }
    }

}

