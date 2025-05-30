package mz.co.cargo.Repository;

import mz.co.cargo.Model.AdminUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    public static void adcionarAdmin(AdminUser admin){
        String sql = "INSERT INTO admin (nome, email, senha) " +
                "VALUES (?, ?, ?);";

        try (Connection conn = AdminDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admin.getNome());
            pstmt.setString(2, admin.getEmail());
            pstmt.setString(3, admin.getSenha());

            pstmt.executeUpdate();
            System.out.println("Admin cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar admin: " + e.getMessage());
        }
    }

    private static AdminUser mapearAdmin(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        AdminUser admin = new AdminUser(nome, email, senha);
        return admin;
    }

    public static boolean existeEmail(String email) {
        String query = "SELECT COUNT(*) FROM admin WHERE email = ?";

        try (Connection conn = AdminDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;  // Retorna true se o email já existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Se nenhum email for encontrado, o email não existe
    }

    public static List<AdminUser> buscarTodosAdmins() {
        List<AdminUser> lista = new ArrayList<>();
        try (Connection conn = AdminDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM admin");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearAdmin(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static AdminUser loginAdmin(String email, String senha) {
        AdminUser admin = null;
        String query = "SELECT * FROM admin WHERE email = ? AND senha = ?";

        try (Connection conn = AdminDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = mapearAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;  // Retorna o Admin se encontrado, ou null se não encontrado
    }

    public static AdminUser buscarPorEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";

        try (Connection conn = AdminDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new AdminUser(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha") // hash completo
                );
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar admin por e-mail: " + e.getMessage());
        }

        return null;
    }






}