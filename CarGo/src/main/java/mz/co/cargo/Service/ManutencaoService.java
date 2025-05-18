package mz.co.cargo.Service;

import mz.co.cargo.Model.Manutencao;
import mz.co.cargo.Repository.ManutencaoRepository;

import java.util.List;

public class ManutencaoService {

    public static String registrarManutencao(Manutencao m) {
        if (m.getDescricao().isEmpty() || m.getPlaca().isEmpty()) {
            return "Erro: Todos os campos devem ser preenchidos.";
        }

        ManutencaoRepository.registrarManutencao(m);
        return "Manutenção registrada com sucesso.";
    }

    public static List<Manutencao> listarPorPlaca(String placa) {
        return ManutencaoRepository.buscarHistoricoPorPlaca(placa);
    }
}
