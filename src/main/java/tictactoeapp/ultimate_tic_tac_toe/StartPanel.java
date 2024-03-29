package tictactoeapp.ultimate_tic_tac_toe;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StartPanel extends Parent {
    private Stage stage;
    private GridPane gamePanel;

    StartPanel(Stage stage, GridPane gamePanel) {
        this.stage = stage;
        this.gamePanel = gamePanel;
        this.init();
    }

    void init() {
        //All toggle buttons configuration
        RadioButton AI = new RadioButton("Computer");
        RadioButton single = new RadioButton("Single");
        RadioButton normalLevel = new RadioButton("Easy");
        RadioButton hardLevel = new RadioButton("Hard");
        ToggleGroup toggleGroupGameType = new ToggleGroup();
        ToggleGroup toggleGroupLevel = new ToggleGroup();
        AI.setToggleGroup(toggleGroupGameType);
        single.setToggleGroup(toggleGroupGameType);
        normalLevel.setToggleGroup(toggleGroupLevel);
        hardLevel.setToggleGroup(toggleGroupLevel);
        HBox radioButtonsGameType = new HBox(40);
        radioButtonsGameType.getChildren().addAll(AI, single);
        HBox radioButtonsLevel = new HBox(40);
        radioButtonsLevel.getChildren().addAll(normalLevel, hardLevel);
        radioButtonsLevel.setVisible(false);

        //All labels
        Label gameTypeLabel = new Label("Choose a game type:");
        Label gameLevelLabel = new Label("Level");
        gameLevelLabel.setVisible(false);

        //Start game button
        Button startButton = new Button("Start game");
        startButton.setVisible(false);

        //Toggling gameType listener
        toggleGroupGameType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == AI) {
                startButton.setVisible(false);
                gameLevelLabel.setVisible(true);
                radioButtonsLevel.setVisible(true);
            } else {
                toggleGroupLevel.selectToggle(null);
                startButton.setVisible(true);
                gameLevelLabel.setVisible(false);
                radioButtonsLevel.setVisible(false);
            }
        });

        //Toggling levelGame listener
        toggleGroupLevel.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == normalLevel) {
                GameManage.GAME_LEVEL = 0;
            } else {
                GameManage.GAME_LEVEL = 1;
            }
            startButton.setVisible(true);
        });

        //startButton listener
        startButton.setOnAction(e -> {
            Scene gameScene = new Scene(this.gamePanel, 900, 900);

            Rectangle2D screen = Screen.getPrimary().getVisualBounds();
            double centerX = screen.getMinX() + (screen.getWidth() - gameScene.getWidth()) / 2;
            double centerY = screen.getMinY() + (screen.getHeight() - gameScene.getHeight()) / 2;

            this.stage.setX(centerX);
            this.stage.setY(centerY);
            this.stage.setScene(gameScene);

            if(single.isSelected()) {
                GameManage.GAME_TYPE = 0;
            } else if(AI.isSelected()) {
                GameManage.GAME_TYPE = 1;
                GameManage.drawTurn();
            }
            GameManage.gameScene = gameScene;
        });

        //Set box content layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(gameTypeLabel, radioButtonsGameType, gameLevelLabel, radioButtonsLevel, startButton);
        getChildren().add(vbox);
    }
}
