package application;

import util.DatabaseConnection;
import util.SessionManager;
import model.User;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;

    @FXML private Label forgotPasswordLabel;
    @FXML private Hyperlink registerNowLink;

    @FXML private ImageView eyeIcon;
    @FXML private javafx.scene.control.Button backButton;

    private final Image eyeOpenImage =
            new Image(getClass().getResourceAsStream("/images/open eye.png"));
    private final Image eyeClosedImage =
            new Image(getClass().getResourceAsStream("/images/eye-disabled_icon-icons.com_50399.png"));

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnMouseEntered(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
                st.setToX(1.15);
                st.setToY(1.15);
                st.play();
            });

            backButton.setOnMouseExited(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }
    }

    // -------------------------------
    // LOGIN BUTTON - UPDATED VERSION
    // -------------------------------
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible()
                ? passwordField.getText()
                : passwordVisibleField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        System.out.println("🔍 Attempting login for: " + username);

        // Step 1: Validate login credentials
        boolean loginSuccess = DatabaseConnection.login(username, password);

        if (!loginSuccess) {
            showAlert("Error", "Invalid username or password.");
            return;
        }

        System.out.println("✅ Login credentials validated");

        // Step 2: Get complete user information
        User user = DatabaseConnection.getUserByUsername(username);
        
        if (user == null) {
            System.out.println("❌ CRITICAL: Login succeeded but could not retrieve user data!");
            showAlert("Error", "Could not retrieve user information. Please try again.");
            return;
        }

        System.out.println("✅ User data retrieved successfully:");
        System.out.println("   - User ID: " + user.getUserId());
        System.out.println("   - Username: " + user.getUsername());
        System.out.println("   - Full Name: " + user.getFullName());
        System.out.println("   - User Type: " + user.getUserType());
        System.out.println("   - Email: " + user.getEmail());

        // Step 3: Store user in SessionManager
        SessionManager.setCurrentUser(user);
        SessionManager.printSessionInfo();

        // Step 4: Navigate based on user type
        if ("admin".equalsIgnoreCase(user.getUserType())) {
            System.out.println("➡ Loading Admin Dashboard for user ID: " + user.getUserId());
            loadAdminDashboard(event, user);
        } else {
            System.out.println("➡ Loading Welcome Page for user ID: " + user.getUserId());
            loadWelcomePage(event, user);
        }
    }

    // -------------------------------
    // LOAD ADMIN DASHBOARD
    // -------------------------------
    private void loadAdminDashboard(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admindashboard.fxml"));
            Parent root = loader.load();
            
            // Set user information in admin dashboard controller
            try {
                Object controller = loader.getController();
                
                // Try to set user ID
                try {
                    java.lang.reflect.Method setUserIdMethod = controller.getClass().getMethod("setUserId", int.class);
                    setUserIdMethod.invoke(controller, user.getUserId());
                    System.out.println("✅ Set user ID in AdminDashboard: " + user.getUserId());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: AdminDashboard doesn't have setUserId method");
                }
                
                // Try to set username
                try {
                    java.lang.reflect.Method setUsernameMethod = controller.getClass().getMethod("setUsername", String.class);
                    setUsernameMethod.invoke(controller, user.getUsername());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: AdminDashboard doesn't have setUsername method");
                }
                
                // Try to set full name
                try {
                    java.lang.reflect.Method setFullNameMethod = controller.getClass().getMethod("setFullName", String.class);
                    setFullNameMethod.invoke(controller, user.getFullName());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: AdminDashboard doesn't have setFullName method");
                }
                
                // Try to set current user (existing method)
                try {
                    java.lang.reflect.Method setCurrentUserMethod = controller.getClass().getMethod("setCurrentUser", String.class);
                    if (setCurrentUserMethod != null) {
                        setCurrentUserMethod.invoke(controller, user.getUsername());
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: AdminDashboard doesn't have setCurrentUser method");
                }
                
                // Try to pass the whole User object if method exists
                try {
                    java.lang.reflect.Method setUserMethod = controller.getClass().getMethod("setUser", User.class);
                    if (setUserMethod != null) {
                        setUserMethod.invoke(controller, user);
                        System.out.println("✅ Passed User object to AdminDashboard");
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: AdminDashboard doesn't have setUser method");
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ Could not set user info in admin controller: " + e.getMessage());
                e.printStackTrace();
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            
            // Add fade transition
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            stage.setScene(newScene);
            stage.setTitle("Eliana Cinema - Admin Dashboard");
            stage.setMaximized(true);
            stage.show();
            
            System.out.println("✅ Admin dashboard loaded for user ID: " + user.getUserId() + ", Name: " + user.getFullName());
            
        } catch (IOException e) {
            System.err.println("❌ Failed to load admin dashboard");
            e.printStackTrace();
            
            // Fallback: load welcome page
            showAlert("Info", "Admin login successful! Loading welcome page...");
            loadWelcomePage(event, user);
        }
    }

    // -------------------------------
    // LOAD WELCOME PAGE
    // -------------------------------
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
                    System.out.println("✅ Set username in WelcomeController: " + user.getUsername());
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUsername method");
                }
                
                // Try to set full name (existing method)
                try {
                    java.lang.reflect.Method setUserNameMethod = controller.getClass().getMethod("setUserName", String.class);
                    if (setUserNameMethod != null) {
                        setUserNameMethod.invoke(controller, user.getFullName());
                        System.out.println("✅ Set user name in WelcomeController: " + user.getFullName());
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Note: WelcomeController doesn't have setUserName method");
                }
                
                // Try to pass the whole User object if method exists
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
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            stage.setScene(newScene);
            stage.show();
            
            System.out.println("✅ Welcome page loaded for user ID: " + user.getUserId() + ", Name: " + user.getFullName());
            
        } catch (IOException e) {
            System.err.println("❌ Failed to load welcome.fxml");
            e.printStackTrace();
            
            // Ultimate fallback: show success message
            showAlert("Success", "Login successful! Welcome, " + user.getFullName());
        }
    }

    // -------------------------------
    // BACK BUTTON CLICK
    // -------------------------------
    @FXML
    private void goBack(MouseEvent event) {
        if (backButton != null) {
            RotateTransition rotate = new RotateTransition(Duration.millis(250), backButton);
            rotate.setByAngle(-180);
            rotate.play();

            ScaleTransition st = new ScaleTransition(Duration.millis(250), backButton);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(0.8);
            st.setToY(0.8);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();

            st.setOnFinished(e -> loadScene(event, "/fxml/main_selection.fxml", "Main Login Page"));
        } else {
            loadScene(event, "/fxml/main_selection.fxml", "Main Login Page");
        }
    }

    // -------------------------------
    // FORGOT PASSWORD CLICK
    // -------------------------------
    @FXML
    private void handleForgotPassword(MouseEvent event) {
        loadScene(event, "/fxml/forgot_password.fxml", "Forgot Password Page");
    }

    // -------------------------------
    // REGISTER NOW CLICK
    // -------------------------------
    @FXML
    private void handleRegister(ActionEvent event) {
        loadScene(event, "/fxml/newacct.fxml", "New Account Page");
    }

    // -------------------------------
    // PASSWORD VISIBILITY TOGGLE
    // -------------------------------
    @FXML
    private void togglePasswordVisibility(MouseEvent event) {
        boolean showing = passwordVisibleField.isVisible();

        if (showing) {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            eyeIcon.setImage(eyeClosedImage);
        } else {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            eyeIcon.setImage(eyeOpenImage);
        }
    }

    // -------------------------------
    // LOAD SCENE (MouseEvent)
    // -------------------------------
    private void loadScene(MouseEvent event, String fxmlPath, String logMsg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            stage.setScene(newScene);
            stage.show();

            System.out.println("➡ Switched to: " + logMsg);

        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // -------------------------------
    // LOAD SCENE (ActionEvent)
    // -------------------------------
    private void loadScene(ActionEvent event, String fxmlPath, String logMsg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            stage.setScene(newScene);
            stage.show();

            System.out.println("➡ Switched to: " + logMsg);

        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // -------------------------------
    // ALERT
    // -------------------------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}