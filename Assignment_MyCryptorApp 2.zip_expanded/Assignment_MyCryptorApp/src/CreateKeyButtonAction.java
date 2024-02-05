import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea; 

public class CreateKeyButtonAction {
    private TextArea generatedKeyArea; 
    private String selectedAlgorithm;

    public CreateKeyButtonAction(TextArea generatedKeyArea, String selectedAlgorithm) { 
        this.generatedKeyArea = generatedKeyArea;
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public String handleCreateKeyButton() {
        try {
            KeyGenerator keyGen = null;
            int keyLength = 0;
            String generatedKey = null;

            // Determine key length based on the selected algorithm
            if (selectedAlgorithm.equals("AES")) {
                keyGen = KeyGenerator.getInstance("AES");
                keyLength = 128;
            } else if (selectedAlgorithm.equals("DES")) {
                keyGen = KeyGenerator.getInstance("DES");
                keyLength = 56;
            }

            if (keyGen != null) {
                keyGen.init(keyLength);
                SecretKey secretKey = keyGen.generateKey();
                byte[] encodedKey = secretKey.getEncoded();
                generatedKey = bytesToHex(encodedKey);
            }
            
            return generatedKey; 
        } catch (NoSuchAlgorithmException e) {
            showAlert("Error generating key: " + e.getMessage());
            return null;
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
