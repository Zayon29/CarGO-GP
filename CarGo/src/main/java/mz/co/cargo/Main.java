package mz.co.cargo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Repository.DatabaseInitializer;
import mz.co.cargo.Service.AdminService;
import mz.co.cargo.Service.AluguelService;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login" +
                ".fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Sistema de Locação de Veículos - CarGo");
        stage.setScene(scene);
        stage.setResizable(true);

        //Aq é pra iniciar o banco de dados alek
        DatabaseInitializer.initializeAll();
        AluguelService.atualizarStatusDosVeiculos();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}