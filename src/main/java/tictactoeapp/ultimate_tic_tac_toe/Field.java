package tictactoeapp.ultimate_tic_tac_toe;

import javafx.scene.control.Button;

import java.net.IDN;

public class Field extends Button {
    static int INDEX = 1;

    private final int index;
    private byte whoMarked;
    private final byte fieldRow;
    private final byte fieldCol;
    FieldCallback fieldCallback;
    UltimateField parentUltimateField;

    Field(byte row, byte col, UltimateField parent) {
        if(INDEX == 10) {
            INDEX = 1;
        }
        this.index = INDEX++;
        this.fieldRow = row;
        this.fieldCol = col;
        this.parentUltimateField = parent;
        this.createButton();
        this.setFieldCallback(parent);
    }

    int getIndex() {
        return this.index;
    }

    private void createButton() {
        this.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: #aaa; -fx-cursor: hand");
        this.setOnAction(e -> {
            if(GameManage.TURN == 1) {
                this.setStyle("-fx-background-color: red");
                this.whoMarked = 1;
                GameManage.TURN = 2;
            } else if(GameManage.TURN == 2){
                this.setStyle("-fx-background-color: blue");
                this.whoMarked = 2;
                GameManage.TURN = 1;
            }
            this.setDisable(true);

            if (this.fieldCallback != null) {
                this.fieldCallback.onButtonClick(this.fieldRow, this.fieldCol);
            }
        });
    }

    void setFieldCallback(UltimateField callback) {
        this.fieldCallback = callback;
    }

    public interface FieldCallback {
        void onButtonClick(byte fieldRow, byte fieldCol);
    }

    byte getWhoMarked() {
        return this.whoMarked;
    }

    boolean checkFieldMarked() {
        return this.whoMarked == 0;
    }

    public void setWonUseOption(byte whoWon) {
        if(whoWon == 1) {
            this.setStyle("-fx-background-color: red");
        } else if(whoWon == 2) {
            this.setStyle("-fx-background-color: blue");
        }
        this.setDisable(true);
    }

    void previewField() {
        if(whoMarked == 1) {
            this.setStyle("-fx-background-color: red");
        } else if(whoMarked == 2) {
            this.setStyle("-fx-background-color: blue");
        } else {
            this.setStyle(null);
        }
    }

    public void blockAction() {
        this.setDisable(true);
    }

    public void retryAction() {
        if(this.whoMarked == 0) {
            this.setDisable(false);
            this.setStyle(null);
            this.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: #aaa; -fx-cursor: hand");
        } else {
            if(this.whoMarked == 1) {
                this.setStyle("-fx-background-color: red");
            } else {
                this.setStyle("-fx-background-color: blue");
            }
        }
    }

}
