package mz.co.cargo.Model;
public class Aluguel {
    private int id;
    private String placa;
    private String dataInicio;
    private String dataFim;
    private String emailCliente;

    public Aluguel(int id, String placa, String dataInicio, String dataFim, String emailCliente) {
        this.id = id;
        this.placa = placa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.emailCliente = emailCliente;
    }

    public String getPlaca() { return placa; }
    public String getDataInicio() { return dataInicio; }
    public String getDataFim() { return dataFim; }
    public String getEmailCliente(){ return emailCliente;}

}

