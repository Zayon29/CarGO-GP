package mz.co.cargo.Service;
import mz.co.cargo.Model.Veiculo;
import mz.co.cargo.Repository.VeiculoRepository;
import java.util.List;
import java.util.regex.Pattern;

public class VeiculoService {

    // Cadastrar veículo com regras de negócio
    public static String cadastrarVeiculo(Veiculo veiculo) {

        //esses codigos aqui estao mais como para ficar ja prontos,ja que a gente nao tem ainda o mdnu para interarir em si
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

        // Verificar se a placa e o chassi já existem no banco
        if (VeiculoRepository.existePlaca(veiculo.getPlaca())) {
            return "Erro: Placa já cadastrada.";
        }

        if (VeiculoRepository.existeChassi(veiculo.getChassi())) {
            return "Erro: Chassi já cadastrado.";
        }

        // caadastrar veículo no banco
        VeiculoRepository.adicionarVeiculo(veiculo);
        return "Veículo cadastrado com sucesso!";
    }

    // Remove veículo
    public static String removerVeiculo(String placa) {
        if (!VeiculoRepository.existePlaca(placa)) {
            return "Erro: Nenhum veículo encontrado com essa placa.";
        }

        VeiculoRepository.removerVeiculo(placa);
        return "Veículo removido com sucesso!";
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

        //o status informado é válido
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

    // Listar veículos nao feito


}
