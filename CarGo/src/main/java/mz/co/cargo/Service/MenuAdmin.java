package mz.co.cargo.Service;

import mz.co.cargo.Model.Manutencao;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.DatabaseInitializer;
import mz.co.cargo.Repository.VeiculoRepository;
import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Repository.ManutencaoDatabase;
import mz.co.cargo.Repository.ManutencaoRepository;

import java.util.List;
import java.util.Scanner;

public class MenuAdmin {
    private static final Scanner scanner = new Scanner(System.in);

    // esse main aq eh pra testar a parte de veiculo
    public static void main(String[] args) {

        DatabaseInitializer.initializeAll();

        //admin de teste
        //AdminUser admin1 = new AdminUser("teste", "teste@email.com", "123456");
        //String resultado = AdminService.cadastrarAdmin(admin1);
        //System.out.println(resultado);

        listarAdmins();

        String email, senha;
        AdminUser admin = null;

        do {
            System.out.println("\n=== LOGIN ADMIN ===");
            System.out.print("Digite seu Email: ");
            email = scanner.nextLine();

            System.out.print("Digite sua Senha: ");
            senha = scanner.nextLine();

            admin = AdminService.realizarLoginAdmin(email, senha);
            if (admin != null) {
                System.out.println("Login realizado com sucesso!");
                menuAdmin();
            } else {
                System.out.println("Email ou senha incorretos. Tente novamente.");
            }

        } while (admin == null);
    }

    public static void menuAdmin(){
        int opcao;
        do{
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Cadastrar Admin");
            System.out.println("2. Listar Admins");
            System.out.println("3. Registrar manutenção em veículo");
            System.out.println("4. Ver histórico de manutenção por placa");
            System.out.println("5. Alterar status de um veículo para 'DISPONIVEL'");
            System.out.println("6. Listar Veiculos");
            System.out.println("7. Listar Clientes");

            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Digite um número válido: ");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> System.out.println(cadastrarAdmin());
                case 2 -> listarAdmins();
                case 3 -> {
                    registrarManutencao(); // Registra a manutenção
                    alterarStatusParaDisponivel(scanner.nextLine());
                }
                case 4 -> verHistoricoManutencao();
                case 5 -> {
                    // Altera o status de manutenção do veículo diretamente
                    System.out.print("Digite a placa do veículo: ");
                    String placa = scanner.nextLine();
                    alterarStatusParaDisponivel(placa);
                }
                case 6 -> MenuVeiculo.listarVeiculos();
                case 7 -> MenuCliente.listarClientes();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static String cadastrarAdmin(){

        System.out.println("\n=== CADASTRO DE ADMIN ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        AdminUser admin1 = new AdminUser(nome, email, senha);

        return AdminService.cadastrarAdmin(admin1);
    }

    public static void listarAdmins() {
        List<AdminUser> admins = AdminService.buscarTodosAdmins();

        System.out.println("\n=== LISTA DE ADMINS ===");

        if (admins.isEmpty()) {
            System.out.println("Nenhum admin cadastrado.");
        } else {
            for (AdminUser admin : admins) {
                System.out.println("Nome: " + admin.getNome());
                System.out.println("Email: " + admin.getEmail());
                System.out.println("--------------------------");
            }
        }
    }
    public static void registrarManutencao() {
        System.out.print("Placa do veículo: ");
        String placa = scanner.nextLine();

        // Verifica se o veículo existe antes de continuar
        if (!VeiculoRepository.existePlaca(placa)) {
            System.out.println("Veículo com a placa " + placa + " não encontrado.");
            return;
        }

        System.out.print("Descrição da manutenção: ");
        String descricao = scanner.nextLine();

        String data = java.time.LocalDate.now().toString();

        // Registrar a manutenção
        Manutencao m = new Manutencao(0, placa, descricao, data);
        ManutencaoRepository.registrarManutencao(m);

        // Alterar o status do veículo para "EM_MANUTENCAO"
        VeiculoRepository.alterarStatusVeiculo(placa, "EM_MANUTENCAO");
    }


    public static void verHistoricoManutencao() {
        System.out.print("Digite a placa do veículo: ");
        String placa = scanner.nextLine();

        // Buscar o histórico de manutenções no repositório
        List<Manutencao> historico = ManutencaoRepository.buscarHistoricoPorPlaca(placa);

        System.out.println("\n=== HISTÓRICO DE MANUTENÇÃO ===");
        if (historico.isEmpty()) {
            System.out.println("Nenhuma manutenção registrada para esse veículo.");
        } else {
            for (Manutencao m : historico) {
                System.out.println("Data: " + m.getData());
                System.out.println("Descrição: " + m.getDescricao());
                System.out.println("--------------------------");
            }
        }
    }


    public static void alterarStatusParaDisponivel(String placa) {

        Veiculo veiculo = VeiculoRepository.buscarVeiculoPorPlaca(placa);

        if (veiculo != null) {
            // Alterar o status
            veiculo.setStatus("DISPONIVEL");

            // Atualizar o veículo no repositório
            VeiculoRepository.editarVeiculo(veiculo);
            System.out.println("Status do veículo alterado para 'DISPONIVEL'.");
        } else {
            System.out.println("Veículo com a placa " + placa + " não encontrado.");
        }
    }

    }
