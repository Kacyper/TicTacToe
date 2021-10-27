package com.tictactoe.tictactoe;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class TicTacToe extends Application implements Serializable {

    private final Image imageback = new Image("file:src/main/resources/com/tictactoe/tic_tac_toe/background.jpg");
    private final ImageView board = new ImageView("file:src/main/resources/com/tictactoe/tic_tac_toe/board.png");
    private final Image cross = new Image("file:src/main/resources/com/tictactoe/tic_tac_toe/Cross.png");
    private final Image circle = new Image("file:src/main/resources/com/tictactoe/tic_tac_toe/Circle.png");

    private final List<Button> buttons = new LinkedList<>();
    private final List<Button> controlButtons = new LinkedList<>();
    private final List<RadioButton> difficultyLevel = new LinkedList<>();
    private final List<Integer> emptyButtons = new LinkedList<>();
    private final List<Line> lines = new LinkedList<>();

    private boolean Turn = true;
    private final Random random = new Random();
    private int difficulty = 0;


    private final Label label = new Label();


    private final Button newGame = new Button("New Game");
    private final Button saveGame = new Button("Save Game");
    private final Button loadGame = new Button("Load Game");


    private final RadioButton easy = new RadioButton("Easy");
    private final RadioButton hard = new RadioButton("Hard");

    private final ToggleGroup toggleDifficulty = new ToggleGroup();
    private final HBox bottomButtonBar = new HBox();
    private final HBox downButtonBar = new HBox();

    private final GridPane grid = new GridPane();
    private final Pane pane = new Pane();

    private static final String O = "o";
    private static final String X = "x";
    private static final String Win = "Congratulations, you win!";
    private static final String Lose = "Bummer!\nTry again!";


    private int lastMove;
    private int lastComputerMove;
    private int lastPlayerMove;
    private int computerMove;
    private final int[] winningLine = new int[3];



    private final Line leftToRight = new Line(1.0f, 1.0f, 350.0f, 350.0f);
    private final Line rightToLeft = new Line(1.0f, 350.0f, 350.0f, 1.0f);
    private final Line topLine = new Line(1.0f, 70.0f, 400.0f, 70f);
    private final Line middleLine = new Line(1.0f, 190.0f, 400.0f, 190.0f);
    private final Line bottomLine = new Line(1.0f, 330.0f, 400.0f, 330.0f);
    private final Line leftTopDown = new Line(80.0f, 1.0f, 40.0f, 400.0f);
    private final Line middleTopDown = new Line(190.0f, 1.0f, 140.0f, 400.0f);
    private final Line rightTopDown = new Line(330.0f, 1.0f, 280.0f, 400.0f);


    private boolean checkThree(int index1, int index2, int index3) {
        if(buttons.get(index1).isDisabled() && buttons.get(index2).isDisabled() && buttons.get(index3).isDisabled() && (buttons.get(index1).getId()).equals(buttons.get(index2).getId()) && buttons.get(index1).getId().equals(buttons.get(index3).getId())) {
            winningLine[0] = index1;
            winningLine[1] = index2;
            winningLine[2] = index3;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTwo(int index1, int index2, int index3) {
        if((buttons.get(index1).getId()).equals(buttons.get(index2).getId()) && !buttons.get(index3).isDisabled()) {
            computerMove = index3;
            return true;
        } else if(buttons.get(index1).getId().equals(buttons.get(index3).getId()) && !buttons.get(index2).isDisabled()) {
            computerMove = index2;
            return true;
        } else {
            return false;
        }
    }

    public boolean ThreeInARow() {
        if(emptyButtons.size()<5) {
            return switch (lastMove) {
                case 0 -> (checkThree(0, 1, 2) || checkThree(0, 3, 6) || checkThree(0, 4, 8));
                case 1 -> (checkThree(0, 1, 2) || checkThree(1, 4, 7));
                case 2 -> (checkThree(0, 1, 2) || checkThree(2, 4, 6) || checkThree(2, 5, 8));
                case 3 -> (checkThree(3, 4, 5) || checkThree(0, 3, 6));
                case 4 -> (checkThree(3, 4, 5) || checkThree(2, 4, 6) || checkThree(1, 4, 7) || checkThree(0, 4, 8));
                case 5 -> (checkThree(3, 4, 5) || checkThree(2, 5, 8));
                case 6 -> (checkThree(6, 7, 8) || checkThree(2, 4, 6) || checkThree(0, 3, 6));
                case 7 -> (checkThree(6, 7, 8) || checkThree(1, 4, 7));
                case 8 -> (checkThree(6, 7, 8) || checkThree(2, 5, 8) || checkThree(0, 4, 8));
                default -> false;
            };
        } else {
            return false;
        }
    }

    public boolean TwoInARow(int move) {
        if(emptyButtons.size()<7) {
            return switch (move) {
                case 0 -> checkTwo(0, 1, 2) || checkTwo(0, 3, 6) || checkTwo(0, 4, 8); //1st Row 1st Col 1st Sla
                case 1 -> checkTwo(1, 0, 2) || checkTwo(1, 4, 7); // 1st Row 2nd Col
                case 2 -> checkTwo(2, 0, 1) || checkTwo(2, 4, 6) || checkTwo(2, 5, 8); // 1st Row 1st Sla 3rd Col
                case 3 -> checkTwo(3, 4, 5) || checkTwo(3, 0, 6); //2nd Row 1st Col
                case 4 -> checkTwo(4, 3, 5) || checkTwo(4, 2, 6) || checkTwo(4, 1, 7) || checkTwo(4, 0, 8); //2nd Row 1st Sla 2nd Col 2nd Sla
                case 5 -> checkTwo(5, 3, 4) || checkTwo(5, 2, 8); // 2nd Row 3rd Col
                case 6 -> checkTwo(6, 7, 8) || checkTwo(6, 2, 4) || checkTwo(6, 0, 3); //3rd Row 2nd Sla 1st Col
                case 7 -> checkTwo(7, 6, 8) || checkTwo(7, 1, 4); // 3rd Row 2nd Col
                case 8 -> checkTwo(8, 6, 7) || checkTwo(8, 2, 5) || checkTwo(8, 0, 4); //3rd Row 3rd Col 1st Sla
                default -> false;
            };
        } return false;
    }

    public void randomMove() {
        int jump = emptyButtons.size();
        int n = random.nextInt(jump);
        computerMove = emptyButtons.get(n);
        buttons.get(computerMove).fire();
    }

    public void computerTurn(int difficulty) {
        if (difficulty == 1) {
            if (TwoInARow(lastComputerMove)) {
                buttons.get(computerMove).fire();
            } else if (TwoInARow(lastPlayerMove)) {
                buttons.get(computerMove).fire();
            } else if (!buttons.get(0).isDisabled()) {
                buttons.get(0).fire();
            } else if (!buttons.get(1).isDisabled()) {
                buttons.get(1).fire();
            } else if (!buttons.get(2).isDisabled()) {
                buttons.get(2).fire();
            } else if (!buttons.get(3).isDisabled()) {
                buttons.get(3).fire();
            } else if (!buttons.get(4).isDisabled()) {
                buttons.get(4).fire();
            } else if (!buttons.get(5).isDisabled()) {
                buttons.get(5).fire();
            } else if (!buttons.get(6).isDisabled()) {
                buttons.get(6).fire();
            } else if (!buttons.get(7).isDisabled()) {
                buttons.get(7).fire();
            } else if (!buttons.get(8).isDisabled()) {
                buttons.get(8).fire();
            }
        } else {
            if (!TwoInARow(lastComputerMove)) {
                randomMove();
            }
            buttons.get(computerMove).fire();
        }
    }

    public boolean isBoardFull() {
        return emptyButtons.isEmpty();
    }


    public void drawLine(int [] table) {
        if(table[0] == 0) {
            if(table[1] == 1) {
                pane.getChildren().add(topLine);
            } else if(table[1] == 3) {
                pane.getChildren().add(leftTopDown);
            } else if(table[1] == 4) {
                pane.getChildren().add(leftToRight);
            }
        } else if(table[0] == 1) {
            pane.getChildren().add(middleTopDown);
        } else if(table[0] == 2) {
            if (table[1] == 5) {
                pane.getChildren().add(rightTopDown);
            } else if(table[1] == 4) {
                pane.getChildren().add(rightToLeft);
            }
        } else if(table[0] == 3) {
            pane.getChildren().add(middleLine);
        } else if(table[0] == 6){
            pane.getChildren().add(bottomLine);
        }
    }

    public void setEmptyButtons() {
        for(int i=0; i<9; i++) {
            emptyButtons.add(i);
        }
    }

    public void disableBoard() {
        for(Button button:buttons) {
            button.setDisable(true);
        }
    }


    public void newGame() {
        for(Button button:buttons) {
            button.setGraphic(null);
            button.setDisable(false);
            button.setId("");
        }
        pane.getChildren().removeAll(lines);
        emptyButtons.clear();
        setEmptyButtons();
        label.setText("");
        Turn = true;

    }

    public void gameplayCross(Button button) {
        button.setGraphic(new ImageView(cross));
        button.setId(X);
        lastMove = lastPlayerMove = buttons.indexOf(button);
        emptyButtons.remove((Integer) lastMove);
        button.setDisable(true);
        if (ThreeInARow()) {
            label.setText(Win);
            drawLine(winningLine);
            disableBoard();
        } else {
            if (isBoardFull()) {
                label.setText("It's a tie!");
                disableBoard();
            } else {
                Turn = !Turn;
                computerTurn(difficulty);
            }
        }
    }

    public void gameplayCircle(Button button) {
        button.setGraphic(new ImageView(circle));
        button.setId(O);
        lastMove = lastComputerMove = buttons.indexOf(button);
        emptyButtons.remove((Integer) lastMove);
        button.setDisable(true);
        if (ThreeInARow()) {
            label.setText(Lose);
            drawLine(winningLine);
            disableBoard();
        } else {
            Turn = !Turn;
        }
    }

    public void saveGame() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/com/tictactoe/tic_tac_toe/save.text"));

            for(Button button:buttons) {
                writer.write(button.getId());
                writer.newLine();
            }
            writer.write(difficulty + "");
            writer.newLine();
            writer.write(lastComputerMove + "");
            writer.newLine();
            writer.write(lastPlayerMove + "");
            writer.newLine();
            writer.write(lastMove + "");
            writer.newLine();
            writer.close();
        } catch(Exception e) {
            System.out.println("Cannot save " + e);
        }
    }

    public void loadGame() {
        Path file = Paths.get("src/main/resources/com/tictactoe/tic_tac_toe/save.text");

        try (Stream<String> stream = Files.lines(file)) {

            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        Collections.addAll(lines,leftToRight, rightToLeft, topLine, middleLine, bottomLine, leftTopDown, middleTopDown, rightTopDown);
        Collections.addAll(controlButtons,newGame, saveGame, loadGame);
        easy.setSelected(true);
        setEmptyButtons();
        Collections.addAll(difficultyLevel, easy, hard);


        label.setTextFill(Color.rgb(41, 73, 115, 1.0));
        label.setFont(new Font("Helvetica", 22));
        label.setPadding(new Insets(0.0, 0.0, 10.0, 20.0));


        for(Button controlButton:controlButtons) {
            controlButton.setTextFill(Color.rgb(41, 73, 115));
            controlButton.setBackground(null);
            controlButton.setOnMouseEntered(event -> {
                controlButton.setTextFill(Color.rgb(42, 75, 140, 1.0));
                controlButton.setBorder(new Border(new BorderStroke(Color.rgb(42, 75, 140, 1.0), BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(2.0,2.0,2.0,2.0))));
            });
            controlButton.setOnMouseExited(event -> {
                controlButton.setTextFill(Color.rgb(41, 73, 115));
                controlButton.setBorder(new Border(new BorderStroke(Color.rgb(42, 75, 140), BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(2.5,2.5,2.5,2.5))));
            });
            controlButton.setFont(new Font("Helvetica", 20));
            controlButton.setBorder(new Border(new BorderStroke(Color.rgb(42, 75, 140), BorderStrokeStyle.DOTTED, new CornerRadii(10.0), new BorderWidths(2.0,2.0,2.0,2.0))));
            HBox.setMargin(controlButton, new Insets(5.0, 70.0, 5.0, 70.0));
        }

        newGame.setOnAction(event -> newGame());
        saveGame.setOnAction(event -> saveGame());
        loadGame.setOnAction(event -> loadGame());

        board.setOpacity(1.0);

        for(RadioButton radioButton:difficultyLevel) {
            radioButton.setToggleGroup(toggleDifficulty);
            radioButton.setBackground(null);
            radioButton.setTextFill(Color.rgb(41, 73, 115));
            radioButton.setFont(new Font("Helvetica", 18));
            HBox.setMargin(radioButton, new Insets(100.0, 5.0, 10.0, 5.0));
            radioButton.setOnMouseEntered(event -> radioButton.setTextFill(Color.rgb(42, 75, 140)));
            radioButton.setOnMouseExited(event -> radioButton.setTextFill(Color.rgb(41, 73, 115)));
            radioButton.setOnMousePressed(event -> radioButton.setTextFill(Color.rgb(242, 242, 242)));
        }
        
        easy.setOnMouseClicked(event -> difficulty = 0);
        hard.setOnMouseClicked(event -> difficulty = 1);

        bottomButtonBar.getChildren().addAll(difficultyLevel);
        bottomButtonBar.setPadding(new Insets(0.0, 100.0, 0.0, 100.0));

        downButtonBar.getChildren().addAll(controlButtons);
        downButtonBar.setPadding(new Insets(20.0, 0.0, 5.0, 0.0));



        grid.setAlignment(Pos.TOP_CENTER);
        grid.setGridLinesVisible(false);
        grid.setBackground(background);
        grid.setAlignment(Pos.CENTER);
        grid.add(pane, 0, 1, 3, 3);
        grid.add(board,0, 1, 3, 3);
        grid.add(label, 3, 1, 1, 1);
        grid.add(bottomButtonBar, 3,2, 1, 1);
        grid.add(downButtonBar,3,3, 1, 1);


        for(Line line:lines) {
            line.setStroke(Color.rgb(248, 20, 20));
            line.setStrokeWidth(3.0);
            line.setOpacity(0.5);
            line.setStrokeLineCap(StrokeLineCap.BUTT);
        }

        for(int i = 1; i < 4; i++) {
            for(int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setBackground(null);
                button.setOpacity(1.0);
                button.setPrefSize(120.0, 120.0);
                button.setOnAction(event -> {
                    if (Turn) {
                        gameplayCross(button);
                    } else {
                        gameplayCircle(button);
                    }
                });
                buttons.add(button);
                grid.add(button, j, i);
            }
        }


        Scene scene = new Scene(grid, 1200, 1200, Color.WHITE);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);



    }
    public static void main(String[] args) {
        launch(args);
    }
}
