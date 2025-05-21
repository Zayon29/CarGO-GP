package mz.co.cargo.Service;

import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.DatabaseInitializer;
import mz.co.cargo.Repository.VeiculoRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static mz.co.cargo.Service.MenuVeiculo.listarVeiculos;

public class MenuCliente {
    private static final Scanner scanner = new Scanner(System.in);

    // Esse main é para testar a parte de cliente
    public static void main(String[] args) {

        DatabaseInitializer.initializeAll();

        // Cliente de teste
        //ClienteUser cliente1 = new ClienteUser("teste", "teste@email.com", "123456");
        //String resultado = ClienteService.cadastrarCliente(cliente1);
        //System.out.println(resultado);

        listarClientes();

        String email, senha;
        ClienteUser cliente = null;

        int opcao;
        do {
            System.out.println("\n=== MENU INICIAL CLIENTE ===");
            System.out.println("1. Login");
            System.out.println("2. Cadastro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Digite um número válido: ");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> realizarLogin();
                case 2 -> cadastrarCliente();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

    }

    public static void menuCliente(ClienteUser cliente) {
        int opcao;
        do {
            System.out.println("\n=== MENU CLIENTE ===");
            System.out.println("1. Ver informações cliente");
            System.out.println("2. Ver carros");
            System.out.println("3. Alugar carro");
            System.out.println("4. Ver meus aluguéis ativos");
            System.out.println("5. Devolver um carro alugado");
            System.out.println("6. Editar dados do cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Digite um número válido: ");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> exibirInfos(cliente);
                case 2 -> menuBuscarVeiculos();
                case 3 -> realizarAluguelCliente(cliente, scanner);
                case 4 -> verHistoricoCliente(cliente);
                case 5 -> devolverCarroAlugado(cliente);
                case 6 -> editarDadosCliente(cliente);
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void realizarAluguelCliente(ClienteUser cliente, Scanner scanner) {
        System.out.print("Digite a placa do veículo que deseja alugar: ");
        String placa = scanner.nextLine().toUpperCase();

        System.out.print("Digite a data de início do aluguel (YYYY-MM-DD): ");
        String dataInicio = scanner.nextLine();

        System.out.print("Digite a data de fim do aluguel (YYYY-MM-DD): ");
        String dataFim = scanner.nextLine();

        String resultado = AluguelService.realizarAluguel(placa, dataInicio, dataFim, cliente.getEmail());
        System.out.println(resultado);
    }


    public static String cadastrarCliente() {

        System.out.println("\n=== CADASTRO DE CLIENTE ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        ClienteUser cliente1 = new ClienteUser(nome, email, senha);

        return ClienteService.cadastrarCliente(cliente1);
    }

    public static void listarClientes() {
        List<ClienteUser> clientes = ClienteService.buscarTodosClientes();

        System.out.println("\n=== LISTA DE CLIENTES ===");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (ClienteUser cliente : clientes) {
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("--------------------------");
            }
        }
    }

    public static void realizarLogin(){
        ClienteUser cliente = null;

        do {
            System.out.println("\n=== LOGIN Cliente ===");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            cliente = ClienteService.realizarLoginCliente(email, senha);
            if (cliente != null) {
                System.out.println("Login realizado com sucesso!");
                menuCliente(cliente);
            } else {
                System.out.println("Email ou senha incorretos. Tente novamente.");
            }

        } while (cliente == null);
    }

    public static void exibirInfos(ClienteUser cliente){
        System.out.println("Email: " + cliente.getNome());
        System.out.println("Nome: " + cliente.getEmail());
    }

    public static void menuBuscarVeiculos() {
        System.out.println("\n=== BUSCA DE VEÍCULOS ===");

        System.out.print("Marca (pressione Enter para ignorar): ");
        String marca = scanner.nextLine();

        System.out.print("Modelo (pressione Enter para ignorar): ");
        String modelo = scanner.nextLine();

        System.out.print("Preço mínimo (ou Enter para 0): ");
        String precoMinStr = scanner.nextLine();
        double precoMin = precoMinStr.isEmpty() ? 0.0 : Double.parseDouble(precoMinStr);

        System.out.print("Preço máximo (ou Enter para ilimitado): ");
        String precoMaxStr = scanner.nextLine();
        double precoMax = precoMaxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(precoMaxStr);

        System.out.print("Status (DISPONIVEL): ");
        String status = scanner.nextLine().isEmpty() ? "DISPONIVEL" : scanner.nextLine();

        System.out.print("Ordenar por (1 = menor preço, 2 = maior preço): ");
        String ordem = switch (scanner.nextLine()) {
            case "2" -> "MAIOR_PRECO";
            default -> "MENOR_PRECO";
        };

        List<Veiculo> resultados = VeiculoService.buscarComFiltros(
                marca, modelo, precoMin, precoMax, status.toUpperCase(), ordem
        );

        System.out.println("\n=== RESULTADOS DA BUSCA ===");

        if (resultados.isEmpty()) {
            System.out.println("Nenhum veículo encontrado com os filtros informados.");
        } else {
            for (Veiculo v : resultados) {
                System.out.printf("• [%s] %s %s (%d) | R$ %.2f | %s\n",
                        v.getPlaca(), v.getMarca(), v.getModelo(),
                        v.getAnoFabricacao(), v.getPrecoAluguel(), v.getStatus());
            }
        }
    }


    public static void verHistoricoCliente(ClienteUser cliente) {
        List<Aluguel> lista = AluguelService.buscarHistoricoCliente(cliente.getEmail());
        LocalDate hoje = LocalDate.now();

        if (lista.isEmpty()) {
            System.out.println("Você ainda não tem aluguéis registrados.");
        } else {
            System.out.println("\n=== SEUS ALUGUEIS ===");
            for (Aluguel a : lista) {
                LocalDate fim = LocalDate.parse(a.getDataFim());
                String status;

                if (fim.isBefore(hoje)) {
                    status = "🔴 VENCIDO";
                } else {
                    status = "🟢 ATIVO";
                }

                System.out.printf("• Placa: %s | De %s até %s | %s\n",
                        a.getPlaca(), a.getDataInicio(), a.getDataFim(), status);
            }
        }
    }

    public static void devolverCarroAlugado(ClienteUser cliente) {
        List<Aluguel> lista = AluguelService.buscarHistoricoCliente(cliente.getEmail());
        LocalDate hoje = LocalDate.now();
        boolean temAtivo = false;

        System.out.println("\n=== DEVOLUÇÃO DE VEÍCULO ===");

        for (Aluguel a : lista) {
            LocalDate fim = LocalDate.parse(a.getDataFim());

            if (!fim.isBefore(hoje)) {
                temAtivo = true;
                System.out.printf("• Placa: %s | De %s até %s\n",
                        a.getPlaca(), a.getDataInicio(), a.getDataFim());
            }
        }

        if (!temAtivo) {
            System.out.println("Você não possui veículos ativos para devolução.");
            return;
        }

        System.out.print("Digite a placa do veículo que deseja devolver: ");
        String placa = scanner.nextLine().toUpperCase();

        VeiculoRepository.alterarStatusVeiculo(placa, "DISPONIVEL");

        System.out.println("Veículo devolvido com sucesso!");
    }

    public static void editarDadosCliente(ClienteUser cliente) {
        System.out.println("\n=== EDITAR DADOS DO CLIENTE ===");

        System.out.print("Novo nome (atual: " + cliente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (novoNome.isEmpty()) novoNome = cliente.getNome();

        System.out.print("Novo email (atual: " + cliente.getEmail() + "): ");
        String novoEmail = scanner.nextLine();
        if (novoEmail.isEmpty()) novoEmail = cliente.getEmail();

        System.out.print("Nova senha (deixe em branco para manter a atual): ");
        String novaSenha = scanner.nextLine();
        if (novaSenha.isEmpty()) novaSenha = cliente.getSenha();

        ClienteUser atualizado = new ClienteUser(novoNome, novoEmail, novaSenha);
        String emailAntigo = cliente.getEmail();

        boolean sucesso = ClienteService.editarCliente(emailAntigo, atualizado);
        if (sucesso) {
            System.out.println("Dados atualizados com sucesso!");
            // Atualiza o objeto local
            cliente.setNome(novoNome);
            cliente.setEmail(novoEmail);
            cliente.setSenha(novaSenha);
        } else {
            System.out.println("Erro ao atualizar os dados.");
        }
    }






}
