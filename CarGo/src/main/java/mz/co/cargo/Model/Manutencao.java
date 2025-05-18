package mz.co.cargo.Model;

public class Manutencao {
    protected int id;
    protected String placa;
    protected String data;
    protected String descricao;


    public Manutencao(String placa, String data, String descricao) {
        this.placa = placa;
        this.descricao = descricao;
        this.data = data;
    }

    public int getId() {
        return id;
    }
    public String getPlaca(){
        return placa;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setPlaca(String placa){
        this.placa = placa;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

