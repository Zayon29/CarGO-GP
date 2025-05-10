package mz.co.cargo.Controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Service.ClienteService;

import java.io.IOException;

public class CadastroUserController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private TextField confirmarSenhaField;

    @FXML
    private Button criarContaButton;

    @FXML
    public void initialize() {
        criarContaButton.setOnAction(event -> criarConta());
    }

    private void criarConta() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();

        if (!senha.equals(confirmarSenha)) {
            mostrarAlerta("Erro", "As senhas não coincidem.");
            return;
        }

        ClienteUser cliente = new ClienteUser(nome, email, senha);
        String resultado = ClienteService.cadastrarCliente(cliente);

        if (resultado.equals("Usuário cadastrado com sucesso!")) {
            mostrarAlerta("Sucesso", resultado);
        } else {
            mostrarAlerta("Erro", resultado);
        }
    }


    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.getButtonTypes().setAll(ButtonType.OK);

        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setVisible(false);
        okButton.setManaged(false);

        alert.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> alert.close());
        delay.play();
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