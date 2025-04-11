package mz.co.cargo.Model;

import java.util.List;

public class Veiculo {
    private String marca;
    private String modelo;
    private int anoFabricacao;
    private String placa;
    private String chassi;
    private double precoAluguel;
    private String status;
    private int quilometragem;
    private String tipoCombustivel;
    private List<String> imagens; // Lista com caminhos das imagens

    public Veiculo(String marca, String modelo, int anoFabricacao, String placa, String chassi,
                   double precoAluguel, String status, int quilometragem, String tipoCombustivel, List<String> imagens) {
        this.marca = marca;
        this.modelo = modelo;
        this.anoFabricacao = anoFabricacao;
        this.placa = placa;
        this.chassi = chassi;
        this.precoAluguel = precoAluguel;
        this.status = status;
        this.quilometragem = quilometragem;
        this.tipoCombustivel = tipoCombustivel;
        this.imagens = imagens;
    }

    // Getters e Setters


    public String getMarca(){ return marca;}
    public void setMarca(String marca){this.marca = marca;}

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(int anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public double getPrecoAluguel() { return precoAluguel; }
    public void setPrecoAluguel(double precoAluguel) { this.precoAluguel = precoAluguel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getQuilometragem() { return quilometragem; }
    public void setQuilometragem(int quilometragem) { this.quilometragem = quilometragem; }

    public String getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(String tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }
}
