package mz.co.cargo.Service;

import mz.co.cargo.Repository.DatabaseInitializer;
import mz.co.cargo.Model.AdminUser;

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
            System.out.println("0. Sair");
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


}
