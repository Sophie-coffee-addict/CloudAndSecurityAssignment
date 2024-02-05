import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;


public class EncryptTextButtonAction {
    public static void handleEncryptButton(
            String selectedAlgorithm,
            String text,
            String generatedKey,
            String keyText,
            TextArea encryptedTextArea

            ) {

        String encryptedText = "";

        if ("AES".equals(selectedAlgorithm)) {
            if (generatedKey.isEmpty()) {
                showAlert("Please create an AES key first.");
                return;
            }
            try {
                encryptedText = AESEncryption.encryptTextWithKey(text, generatedKey);
            } catch (Exception ex) {
                showAlert("Error encrypting text: " + ex.getMessage());
            }
        } else if ("Caesar Cipher".equals(selectedAlgorithm)) {
            if (keyText.isEmpty()) {
                showAlert("Please enter a key.");
                return;
            }
            int key = Integer.parseInt(keyText);
            encryptedText = CaesarCipher.encrypt(text, key);
        } else if ("DES".equals(selectedAlgorithm)) {
            if (generatedKey.isEmpty()) {
                showAlert("Please enter a key.");
                return;
            }
            try {
                encryptedText = DESEncryption.encryptTextWithKey(text, generatedKey);
            } catch (Exception ex) {
                showAlert("Error encrypting text: " + ex.getMessage());
            }
        }

        encryptedTextArea.setText(encryptedText);
    }

    private static void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		}
}
