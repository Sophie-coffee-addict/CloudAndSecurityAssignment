
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterPage {
	private Connection connection;
	private Stage primaryStage;

	public RegisterPage(Stage primaryStage, Connection connection) {
		this.primaryStage = primaryStage;
		this.connection = connection;

		primaryStage.setTitle("Register");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER); // 设置GridPane中所有元素在界面中间对齐
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setVgap(10);
		grid.setHgap(10);

		Label nameLabel = new Label("Username:");
		GridPane.setConstraints(nameLabel, 0, 0);

		TextField nameInput = new TextField();
		nameInput.setPromptText("Enter your username");
		GridPane.setConstraints(nameInput, 1, 0);

		Label passLabel = new Label("Password:");
		GridPane.setConstraints(passLabel, 0, 1);

		PasswordField passInput = new PasswordField();
		passInput.setPromptText("Enter your password");
		GridPane.setConstraints(passInput, 1, 1);

		Label confirmPassLabel = new Label("Confirm Password:");
		GridPane.setConstraints(confirmPassLabel, 0, 2);

		PasswordField confirmPassInput = new PasswordField();
		confirmPassInput.setPromptText("Confirm your password");
		GridPane.setConstraints(confirmPassInput, 1, 2);

		Button registerButton = new Button("Register");
		GridPane.setConstraints(registerButton, 1, 3);
		registerButton.setMaxWidth(80);

		Button loginButton = new Button("Back to Login");
		GridPane.setConstraints(loginButton, 1, 4);
		loginButton.setMaxWidth(120);

		Label messageLabel = new Label();
		GridPane.setConstraints(messageLabel, 1, 5);

		registerButton.setOnAction(e -> {
			String username = nameInput.getText();
			String password = passInput.getText();
			String confirmPassword = confirmPassInput.getText();

			if (isUsernameExists(username)) {
				messageLabel.setText("User name already exists. Change the user name or login now.");
			} else {
				if (password.equals(confirmPassword)) {
					boolean isRegistered = registerUser(username, password);
					if (isRegistered) {
						messageLabel.setText("Registration successful!");
					} else {
						messageLabel.setText("Registration failed!");
					}
				} else {
					messageLabel.setText("Passwords do not match!");
				}
			}
		});

		loginButton.setOnAction(e -> {
			// Call a method to switch back to Login page
			openLoginPage();
		});

		grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, confirmPassLabel, confirmPassInput,
				registerButton, loginButton, messageLabel);

		Scene scene = new Scene(grid, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void openLoginPage() {
		// Create an instance of LoginPage class to switch back to the login page
		LoginPage loginPage = new LoginPage();
		loginPage.start(primaryStage); // Start the login page
	}

	// 检查用户名是否已经存在于数据库中的方法
	private boolean isUsernameExists(String username) {
		String query = "SELECT * FROM users WHERE username = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next(); // 如果结果集中存在数据，表示用户名已存在
		} catch (SQLException e) {
			System.out.println("Error checking username existence: " + e.getMessage());
			return false;
		}
	}

	// 在 RegisterPage 类的 registerUser 方法中调用哈希方法
	private boolean registerUser(String username, String password) {
	    // 对用户输入的密码进行哈希加密
	    String hashedPassword = HashingPassword.hashPassword(password);

	    if (hashedPassword != null) {
	        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, username);
	            statement.setString(2, hashedPassword);
	            int rowsAffected = statement.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            System.out.println("Error registering user: " + e.getMessage());
	            return false;
	        }
	    } else {
	        return false;
	    }
	}

}
