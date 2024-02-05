import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SaveKeyCloudButtonAction {
    private String selectedAlgorithm;
    private String generatedKey;
    private Stage primaryStage;

    public SaveKeyCloudButtonAction(String selectedAlgorithm, String generatedKey, Stage primaryStage) {
        this.selectedAlgorithm = selectedAlgorithm;
        this.generatedKey = generatedKey;
        this.primaryStage = primaryStage;
    }

    public void handleSaveKeyButton(String loggedInUserName) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO encryption_keys (user_id, algorithm, key_value) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, loggedInUserName); 
                statement.setString(2, selectedAlgorithm);
                statement.setString(3, generatedKey);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Key successfully saved to the cloud database.");
                } else {
                    showAlert("Failed to save key to the cloud database.");
                }
            }
        } catch (SQLException e) {
            showAlert("Error saving key to the cloud database: " + e.getMessage());
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
