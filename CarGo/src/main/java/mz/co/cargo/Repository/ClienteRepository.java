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
        String sql = "SELECT * FROM cliente ORDER BY nome ASC"; // <-- ordena por nome
        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
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

    public static boolean excluirCliente(String email) {
        String sql = "DELETE FROM cliente WHERE email = ?";
        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
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
    public static boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM cliente WHERE email = ? LIMIT 1";

        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Erro ao verificar e-mail do cliente: " + e.getMessage());
            return false;
        }
    }

    public static boolean editarCliente(String emailAntigo, ClienteUser clienteAtualizado) {
        String sqlUpdateCliente = "UPDATE cliente SET nome = ?, email = ?, senha = ? WHERE email = ?";
        String sqlUpdateAluguel = "UPDATE aluguel SET email_cliente = ? WHERE email_cliente = ?";

        Connection connCliente = null;
        Connection connAluguel = null;

        try {
            // Conexões independentes
            connCliente = ClienteDatabase.connect();
            connAluguel = AluguelDatabase.connect();

            // Auto-commit deve ser falso para controle transacional
            connCliente.setAutoCommit(false);
            connAluguel.setAutoCommit(false);

            // Atualizar dados do cliente
            try (PreparedStatement pstmtCliente = connCliente.prepareStatement(sqlUpdateCliente)) {
                pstmtCliente.setString(1, clienteAtualizado.getNome());
                pstmtCliente.setString(2, clienteAtualizado.getEmail());
                pstmtCliente.setString(3, clienteAtualizado.getSenha());
                pstmtCliente.setString(4, emailAntigo);
                pstmtCliente.executeUpdate();
                connCliente.commit();
            }

            // Atualizar email nos alugueis, se mudou
            if (!emailAntigo.equals(clienteAtualizado.getEmail())) {
                try (PreparedStatement pstmtAluguel = connAluguel.prepareStatement(sqlUpdateAluguel)) {
                    pstmtAluguel.setString(1, clienteAtualizado.getEmail());
                    pstmtAluguel.setString(2, emailAntigo);
                    pstmtAluguel.executeUpdate();
                    connAluguel.commit();

                }
            }

            // Commit nas duas conexões


            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao editar cliente: " + e.getMessage());

            try {
                if (connCliente != null) connCliente.rollback();
                if (connAluguel != null) connAluguel.rollback();
            } catch (SQLException ex) {
                System.out.println("Erro no rollback: " + ex.getMessage());
            }

            return false;
        } finally {
            try {
                if (connCliente != null) connCliente.setAutoCommit(true);
                if (connAluguel != null) connAluguel.setAutoCommit(true);
                if (connCliente != null) connCliente.close();
                if (connAluguel != null) connAluguel.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }
    }

    public static ClienteUser buscarPorEmail(String email) {
        String sql = "SELECT * FROM cliente WHERE email = ?";

        try (Connection conn = ClienteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ClienteUser(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha") // hash completo
                );
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente por e-mail: " + e.getMessage());
        }

        return null; // cliente não encontrado
    }







}
