package mz.co.cargo.Repository;

import java.sql.*;

public class ClienteDatabase {
    private static final String URL = "jdbc:sqlite:cliente.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Erro conexão cliente: " + e.getMessage());
            return null;
        }
    }

    public static void inicializar() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS cliente (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "senha TEXT NOT NULL);";
            stmt.execute(sql);
            System.out.println("Tabela 'cliente' pronta!");
        } catch (Exception e) {
            System.out.println("Erro ao criar tabela cliente: " + e.getMessage());
        }
    }
}

