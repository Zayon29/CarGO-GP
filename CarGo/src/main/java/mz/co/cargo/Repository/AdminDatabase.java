package mz.co.cargo.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
            String sql = "CREATE TABLE IF NOT EXISTS admin (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "senha TEXT NOT NULL);";
            stmt.execute(sql);
            System.out.println("Tabela 'admin' pronta!");
        } catch (Exception e) {
            System.out.println("Erro ao criar tabela admin: " + e.getMessage());
        }
    }
}

