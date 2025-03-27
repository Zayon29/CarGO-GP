package mz.co.cargo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-usuario.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 900);
        stage.setTitle("Sistema de Locação de veículos - CarGo");
        stage.setScene(scene);
        stage.setResizable(false); // Impede redimensionamento
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}