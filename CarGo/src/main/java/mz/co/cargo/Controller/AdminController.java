package mz.co.cargo.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Service.AluguelService;

public class AdminController implements Initializable {

    @FXML
    private TableView<Aluguel> tabelaAlugueis;

    @FXML
    private TableColumn<Aluguel, String> colunaCliente;

    @FXML
    private TableColumn<Aluguel, String> colunaVeiculo;

    @FXML
    private TableColumn<Aluguel, String> colunaInicio;

    @FXML
    private TableColumn<Aluguel, String> colunaFim;

    private AdminUser adminLogado;

    public void carregarInformacoes(AdminUser admin, ActionEvent event) {
        this.adminLogado = admin;

        // Removido uso de emailLabel e saudacaoLabel pois não existem no FXML
    }

    @FXML
    private void abrirCadastroVeiculo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/cadastroVeiculo.fxml"));
            Parent cadastroRoot = loader.load();

            CadastroVeiculoController controller = loader.getController();
            controller.setAdminLogado(adminLogado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastrar Veículo - CarGo");
        } catch (IOException e) {
            e.printStackTrace();
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
    private void abrirTeste(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/teste.fxml"));
            Parent cadastroRoot = loader.load();

            Teste ctrl = loader.getController();
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
    private void abrirGerenciarVeiculos(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/gerenciarVeiculos.fxml"));
            Parent cadastroRoot = loader.load();

            GerenciarVeiculosController ctrl = loader.getController();
            ctrl.setAdminLogado(adminLogado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Gerenciamento de Veiculos - CarGo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o gerenciamento de veiculos.").showAndWait();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDadosExemplo();

        tabelaAlugueis.setRowFactory(tv -> new TableRow<Aluguel>() {
            @Override
            protected void updateItem(Aluguel aluguel, boolean empty) {
                super.updateItem(aluguel, empty);
                if (aluguel == null || empty) {
                    setStyle("");
                } else {
                    try {
                        String dataFimStr = aluguel.getDataFim();
                        if (dataFimStr != null && !dataFimStr.isBlank()) {
                            LocalDate dataFim = LocalDate.parse(dataFimStr);
                            LocalDate hoje = LocalDate.now();

                            if (dataFim.isBefore(hoje)) {
                                setStyle("-fx-background-color: #ffcccc;");
                            } else {
                                setStyle("");
                            }
                        } else {
                            setStyle("");
                        }
                    } catch (Exception e) {
                        setStyle(""); // Em caso de erro de parsing, não aplicar cor
                    }
                }
            }
        });
    }

    public void carregarDadosExemplo() {
        List<Aluguel> alugueis = AluguelService.listarTodos();
        ObservableList<Aluguel> observableAlugueis = FXCollections.observableArrayList(alugueis);
        tabelaAlugueis.setItems(observableAlugueis);

        colunaCliente.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getEmailCliente())
        );
        colunaVeiculo.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getPlaca())
        );
        colunaInicio.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getDataInicio())
        );
        colunaFim.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getDataFim())
        );
    }
}
