package mz.co.cargo.Service;

import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Repository.AluguelRepository;
import mz.co.cargo.Repository.VeiculoRepository;

import java.util.List;

public class AluguelService {

    public static String realizarAluguel(String placa, String dataInicio, String dataFim) {
        // Verificar se já existe conflito
        if (AluguelRepository.haConflito(placa, dataInicio, dataFim)) {
            return "Erro: O veículo já está alugado nesse período.";
        }

        // Registrar aluguel
        Aluguel aluguel = new Aluguel(0, placa, dataInicio, dataFim);
        AluguelRepository.registrarAluguel(aluguel);

        // Atualizar status do veículo
        VeiculoRepository.alterarStatusVeiculo(placa, "ALUGADO");

        return "Aluguel realizado com sucesso.";
    }

    public static List<Aluguel> listarPorPlaca(String placa) {
        return AluguelRepository.buscarPorPlaca(placa);
    }
}

