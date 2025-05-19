package mz.co.cargo.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AluguelDatabase {
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
            String aluguelSql = "CREATE TABLE IF NOT EXISTS aluguel (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "placa TEXT NOT NULL, " +
                    "data_inicio TEXT NOT NULL, " +
                    "data_fim TEXT NOT NULL, " +
                    "FOREIGN KEY (placa) REFERENCES veiculo(placa)" +
                    ");";
            stmt.execute("ALTER TABLE aluguel ADD COLUMN email_cliente TEXT");
            System.out.println("Tabela 'Aluguel' pronta!");
        } catch (Exception e) {
            System.out.println("Erro ao criar tabela Alguel: " + e.getMessage());
        }
    }

}
