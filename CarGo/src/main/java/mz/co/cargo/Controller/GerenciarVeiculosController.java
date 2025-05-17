package mz.co.cargo.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.ClienteRepository;
import mz.co.cargo.Service.VeiculoService;

import java.io.IOException;

public class GerenciarVeiculosController {



    @FXML
    private TableView<Veiculo> tabelaVeiculos;
    @FXML
    private TableColumn<Veiculo, String> colMarca;
    @FXML
    private TableColumn<Veiculo, String> colModelo;
    @FXML
    private TableColumn<Veiculo, Integer> colAnoFabricacao;
    @FXML
    private TableColumn<Veiculo, String> colPlaca;
    @FXML
    private TableColumn<Veiculo, String> colChassi;
    @FXML
    private TableColumn<Veiculo, Double> colPrecoAluguel;
    @FXML
    private TableColumn<Veiculo, String> colStatus;
    @FXML
    private TableColumn<Veiculo, Integer> colQuilometragem;
    @FXML
    private  TableColumn<Veiculo, String> colCombustivel;

    @FXML
    private TextField campoBusca;

    private AdminUser adminLogado;
    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
    }

    @FXML
    public void initialize() {
        colMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarca()));
        colModelo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelo()));
        colAnoFabricacao.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAnoFabricacao()).asObject());
        colPlaca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlaca()));
        colChassi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChassi()));
        colPrecoAluguel.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoAluguel()).asObject());
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        colQuilometragem.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuilometragem()).asObject());
        colCombustivel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCombustivel()));

        atualizarTabela();
    }

    @FXML
    private void atualizarTabela() {
        tabelaVeiculos.setItems(FXCollections.observableArrayList(VeiculoService.buscarTodosVeiculos()));
    }

    //@FXML
    //private void buscarVeiculo() {
    //    String nome = campoBusca.getText();
    //    if (nome.isEmpty()) {
    //        atualizarTabela();
    //    } else {
     //       tabelaVeiculos.setItems(FXCollections.observableArrayList(ClienteRepository.buscarClientesPorNome(nome)));
    //    }
    //}

    @FXML
    private void excluirVeiculoSelecionado() {
        Veiculo veiculoSelecionado = tabelaVeiculos.getSelectionModel().getSelectedItem();
        if (veiculoSelecionado != null) {
            boolean sucesso = VeiculoService.removerVeiculo(veiculoSelecionado.getPlaca());
            if (sucesso) {
                tabelaVeiculos.getItems().remove(veiculoSelecionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Veiculo excluído com sucesso.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir o veiculo.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um veiculo para excluir.");
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


