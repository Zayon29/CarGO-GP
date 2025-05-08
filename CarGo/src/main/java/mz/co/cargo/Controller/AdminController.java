package mz.co.cargo.Controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import javafx.scene.control.Label;

public class AdminController {

    @FXML
    private Label saudacaoLabel;

    @FXML
    private Label emailLabel;

    public void carregarInformacoes(AdminUser admin, ActionEvent event) {
        saudacaoLabel.setText("Olá, Adm " + admin.getNome() + "!");
        emailLabel.setText("Email: " + admin.getEmail());
    }

    @FXML
    private void abrirCadastroVeiculo(ActionEvent event) {
        try {
            // Carregar o FXML de cadastro de veículos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/cadastroVeiculo.fxml"));
            Parent cadastroRoot = loader.load();

            // Recupera a janela atual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Troca a cena
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastrar Veículo - CarGo");
        } catch (IOException e) {
            e.printStackTrace();
            // Alerta de erro, se necessário
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
            // Alerta de erro, se necessário
        }
    }
}
