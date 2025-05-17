package mz.co.cargo.Service;

import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Repository.ClienteRepository;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteService {

    public static String cadastrarCliente(ClienteUser cliente) {

        // Validação dos critérios de aceitação
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            return "Erro: O nome do cliente não pode ser vazio.";
        }

        if (!Pattern.matches("[a-zA-Z ]+", cliente.getNome())) {
            return "Erro: O nome deve conter apenas letras e espaços.";
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            return "Erro: O email do cliente não pode ser vazio.";
        }

        if (!Pattern.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", cliente.getEmail())) {
            return "Erro: O email fornecido não é válido.";
        }

        // Verificar se o email já está cadastrado no banco
        if (ClienteRepository.existeEmail(cliente.getEmail())) {
            return "Erro: O email já está cadastrado.";
        }

        if (cliente.getSenha() == null || cliente.getSenha().length() < 6) {
            return "Erro: A senha deve ter pelo menos 6 caracteres.";
        }

        // Cadastrar o cliente no banco
        ClienteRepository.adicionarCliente(cliente);
        return "Cliente cadastrado com sucesso!";
    }

    public static ClienteUser realizarLoginCliente(String email, String senha){
        return ClienteRepository.loginCliente(email, senha);
    }

    public static List<ClienteUser> buscarTodosClientes(){
        return ClienteRepository.buscarTodosClientes();
    }

    public static List<ClienteUser> buscarClientesPorNome(String nome){
        return ClienteRepository.buscarClientesPorNome(nome);
    }

    public static boolean excluirCliente(String email){
        return ClienteRepository.excluirCliente(email);
    }

}
