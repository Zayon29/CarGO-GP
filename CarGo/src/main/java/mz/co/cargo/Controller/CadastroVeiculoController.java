package mz.co.cargo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mz.co.cargo.Repository.VeiculoRepository;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Service.VeiculoService;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mz.co.cargo.Model.AdminUser;
import javafx.stage.Window;

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
    private void salvarVeiculo(ActionEvent event) {
        try {
            String marca = marcaTextField.getText();
            String modelo = modeloTextField.getText();
            int anoFabricacao = Integer.parseInt(anoFabricacaoTextField.getText());
            String placa = placaTextField.getText();
            String chassi = chassiTextField.getText();
            double preco = Double.parseDouble(precoTextField.getText());
            String status = statusTextField.getText();
            int km = Integer.parseInt(kmTextField.getText());
            String combustivel = combustivelTextField.getText();

            String caminhoImagemStr = caminhoTextField.getText();
            List<String> caminhos = Arrays.asList(caminhoImagemStr.split("\\s*,\\s*"));
            List<String> nomesImagens = new ArrayList<>();

            for (String caminho : caminhos) {
                File arquivo = new File(caminho);
                nomesImagens.add(arquivo.getName()); // Só o nome da imagem
            }

            Veiculo veiculo = new Veiculo(
                    marca, modelo, anoFabricacao, placa, chassi,
                    preco, status, km, combustivel, nomesImagens
            );

            String resultado = VeiculoService.cadastrarVeiculo(veiculo);

            // Se contiver "Erro", mostra um alerta de erro
            if (resultado.startsWith("Erro")) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao cadastrar veículo", resultado);
            } else {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", resultado);
                limparCampos();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "Preencha corretamente os campos numéricos (ano, preço, quilometragem).");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro inesperado", e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    private void limparCampos() {
        marcaTextField.clear();
        modeloTextField.clear();
        anoFabricacaoTextField.clear();
        placaTextField.clear();
        chassiTextField.clear();
        precoTextField.clear();
        statusTextField.clear();
        kmTextField.clear();
        combustivelTextField.clear();
        caminhoTextField.clear();
    }

    private AdminUser adminLogado;

    public void setAdminLogado(AdminUser admin) {
        this.adminLogado = admin;
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

    @FXML
    private void selecionarArquivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Veículo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
        );

        // Obtém a janela da aplicação a partir do campo
        Window stage = caminhoTextField.getScene().getWindow();

        File arquivo = fileChooser.showOpenDialog(stage);

        if (arquivo != null) {
            caminhoTextField.setText(arquivo.getAbsolutePath());
        }
    }
}