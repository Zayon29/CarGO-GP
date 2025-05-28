package mz.co.cargo.Security;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
public class PasswordHash {
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public static String gerarHash(String senha) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] hash = hashSenha(senha.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    public static boolean validarSenha(String senha, String hashArmazenado) {
        String[] partes = hashArmazenado.split(":");
        if (partes.length != 2) return false;

        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] hashEsperado = Base64.getDecoder().decode(partes[1]);

        byte[] hashCalculado = hashSenha(senha.toCharArray(), salt);

        if (hashCalculado.length != hashEsperado.length) return false;

        for (int i = 0; i < hashCalculado.length; i++) {
            if (hashCalculado[i] != hashEsperado[i]) return false;
        }
        return true;
    }

    private static byte[] hashSenha(final char[] senha, final byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erro ao gerar hash de senha", e);
        }
    }





}
