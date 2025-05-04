package mz.co.cargo.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ManutencaoDatabase {
    private static final String URL = "jdbc:sqlite:manutencao.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Erro conexão com o banco: " + e.getMessage());
            return null;
        }
    }

    public static void inicializar() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS manutencao (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "placa TEXT NOT NULL, " +
                    "descricao TEXT NOT NULL, " +
                    "data TEXT NOT NULL" + // Remova a vírgula daqui
                    ");";

            stmt.execute(sql);
            System.out.println("Tabela 'manutencao' criada/verificada com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao criar tabela de Manutenção: " + e.getMessage());
        }
    }
}
