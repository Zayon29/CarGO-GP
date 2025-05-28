package mz.co.cargo.Service;

import mz.co.cargo.Model.AdminUser;
import mz.co.cargo.Repository.AdminRepository;
import mz.co.cargo.Security.PasswordHash;

import java.util.List;
import java.util.regex.Pattern;

public class AdminService {

    public static String cadastrarAdmin(AdminUser admin) {

        // Validação
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

        if (AdminRepository.existeEmail(admin.getEmail())) {
            return "Erro: O email já está cadastrado.";
        }

        if (admin.getSenha() == null || admin.getSenha().length() < 6) {
            return "Erro: A senha deve ter pelo menos 6 caracteres.";
        }
        String senhaHasheada = PasswordHash.gerarHash(admin.getSenha());
        admin.setSenha(senhaHasheada);

        AdminRepository.adcionarAdmin(admin);
        return "Administrador cadastrado com sucesso!";
    }

    public static AdminUser realizarLoginAdmin(String email, String senhaDigitada) {
        AdminUser admin = AdminRepository.buscarPorEmail(email);

        if (admin == null) return null;

        boolean senhaCorreta = PasswordHash.validarSenha(senhaDigitada, admin.getSenha());

        return senhaCorreta ? admin : null;
    }
    public static List<AdminUser> buscarTodosAdmins() {
        return AdminRepository.buscarTodosAdmins();
    }
}
