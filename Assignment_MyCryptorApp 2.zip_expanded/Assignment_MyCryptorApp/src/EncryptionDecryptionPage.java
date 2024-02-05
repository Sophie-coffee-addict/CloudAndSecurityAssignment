import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EncryptionDecryptionPage extends Application {

	private Label keyLabel;
	private ToggleGroup algorithmGroup;
	private TextArea encryptedTextArea;
	private TextArea decryptedTextArea;
	private TextArea generatedKeyArea;
	private String keyText;
	private double buttonWidth = 100.0;
	private String generatedKey;
	private String selectedAlgorithm;
	private String loggedInUsername;
	private TextField textField;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("My Cryptor App");

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(20));

		// left pane for text input and decypted text output
		VBox leftPane = new VBox(10);
		leftPane.setAlignment(Pos.TOP_LEFT);

		Label textLabel = new Label("Plaintext:");
		textField = new TextField();
		textField.setPromptText("Enter your text");
		textField.setMaxHeight(Double.MAX_VALUE);
		textField.setMaxWidth(250);

		Label decryptedTextLabel = new Label("Decrypted text:");
		decryptedTextArea = new TextArea();
		decryptedTextArea.setEditable(false);
		decryptedTextArea.setMaxWidth(250);
		
		Button clearButton1 = new Button("Clear");
		clearButton1.setOnAction(e -> {
			decryptedTextArea.clear(); 
		});

		leftPane.getChildren().addAll(textLabel, textField, decryptedTextLabel, decryptedTextArea, clearButton1);

		// center pane for encryption algorithm and key
		VBox centerPane = new VBox(10);
		centerPane.setAlignment(Pos.TOP_LEFT);

		algorithmGroup = new ToggleGroup();

		Label algorithmLabel = new Label("Encryption algorithm");
		RadioButton caesarCipher = new RadioButton("Caesar Cipher");
		caesarCipher.setToggleGroup(algorithmGroup);
		caesarCipher.setSelected(true);

		RadioButton aes = new RadioButton("AES");
		aes.setToggleGroup(algorithmGroup);

		RadioButton des = new RadioButton("DES");
		des.setToggleGroup(algorithmGroup);

		HBox algorithmBox = new HBox(10, caesarCipher, aes, des);
		algorithmBox.setAlignment(Pos.CENTER_LEFT);

		// key textfield
		keyLabel = new Label("Key:");
		TextField keyField = new TextField();
		keyField.setPromptText("Enter the key");
		keyField.setMaxWidth(300);
		HBox keyArea = new HBox(10, keyLabel, keyField);
		keyArea.setAlignment(Pos.CENTER_LEFT);

		// adding four buttons
		Button encryptTextButton = new Button("Encrypt text ->");
		Button decryptTextButton = new Button("<- Decrypt text");
		HBox enAndDeTextButtons = new HBox(10, encryptTextButton, decryptTextButton);
		enAndDeTextButtons.setAlignment(Pos.CENTER_LEFT);

		Button createKey = new Button("Create key (AES or DES)");
		createKey.setMinWidth(buttonWidth);
		Button saveKeyLocalButton = new Button("Save key local");
		Button loadKeyLocalButton = new Button("Load key local");
		HBox saveAndLoadKeyLocal = new HBox(10, saveKeyLocalButton, loadKeyLocalButton);
		saveAndLoadKeyLocal.setAlignment(Pos.CENTER_LEFT);

		Button saveKeyCloudButton = new Button("Save key cloud");
		Button loadKeyCloudButton = new Button("Load key cloud");
		HBox saveAndLoadKeyCloud = new HBox(10, saveKeyCloudButton, loadKeyCloudButton);
		saveAndLoadKeyCloud.setAlignment(Pos.CENTER_LEFT);

		Button encryptFileButton = new Button("Encrypt file");
		Button decryptFileButton = new Button("Decrypt file");
		HBox enAndDeFileButtons = new HBox(10, encryptFileButton, decryptFileButton);
		enAndDeFileButtons.setAlignment(Pos.CENTER_LEFT);

		HBox createKeyButton = new HBox(createKey);
		createKeyButton.setAlignment(Pos.CENTER_LEFT);

		encryptTextButton.setMinWidth(buttonWidth);
		decryptTextButton.setMinWidth(buttonWidth);
		saveKeyLocalButton.setMinWidth(buttonWidth);
		loadKeyLocalButton.setMinWidth(buttonWidth);
		saveKeyCloudButton.setMinWidth(buttonWidth);
		loadKeyCloudButton.setMinWidth(buttonWidth);
		encryptFileButton.setMinWidth(buttonWidth);
		decryptFileButton.setMinWidth(buttonWidth);

		Label encryptedTextLabel = new Label("Ciphertext:");
		encryptedTextArea = new TextArea();
		encryptedTextArea.setEditable(false);
		encryptedTextArea.setMaxWidth(250);
		encryptedTextArea.setMaxHeight(250);

		generatedKeyArea = new TextArea();
		generatedKeyArea.setEditable(false);
		generatedKeyArea.setMaxWidth(250);
		generatedKeyArea.setMaxHeight(150);

		centerPane.getChildren().addAll(algorithmLabel, algorithmBox, keyArea, enAndDeTextButtons, createKeyButton,
				generatedKeyArea, saveAndLoadKeyLocal, saveAndLoadKeyCloud, enAndDeFileButtons);

		// right pane for the encrypted text display
		VBox rightPane = new VBox(10);
		rightPane.setAlignment(Pos.TOP_LEFT);

		Button saveCipherTextLocal = new Button("Save ciphertext to local");
		Button saveCipherTextCloud = new Button("Save cipher text to cloud");
		Button clearButton2 = new Button("Clear");
		saveCipherTextLocal.setMaxWidth(160);
		saveCipherTextCloud.setMaxWidth(160);
		clearButton2.setMaxWidth(160);
		clearButton2.setOnAction(e -> {
		    encryptedTextArea.clear(); // 清除加密文本区域的内容
		});

		rightPane.getChildren().addAll(encryptedTextLabel, encryptedTextArea, saveCipherTextLocal, saveCipherTextCloud, clearButton2);

		root.setLeft(leftPane);
		root.setCenter(centerPane);
		root.setRight(rightPane);
		BorderPane.setMargin(leftPane, new Insets(0, 20, 0, 20));
		BorderPane.setMargin(centerPane, new Insets(0, 20, 0, 20));
		BorderPane.setMargin(rightPane, new Insets(0, 20, 0, 20));

		// action responding to the encrypt button
		encryptTextButton.setOnAction(e -> {
		    String text = textField.getText();
		    String algorithm = getSelectedAlgorithm(algorithmGroup); // 获取用户选择的加密算法
		     keyText = keyField.getText();
		     generatedKey = generatedKeyArea.getText(); // 获取生成的密钥文本

		    EncryptTextButtonAction.handleEncryptButton(
		            algorithm,
		            text,
		            generatedKey,
		            keyText,
//		            textField,
		            encryptedTextArea
//		            generatedKeyArea
		            );
		});


		// action responding to the decrypt button
		
		decryptTextButton.setOnAction(e -> {
		    String encryptedText = encryptedTextArea.getText(); // 获取要解密的文本
		    String algorithm = getSelectedAlgorithm(algorithmGroup); // 获取用户选择的加密算法
		    generatedKey = generatedKeyArea.getText(); // 获取生成的密钥文本
		    keyText = keyField.getText();

		    DecryptTextButtonAction.handleDecryptButton(
		    		keyText,
		            algorithm,
		            encryptedText,
		            generatedKey,
		            decryptedTextArea);
		});

