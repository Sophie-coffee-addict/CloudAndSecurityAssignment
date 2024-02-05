import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveKeyLocalButtonAction {
    private String selectedAlgorithm;
    private String generatedKey;
    private Stage primaryStage;

    public SaveKeyLocalButtonAction(String selectedAlgorithm, String generatedKey, Stage primaryStage) {
        this.selectedAlgorithm = selectedAlgorithm;
        this.generatedKey = generatedKey;
        this.primaryStage = primaryStage;
    }

    public void handleSaveKeyButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Key File");
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("Algorithm: " + selectedAlgorithm + "\n");
                fileWriter.write("Key: " + generatedKey + "\n");
                fileWriter.close();
                showAlert("Key successfully saved to " + file.getName());
            } catch (IOException ex) {
                showAlert("Error saving key: " + ex.getMessage());
            }
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
