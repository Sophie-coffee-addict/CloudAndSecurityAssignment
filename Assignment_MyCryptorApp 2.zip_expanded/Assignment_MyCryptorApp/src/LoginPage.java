import javafx.application.Application;
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

public class LoginPage extends Application {

	private Connection connection;
	private Stage primaryStage;
	private Label messageLabel;
	private static String loggedInUsername;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		connectToDatabase();

		primaryStage.setTitle("Login or Register");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER); 
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

		Button loginButton = new Button("Login");
		GridPane.setConstraints(loginButton, 1, 2);
		loginButton.setMaxWidth(80);

		Button registerButton = new Button("Register");
		GridPane.setConstraints(registerButton, 1, 3);
		registerButton.setMaxWidth(80);

		messageLabel = new Label();
		GridPane.setConstraints(messageLabel, 1, 4);

		loginButton.setOnAction(e -> {
			String username = nameInput.getText();
			String password = passInput.getText();

			boolean isValid = validateLogin(username, password);

			if (isValid) {
				loggedInUsername = username; 
				openEncryptionAndDecryptionPage(primaryStage);
			} else {
				messageLabel.setText("Invalid username or password!");
			}
		});
		
		registerButton.setOnAction(e -> {
			RegisterPage registerPage = new RegisterPage(primaryStage, connection);
		});
		
		grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, loginButton, registerButton,
				messageLabel);

		Scene scene = new Scene(grid, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static String getLoggedInUsername() {
        return loggedInUsername;
    }
	
	private void connectToDatabase() {
	    try {
	        connection = DatabaseConnection.getConnection();
	        System.out.println("Connected to the database.");
	    } catch (SQLException e) {
	        System.out.println("Error connecting to the database: " + e.getMessage());
	    }
	}

	private void openEncryptionAndDecryptionPage(Stage primaryStage) {
        EncryptionDecryptionPage encryptionAndDecryption = new EncryptionDecryptionPage();
        encryptionAndDecryption.start(new Stage());

        primaryStage.close(); // Close the login window if needed
    }

	
	private boolean validateLogin(String username, String password) {
	    String query = "SELECT * FROM users WHERE username = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, username);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            String storedPassword = resultSet.getString("password");

	            // 对用户输入的密码进行哈希处理，然后与数据库中存储的密码进行比较
	            String hashedPassword = HashingPassword.hashPassword(password);

	            if (hashedPassword != null && hashedPassword.equals(storedPassword)) {
	                // 登录成功
	                return true;
	            } else {
	                messageLabel.setText("Invalid username or password!");
	                return false;
	            }
	        } else {
	            messageLabel.setText("User does not exist. Please register.");
	            return false;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error validating login: " + e.getMessage());
	        return false;
	    }
	}

	// 打开 RegisterPage 页面的方法
	private void openRegisterPage() {
		  // 创建 RegisterPage 类的实例
	    RegisterPage registerPage = new RegisterPage(primaryStage, connection);

//	    // 获取 RegisterPage 的 Scene 对象
//	    Scene registerScene = registerPage.createRegisterPage();
//
//	    // 创建一个新的 Stage 来显示注册页面
//	    Stage registerStage = new Stage();
//	    registerStage.setTitle("Register"); // 设置注册页面的标题
//	    registerStage.setScene(registerScene);
//	    registerStage.show();
	}

}
