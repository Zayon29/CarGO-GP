package mz.co.cargo.Controller;

import mz.co.cargo.Service.AdminService;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private void fazerLogin(ActionEvent event) {
        String email = campoUsuario.getText();
        String senha = campoSenha.getText();
        AdminUser admin = AdminService.realizarLoginAdmin(email, senha);

        if (admin != null) {
            loginSucesso(true);
            carregarPaginaAdm(admin, event);
        } else {
            loginSucesso(false);
        }

        campoUsuario.clear();
        campoSenha.clear();
    }

    private void loginSucesso(boolean sucesso) {
        Alert alert;
        if (sucesso) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText("Sucesso");
            alert.setContentText("Login realizado com sucesso!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText("Erro");
            alert.setContentText("Email ou senha incorretos.");
        }
        alert.showAndWait();
    }

    private static boolean carregarPaginaAdm(AdminUser admin, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("telaAdmin.fxml"));
            Parent adminPage = loader.load();

            AdminController controller = loader.getController();
            controller.carregarInformacoes(admin, event);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(adminPage);
            stage.setScene(scene);
            stage.show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao carregar a página administrativa");
            alert.setContentText("Houve um erro ao tentar carregar a página administrativa.");
            alert.showAndWait();
            return false;
        }
    }
}
