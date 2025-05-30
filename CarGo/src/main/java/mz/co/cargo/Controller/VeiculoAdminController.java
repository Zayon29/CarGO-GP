package mz.co.cargo.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Model.Manutencao;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Service.VeiculoService;
import mz.co.cargo.Service.ManutencaoService;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class VeiculoAdminController {

        @FXML private TextField marcaTextField;
        @FXML private TextField modeloTextField;
        @FXML private TextField anoFabricacaoTextField;
        @FXML private TextField placaTextField;
        @FXML private TextField precoTextField;
    @FXML
    private ComboBox<String> statusComboBox;
        @FXML private TextField kmTextField;
        @FXML private TextField combustivelTextField;

    @FXML
    private TableView<Manutencao> tableHistorico;

    @FXML
    private TableColumn<Manutencao, String> colunaData;

    @FXML
    private TableColumn<Manutencao, String> colunaDescricao;

        private Veiculo veiculoSelecionado;

    private AdminUser adminLogado;
    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
    }


    @FXML
    public void initialize() {
        colunaData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
        colunaDescricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));

        ObservableList<String> opcoesStatus = FXCollections.observableArrayList("Disponível", "Em Manutenção");
        statusComboBox.setItems(opcoesStatus);

        statusComboBox.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            if ("Em Manutenção".equals(newStatus) && !"Em Manutenção".equals(oldStatus)) {
                Optional<String> motivo = solicitarMotivoManutencao();
                if (motivo.isPresent() && !motivo.get().trim().isEmpty()) {
                    Manutencao manut = new Manutencao(
                            veiculoSelecionado.getPlaca(),
                            java.time.LocalDate.now().toString(),
                            motivo.get()
                    );
                    ManutencaoService.registrarManutencao(manut);
                    carregarHistorico(veiculoSelecionado);
                } else {
                    statusComboBox.setValue("Status alterado para manutenção");
                }
            }
        });

    }



    public void carregarVeiculo(Veiculo veiculo) {
            this.veiculoSelecionado = veiculo;

            marcaTextField.setText(veiculo.getMarca());
            modeloTextField.setText(veiculo.getModelo());
            anoFabricacaoTextField.setText(String.valueOf(veiculo.getAnoFabricacao()));
            placaTextField.setText(veiculo.getPlaca());
            precoTextField.setText(String.valueOf(veiculo.getPrecoAluguel()));
            kmTextField.setText(String.valueOf(veiculo.getQuilometragem()));
            combustivelTextField.setText(veiculo.getTipoCombustivel());

        statusComboBox.setValue(veiculo.getStatus());
        if ("Alugado".equalsIgnoreCase(veiculo.getStatus())) {
            statusComboBox.setDisable(true);
        } else {
            statusComboBox.setDisable(false);
        }


        carregarHistorico(veiculo);
        }

    private void carregarHistorico(Veiculo veiculo) {
        String placa = veiculo.getPlaca();
        var manutencoes = ManutencaoService.listarPorPlaca(placa);
        tableHistorico.setItems(FXCollections.observableArrayList(manutencoes));
    }

        @FXML
        private void salvarAlteracoes() {
            try {
                veiculoSelecionado.setMarca(marcaTextField.getText());
                veiculoSelecionado.setModelo(modeloTextField.getText());
                veiculoSelecionado.setAnoFabricacao(Integer.parseInt(anoFabricacaoTextField.getText()));
                veiculoSelecionado.setPlaca(placaTextField.getText());
                veiculoSelecionado.setPrecoAluguel(Double.parseDouble(precoTextField.getText()));
                veiculoSelecionado.setStatus(statusComboBox.getValue());
                veiculoSelecionado.setQuilometragem(Integer.parseInt(kmTextField.getText()));
                veiculoSelecionado.setTipoCombustivel(combustivelTextField.getText());

                VeiculoService.editarVeiculo(veiculoSelecionado);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Veículo atualizado com sucesso!");
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Preencha corretamente os campos numéricos.");
                alert.showAndWait();
            }
        }

        @FXML
        private void voltar(ActionEvent event) {
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

    private Optional<String> solicitarMotivoManutencao() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Motivo da Manutenção");
        dialog.setHeaderText("Informe o motivo da manutenção");
        dialog.setContentText("Motivo:");

        return dialog.showAndWait();
    }
}
