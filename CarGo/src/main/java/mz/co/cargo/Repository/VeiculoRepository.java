package mz.co.cargo.Repository;
import mz.co.cargo.Model.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VeiculoRepository {

    public static void adicionarVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (marca, modelo, ano, placa, chassi, preco, status, quilometragem, combustivel, imagens) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, veiculo.getMarca());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setInt(3, veiculo.getAnoFabricacao());
            pstmt.setString(4, veiculo.getPlaca());
            pstmt.setString(5, veiculo.getChassi());
            pstmt.setDouble(6, veiculo.getPrecoAluguel());
            pstmt.setString(7, veiculo.getStatus());
            pstmt.setInt(8, veiculo.getQuilometragem());
            pstmt.setString(9, veiculo.getTipoCombustivel());

            String imagensStr = String.join(";", veiculo.getImagens());
            pstmt.setString(10, imagensStr);

            pstmt.executeUpdate();
            System.out.println("Veículo cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    public static void removerVeiculo(String placa) {
        String sql = "DELETE FROM veiculo WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Veículo removido com sucesso!");
            } else {
                System.out.println("Nenhum veículo encontrado com a placa: " + placa);
            }

        } catch (Exception e) {
            System.out.println("Erro ao remover veículo: " + e.getMessage());
        }
    }


    public static void editarVeiculo(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET marca = ?, modelo = ?, ano = ?, preco = ?, status = ?, quilometragem = ?, combustivel = ?, imagens = ? " +
                "WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, veiculo.getMarca());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setInt(3, veiculo.getAnoFabricacao());
            pstmt.setDouble(4, veiculo.getPrecoAluguel());
            pstmt.setString(5, veiculo.getStatus());
            pstmt.setInt(6, veiculo.getQuilometragem());
            pstmt.setString(7, veiculo.getTipoCombustivel());
            pstmt.setString(8, String.join(";", veiculo.getImagens()));
            pstmt.setString(9, veiculo.getPlaca());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Veículo atualizado com sucesso!");
            } else {
                System.out.println("Nenhum veículo encontrado com a placa: " + veiculo.getPlaca());
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar veículo: " + e.getMessage());
        }

    }
    public static boolean existePlaca(String placa) {
        String sql = "SELECT COUNT(*) FROM veiculo WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se a placa já existir
            }

        } catch (Exception e) {
            System.out.println("Erro ao verificar placa: " + e.getMessage());
        }
        return false;
    }
    public static boolean existeChassi(String chassi) {
        String sql = "SELECT COUNT(*) FROM veiculo WHERE chassi = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, chassi);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se o chassi já existir
            }

        } catch (Exception e) {
            System.out.println("Erro ao verificar chassi: " + e.getMessage());
        }
        return false;
    }

    public static void alterarStatusVeiculo(String placa, String novoStatus) {
        String sql = "UPDATE veiculo SET status = ? WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus);
            pstmt.setString(2, placa);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Status do veículo atualizado para: " + novoStatus);
            } else {
                System.out.println("Erro: Nenhum veículo encontrado com a placa: " + placa);
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar status do veículo: " + e.getMessage());
        }
    }
    public static String buscarStatusPorPlaca(String placa) {
        String sql = "SELECT status FROM veiculo WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar status do veículo: " + e.getMessage());
        }
        return "DESCONHECIDO"; // Caso não encontre o veículo
    }

    private static Veiculo mapearVeiculo(ResultSet rs) throws SQLException {
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        int ano = rs.getInt("ano");
        String placa = rs.getString("placa");
        String chassi = rs.getString("chassi");
        double preco = rs.getDouble("preco");
        String status = rs.getString("status");
        int quilometragem = rs.getInt("quilometragem");
        String combustivel = rs.getString("combustivel");

        // Supondo que o campo "imagens" seja armazenado como string separada por ponto e vírgula
        String imagensStr = rs.getString("imagens");
        List<String> imagens = new ArrayList<>();
        if (imagensStr != null && !imagensStr.isEmpty()) {
            imagens = new ArrayList<>(Arrays.asList(imagensStr.split(";")));
        }

        Veiculo veiculo = new Veiculo(marca, modelo, ano, placa, chassi, preco, status, quilometragem, combustivel, imagens);
        return veiculo;
    }


    public static List<Veiculo> buscarTodosVeiculos() {
        List<Veiculo> lista = new ArrayList<>();
        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM veiculo");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearVeiculo(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    public static Veiculo buscarVeiculoPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculo WHERE placa = ?;";

        try (Connection conn = VeiculoDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapearVeiculo(rs);
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar veículo: " + e.getMessage());
        }
        return null; // Retorna null caso não encontre o veículo
    }





    //listar veiculo -falta



}
