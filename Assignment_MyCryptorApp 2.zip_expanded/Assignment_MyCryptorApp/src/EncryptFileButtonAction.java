import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EncryptFileButtonAction {
	
    public static void handleEncryptFileButton(
            Stage primaryStage,
            ToggleGroup algorithmGroup,
            String keyText,
            String generatedKey
    ) {
//        Button encryptFileButton = new Button("Encrypt file");
//        encryptFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a file to encrypt");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                String algorithm = EncryptionDecryptionPage.getSelectedAlgorithm(algorithmGroup);
                String generatedOrEnteredKey = (algorithm.equals("Caesar Cipher")) ?keyText : generatedKey;

                try {
                    String content = readFileContent(selectedFile);

                    String encryptedContent = performEncryption(content, generatedOrEnteredKey, algorithm);

                    saveEncryptedContentToFile(encryptedContent);
                } catch (IOException ex) {
                    EncryptionDecryptionPage.showAlert("Error reading or saving file: " + ex.getMessage());
                }
            }
//        });
    }

    private static String readFileContent(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString();
    }

    private static String performEncryption(String content, String key, String algorithm) {
        String encryptedContent = "";

        try {
            if ("Caesar Cipher".equals(algorithm)) {
                int caesarKey = Integer.parseInt(key);
                encryptedContent = CaesarCipher.encrypt(content, caesarKey);
            } else if ("AES".equals(algorithm)) {
                encryptedContent = AESEncryption.encryptTextWithKey(content, key);
            } else if ("DES".equals(algorithm)) {
                encryptedContent = DESEncryption.encryptTextWithKey(content, key);
            } else {
                // Handle unsupported algorithm
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle encryption error
        }

        return encryptedContent;
    }

    private static void saveEncryptedContentToFile(String encryptedContent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Encrypted File As");
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(selectedFile);
                fileWriter.write(encryptedContent);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
   
}
