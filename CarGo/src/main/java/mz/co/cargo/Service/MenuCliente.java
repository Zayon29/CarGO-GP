package mz.co.cargo.Service;

import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Repository.DatabaseInitializer;

import java.util.List;
import java.util.Scanner;

import static mz.co.cargo.Service.MenuVeiculo.listarVeiculos;

public class MenuCliente {
    private static final Scanner scanner = new Scanner(System.in);

    // Esse main é para testar a parte de cliente
    public static void main(String[] args) {

        DatabaseInitializer.initializeAll();

        // Cliente de teste
        ClienteUser cliente1 = new ClienteUser("teste", "teste@email.com", "123456");
        String resultado = ClienteService.cadastrarCliente(cliente1);
        System.out.println(resultado);

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
                case 2 -> listarVeiculos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
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

}
