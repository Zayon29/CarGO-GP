package mz.co.cargo.Repository;

import mz.co.cargo.Model.AdminUser;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminRepository {

    public static void adcionarAdmin(AdminUser admin){
        String sql = "INSERT INTO admin (nome, email, senha) " +
                "VALUES (?, ?, ?);";

        try (Connection conn = VeiculoDatabase.connect();
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
}