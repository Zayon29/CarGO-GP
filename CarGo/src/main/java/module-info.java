module mz.co.cargo {
    requires javafx.controls;
    requires javafx.fxml;


    opens mz.co.cargo to javafx.fxml;
    exports mz.co.cargo;
}