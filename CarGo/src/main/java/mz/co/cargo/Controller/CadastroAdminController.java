package mz.co.cargo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Service.AdminService;

import java.io.IOException;

public class CadastroAdminController {

    private AdminUser adminLogado;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private PasswordField confirmarSenhaField;

    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
    }

    @FXML
    private void salvarAdmin(ActionEvent event) {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String confirma = confirmarSenhaField.getText();

        if (!senha.equals(confirma)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Senha", "As senhas não conferem.");
            return;
        }

        AdminUser novoAdmin = new AdminUser(nome, email, senha);
        String resultado = AdminService.cadastrarAdmin(novoAdmin);

        if (resultado.startsWith("Erro")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Cadastro", resultado);
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro", resultado);
            limparCampos();
        }
    }

    private void limparCampos() {
        nomeField.clear();
        emailField.clear();
        senhaField.clear();
        confirmarSenhaField.clear();
    }

    @FXML
    private void voltarParaLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/telaAdmin.fxml"));
            Parent adminRoot = loader.load();

            AdminController controller = loader.getController();
            controller.carregarInformacoes(adminLogado, event);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(adminRoot));
            stage.setTitle("Painel Admin - CarGo");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível retornar ao painel administrativo.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
