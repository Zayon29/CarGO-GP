package mz.co.cargo.Controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private void abrirCadastroVeiculo(ActionEvent event) {
        try {
            // Carrega o FXML de cadastro de veículos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/cadastroVeiculo.fxml"));
            Parent cadastroRoot = loader.load();

            // Obtém a janela atual (Stage) a partir do botão
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Cria e seta a nova cena
            Scene scene = new Scene(cadastroRoot);
            stage.setScene(scene);
            stage.setTitle("Cadastrar Veículo - CarGo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarParaLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene loginScene = new Scene(loginRoot);

            stage.setScene(loginScene);
            stage.setTitle("Login - CarGo");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}