//		decryptTextButton.setOnAction(e -> {
//			String text = encryptedTextArea.getText();
//			String algorithm = getSelectedAlgorithm(algorithmGroup);
//			String decryptedText = "";
//			keyText = keyField.getText();
//			if (keyText.isEmpty()) {
//				showAlert("Please enter a key.");
//				return;
//			}
//			int key = Integer.parseInt(keyText);
//			if ("Caesar Cipher".equals(algorithm)) {
////                int key = Integer.parseInt(keyField.getText()); 
//				decryptedText = CaesarCipher.decrypt(text, key);
//			}
//			decryptedTextArea.setText(decryptedText);
//		});

		
		// action responding to the create key button
		createKey.setOnAction(e -> {
			// 获取用户选择的加密算法
			selectedAlgorithm = getSelectedAlgorithm(algorithmGroup);
			// 创建 CreateKeyButtonAction 的实例并调用 handleCreateKeyButton() 方法
			CreateKeyButtonAction createKeyButtonAction = new CreateKeyButtonAction(generatedKeyArea,
					selectedAlgorithm);
			generatedKey = createKeyButtonAction.handleCreateKeyButton();
			// 如果生成的密钥不为空，则设置到 generatedKeyArea 中显示
			if (generatedKey != null && !generatedKey.isEmpty()) {
				generatedKeyArea.setText(generatedKey);
			} else {
				showAlert("Select AES or DES.");
			}
		});

		// action responding to the save key local button
		saveKeyLocalButton.setOnAction(e -> {
			if (generatedKey == null || generatedKey.isEmpty()) {
				showAlert("Please create a key first.");
			} else {
			SaveKeyLocalButtonAction saveKeyLocalButtonAction = new SaveKeyLocalButtonAction(selectedAlgorithm,
					generatedKey, primaryStage);
			saveKeyLocalButtonAction.handleSaveKeyButton();
			}
		});

		// action responding to the load key local button
		loadKeyLocalButton.setOnAction(e -> {
			LoadKeyLocalButtonAction loadKeyLocalButtonAction = new LoadKeyLocalButtonAction(primaryStage, algorithmGroup, generatedKeyArea);
			loadKeyLocalButtonAction.handleLoadKeyButton();
		});

		// action reponding to save key cloud button
		saveKeyCloudButton.setOnAction(e -> {
			if (generatedKey == null || generatedKey.isEmpty()) {
				showAlert("Please create a key first."); // 如果密钥为空，显示警告
			} else {
				// 获取已登录的用户名
				loggedInUsername = LoginPage.getLoggedInUsername();

				if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
					// 已经有了生成的密钥
					SaveKeyCloudButtonAction saveKeyCloudAction = new SaveKeyCloudButtonAction(selectedAlgorithm,
							generatedKey, primaryStage);
					saveKeyCloudAction.handleSaveKeyButton(loggedInUsername); // 将用户名作为参数传递给方法
				} else {
					showAlert("User not logged in."); // 如果用户未登录，则显示警告
				}
			}
		});
		
		//action responding to encrypt file button
		encryptFileButton.setOnAction(e -> {
		    EncryptFileButtonAction.handleEncryptFileButton(primaryStage, algorithmGroup, keyText, generatedKey);
		});

		Scene scene = new Scene(root, 900, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// toggle group that enables the user to select an encryption angorithm
	public static String getSelectedAlgorithm(ToggleGroup group) {
		RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
		return selectedRadioButton.getText();
	}

	static void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
