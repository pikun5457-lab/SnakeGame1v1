package com.example.snakefinal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class SnakeAlgorithm {

    Random random;

    // define each block by 25 pixels of height and 25 pixels of width
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardHeight;
    int boardWidth;
    int tileSizes = 25;

    // whether this round is 1 player or 2 players
    private final boolean twoPlayerMode;

    Tile snakeHead;
    Tile snakeHead2;

    Tile food;
    ArrayList<Tile> snakeBody;
    ArrayList<Tile> snakeBody2;

    Timeline gameLoop;
    int velocityX;
    int velocityY;
    int velocityX1;
    int velocityY1;

    boolean gameOver = false;
    boolean snake1Dead = false;
    boolean snake2Dead = false;

    private GraphicsContext gc;
    private Runnable onGameOver;

    SnakeAlgorithm(int boardWidth, int boardHeight, boolean twoPlayerMode) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.twoPlayerMode = twoPlayerMode;

        snakeHead = new Tile(5, 5);
        snakeHead2 = new Tile(boardWidth / tileSizes - 10, 5);

        snakeBody = new ArrayList<Tile>();
        snakeBody2 = new ArrayList<Tile>();

        food = new Tile(0, 0);
        random = new Random();
        foodPlace();

        velocityX = 0;
        velocityY = 1;
        velocityX1 = 0;
        velocityY1 = 1;

        gameLoop = new Timeline(new KeyFrame(Duration.millis(100), e -> tick()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    public boolean isTwoPlayerMode() {
        return twoPlayerMode;
    }

    public void init(GraphicsContext gc, Runnable onGameOver) {
        this.gc = gc;
        this.onGameOver = onGameOver;
        gameLoop.play();
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, boardWidth, boardHeight);

        // snake 1 head
        gc.setFill(Color.ORANGE);
        gc.fillOval(snakeHead.x * tileSizes, snakeHead.y * tileSizes, tileSizes, tileSizes);

        // snake 1 body
        gc.setFill(Color.BLUE);
        for (Tile snakePart : snakeBody) {
            gc.fillOval(snakePart.x * tileSizes, snakePart.y * tileSizes, tileSizes, tileSizes);
        }

        if (twoPlayerMode) {
            // snake 2 head
            gc.setFill(Color.GREEN);
            gc.fillOval(snakeHead2.x * tileSizes, snakeHead2.y * tileSizes, tileSizes, tileSizes);

            // snake 2 body
            gc.setFill(Color.PURPLE);
            for (Tile snakePart2 : snakeBody2) {
                gc.fillRect(snakePart2.x * tileSizes, snakePart2.y * tileSizes, tileSizes, tileSizes);
            }
        }

        // food
        gc.setFill(Color.RED);
        gc.fillOval(food.x * tileSizes, food.y * tileSizes, tileSizes, tileSizes);

        // HUD text
        gc.setFont(Font.font("Arial", 16));

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText(gameOverMessage(), tileSizes - 16, tileSizes);
        } else {
            gc.setFill(Color.WHITE);
            if (twoPlayerMode) {
                gc.fillText("P1: " + snakeBody.size() + "     P2: " + snakeBody2.size(), tileSizes - 16, tileSizes);
            } else {
                gc.fillText("Score: " + snakeBody.size(), tileSizes - 16, tileSizes);
            }
        }
    }

    private String gameOverMessage() {
        if (!twoPlayerMode) {
            return "Game Over! Score: " + snakeBody.size();
        }
        if (snake1Dead && snake2Dead) {
            return "Draw!  P1: " + snakeBody.size() + "  P2: " + snakeBody2.size();
        } else if (snake1Dead) {
            return "Player 2 Wins!  P1: " + snakeBody.size() + "  P2: " + snakeBody2.size();
        } else {
            return "Player 1 Wins!  P1: " + snakeBody.size() + "  P2: " + snakeBody2.size();
        }
    }

    public void foodPlace() {
        food.x = random.nextInt(boardWidth / tileSizes);
        food.y = random.nextInt(boardHeight / tileSizes);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eating food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            foodPlace();
        }
        if (twoPlayerMode && collision(snakeHead2, food)) {
            snakeBody2.add(new Tile(food.x, food.y));
            foodPlace();
        }

        // shift snake 1 body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // shift snake 2 body
        if (twoPlayerMode) {
            for (int i = snakeBody2.size() - 1; i >= 0; i--) {
                Tile snakePart2 = snakeBody2.get(i);
                if (i == 0) {
                    snakePart2.x = snakeHead2.x;
                    snakePart2.y = snakeHead2.y;
                } else {
                    Tile prevSnakePart2 = snakeBody2.get(i - 1);
                    snakePart2.x = prevSnakePart2.x;
                    snakePart2.y = prevSnakePart2.y;
                }
            }
        }

        // move heads
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
        if (twoPlayerMode) {
            snakeHead2.x += velocityX1;
            snakeHead2.y += velocityY1;
        }

        // wall collisions
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSizes ||
                snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSizes) {
            snake1Dead = true;
        }

        // snake 1 hits its own body
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                snake1Dead = true;
            }
        }

        if (twoPlayerMode) {
            // wall collision for snake 2
            if (snakeHead2.x < 0 || snakeHead2.x >= boardWidth / tileSizes ||
                    snakeHead2.y < 0 || snakeHead2.y >= boardHeight / tileSizes) {
                snake2Dead = true;
            }

            // snake 2 hits its own body
            for (Tile snakePart2 : snakeBody2) {
                if (collision(snakeHead2, snakePart2)) {
                    snake2Dead = true;
                }
            }

            // snake 1 hits snake 2's body, and vice versa
            for (Tile snakePart2 : snakeBody2) {
                if (collision(snakeHead, snakePart2)) {
                    snake1Dead = true;
                }
            }
            for (Tile snakePart : snakeBody) {
                if (collision(snakeHead2, snakePart)) {
                    snake2Dead = true;
                }
            }

            // head-on collision
            if (collision(snakeHead, snakeHead2)) {
                snake1Dead = true;
                snake2Dead = true;
            }
        }

        gameOver = twoPlayerMode ? (snake1Dead || snake2Dead) : snake1Dead;
    }

    // define keypress
    public void keyPressed(KeyCode code) {
        // Player 1: WASD
        if (code == KeyCode.W && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (code == KeyCode.S && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (code == KeyCode.A && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (code == KeyCode.D && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        if (!twoPlayerMode) {
            return;
        }

        // Player 2: IJKL
        if (code == KeyCode.UP && velocityY1 != 1) {
            velocityX1 = 0;
            velocityY1 = -1;
        } else if (code == KeyCode.DOWN && velocityY1 != -1) {
            velocityX1 = 0;
            velocityY1 = 1;
        } else if (code == KeyCode.LEFT && velocityX1 != 1) {
            velocityX1 = -1;
            velocityY1 = 0;
        } else if (code == KeyCode.RIGHT && velocityX1 != -1) {
            velocityX1 = 1;
            velocityY1 = 0;
        }
    }

    // every 100ms, update the game state and repaint
    private void tick() {
        move();
        draw(gc);

        if (gameOver) {
            gameLoop.stop();
            if (onGameOver != null) {
                onGameOver.run();
            }
        }
    }

    public int getScore() {
        return snakeBody.size();
    }

    public int getScore2() {
        return snakeBody2.size();
    }

    public void restartGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        velocityX = 0;
        velocityY = 1;

        snakeHead2 = new Tile(boardWidth / tileSizes - 10, 5);
        snakeBody2.clear();
        velocityX1 = 0;
        velocityY1 = 1;

        gameOver = false;
        snake1Dead = false;
        snake2Dead = false;

        foodPlace();
        gameLoop.play();
    }
}
