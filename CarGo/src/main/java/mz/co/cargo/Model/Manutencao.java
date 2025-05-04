package mz.co.cargo.Model;

public class Manutencao {
    private int id;
    private String placa;
    private String data;
    private String descricao;


    public Manutencao( int Id, String placa, String data, String descricao) {
        this.id = id;
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

