package mz.co.cargo.Repository;

import mz.co.cargo.Model.Aluguel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AluguelRepository {

    public static void registrarAluguel(Aluguel aluguel) {
        String sql = "INSERT INTO aluguel (placa, data_inicio, data_fim, email_cliente) VALUES (?, ?, ?, ?)";
        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aluguel.getPlaca());
            pstmt.setString(2, aluguel.getDataInicio());
            pstmt.setString(3, aluguel.getDataFim());
            pstmt.setString(4, aluguel.getEmailCliente());
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
                        rs.getString("data_fim"),
                        rs.getString("email_cliente")

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

    public static List<Aluguel> buscarPorEmailCliente(String email) {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT * FROM aluguel WHERE email_cliente = ? ORDER BY data_inicio DESC";

        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Aluguel(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("data_inicio"),
                        rs.getString("data_fim"),
                        rs.getString("email_cliente")
                ));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar aluguéis por cliente: " + e.getMessage());
        }

        return lista;
    }

    public static String limparAlugueisPorEmailCliente(String emailCliente) {
        if (emailCliente == null || emailCliente.isEmpty()) {
            return "Erro: email do cliente inválido.";
        }

        List<Aluguel> alugueisCliente = buscarPorEmailCliente(emailCliente);

        if (alugueisCliente.isEmpty()) {
            return "Nenhum aluguel encontrado para este cliente.";
        }

        try (Connection conn = AluguelDatabase.connect()) {
            // Começar transação
            conn.setAutoCommit(false);

            try {
                // Atualizar status dos veículos para DISPONIVEL
                for (Aluguel aluguel : alugueisCliente) {
                    VeiculoRepository.alterarStatusVeiculo(aluguel.getPlaca(), "DISPONIVEL");
                }

                // Deletar os alugueis do cliente
                String sqlDelete = "DELETE FROM aluguel WHERE email_cliente = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
                    pstmt.setString(1, emailCliente);
                    pstmt.executeUpdate();
                }

                // Commit na transação
                conn.commit();
                return "Todos os aluguéis do cliente foram removidos com sucesso.";

            } catch (Exception e) {
                conn.rollback(); // rollback em caso de erro
                System.out.println("Erro ao limpar aluguéis do cliente: " + e.getMessage());
                return "Erro ao limpar aluguéis do cliente.";
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Erro na conexão com o banco: " + e.getMessage());
            return "Erro na conexão com o banco.";
        }
    }

    public static List<Aluguel> buscarAlugueisAtivos() {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT * FROM aluguel ORDER BY data_inicio DESC";

        try (Connection conn = AluguelDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String placa = rs.getString("placa");

                // Verifica o status diretamente pela placa do veículo
                String status = VeiculoRepository.buscarStatusPorPlaca(placa);

                if ("ALUGADO".equalsIgnoreCase(status)) {
                    lista.add(new Aluguel(
                            rs.getInt("id"),
                            placa,
                            rs.getString("data_inicio"),
                            rs.getString("data_fim"),
                            rs.getString("email_cliente")
                    ));
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar aluguéis ativos: " + e.getMessage());
        }

        return lista;
    }







}

