package tictactoeapp.ultimate_tic_tac_toe;

import javafx.scene.Scene;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManage {
    public static final String FILE_PATH = "results.txt";
    public static byte TURN = 1;
    public static byte ME_IDENTIFY_NUMBER;
    public static byte AI_IDENTIFY_NUMBER;
    public static byte GAME_TYPE;
    static byte GAME_LEVEL;
    static Scene gameScene;

    static Integer MAX_CELL_VALUE = 81;
    static Integer MIN_CELL_VALUE = 1;

    public static void drawTurn() {
        if(GAME_TYPE == 1) {
            ME_IDENTIFY_NUMBER = (byte) (Math.round(Math.random()) + 1);
            AI_IDENTIFY_NUMBER = (byte) (ME_IDENTIFY_NUMBER == 2 ? 1 : 2);

            if(TURN == AI_IDENTIFY_NUMBER) {
                actionAI((byte) (Math.random() * 3), (byte) (Math.random() * 3));
            }
        }
    }

    public static void saveAction(byte ultimateFieldRow, byte ultimateFieldCol, int fieldIndex, byte playerTurn) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(playerTurn + "," + ultimateFieldRow + "," + ultimateFieldCol + "," + fieldIndex);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, String[]> readActionResults() {
        HashMap<Integer, String[]> results = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            Integer i = 1;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                results.put(i, tokens);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void actionAI(byte row, byte col) {
        AtomicBoolean isFired = new AtomicBoolean(false);
        int randomNumber = (int) (Math.random() * 9) + 1;
        final UltimateField preciseUltimateField = HelloApplication.ultimateFields[row][col];

        if(GAME_LEVEL == 1) {
            preciseUltimateField.getFields().forEach(field -> {
                if(field.getIndex() == randomNumber && field.checkFieldMarked() && !field.isDisabled()) {
                    field.fire();
                    isFired.set(true);
                }
            });
            if(!isFired.get()) {
                actionAI(row, col);
            }
        } else {
            Arrays.stream(HelloApplication.ultimateFields).flatMap(Arrays::stream).forEach(ultimateField -> {
                if(!isFired.get()) {
                    ultimateField.getFields().forEach(field -> {
                        if(field.getIndex() == randomNumber && field.checkFieldMarked() && !field.isDisabled()) {
                            field.fire();
                            isFired.set(true);
                        }
                    });
                }
            });
            if(!isFired.get()) {
                actionAI((byte) (Math.random() * 3), (byte) (Math.random() * 3));
            }
        }
    }
}
