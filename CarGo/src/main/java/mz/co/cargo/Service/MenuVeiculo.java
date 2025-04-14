package mz.co.cargo.Service;

import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.DatabaseInitializer;

import java.util.*;

public class MenuVeiculo {

    private static final Scanner scanner = new Scanner(System.in);

    // esse main aq eh pra testar a parte de veiculo
    public static void main(String[] args) {

        DatabaseInitializer.initializeAll();

        int opcao;

        do {
            System.out.println("\n=== MENU VEÍCULO ===");
            System.out.println("1. Cadastrar Veículo");
            System.out.println("2. Remover Veículo");
            System.out.println("3. Editar Veículo");
            System.out.println("4. Alterar Status do Veículo");
            System.out.println("5. Listar Veículos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> cadastrarVeiculo();
                case 2 -> removerVeiculo();
                case 3 -> editarVeiculo();
                case 4 -> alterarStatus();
                case 5 -> listarVeiculos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private static void cadastrarVeiculo() {
        Veiculo veiculo = lerDadosVeiculo(false);
        String resultado = VeiculoService.cadastrarVeiculo(veiculo);
        System.out.println(resultado);
    }

    private static void removerVeiculo() {
        System.out.print("Digite a placa do veículo a remover: ");
        String placa = scanner.nextLine();
        String resultado = VeiculoService.removerVeiculo(placa);
        System.out.println(resultado);
    }

    private static void editarVeiculo() {
        Veiculo veiculo = lerDadosVeiculo(true);
        String resultado = VeiculoService.editarVeiculo(veiculo);
        System.out.println(resultado);
    }

    private static void alterarStatus() {
        System.out.print("Digite a placa do veículo: ");
        String placa = scanner.nextLine();
        System.out.print("Novo status (DISPONIVEL, ALUGADO, EM_MANUTENCAO): ");
        String status = scanner.nextLine();
        String resultado = VeiculoService.alterarStatusVeiculo(placa, status);
        System.out.println(resultado);
    }

    private static Veiculo lerDadosVeiculo(boolean edicao) {
        System.out.print("Placa: ");
        String placa = scanner.nextLine();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Ano de fabricação: ");
        int ano = scanner.nextInt();

        System.out.print("Chassi: ");
        scanner.nextLine(); // limpar buffer
        String chassi = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();

        System.out.print("Quilometragem: ");
        int km = scanner.nextInt();

        scanner.nextLine(); // limpar buffer
        System.out.print("Tipo de combustível: ");
        String combustivel = scanner.nextLine();

        System.out.print("Status (DISPONIVEL, ALUGADO, EM_MANUTENCAO): ");
        String status = scanner.nextLine();

        System.out.print("Imagens (separadas por ';'): ");
        String imagensStr = scanner.nextLine();
        List<String> imagens = Arrays.asList(imagensStr.split(";"));

        return new Veiculo(marca, modelo, ano, placa, chassi, preco, status, km, combustivel, imagens);
    }

    public static void listarVeiculos() {
        List<Veiculo> lista = VeiculoService.buscarTodosVeiculos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum veículo encontrado.");
            return;
        }

        System.out.println("\n--- Lista de Veículos ---");
        for (Veiculo v : lista) {
            System.out.println("Marca: " + v.getMarca());
            System.out.println("Modelo: " + v.getModelo());
            System.out.println("Ano: " + v.getAnoFabricacao());
            System.out.println("Placa: " + v.getPlaca());
            System.out.println("Chassi: " + v.getChassi());
            System.out.println("Preço: " + v.getPrecoAluguel());
            System.out.println("Quilometragem: " + v.getQuilometragem());
            System.out.println("Status: " + v.getStatus());
            System.out.println("Imagens: " + v.getImagens());
            System.out.println("-------------------------");
        }
    }

}
