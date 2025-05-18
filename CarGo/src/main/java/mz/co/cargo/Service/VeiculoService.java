package mz.co.cargo.Service;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.VeiculoRepository;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VeiculoService {


    public static String cadastrarVeiculo(Veiculo veiculo) {


        // Validação dos critérios de aceitação
        if (!Pattern.matches("[a-zA-Z0-9 ]+", veiculo.getMarca()) ||
                !Pattern.matches("[a-zA-Z0-9 ]+", veiculo.getModelo())) {
            return "Erro: Marca e modelo devem conter apenas caracteres alfanuméricos.";
        }

        if (veiculo.getAnoFabricacao() < 1900 || veiculo.getAnoFabricacao() > 2050) {
            return "Erro: Ano de fabricação inválido.";
        }

        if (veiculo.getPrecoAluguel() <= 0 || veiculo.getQuilometragem() < 0) {
            return "Erro: Preço e quilometragem devem ser valores positivos.";
        }

        if (veiculo.getImagens().size() > 5) {
            return "Erro: Apenas 5 imagens são permitidas.";
        }


        if (VeiculoRepository.existePlaca(veiculo.getPlaca())) {
            return "Erro: Placa já cadastrada.";
        }

        if (VeiculoRepository.existeChassi(veiculo.getChassi())) {
            return "Erro: Chassi já cadastrado.";
        }


        VeiculoRepository.adicionarVeiculo(veiculo);
        return "Veículo cadastrado com sucesso!";
    }


    public static Boolean removerVeiculo(String placa) {
        if (!VeiculoRepository.existePlaca(placa)) {
            return false;
        } else {
            VeiculoRepository.removerVeiculo(placa);
            return true;
        }
    }

    // editar veículo- precisa de validação
    public static String editarVeiculo(Veiculo veiculo) {
        if (!VeiculoRepository.existePlaca(veiculo.getPlaca())) {
            return "Erro: Veículo não encontrado.";
        }

        VeiculoRepository.editarVeiculo(veiculo);
        return "Veículo atualizado com sucesso!";
    }

    public static String alterarStatusVeiculo(String placa, String novoStatus) {
        // o veículo existe
        if (!VeiculoRepository.existePlaca(placa)) {
            return "Erro: Veículo não encontrado.";
        }


        List<String> statusPermitidos = List.of("DISPONIVEL", "ALUGADO", "EM_MANUTENCAO");
        if (!statusPermitidos.contains(novoStatus.toUpperCase())) {
            return "Erro: Status inválido. Escolha entre Disponível, Alugado ou Em Manutenção.";
        }

        //Impedir que um veículo "Alugado" volte para "Disponível" diretamente
        String statusAtual = VeiculoRepository.buscarStatusPorPlaca(placa);
        if (statusAtual.equalsIgnoreCase("ALUGADO") && novoStatus.equalsIgnoreCase("DISPONIVEL")) {
            return "Erro: Um veículo alugado não pode ser marcado como disponível diretamente.";
        }

        //Atualizar status no banco
        VeiculoRepository.alterarStatusVeiculo(placa, novoStatus);
        return "Status atualizado para: " + novoStatus;
    }

    public static List<Veiculo> buscarTodosVeiculos() {
        return VeiculoRepository.buscarTodosVeiculos();
    }

    public static List<Veiculo> buscarComFiltros(String marca, String modelo, double precoMin, double precoMax, String status, String ordem) {
        return VeiculoRepository. buscarTodosVeiculos()
                .stream()
                .filter(v -> v.getStatus().equalsIgnoreCase(status))
                .filter(v -> marca == null || marca.isEmpty() || v.getMarca().equalsIgnoreCase(marca))
                .filter(v -> modelo == null || modelo.isEmpty() || v.getModelo().equalsIgnoreCase(modelo))
                .filter(v -> v.getPrecoAluguel() >= precoMin && v.getPrecoAluguel() <= precoMax)
                .sorted((v1, v2) -> {
                    if ("MENOR_PRECO".equalsIgnoreCase(ordem)) return Double.compare(v1.getPrecoAluguel(), v2.getPrecoAluguel());
                    if ("MAIOR_PRECO".equalsIgnoreCase(ordem)) return Double.compare(v2.getPrecoAluguel(), v1.getPrecoAluguel());
                    return 0;
                })
                .collect(Collectors.toList());
    }

    public static List<Veiculo> buscarPorStatus(int num) {
        switch (num) {
            case 1:
                return VeiculoRepository.buscarVeiculosStatus("Em_manutencao");
            case 2:
                return VeiculoRepository.buscarVeiculosStatus("disponivel");
            case 3:
                return VeiculoRepository.buscarVeiculosStatus("indisponivel");
            default:
                return null;
        }
    }

}


