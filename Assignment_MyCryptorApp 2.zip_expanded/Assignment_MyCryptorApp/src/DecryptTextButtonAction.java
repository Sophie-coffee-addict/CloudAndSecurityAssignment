import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class DecryptTextButtonAction {
    public static void handleDecryptButton(
    		String keyText,
            String selectedAlgorithm,
            String encryptedText,
            String generatedKey,
            TextArea decryptedTextArea) {

        String decryptedText = "";

        if ("AES".equals(selectedAlgorithm)) {
            if (generatedKey.isEmpty()) {
                showAlert("Please create or load an AES key first.");
                return;
            }
            try {
                decryptedText = AESEncryption.decryptTextWithKey(encryptedText, generatedKey);
            } catch (Exception ex) {
                showAlert("Error decrypting text: " + ex.getMessage());
            }
        } else if ("Caesar Cipher".equals(selectedAlgorithm)) {
        	 if (keyText.isEmpty()) {
                 showAlert("Please enter a key.");
                 return;
             }
             int key = Integer.parseInt(keyText);
             decryptedText = CaesarCipher.decrypt(encryptedText, key);
        }
        else if ("DES".equals(selectedAlgorithm)) {
            if (generatedKey.isEmpty()) {
                showAlert("Please enter a key.");
                return;
            }
            try {
                decryptedText = DESEncryption.decryptTextWithKey(encryptedText, generatedKey);
            } catch (Exception ex) {
                showAlert("Error decrypting text: " + ex.getMessage());
            }
        }
        decryptedTextArea.setText(decryptedText);
    }

    private static void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		}
}
