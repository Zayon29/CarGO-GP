package mz.co.cargo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Service.ClienteService;

import java.io.IOException;

public class EditarClienteController {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label mensagemLabel;

    private ClienteUser clienteAtual;

    public void setCliente(ClienteUser cliente) {
        this.clienteAtual = cliente;
        nomeField.setText(cliente.getNome());
        emailField.setText(cliente.getEmail());
    }

    @FXML
    public void salvarAlteracoes() {
        String novoNome = nomeField.getText();
        String novoEmail = emailField.getText();
        String novaSenha = senhaField.getText();

        // Se o campo de senha estiver vazio, mantém a senha atual
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            novaSenha = clienteAtual.getSenha();
        }

        ClienteUser clienteAtualizado = new ClienteUser(novoNome, novoEmail, novaSenha);

        boolean sucesso = ClienteService.editarCliente(clienteAtual.getEmail(), clienteAtualizado);
        if (sucesso) {
            mensagemLabel.setText("Dados atualizados com sucesso!");
            clienteAtual = clienteAtualizado; // Atualiza o cliente atual com os novos dados
        } else {
            mensagemLabel.setText(ClienteService.getUltimaMensagemErro());
        }
    }



    //nao estava funcionado ainda esse botao voltar af
    @FXML
    public void voltarParaTelaUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/User.fxml"));
            Parent root = loader.load();

            // Pegando o controller da tela User para repassar o cliente logado
            UserController controller = loader.getController();
            controller.carregarInformacoes(clienteAtual, event); // ou outro nome do campo do cliente

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Área do Cliente - CarGO");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
