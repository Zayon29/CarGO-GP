package mz.co.cargo.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Service.AluguelService;
import mz.co.cargo.Model.ClienteUser;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.time.LocalDate;

public class VeiculosClienteController {

    @FXML
    private Label marcaLabel;
    @FXML
    private Label modeloLabel;
    @FXML
    private Label placaLabel;
    @FXML
    private Label anoLabel;
    @FXML
    private ImageView imagemVeiculo;
    @FXML
    private Label statusLabel;
    @FXML
    private Button botaoAlugar;
    @FXML
    private DatePicker dataInicioPicker;
    @FXML
    private DatePicker dataFimPicker;

    private Veiculo veiculoSelecionado;
    private ClienteUser clienteLogado;

    public void carregarInformacoes(Veiculo veiculo, ClienteUser cliente) {
        this.veiculoSelecionado = veiculo;
        this.clienteLogado = cliente;

        System.out.println(cliente.getEmail());
        System.out.println(veiculo.getPlaca());

        marcaLabel.setText(veiculo.getMarca());
        modeloLabel.setText(veiculo.getModelo());
        placaLabel.setText(veiculo.getPlaca());
        anoLabel.setText(String.valueOf(veiculo.getAnoFabricacao()));
        statusLabel.setText(veiculo.getStatus());

        String status = veiculo.getStatus().toLowerCase();

        if (veiculo.getStatus().toLowerCase().contains("alugad") || veiculo.getStatus().toLowerCase().contains("indisp")  ) {
            botaoAlugar.setDisable(true);
        }

        if (veiculo.getImagens() != null && !veiculo.getImagens().isEmpty()) {
            String nomeImagem = veiculo.getImagens().get(0); // Ex: "2015_ferrari_488_gtb-wide.jpg"
            String caminhoRelativo = "/images/" + nomeImagem;

            try {
                Image imagem = new Image(getClass().getResource(caminhoRelativo).toExternalForm());
                imagemVeiculo.setImage(imagem);
            } catch (Exception e) {
                System.out.println("Erro ao carregar imagem: " + caminhoRelativo);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void alugarVeiculo(ActionEvent event) {
        LocalDate dataInicio = dataInicioPicker.getValue();
        LocalDate dataFim = dataFimPicker.getValue();
        Alert alert;

        if (dataInicio == null || dataFim == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Por favor, selecione as datas de início e fim do aluguel.");
            alert.showAndWait();
            return;
        }

        if (dataFim.isBefore(dataInicio)) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("A data de fim não pode ser anterior à data de início.");
            alert.showAndWait();
            return;
        }

        String dataInicioStr = dataInicio.toString();
        String dataFimStr = dataFim.toString();

        System.out.println(AluguelService.realizarAluguel(
                veiculoSelecionado.getPlaca(),
                dataInicioStr,
                dataFimStr,
                clienteLogado.getEmail()
        ));

    }



}
