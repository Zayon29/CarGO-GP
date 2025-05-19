package mz.co.cargo.Model;
public class Aluguel {
    private int id;
    private String placa;
    private String dataInicio;
    private String dataFim;

    public Aluguel(int id, String placa, String dataInicio, String dataFim) {
        this.id = id;
        this.placa = placa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String getPlaca() { return placa; }
    public String getDataInicio() { return dataInicio; }
    public String getDataFim() { return dataFim; }
}

