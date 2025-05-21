package mz.co.cargo.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Service.AluguelService;

import java.io.IOException;
import java.util.List;

public class AlugueisClienteController {

    @FXML private TableView<Aluguel> tabelaAlugueis;
    @FXML private TableColumn<Aluguel, String> colVeiculo;
    @FXML private TableColumn<Aluguel, String> colDataInicio;
    @FXML private TableColumn<Aluguel, String> colDataFim;
    @FXML private TableColumn<Aluguel, String> colStatus;
    @FXML private Label mensagemLabel;

    private ClienteUser cliente;

    public void carregarCliente(ClienteUser cliente) {
        this.cliente = cliente;
        carregarAlugueis();
    }

    private void carregarAlugueis() {
        List<Aluguel> alugueis = AluguelService.listarAlugueisAtivos(cliente.getEmail());
        ObservableList<Aluguel> lista = FXCollections.observableArrayList(alugueis);
        tabelaAlugueis.setItems(lista);

        colVeiculo.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getPlaca())
        );
        colDataInicio.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getDataInicio())
        );
        colDataFim.setCellValueFactory(aluguel ->
                new SimpleStringProperty(aluguel.getValue().getDataFim())
        );
        colStatus.setCellValueFactory(aluguel ->
                new SimpleStringProperty("ALUGADO")  // Valor fixo, já que é ativo
        );
    }

    @FXML
    public void devolverVeiculo() {
        Aluguel selecionado = tabelaAlugueis.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mensagemLabel.setText("Selecione um aluguel para devolver.");
            return;
        }

        boolean sucesso = AluguelService.removerAluguelAtivo(selecionado.getPlaca(),cliente.getEmail());
        if (sucesso) {
            mensagemLabel.setText("Veículo devolvido com sucesso.");
            carregarAlugueis();
            AluguelService.atualizarStatusDosVeiculos();
        } else {
            mensagemLabel.setText("Erro ao devolver veículo.");
        }
    }

    @FXML
    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mz/co/cargo/User.fxml"));
            Parent root = loader.load();

            UserController controller = loader.getController();
            controller.carregarInformacoes(cliente, event);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Área do Cliente - CarGO");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
