package mz.co.cargo.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import mz.co.cargo.Service.VeiculoService;

import java.io.IOException;

public class UserController {

    @FXML
    private TableView<Veiculo> tabelaVeiculos;

    @FXML
    private TableColumn<Veiculo, String> colMarca;

    @FXML
    private TableColumn<Veiculo, String> colModelo;

    @FXML
    private TableColumn<Veiculo, Double> colPrecoAluguel;

    @FXML
    private TextField txtMarcaBusca;

    @FXML
    private TextField txtModeloBusca;

    @FXML
    private TextField txtPrecoMinBusca;

    @FXML
    private TextField txtPrecoMaxBusca;

    @FXML
    private TextField txtStatusBusca;

    @FXML
    private TextField txtOrdemBusca;

    private ObservableList<Veiculo> listaDeVeiculos = FXCollections.observableArrayList();

    private ClienteUser userLogado;

    public void carregarInformacoes(ClienteUser user, ActionEvent event) {
        this.userLogado = user;
    }

    @FXML
    public void initialize() {
        colMarca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarca()));
        colModelo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelo()));
        colPrecoAluguel.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoAluguel()).asObject());

        listaDeVeiculos.clear();
        listaDeVeiculos.addAll(VeiculoService.buscarTodosVeiculos());
        tabelaVeiculos.setItems(listaDeVeiculos);
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

    @FXML
    private void abrirTelaDetalhesVeiculo(ActionEvent event) {
        Veiculo veiculoSelecionado = tabelaVeiculos.getSelectionModel().getSelectedItem();

        if (veiculoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/VeiculosCliente.fxml"));
                Parent root = loader.load();

                // Obtém o controller e passa os dados
                VeiculosClienteController controller = loader.getController();
                controller.carregarInformacoes(veiculoSelecionado, userLogado);

                Stage stage = new Stage();
                stage.setTitle("Detalhes do Veículo");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erro ao abrir a tela de detalhes: " + e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum veículo selecionado");
            alert.setContentText("Por favor, selecione um veículo na tabela antes de ver os detalhes.");
            alert.showAndWait();
        }
    }



    @FXML
    private void buscarVeiculosPorMarca() {

    }
}