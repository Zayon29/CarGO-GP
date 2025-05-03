module mz.co.cargo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens mz.co.cargo.Controller to javafx.fxml;
    opens mz.co.cargo to javafx.fxml;
    exports mz.co.cargo;
}