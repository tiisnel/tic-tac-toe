package mnk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import mnk.controls.ClockTime;
import mnk.controls.Player;
import mnk.controls.Size;


public class Main extends Application {

    int rows = 4, columns = 4, won =4; // program was meant for 4x4x4, but should theoretically support other sizes as well
    String move ="X";
    Square[][] board;
    int moves = -1;
    int maxMoves;
    public Timer xTime;
    public Timer oTime;
    Opponent xPlayer;
    Opponent oPlayer;
    private Size rowNr;
    private Size colNr;
    private Size winNr;
    private Player player1;
    private Player player2;
    private BorderPane root;
    private ClockTime timer;
    public boolean gameOver = false;
    Label label1 = new Label("X to move");
    Label label2 = new Label("O to move");
    boolean beginning = true;

    public Stage main;

    @Override
    public void start(Stage primaryStage) {
        this.main = primaryStage;
        VBox times = new VBox();
        this.xTime = new Timer(this);
        this.oTime = new Timer(this);
        Label x = new Label("X remaining time");
        Label o = new Label("O remaining time");

        times.getChildren().addAll(x, xTime.clock, o, oTime.clock);
        times.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(times); //could be stacked with 'untimed' label for casual games
        stackPane.setAlignment(Pos.CENTER);
        root = new BorderPane();

        StackPane turnInfo = new StackPane(label1, label2);
        root.setRight(stackPane);
        root.setBottom(turnInfo);
        label2.setVisible(true);
        label1.setVisible(false);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("MNK GAME");
        primaryStage.setOnCloseRequest(event->{ //to not let program hanging after GUI closes
            xTime.stopTimer();
            oTime.stopTimer();
        });
        primaryStage.setScene(scene);
        newGame();

        primaryStage.show();

    }
    public void newMove(Opponent current){
        moves++;
        label1.setVisible(!label1.isVisible()); // switch mover
        label2.setVisible(!label2.isVisible());

        xTime.flipTimer();
        oTime.flipTimer();

        if(moves ==maxMoves){
            gameOver("DRAW");
            return;
        }

        if(current!=null)current.find();

    }

    public void newGame(){
        Stage newGame = new Stage();
        newGame.setX(900); //out of way for (default sized) main board
        newGame.setY(200);

        rowNr = new Size(new Label("Select number of rows"));
        rowNr.setDef(rows);

        colNr = new Size(new Label("Select number of columns"));
        colNr.setDef(columns);

        winNr = new Size(new Label("How many for win"));
        winNr.setDef(won);

        HBox controls = new HBox(10);
        controls.getChildren().addAll( rowNr, colNr, winNr);

        String p1 = player1 !=null ? player1.value(): "Human";
        player1 = new Player(new Label("Choose X player"));
        player1.setOpponent(p1);
        String p2 = player2 != null ? player2.value() : "Human";

        player2 = new Player(new Label("Choose O player"));
        player2.setOpponent(p2);



        int deftime = timer !=null ? timer.value: 300;
        timer = new ClockTime(new Label("Time control"));
        timer.setDef(deftime);

        HBox settings =new HBox(30);
        settings.getChildren().addAll( player1, player2, timer);


        HBox buttons = new HBox();
        Button submit = new Button("Start new game");
        submit.setOnAction(e -> {
            newGame.close();
            xTime.resetTimer();
            oTime.resetTimer();
            if(!label1.isVisible()) oTime.flipTimer();
            else xTime.flipTimer();
            gameOver = false;
            gameSettings();

        });
        Button exit = new Button("Exit game");
        exit.setOnAction(e->{
            newGame.close();
            xTime.stopTimer();
            oTime.stopTimer();
            main.close();
            Platform.exit(); // to exit before first game was created

        });
        buttons.getChildren().addAll(submit, exit);
        VBox vbox = new VBox(30);
        vbox.getChildren().addAll( controls, settings, buttons);

        Scene scene = new Scene(vbox);
        newGame.setTitle("Game settings");
        newGame.initModality(Modality.APPLICATION_MODAL);
        newGame.setScene(scene);
        newGame.setOnCloseRequest(Event::consume);
        if(beginning){
            beginning=false;
            newGame.showAndWait(); // do not show empty clocks before time control has chosen
        }
        else{
            newGame.show(); // let processes finish to not hang gui
        }
    }
    private void gameSettings() {
        root.getChildren().remove(root.getCenter());
        rows = rowNr.value();
        columns = colNr.value();
        won = winNr.value();
        board = new Square[rows][columns];

        GridPane gameBoard = new GridPane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard.add(board[i][j]=new Square(i,j, this),i,j);
            }
        }
        root.setCenter(gameBoard);
        moves = -1;
        maxMoves =rows * columns;

        xPlayer = new Opponent(this, player1.value());
        oPlayer =new Opponent(this, player2.value());
        xTime.setTimeControl(timer.value*1000); //milliseconds to seconds
        oTime.setTimeControl(timer.value*1000);
        Opponent current = move.equals("X") ? xPlayer: oPlayer;
        newMove(current); // first "null-move" just sets up clocks


    }
    public void gameOver(String result){
        if(gameOver)return;//so that timeout timer couldn't end "multiple games" accidentally
        gameOver = true;
        main.show();

        System.out.println(result);
        xTime.stopTimer();
        oTime.stopTimer();
        newGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}