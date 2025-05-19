package mz.co.cargo.Repository;

public class DatabaseInitializer {

        public static void initializeAll() {
            VeiculoDatabase.inicializar();
            ClienteDatabase.inicializar();
            AdminDatabase.inicializar();
            ManutencaoDatabase.inicializar();
            AluguelDatabase.inicializar();
        }
    }

