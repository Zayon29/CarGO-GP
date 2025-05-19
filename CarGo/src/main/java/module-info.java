module mz.co.cargo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens mz.co.cargo.Model to javafx.base;
    opens mz.co.cargo.Controller to javafx.fxml;
    opens mz.co.cargo to javafx.fxml;
    exports mz.co.cargo;
}