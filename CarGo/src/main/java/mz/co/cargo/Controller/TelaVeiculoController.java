package mz.co.cargo.Controller;

import javafx.fxml.FXML;
import mz.co.cargo.Model.Veiculo;

import java.awt.*;

public class TelaVeiculoController {

    @FXML
    private Label lblMarca;

    @FXML
    private Label lblModelo;

    @FXML
    private Label lblPreco;

    public void exibirDetalhes(Veiculo veiculo) {
        lblMarca.setText(veiculo.getMarca());
        lblModelo.setText(veiculo.getModelo());
        lblPreco.setText(String.valueOf(veiculo.getPrecoAluguel()));
    }
}