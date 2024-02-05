import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LoadKeyLocalButtonAction {
    private Stage primaryStage;
    private ToggleGroup algorithmGroup;
    private TextArea generatedKeyArea;

    public LoadKeyLocalButtonAction(Stage primaryStage, ToggleGroup algorithmGroup, TextArea generatedKeyArea) {
        this.primaryStage = primaryStage;
        this.algorithmGroup = algorithmGroup;
        this.generatedKeyArea = generatedKeyArea;
    }

    public void handleLoadKeyButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Key File");
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                Scanner scanner = new Scanner(file);
                StringBuilder content = new StringBuilder();
                String algorithm = null;
                String key = null;

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    content.append(line).append("\n");

                    if (line.startsWith("Algorithm: ")) {
                        algorithm = line.substring("Algorithm: ".length());
                    } else if (line.startsWith("Key: ")) {
                        key = line.substring("Key: ".length());
                    }
                }

                if (algorithm != null && key != null) {
                    // 设置读取的算法选项到界面上的 ToggleGroup
                    setAlgorithmOption(algorithm);

                    // 将读取的密钥加载到界面的 TextArea 中
                    generatedKeyArea.setText(key);
                    showAlert("Loaded key:\n" + content.toString());
                } else {
                    showAlert("Error: Invalid file format.");
                }

            } catch (IOException ex) {
                showAlert("Error loading key: " + ex.getMessage());
            }
        }
    }

    private void setAlgorithmOption(String algorithm) {
        RadioButton selectedRadioButton = (RadioButton) algorithmGroup.getToggles().stream()
                .filter(toggle -> ((RadioButton) toggle).getText().equals(algorithm))
                .findFirst().orElse(null);

        if (selectedRadioButton != null) {
            selectedRadioButton.setSelected(true);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
