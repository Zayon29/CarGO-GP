package mz.co.cargo.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class VeiculoDatabase {

        private static final String URL = "jdbc:sqlite:veiculo.db";

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

            String sql = "CREATE TABLE IF NOT EXISTS veiculo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "marca TEXT NOT NULL, " +
                    "modelo TEXT NOT NULL, " +
                    "ano INTEGER NOT NULL, " +
                    "placa TEXT UNIQUE NOT NULL, " +
                    "chassi TEXT UNIQUE NOT NULL, " +
                    "preco REAL NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "quilometragem INTEGER NOT NULL, " +
                    "combustivel TEXT NOT NULL, " +
                    "imagens TEXT" +
                    ");";
            stmt.execute(sql);
            System.out.println("Tabela 'veiculo' criada/verificada com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao criar tabela de veículos: " + e.getMessage());
        }
    }
    }


