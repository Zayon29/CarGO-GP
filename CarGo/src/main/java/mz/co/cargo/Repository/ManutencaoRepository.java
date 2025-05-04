package mz.co.cargo.Repository;

import mz.co.cargo.Repository.ManutencaoDatabase;
import mz.co.cargo.Model.Manutencao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ManutencaoRepository {

    public static void registrarManutencao(Manutencao m) {
        String sql = "INSERT INTO manutencao (placa, descricao, data) VALUES (?, ?, ?);";

        try (Connection conn = ManutencaoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, m.getPlaca());
            pstmt.setString(2, m.getDescricao());
            pstmt.setString(3, m.getData());

            pstmt.executeUpdate();
            System.out.println("Manutenção registrada com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao registrar manutenção: " + e.getMessage());
        }
    }

    public static List<Manutencao> buscarHistoricoPorPlaca(String placa) {
        List<Manutencao> lista = new ArrayList<>();
        String sql = "SELECT * FROM manutencao WHERE placa = ? ORDER BY data DESC;";

        try (Connection conn = ManutencaoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Manutencao(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("descricao"),
                        rs.getString("data")
                ));
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }

        return lista;
    }
}
