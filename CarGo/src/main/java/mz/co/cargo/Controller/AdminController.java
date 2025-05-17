package mz.co.cargo.Controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import javafx.scene.control.Label;

public class AdminController {

    @FXML
    private Label saudacaoLabel;

    @FXML
    private Label emailLabel;



    private AdminUser adminLogado;

    public void carregarInformacoes(AdminUser admin, ActionEvent event) {
        this.adminLogado = admin;

        emailLabel.setText("Email: " + admin.getEmail());
        saudacaoLabel.setText("Olá, Adm " + admin.getNome() + "!");
    }

    @FXML
    private void abrirCadastroVeiculo(ActionEvent event) {
        try {
            // Carregar o FXML de cadastro de veículos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/cadastroVeiculo.fxml"));
            Parent cadastroRoot = loader.load();

            CadastroVeiculoController controller = loader.getController();
            controller.setAdminLogado(adminLogado);

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
    private void abrirCadastroAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/CadastroAdmin.fxml"));
            Parent cadastroRoot = loader.load();

            CadastroAdminController ctrl = loader.getController();
            ctrl.setAdminLogado(adminLogado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastro de Novo Admin - CarGo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o cadastro de admin.").showAndWait();
        }
    }

    @FXML
    private void abrirGerenciarClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/GerenciarClientes.fxml"));

            Parent cadastroRoot = loader.load();

            GerenciarClientesController ctrl = loader.getController();
            ctrl.setAdminLogado(adminLogado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Gerenciamento de Clientes - CarGo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Não foi possível abrir a tela de gerenciamento de clientes.")
                    .showAndWait();
        }
    }

    @FXML
    private void abrirTeste(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/teste.fxml"));
            Parent cadastroRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastro de Novo Admin - CarGo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o cadastro de admin.").showAndWait();
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
