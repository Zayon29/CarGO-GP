package mz.co.cargo.Repository;

import mz.co.cargo.Model.Aluguel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AluguelRepository {

    public static void registrarAluguel(Aluguel aluguel) {
        String sql = "INSERT INTO aluguel (placa, data_inicio, data_fim) VALUES (?, ?, ?)";
        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aluguel.getPlaca());
            pstmt.setString(2, aluguel.getDataInicio());
            pstmt.setString(3, aluguel.getDataFim());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro ao registrar aluguel: " + e.getMessage());
        }
    }

    public static List<Aluguel> buscarPorPlaca(String placa) {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT * FROM aluguel WHERE placa = ?";

        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Aluguel(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("data_inicio"),
                        rs.getString("data_fim")
                ));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar aluguéis: " + e.getMessage());
        }

        return lista;
    }

    public static boolean haConflito(String placa, String novaInicio, String novaFim) {
        String sql = "SELECT * FROM aluguel WHERE placa = ? AND " +
                "(date(data_inicio) <= date(?) AND date(data_fim) >= date(?))";

        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            pstmt.setString(2, novaFim);
            pstmt.setString(3, novaInicio);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // existe conflito
        } catch (Exception e) {
            System.out.println("Erro ao verificar conflito: " + e.getMessage());
            return true;
        }
    }
}

