package mz.co.cargo.Controller;

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
import mz.co.cargo.Model.Manutencao;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Service.VeiculoService;
import mz.co.cargo.Service.ManutencaoService;

import java.io.IOException;

public class VeiculoAdminController {

        @FXML private TextField marcaTextField;
        @FXML private TextField modeloTextField;
        @FXML private TextField anoFabricacaoTextField;
        @FXML private TextField placaTextField;
        @FXML private TextField precoTextField;
        @FXML private TextField statusTextField;
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
    }



    public void carregarVeiculo(Veiculo veiculo) {
            this.veiculoSelecionado = veiculo;

            marcaTextField.setText(veiculo.getMarca());
            modeloTextField.setText(veiculo.getModelo());
            anoFabricacaoTextField.setText(String.valueOf(veiculo.getAnoFabricacao()));
            placaTextField.setText(veiculo.getPlaca());
            precoTextField.setText(String.valueOf(veiculo.getPrecoAluguel()));
            statusTextField.setText(veiculo.getStatus());
            kmTextField.setText(String.valueOf(veiculo.getQuilometragem()));
            combustivelTextField.setText(veiculo.getTipoCombustivel());

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
                veiculoSelecionado.setStatus(statusTextField.getText());
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

}
