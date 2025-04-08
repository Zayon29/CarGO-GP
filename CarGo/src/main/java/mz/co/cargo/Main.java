package mz.co.cargo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tela-admin.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root); // Sem tamanho fixo

        stage.setTitle("Sistema de Locação de Veículos - CarGo");
        stage.setScene(scene);
        stage.setResizable(true); // Permite redimensionamento, mas não inicia maximizado

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}