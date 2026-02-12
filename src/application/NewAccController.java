package application;

import util.DatabaseConnection;
import util.SessionManager;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class NewAccController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phonenumberField;
    @FXML private TextField emailaddressField;
    @FXML private TextField usernameField;

    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;

    @FXML private CheckBox termsCheckBox;
    @FXML private ImageView togglePasswordEye;

    private final Image eyeOpenImage = new Image(getClass().getResourceAsStream("/images/open eye.png"));
    private final Image eyeClosedImage = new Image(getClass().getResourceAsStream("/images/eye-disabled_icon-icons.com_50399.png"));

    @FXML
    private void togglePasswordVisibility(MouseEvent event) {
        boolean showing = passwordVisibleField.isVisible();

        if (showing) {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            togglePasswordEye.setImage(eyeClosedImage);
        } else {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            togglePasswordEye.setImage(eyeOpenImage);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        // Check terms and conditions
        if (!termsCheckBox.isSelected()) {
            showAlert("Terms Not Accepted", "You must agree to the Terms and Conditions.");
            return;
        }

        // Get values from fields
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String phone = phonenumberField.getText().trim();
        String email = emailaddressField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible()
                ? passwordField.getText()
                : passwordVisibleField.getText();

        // Validate all fields
        if (first.isEmpty() || last.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return;
        }

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Validate phone number (basic check)
        if (!phone.matches("\\d{10,15}")) {
            showAlert("Invalid Phone", "Please enter a valid phone number (10-15 digits).");
            return;
        }

        // Validate password strength
        if (password.length() < 6) {
            showAlert("Weak Password", "Password must be at least 6 characters long.");
            return;
        }

        // Check if username already exists
        if (DatabaseConnection.userExists(username)) {
            showAlert("Username Taken", "This username is already taken. Please choose another one.");
            return;
        }

        System.out.println("🔍 Attempting to register user: " + username);
        System.out.println("📧 Email: " + email);
        System.out.println("👤 Name: " + first + " " + last);

        // Call DatabaseConnection.registerUser with the correct parameters
        boolean registered = DatabaseConnection.registerUser(
            username,     // username
            password,     // password
            email,        // email
            first,        // firstName
            last,         // lastName
            phone         // phone
        );

        if (registered) {
            System.out.println("✅ Registration successful for: " + username);
            
            // After successful registration, get the user ID and create User object
            User newUser = DatabaseConnection.getUserByUsername(username);
            
            if (newUser == null) {
                System.out.println("⚠️ Could not retrieve user after registration. Trying alternate method...");
                // Try to get user ID directly
                int userId = DatabaseConnection.getUserId(username);
                if (userId > 0) {
                    // Create User object manually
                    newUser = new User(username, password, email, first, last, phone, "customer");
                    newUser.setUserId(userId);
                }
            }
            
            if (newUser != null) {
                System.out.println("✅ Retrieved new user ID: " + newUser.getUserId());
                
                // Store in SessionManager
                SessionManager.setCurrentUser(newUser);
                SessionManager.printSessionInfo();
                
                // Go to welcome page with user data
                loadWelcomePage(event, newUser);
            } else {
                System.out.println("⚠️ Could not create user session after registration");
                showAlert("Success", "Account created successfully! Please login with your credentials.");
                loadWelcomePageWithName(event, first + " " + last);
            }
            
        } else {
            System.err.println("❌ Registration failed for: " + username);
            showAlert("Error", "Failed to create account. Please try again.");
        }
    }

    // Load Welcome Page with User object
    private void loadWelcomePage(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
            Parent root = loader.load();
            
            // Set user information in welcome controller
            try {
                Object controller = loader.getController();
                
                // Try to set user ID
                try {
                    java.lang.reflect.Method setUserIdMethod = controller.getClass().getMethod("setUserId", int.class);
                    setUserIdMethod.invoke(controller, user.getUserId());
                    System.out.println("✅ Set user ID in WelcomeController: " + user.getUserId());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUserId method");
                }
                
                // Try to set username
                try {
                    java.lang.reflect.Method setUsernameMethod = controller.getClass().getMethod("setUsername", String.class);
                    setUsernameMethod.invoke(controller, user.getUsername());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUsername method");
                }
                
                // Try to set full name
                try {
                    java.lang.reflect.Method setUserNameMethod = controller.getClass().getMethod("setUserName", String.class);
                    if (setUserNameMethod != null) {
                        setUserNameMethod.invoke(controller, user.getFullName());
                        System.out.println("✅ Set user name in WelcomeController: " + user.getFullName());
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUserName method");
                }
                
                // Try to pass the whole User object
                try {
                    java.lang.reflect.Method setUserMethod = controller.getClass().getMethod("setUser", User.class);
                    if (setUserMethod != null) {
                        setUserMethod.invoke(controller, user);
                        System.out.println("✅ Passed User object to WelcomeController");
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUser method");
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ Could not set user info in welcome controller: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Switch to welcome page
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            
            System.out.println("✅ Welcome page loaded for new user ID: " + user.getUserId() + ", Name: " + user.getFullName());

        } catch (IOException e) {
            System.err.println("❌ Failed to load welcome.fxml");
            e.printStackTrace();
            
            // Fallback: show success message and go back to login
            showAlert("Success", "Account created successfully! You can now login.");
            goBackToLogin(event);
        }
    }

    // Fallback method - load welcome page with just name
    private void loadWelcomePageWithName(ActionEvent event, String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
            Parent root = loader.load();
            
            // Try to set the user name in the welcome controller
            try {
                Object controller = loader.getController();
                java.lang.reflect.Method method = controller.getClass().getMethod("setUserName", String.class);
                if (method != null) {
                    method.invoke(controller, name);
                }
            } catch (Exception e) {
                System.out.println("Note: Could not set user name in controller");
            }

            // Switch to welcome page
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("➡ Welcome page loaded for: " + name);

        } catch (IOException e) {
            System.err.println("❌ Failed to load welcome.fxml");
            e.printStackTrace();
            
            // Fallback: show success message and go back to login
            showAlert("Success", "Account created successfully! Please login.");
            goBackToLogin(event);
        }
    }

    private void goBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to login page.");
        }
    }

    @FXML
    private void goBack(MouseEvent event) {
        switchScene(event, "/fxml/login.fxml");
    }

    private void switchScene(MouseEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Load Error", "Unable to open page.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}