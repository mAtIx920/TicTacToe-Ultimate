package tictactoeapp.ultimate_tic_tac_toe;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import java.util.Arrays;
import java.util.stream.Stream;

public class UltimateField extends GridPane implements Field.FieldCallback, CheckFields {
    private static byte INDEX = 1;

    private final byte index;
    private Field[][] fields;
    byte ultimateRow;
    byte ultimateCol;
    byte whoMarked;
    boolean isPreviewMode = false;
    private boolean endGame = false;
    UltimateFieldCallback ultimateFieldCallback;

    UltimateField(byte ultimateRow, byte ultimateCol, HelloApplication parent) {
        this.index = UltimateField.INDEX++;
        this.ultimateRow = ultimateRow;
        this.ultimateCol = ultimateCol;
        this.setUltimateFieldCallback(parent);

        this.setOnMouseClicked(mouseEvent -> {
            if(this.whoMarked != 0) {
                if(!isPreviewMode) {
                    Arrays.stream(this.fields).flatMap(Arrays::stream).forEach(Field::previewField);
                    this.isPreviewMode = true;
                } else {
                    this.setField(this.whoMarked);
                    this.isPreviewMode = false;
                }
            }
        });

        GridPane gridPane = createGridPane();
        createField(gridPane);
        add(gridPane, ultimateCol, ultimateCol);
        setStyle("-fx-border-width: 1px; -fx-border-color: #333");

    }

    void setGameEndState() {
        this.endGame = true;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void createField(GridPane gridPane) {
        fields = new Field[3][3];

        for (byte row = 0; row < 3; row++) {
            for (byte col = 0; col < 3; col++) {
                Field field = new Field(row, col, this);
                field.setPrefSize(100, 100);
                fields[row][col] = field;
                gridPane.add(field, col, row);
            }
        }
    }

    Stream<Field> getFields() {
        return Arrays.stream(this.fields).flatMap(Arrays::stream);
    }

    @Override
    public void onButtonClick(byte row, byte col) {
        if(this.endGame) return;

        if(!this.checkRows()) {
            if(!this.checkCols()) {
                if(!this.checkFistDiagonal()) {
                    this.checkSecondDiagonal();
                }
            }
        }

        UltimateField ultimateField = HelloApplication.ultimateFields[row][col];
        if(ultimateField.isMarked() || ultimateField.ifAnyFieldMarked()) {
            ultimateField.setActive();
            ultimateField.blockUltimateFields(this, row, col);
        } else {
            UltimateField.resetActiveState(row, col);
        }

        GameManage.saveAction(this.ultimateRow, this.ultimateCol, ultimateField.fields[row][col].getIndex(), GameManage.TURN);

        if(GameManage.TURN == GameManage.AI_IDENTIFY_NUMBER) {
            GameManage.actionAI(row, col);
        }
    }

    static void resetActiveState(byte row, byte col) {
        Arrays.stream(HelloApplication.ultimateFields).flatMap(Arrays::stream).forEach(ultimateField -> {
            ultimateField.setStyle("-fx-border-width: 1px; -fx-border-color: #333");
            if(ultimateField.ultimateRow == row && ultimateField.ultimateCol == col || !ultimateField.isMarked()) {
                return;
            }
            Arrays.stream(ultimateField.fields).forEach(fields -> Arrays.stream(fields).forEach(Field::retryAction));
        });
    }

    boolean ifAnyFieldMarked() {
        return Arrays.stream(this.fields).flatMap(Arrays::stream).allMatch(field -> field.getWhoMarked() != 0);
    }

    @Override
    public boolean checkRows() {
        for (Field[] field : fields) {
            boolean allSame = true;

            if (field[0].checkFieldMarked()) {
                continue;
            }
            byte firstValue = field[0].getWhoMarked();

            for (int j = 1; j < field.length; j++) {
                if (field[j].getWhoMarked() != firstValue || field[j].checkFieldMarked()) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                this.setField(firstValue);
                this.ultimateFieldCallback.onButtonClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkCols() {

        for (int col = 0; col < fields[0].length; col++) {
            boolean allSame = true;

            if(fields[0][col].checkFieldMarked()) {
                continue;
            }
            byte firstValue = fields[0][col].getWhoMarked();

            for (int row = 1; row < fields.length; row++) {
                if (fields[row][col].getWhoMarked() != firstValue || fields[row][col].checkFieldMarked()) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                this.setField(firstValue);
                this.ultimateFieldCallback.onButtonClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkFistDiagonal() {
        boolean firstTheSame = true;
        if(fields[0][0].checkFieldMarked()) {
            firstTheSame = false;
        }
        byte mainDiagonalValue = fields[0][0].getWhoMarked();
        for (int i = 1; i < fields.length; i++) {
            if (fields[i][i].getWhoMarked() != mainDiagonalValue || fields[i][i].checkFieldMarked()) {
                firstTheSame = false;
                break;
            }
        }
        if(firstTheSame) {
            this.setField(mainDiagonalValue);
            this.ultimateFieldCallback.onButtonClick();
        }
        return firstTheSame;
    }

    @Override
    public boolean checkSecondDiagonal() {
        boolean secondTheSame = true;
        if(fields[0][fields.length - 1].checkFieldMarked()) {
            secondTheSame = false;
        }
        byte secondaryDiagonalValue = fields[0][fields.length - 1].getWhoMarked();
        for (int i = 1; i < fields.length; i++) {
            if (fields[i][fields.length - 1 - i].getWhoMarked() != secondaryDiagonalValue || fields[i][fields.length - 1 - i].checkFieldMarked()) {
                secondTheSame = false;
                break;
            }
        }
        if(secondTheSame) {
            this.setField(secondaryDiagonalValue);
            this.ultimateFieldCallback.onButtonClick();
        }
        return secondTheSame;
    }

    void setField(byte whoWon) {
        this.whoMarked = whoWon;
        Arrays.stream(this.fields).forEach(fields -> Arrays.stream(fields).forEach(field -> field.setWonUseOption(whoWon)));
    }

    public interface UltimateFieldCallback {
        void onButtonClick();
    }

    void setUltimateFieldCallback(HelloApplication callback) {
        this.ultimateFieldCallback = callback;
    }

    boolean isMarked() {
        return this.whoMarked == 0;
    }

    byte getWhoMarked() {
        return this.whoMarked;
    }

    void blockUltimateFields(UltimateField ultimateField1, byte row, byte col) {
        Arrays.stream(HelloApplication.ultimateFields).flatMap(Arrays::stream).forEach(ultimateField -> {
            if(ultimateField.ultimateRow == row && ultimateField.ultimateCol == col) {
                return;
            }
            ultimateField.removeBorder(ultimateField1, row, col);
            Arrays.stream(ultimateField.fields).flatMap(Arrays::stream).forEach(Field::blockAction);
        });
    }

    void removeBorder(UltimateField ultimateField, byte row, byte col) {
        Arrays.stream(this.fields).forEach(fields -> Arrays.stream(fields).forEach(Field::blockAction));
        if(ultimateField.ultimateRow == row && ultimateField.ultimateCol == col) {
            return;
        }
        ultimateField.setStyle("-fx-border-width: 1px; -fx-border-color: #333");

    }

    void setActive() {
        this.setStyle("-fx-border-width: 3px; -fx-border-color: lime");
        Arrays.stream(this.fields).flatMap(Arrays::stream).forEach(Field::retryAction);
    }
}
