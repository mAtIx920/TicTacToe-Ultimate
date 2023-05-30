module tictactoeapp.ultimate_tic_tac_toe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens tictactoeapp.ultimate_tic_tac_toe to javafx.fxml;
    exports tictactoeapp.ultimate_tic_tac_toe;
}