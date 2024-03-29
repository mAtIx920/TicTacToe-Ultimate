package tictactoeapp.ultimate_tic_tac_toe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.Arrays;

public class HelloApplication extends Application implements CheckFields, UltimateField.UltimateFieldCallback {
    static UltimateField[][] ultimateFields;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        GridPane gameFieldsPanel = createGameFieldsPanel();
        createUltimateFields(gameFieldsPanel);
        GameManage.clearFile();

        Scene startPanel = new Scene(new StartPanel(stage, gameFieldsPanel), 300, 300);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        final double X = screen.getMinX() + (screen.getWidth() - startPanel.getWidth()) / 2;
        final double Y = screen.getMinY() + (screen.getHeight() - startPanel.getHeight()) / 2;

        stage.setX(X);
        stage.setY(Y);
        stage.setTitle("Welcome Game");
        stage.setScene(startPanel);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private GridPane createGameFieldsPanel() {
        GridPane gameFieldsPanel = new GridPane();
        gameFieldsPanel.setAlignment(Pos.CENTER);
        return gameFieldsPanel;
    }

    private void createUltimateFields(GridPane gridPane) {
        ultimateFields = new UltimateField[3][3];

        for (byte row = 0; row < 3; row++) {
            for (byte col = 0; col < 3; col++) {
                UltimateField ultimateField = new UltimateField(row, col, this);
                ultimateField.setPrefSize(300, 300);
                ultimateFields[row][col] = ultimateField;
                gridPane.add(ultimateField, col, row);
            }
        }
    }

    void endGame(byte winner) {
        Arrays.stream(ultimateFields).flatMap(Arrays::stream).forEach(UltimateField::setGameEndState);

        GridPane newGameFieldsPanel = createGameFieldsPanel();
        createUltimateFields(newGameFieldsPanel);
        Stage endGameStage = new Stage();

        Scene endGamePanel = new Scene(new EndGamePanel(this.stage, endGameStage, newGameFieldsPanel, winner), 300, 300);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        double centerX = screen.getMinX() + (screen.getWidth() - endGamePanel.getWidth()) / 2;
        double centerY = screen.getMinY() + (screen.getHeight() - endGamePanel.getHeight()) / 2;
        endGameStage.setX(centerX);
        endGameStage.setY(centerY);
        endGameStage.setScene(endGamePanel);
        endGameStage.setScene(endGamePanel);
        endGameStage.setTitle("Game result");
        endGameStage.show();
    }

    @Override
    public void onButtonClick() {
        if(!this.checkRows()) {
            if(!this.checkCols()) {
                if(!this.checkFistDiagonal()) {
                    this.checkSecondDiagonal();
                }
            }
        }
    }

    @Override
    public boolean checkRows() {
        for (UltimateField[] ultimateField : ultimateFields) {
            boolean allSame = true;
            if (ultimateField[0].isMarked()) continue;
            byte firstValue = ultimateField[0].getWhoMarked();

            for (int j = 1; j < ultimateField.length; j++) {
                if (ultimateField[j].getWhoMarked() != firstValue || ultimateField[j].isMarked()) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                this.endGame(firstValue);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkCols() {
        for (int col = 0; col < ultimateFields[0].length; col++) {
            boolean allSame = true;
            if(ultimateFields[0][col].isMarked()) continue;
            byte firstValue = ultimateFields[0][col].getWhoMarked();

            for (int row = 1; row < ultimateFields.length; row++) {
                if (ultimateFields[row][col].getWhoMarked() != firstValue || ultimateFields[row][col].isMarked()) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                this.endGame(firstValue);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkFistDiagonal() {
        boolean firstTheSame = true;
        if(ultimateFields[0][0].isMarked()) {
            firstTheSame = false;
        }
        byte mainDiagonalValue = ultimateFields[0][0].getWhoMarked();
        for (int i = 1; i < ultimateFields.length; i++) {
            if (ultimateFields[i][i].getWhoMarked() != mainDiagonalValue || ultimateFields[i][i].isMarked()) {
                firstTheSame = false;
                break;
            }
        }
        if(firstTheSame) {
            this.endGame(mainDiagonalValue);
        }
        return firstTheSame;
    }

    @Override
    public boolean checkSecondDiagonal() {
        boolean secondTheSame = true;
        if(ultimateFields[0][ultimateFields.length - 1].isMarked()) {
            secondTheSame = false;
        }
        byte secondaryDiagonalValue = ultimateFields[0][ultimateFields.length - 1].getWhoMarked();
        for (int i = 1; i < ultimateFields.length; i++) {
            if (ultimateFields[i][ultimateFields.length - 1 - i].getWhoMarked() != secondaryDiagonalValue || ultimateFields[i][ultimateFields.length - 1 - i].isMarked()) {
                secondTheSame = false;
                break;
            }
        }
        if(secondTheSame) {
            this.endGame(secondaryDiagonalValue);
        }
        return secondTheSame;
    }
}