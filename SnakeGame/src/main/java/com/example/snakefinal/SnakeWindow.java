package com.example.snakefinal;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Builds and displays the actual gameplay screen (canvas + HUD) for either
 * 1 player or 2 player mode, and wires it back into the given Stage so we
 * can hop between the menu and the game without opening extra windows.
 */
public class SnakeWindow {

    public static void show(Stage stage, boolean twoPlayerMode) {

        int boardWidth = twoPlayerMode ? 1000 : 600;
        int boardHeight = 600;

        Canvas canvas = new Canvas(boardWidth, boardHeight);
        canvas.setFocusTraversable(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Label controlsLabel = new Label(
                twoPlayerMode
                        ? "PLAYER 1: WASD      PLAYER 2: Arrowkeys"
                        : "MOVE: WASD"
        );
        controlsLabel.getStyleClass().add("controls-label");

        VBox hud = new VBox(controlsLabel);
        hud.setAlignment(Pos.CENTER);
        hud.getStyleClass().add("game-hud");

        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.getStyleClass().add("game-canvas-holder");

        VBox root = new VBox(10, hud, canvasHolder);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("game-page");

        Scene scene = new Scene(root, boardWidth + 40, boardHeight + 100);
        String css = SnakeWindow.class.getResource("/snake.css") != null
                ? SnakeWindow.class.getResource("/snake.css").toExternalForm()
                : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        }

        SnakeAlgorithm snakeGame = new SnakeAlgorithm(boardWidth, boardHeight, twoPlayerMode);

        scene.setOnKeyPressed(e -> snakeGame.keyPressed(e.getCode()));

        snakeGame.draw(gc);
        snakeGame.init(gc, () -> Platform.runLater(() -> showGameOverDialog(stage, snakeGame, twoPlayerMode)));

        stage.setTitle(twoPlayerMode ? "Snake - 2 Player" : "Snake - 1 Player");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        canvas.requestFocus();
    }

    private static void showGameOverDialog(Stage stage, SnakeAlgorithm snakeGame, boolean twoPlayerMode) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over!");

        String content = twoPlayerMode
                ? "Player 1: " + snakeGame.getScore() + "    Player 2: " + snakeGame.getScore2()
                : "Score: " + snakeGame.getScore();
        alert.setContentText(content);

        ButtonType restart = new ButtonType("Restart");
        ButtonType mainMenu = new ButtonType("Main Menu");
        ButtonType quit = new ButtonType("Quit");

        alert.getButtonTypes().setAll(restart, mainMenu, quit);

        ButtonType result = alert.showAndWait().orElse(quit);

        if (result == restart) {
            snakeGame.restartGame();
        } else if (result == mainMenu) {
            new SnakeMenu().show(stage);
        } else {
            stage.close();
        }
    }
}
