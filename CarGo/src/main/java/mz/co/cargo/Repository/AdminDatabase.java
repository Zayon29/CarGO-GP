package mz.co.cargo.Repository;

import java.sql.*;

public class AdminDatabase {
    private static final String URL = "jdbc:sqlite:admin.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Erro conexão admin: " + e.getMessage());
            return null;
        }
    }

    public static void inicializar() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Criação da tabela
            String sql = "CREATE TABLE IF NOT EXISTS admin (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "senha TEXT NOT NULL);";
            stmt.execute(sql);
            System.out.println("Tabela 'admin' pronta!");

            // Verifica se já existe algum admin
            String checkSql = "SELECT COUNT(*) FROM admin;";
            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Nenhum admin encontrado, insere o admin genérico
                    String insertSql = "INSERT INTO admin (nome, email, senha) VALUES (?, ?, ?);";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                        pstmt.setString(1, "Administrador");
                        pstmt.setString(2, "admin@admin.com");
                        pstmt.setString(3, "admin12345"); // Você pode usar hash aqui se quiser mais segurança
                        pstmt.executeUpdate();
                        System.out.println("Admin genérico criado.");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao criar tabela admin ou inserir admin padrão: " + e.getMessage());
        }
    }

}

