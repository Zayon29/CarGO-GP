package mz.co.cargo.Service;

import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Repository.AluguelRepository;
import mz.co.cargo.Repository.ClienteRepository;
import mz.co.cargo.Repository.VeiculoRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AluguelService {

    public static String realizarAluguel(String placa, String dataInicio, String dataFim, String emailCliente) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inicio, fim;

        if (!ClienteRepository.emailExiste(emailCliente)) {
            return "Erro: Esse e-mail de cliente não está cadastrado no sistema.";
        }

        if (!VeiculoRepository.existePlaca(placa)) {
            return "Erro: Nenhum veículo encontrado com a placa: " + placa;
        }

        try {
            inicio = LocalDate.parse(dataInicio, formatter);
            fim = LocalDate.parse(dataFim, formatter);
        } catch (DateTimeParseException e) {
            return "Erro: Datas em formato inválido. Use o formato YYYY-MM-DD.";
        }

        if (!fim.isAfter(inicio)) {
            return "Erro: A data de fim deve ser posterior à data de início.";
        }

        if (AluguelRepository.haConflito(placa, dataInicio, dataFim)) {
            return "Erro: O veículo já está alugado nesse período.";
        }

        Aluguel aluguel = new Aluguel(0, placa.toUpperCase(), dataInicio, dataFim, emailCliente);
        AluguelRepository.registrarAluguel(aluguel);

        VeiculoRepository.alterarStatusVeiculo(placa.toUpperCase(), "ALUGADO");

        return "Aluguel realizado com sucesso.";
    }

    public static String limparAlugueis(String email){
        return AluguelRepository.limparAlugueisPorEmailCliente(email);
    }



    public static List<Aluguel> listarPorPlaca(String placa) {
        return AluguelRepository.buscarPorPlaca(placa);
    }
    public static List<Aluguel> buscarHistoricoCliente(String email) {
        return AluguelRepository.buscarPorEmailCliente(email);
    }


}

