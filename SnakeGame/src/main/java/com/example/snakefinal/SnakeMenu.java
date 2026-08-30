package com.example.snakefinal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

/**
 * Start screen: lets the player pick 1 Player or 2 Player mode before the
 * game window opens. Visual style matches the rest of the arcade (GameGuy)
 * home screen so this can live as a standalone app or be dropped back in.
 */
public class SnakeMenu {

    public void show(Stage stage) {

        InputStream fontStream = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
        if (fontStream != null) {
            Font.loadFont(fontStream, 20);
        } else {
            System.out.println("FONT NOT FOUND: /fonts/PressStart2P-Regular.ttf");
        }

        Label logo = new Label("SNAKE");
        logo.getStyleClass().add("home-title");

        Label tagline = new Label("SELECT MODE");
        tagline.getStyleClass().add("home-subtitle");

        VBox header = new VBox(12, logo, tagline);
        header.setAlignment(Pos.CENTER);

        Button onePlayerButton = modeCard(
                "1 PLAYER",
                "CLASSIC MODE  \u2022  WASD"
        );
        Button twoPlayerButton = modeCard(
                "2 PLAYER",
                "VERSUS MODE  \u2022  WASD vs ArrowKeys"
        );

        onePlayerButton.setOnAction(e -> SnakeWindow.show(stage, false));
        twoPlayerButton.setOnAction(e -> SnakeWindow.show(stage, true));

        VBox modes = new VBox(14, onePlayerButton, twoPlayerButton);
        modes.setAlignment(Pos.CENTER);
        modes.setMaxWidth(500);

        Button exitButton = new Button("QUIT");
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e -> stage.close());

        Label footer = new Label("\u25B2 \u25BC EAT \u2022 GROW \u2022 SURVIVE \u25BC \u25B2");
        footer.getStyleClass().add("footer-text");

        VBox content = new VBox(24, header, modes, exitButton, footer);
        content.getStyleClass().add("home-pane");
        content.setPadding(new Insets(36));
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(570);

        VBox page = new VBox(content);
        page.getStyleClass().add("home-page");
        page.setAlignment(Pos.CENTER);
        page.setPadding(new Insets(24));

        Scene scene = new Scene(page, 620, 620);

        URL css = getClass().getResource("/snake.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("CSS NOT FOUND: /snake.css");
        }

        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private Button modeCard(String name, String description) {

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("game-name");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("game-description");

        VBox text = new VBox(8, nameLabel, descriptionLabel);
        text.setAlignment(Pos.CENTER_LEFT);

        Region pushRight = new Region();
        HBox.setHgrow(pushRight, Priority.ALWAYS);

        Label arrow = new Label(">");
        arrow.getStyleClass().add("game-arrow");

        HBox cardContent = new HBox(18, text, pushRight, arrow);
        cardContent.setAlignment(Pos.CENTER_LEFT);

        Button card = new Button();
        card.setGraphic(cardContent);
        card.setMaxWidth(Double.MAX_VALUE);
        card.getStyleClass().add("game-card");

        return card;
    }
}
