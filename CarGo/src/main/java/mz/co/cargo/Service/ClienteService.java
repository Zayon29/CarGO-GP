package mz.co.cargo.Service;

import mz.co.cargo.Model.ClienteUser;
import mz.co.cargo.Repository.ClienteRepository;
import mz.co.cargo.Security.PasswordHash;

import java.util.List;
import java.util.regex.Pattern;

public class ClienteService {

    public static String cadastrarCliente(ClienteUser cliente) {

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

        if (ClienteRepository.existeEmail(cliente.getEmail())) {
            return "Erro: O email já está cadastrado.";
        }

        if (cliente.getSenha() == null || cliente.getSenha().length() < 6) {
            return "Erro: A senha deve ter pelo menos 6 caracteres.";
        }

        // Criptografar senha
        String senhaHash = PasswordHash.gerarHash(cliente.getSenha());
        cliente.setSenha(senhaHash);

        ClienteRepository.adicionarCliente(cliente);
        return "Cliente cadastrado com sucesso!";
    }

    public static ClienteUser realizarLoginCliente(String email, String senhaDigitada) {
        ClienteUser cliente = ClienteRepository.buscarPorEmail(email);

        if (cliente == null) return null;

        boolean senhaCorreta = PasswordHash.validarSenha(senhaDigitada, cliente.getSenha());

        return senhaCorreta ? cliente : null;
    }

    public static List<ClienteUser> buscarTodosClientes() {
        return ClienteRepository.buscarTodosClientes();
    }

    public static List<ClienteUser> buscarClientesPorNome(String nome) {
        return ClienteRepository.buscarClientesPorNome(nome);
    }

    public static boolean excluirCliente(String email) {
        return ClienteRepository.excluirCliente(email);
    }

    private static String ultimaMensagemErro = "";

    public static boolean editarCliente(String emailAntigo, ClienteUser clienteAtualizado) {
        if (clienteAtualizado.getNome() == null || clienteAtualizado.getNome().trim().isEmpty()) {
            ultimaMensagemErro = "O nome do cliente não pode ser vazio.";
            return false;
        }

        if (!Pattern.matches("[a-zA-Z ]+", clienteAtualizado.getNome())) {
            ultimaMensagemErro = "O nome deve conter apenas letras e espaços.";
            return false;
        }

        if (clienteAtualizado.getEmail() == null || clienteAtualizado.getEmail().trim().isEmpty()) {
            ultimaMensagemErro = "O email do cliente não pode ser vazio.";
            return false;
        }

        if (!Pattern.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", clienteAtualizado.getEmail())) {
            ultimaMensagemErro = "O email fornecido não é válido.";
            return false;
        }

        if (!emailAntigo.equals(clienteAtualizado.getEmail()) &&
                ClienteRepository.existeEmail(clienteAtualizado.getEmail())) {
            ultimaMensagemErro = "O novo email já está cadastrado.";
            return false;
        }

        if (clienteAtualizado.getSenha() == null || clienteAtualizado.getSenha().length() < 6) {
            ultimaMensagemErro = "A senha deve ter pelo menos 6 caracteres.";
            return false;
        }

        // Re-hash da nova senha (caso alterada)
        clienteAtualizado.setSenha(PasswordHash.gerarHash(clienteAtualizado.getSenha()));

        boolean sucesso = ClienteRepository.editarCliente(emailAntigo, clienteAtualizado);
        if (!sucesso) {
            ultimaMensagemErro = "Erro ao atualizar os dados do cliente.";
        }

        return sucesso;
    }

    public static String getUltimaMensagemErro() {
        return ultimaMensagemErro;
    }
}
