package mz.co.cargo.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mz.co.cargo.Repository.VeiculoRepository;
import mz.co.cargo.Model.Veiculo;
import java.util.Collections;

public class CadastroVeiculoController {

    @FXML
    private TextField marcaTextField;
    @FXML
    private TextField modeloTextField;
    @FXML
    private TextField anoFabricacaoTextField;
    @FXML
    private TextField placaTextField;
    @FXML
    private TextField chassiTextField;
    @FXML
    private TextField precoTextField;
    @FXML
    private TextField statusTextField;
    @FXML
    private TextField kmTextField;
    @FXML
    private TextField combustivelTextField;
    @FXML
    private TextField caminhoTextField;

    @FXML
    private void initialize() {
        // Esse método é chamado automaticamente após o carregamento do FXML
    }

    @FXML
    private void salvarVeiculo() {
        try {
            String marca = marcaTextField.getText();
            String modelo = modeloTextField.getText();
            int ano = Integer.parseInt(anoFabricacaoTextField.getText());
            String placa = placaTextField.getText();
            String chassi = chassiTextField.getText();
            double preco = Double.parseDouble(precoTextField.getText());
            String status = statusTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());
            String combustivel = combustivelTextField.getText();
            String caminhoImagem = caminhoTextField.getText();

            // Cria o objeto Veiculo
            Veiculo veiculo = new Veiculo(marca, modelo, ano, placa, chassi, preco, status, km, combustivel, Collections.singletonList(caminhoImagem));

            // Chama o método do repositório
            VeiculoRepository.adicionarVeiculo(veiculo);


            // Alerta de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Veículo cadastrado com sucesso!");
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println("Erro ao salvar veículo: " + e.getMessage());
            // Aqui você também pode mostrar uma janela de alerta com JavaFX se quiser.
        }
    }
}