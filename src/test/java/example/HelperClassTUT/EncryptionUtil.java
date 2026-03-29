package example.HelperClassTUT;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Properties;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(); // 16-byte key

    // Encrypt the password
    public static String encrypt(String data) throws Exception {
        SecretKey key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Decrypt the password
    public static String decrypt(String encryptedData) throws Exception {
        SecretKey key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedData));
    }

    public static void saveEncryptedPasswordsToConfig(String password1, String password2, String password3) {
        try {
            // Encrypt each password
            String encryptedPassword1 = EncryptionUtil.encrypt(password1);
            String encryptedPassword2 = EncryptionUtil.encrypt(password2);
            String encryptedPassword3 = EncryptionUtil.encrypt(password3);

            System.out.println("Encrypted Passwords:");
            System.out.println("Password1: " + encryptedPassword1);
            System.out.println("Password2: " + encryptedPassword2);
            System.out.println("Password3: " + encryptedPassword3);

            // Save encrypted passwords to config.properties
            Properties properties = new Properties();
            String propertiesFilePath = "src/test/resources/config.properties";

            try (FileOutputStream output = new FileOutputStream(propertiesFilePath, true)) {
                properties.setProperty("admin_encrypted_password", encryptedPassword1);
                properties.setProperty("instructor_encrypted_password", encryptedPassword2);
                properties.setProperty("learner_encrypted_password", encryptedPassword3);
                properties.store(output, null);
            }

            System.out.println("Encrypted passwords saved to config.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveEncryptedPasswordsToConfigRC(String password) {
        try {
            // Encrypt each password
            String encryptedPassword= EncryptionUtil.encrypt(password);

            System.out.println("Encrypted Passwords:");
            System.out.println("Password: " + encryptedPassword);

            // Save encrypted passwords to config.properties
            Properties properties = new Properties();
            String propertiesFilePath = "src/test/resources/config.properties";

            try (FileOutputStream output = new FileOutputStream(propertiesFilePath, true)) {
                properties.setProperty("admin_encrypted_password_rc",encryptedPassword);
                properties.store(output, null);
            }

            System.out.println("Encrypted passwords saved to config.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }










    public static void main(String[] args) {
        //saveEncryptedPasswordsToConfig("roadstar1988","12345678","12345678");
        saveEncryptedPasswordsToConfigRC("Roadstar1988");
    }

}
