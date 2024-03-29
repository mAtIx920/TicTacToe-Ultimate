package tictactoeapp.ultimate_tic_tac_toe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.HashMap;

public class EndGamePanel extends Parent {
    private final Stage stage;
    private final Stage newStage;
    private final byte winner;
    private GridPane gamepanel;

    EndGamePanel(Stage stage, Stage newStage, GridPane gamePanel, byte winner) {
        this.stage = stage;
        this.newStage = newStage;
        this.winner = winner;
        this.gamepanel = gamePanel;
        this.init();
    }

    private void init() {
            Text gameWonText = new Text("Game won: " + (this.winner == 1 ? "RED" : "BLUE"));
            gameWonText.setFont(Font.font("Arial", 24));

            Button returnGame = new Button("Return game");
            Button settings = new Button("Settings");

            settings.setOnAction(e -> {
                this.openSettings();
            });

            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().addAll(returnGame, settings);

            HashMap<Integer, String[]> resultsHistory = GameManage.readActionResults();

        TableView<DataModel> tableView = new TableView<>();

        TableColumn<DataModel, String> lPColumn = new TableColumn<>("l.p");
        lPColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("Lp"));

        TableColumn<DataModel, String> playerColumn = new TableColumn<>("Gracz");
        playerColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("Player"));

        TableColumn<DataModel, String> gameBoardColumn = new TableColumn<>("Gplansza");
        gameBoardColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("UltimateField"));

        TableColumn<DataModel, String> boardLocationColumn = new TableColumn<>("Miejsce plansza");
        boardLocationColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("Field"));

        tableView.getColumns().addAll(lPColumn, playerColumn, gameBoardColumn, boardLocationColumn);

        for(String[] array : resultsHistory.values()) {
            System.out.println(Arrays.toString(array));
        }

//        tableView.getItems().add(new DataModel("df","df0", "sdfsf", "dsfdsg"));

//        tableView.setItems(FXCollections.observableArrayList(
//                new Object[]{1, "John", "Chess", "A1"},
//                new Object[]{2, "Alice", "Monopoly", "B3"},
//                new Object[]{3, "Bob", "Scrabble", "C2"}
//        ));



        VBox listViewBox = new VBox(10);
        listViewBox.getChildren().addAll(tableView);

            VBox vbox = new VBox(10);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(gameWonText, hbox, listViewBox);
            getChildren().add(vbox);
    }

    private void openSettings() {
        this.newStage.close();

        Scene scene = new Scene(new StartPanel(this.stage, this.gamepanel), 300, 300);
        this.stage.setScene(scene);
        this.stage.show();
    }

    class DataModel {
        String lp;
        String player;
        String ultimateField;
        String field;

        DataModel(String lp, String player, String ultimateField, String field) {
            this.lp = lp;
            this.player = player;
            this.ultimateField = ultimateField;
            this.field = field;
        }

        String getLp() {
            return this.lp;
        }

        String getPlayer() {
            return this.player;
        }

        String getUltimateField() {
            return this.ultimateField;
        }

        String getField() {
            return this.field;
        }

    }
}
