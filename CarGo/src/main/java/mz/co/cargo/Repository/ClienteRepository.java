package mz.co.cargo.Repository;

import mz.co.cargo.Model.ClienteUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public static void adicionarCliente(ClienteUser cliente) {
        String sql = "INSERT INTO cliente (nome, email, senha) VALUES (?, ?, ?);";

        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getSenha());

            pstmt.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private static ClienteUser mapearCliente(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        return new ClienteUser(nome, email, senha);
    }

    public static boolean existeEmail(String email) {
        String query = "SELECT COUNT(*) FROM cliente WHERE email = ?";

        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<ClienteUser> buscarTodosClientes() {
        List<ClienteUser> lista = new ArrayList<>();
        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cliente");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<ClienteUser> buscarClientesPorNome(String nome) {
        List<ClienteUser> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";
        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean excluirCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static ClienteUser loginCliente(String email, String senha) {
        ClienteUser cliente = null;
        String query = "SELECT * FROM cliente WHERE email = ? AND senha = ?";

        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }
}
