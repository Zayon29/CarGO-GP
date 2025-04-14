package mz.co.cargo.Service;

import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Repository.AdminRepository;
import java.util.List;
import java.util.regex.Pattern;

public class AdminService {

    public static String cadastrarAdmin(AdminUser admin) {

        // Validação dos critérios de aceitação
        if (admin.getNome() == null || admin.getNome().trim().isEmpty()) {
            return "Erro: O nome do administrador não pode ser vazio.";
        }

        if (!Pattern.matches("[a-zA-Z ]+", admin.getNome())) {
            return "Erro: O nome deve conter apenas letras e espaços.";
        }

        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            return "Erro: O email do administrador não pode ser vazio.";
        }

        if (!Pattern.matches("^[\\w-]+(?:\\.[\\w-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", admin.getEmail())) {
            return "Erro: O email fornecido não é válido.";
        }

        // Verificar se o email já está cadastrado no banco
        if (AdminRepository.existeEmail(admin.getEmail())) {
            return "Erro: O email já está cadastrado.";
        }

        if (admin.getSenha() == null || admin.getSenha().length() < 6) {
            return "Erro: A senha deve ter pelo menos 6 caracteres.";
        }

        // Cadastrar o administrador no banco
        AdminRepository.adcionarAdmin(admin);
        return "Administrador cadastrado com sucesso!";
    }

    public static AdminUser realizarLoginAdmin(String email, String senha){
        return AdminRepository.loginAdmin(email, senha);
    }

    public static List<AdminUser> buscarTodosAdmins(){
        return AdminRepository.buscarTodosAdmins();
    }

}
