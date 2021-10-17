module com.tictactoe.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.tictactoe.tictactoe to javafx.fxml;
    exports com.tictactoe.tictactoe;
}