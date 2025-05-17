package mz.co.cargo.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Repository.ClienteRepository;

import java.io.IOException;

public class Teste {

        @FXML
        private TableView<ClienteUser> tabelaClientes;

        @FXML
        private TableColumn<ClienteUser, String> colNome;

        @FXML
        private TableColumn<ClienteUser, String> colEmail;

        @FXML
        private TextField campoBusca;

    private AdminUser adminLogado;
    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
    }

        @FXML
        public void initialize() {
            colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
            colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
            atualizarTabela();
        }

        @FXML
        private void atualizarTabela() {
            tabelaClientes.setItems(FXCollections.observableArrayList(ClienteRepository.buscarTodosClientes()));
        }

        @FXML
        private void buscarCliente() {
            String nome = campoBusca.getText();
            if (nome.isEmpty()) {
                atualizarTabela();
            } else {
                tabelaClientes.setItems(FXCollections.observableArrayList(ClienteRepository.buscarClientesPorNome(nome)));
            }
        }

        @FXML
        private void excluirClienteSelecionado() {
            ClienteUser clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
            if (clienteSelecionado != null) {
                boolean sucesso = ClienteRepository.excluirCliente(clienteSelecionado.getEmail());
                if (sucesso) {
                    tabelaClientes.getItems().remove(clienteSelecionado);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Cliente excluído com sucesso.");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir o cliente.");
                }
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Selecione um cliente para excluir.");
            }
        }

        private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
            Alert alert = new Alert(tipo);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        }


    @FXML
    private void abrirTeste(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/teste.fxml"));
            Parent cadastroRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Gerenciar clientes - CarGo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o cadastro de admin.").showAndWait();
        }
    }

    @FXML
    private void voltarParaTelaAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/telaAdmin.fxml"));
            Parent adminRoot = loader.load();

            AdminController controller = loader.getController();
            controller.carregarInformacoes(adminLogado, event);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene adminScene = new Scene(adminRoot);

            stage.setScene(adminScene);
            stage.setTitle("Painel Admin - CarGo");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
