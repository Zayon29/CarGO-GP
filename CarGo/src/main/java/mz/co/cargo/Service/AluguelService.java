package mz.co.cargo.Service;

import mz.co.cargo.Model.Aluguel;
import mz.co.cargo.Model.Veiculo;
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

        VeiculoRepository.alterarStatusVeiculo(placa.toUpperCase(), "RESERVADO");
        atualizarStatusDosVeiculos();

        return "Aluguel realizado com sucesso.";
    }

    public static boolean realizarAluguelBoolean(String placa, String dataInicio, String dataFim, String emailCliente) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inicio, fim;

        if (!ClienteRepository.emailExiste(emailCliente)) {
            System.out.println("Erro: Esse e-mail de cliente não está cadastrado no sistema.");
            return false;
        }

        if (!VeiculoRepository.existePlaca(placa)) {
            System.out.println("Erro: Nenhum veículo encontrado com a placa: " + placa);
            return false;
        }

        try {
            inicio = LocalDate.parse(dataInicio, formatter);
            fim = LocalDate.parse(dataFim, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Datas em formato inválido. Use o formato YYYY-MM-DD.");
            return false;
        }

        if (!fim.isAfter(inicio)) {
            System.out.println("Erro: A data de fim deve ser posterior à data de início.");
            return false;
        }

        if (AluguelRepository.haConflito(placa, dataInicio, dataFim)) {
            System.out.println("Erro: O veículo já está alugado nesse período.");
            return false;
        }

        Aluguel aluguel = new Aluguel(0, placa.toUpperCase(), dataInicio, dataFim, emailCliente);
        AluguelRepository.registrarAluguel(aluguel);

        VeiculoRepository.alterarStatusVeiculo(placa.toUpperCase(), "RESERVADO");

        return true;
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

    public static List<Aluguel> listarTodos() {
        return AluguelRepository.buscarAlugueisAtivos();
    }

    public static void atualizarStatusDosVeiculos() {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Veiculo> todos = VeiculoRepository.buscarTodosVeiculos();

        for (Veiculo veiculo : todos) {
            String placa = veiculo.getPlaca();
            List<Aluguel> alugueis = AluguelRepository.buscarPorPlaca(placa);

            boolean alugadoHoje = false;
            boolean reservadoFuturo = false;

            for (Aluguel aluguel : alugueis) {
                try {
                    LocalDate inicio = LocalDate.parse(aluguel.getDataInicio(), formatter);
                    LocalDate fim = LocalDate.parse(aluguel.getDataFim(), formatter);

                    if (!hoje.isBefore(inicio) && !hoje.isAfter(fim)) {
                        alugadoHoje = true;
                        break;
                    } else if (hoje.isBefore(inicio)) {
                        reservadoFuturo = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Erro ao converter datas do aluguel: " + e.getMessage());
                }
            }

            if (alugadoHoje) {
                VeiculoRepository.alterarStatusVeiculo(placa, "ALUGADO");
            } else if (reservadoFuturo) {
                VeiculoRepository.alterarStatusVeiculo(placa, "RESERVADO");
            } else {
                VeiculoRepository.alterarStatusVeiculo(placa, "DISPONIVEL");
            }
        }
    }

    public static List<Aluguel> listarAlugueisAtivos(String email) {
        return AluguelRepository.buscarAlugueisAtivosPorEmail(email);
    }

    public static boolean removerAluguelAtivo(String placa, String email){
        return AluguelRepository.removerAluguelAtivo(placa, email);
    }





}

