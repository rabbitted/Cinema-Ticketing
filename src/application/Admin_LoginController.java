package application;

import util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Admin_LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private ImageView eyeIcon;
    @FXML private Button backButton;
    
    private final Image eyeOpenImage =
            new Image(getClass().getResourceAsStream("/images/open eye.png"));
    private final Image eyeClosedImage =
            new Image(getClass().getResourceAsStream("/images/eye-disabled_icon-icons.com_50399.png"));
    
    @FXML
    public void initialize() {
        // Initialize eye icon with closed image
        if (eyeIcon != null) {
            eyeIcon.setImage(eyeClosedImage);
        }
        
        // Add back button hover animations
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
    // BACK BUTTON CLICK (with animation)
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

            st.setOnFinished(e -> loadScene("/fxml/main_selection.fxml", "Main Login Page"));
        } else {
            loadScene("/fxml/main_selection.fxml", "Main Login Page");
        }
    }
    
    // -------------------------------
    // EYE ICON TOGGLE PASSWORD VISIBILITY
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
    
    @FXML
    private void handleAdminLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible()
                ? passwordField.getText()
                : passwordVisibleField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT user_id, password, first_name FROM users WHERE username = ? AND user_type = 'admin'";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                
                // CRITICAL: Debug logging
                System.out.println("DEBUG: Username: " + username);
                System.out.println("DEBUG: Password from DB: '" + storedPassword + "'");
                System.out.println("DEBUG: Length: " + (storedPassword != null ? storedPassword.length() : "null"));
                
                boolean passwordValid = false;
                
                // Check if the stored password is a valid BCrypt hash
                if (storedPassword != null && !storedPassword.isEmpty()) {
                    if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
                        // It's a BCrypt hash, verify it
                        try {
                            passwordValid = BCrypt.checkpw(password, storedPassword);
                            System.out.println("DEBUG: BCrypt verification attempted - Result: " + passwordValid);
                        } catch (IllegalArgumentException e) {
                            // The hash looks like BCrypt but is malformed
                            System.err.println("ERROR: Malformed BCrypt hash in database for user: " + username);
                            System.err.println("ERROR: " + e.getMessage());
                            passwordValid = false;
                        }
                    } else {
                        // Not a BCrypt hash - might be plain text (for development/testing)
                        System.out.println("DEBUG: Password is not BCrypt format, checking plain text match");
                        passwordValid = password.equals(storedPassword);
                        System.out.println("DEBUG: Plain text match result: " + passwordValid);
                        
                        // If plain text matches, you should hash it for security
                        if (passwordValid) {
                            System.out.println("WARNING: Plain text password found for admin. Should be hashed!");
                            // Optional: Update to hashed version
                            // hashAndUpdatePassword(username, password);
                        }
                    }
                } else {
                    System.err.println("ERROR: Empty or null password in database for user: " + username);
                }
                
                if (passwordValid) {
                    // Login successful
                    int adminId = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    
                    System.out.println("✅ Admin login successful for: " + username);
                    loadAdminDashboard(adminId, firstName);
                } else {
                    System.out.println("❌ Login failed for: " + username);
                    showAlert("Login Failed", "Invalid username or password");
                }
            } else {
                System.out.println("❌ No admin user found with username: " + username);
                showAlert("Login Failed", "Invalid username or password");
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Error connecting to database: " + e.getMessage());
        }
    }
    
    private void loadAdminDashboard(int adminId, String firstName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admindashboard.fxml"));
            Parent root = loader.load();
            
            // Add fade transition
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            // Pass admin info to dashboard controller
            Admin_DashboardController controller = loader.getController();
            controller.setAdminInfo(adminId, firstName);
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard - Eliana Cinema");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load admin dashboard");
        }
    }
    
    // -------------------------------
    // LOAD SCENE
    // -------------------------------
    private void loadScene(String fxmlPath, String logMsg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene newScene = new Scene(root);
            
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            stage.setScene(newScene);
            stage.show();
            
            System.out.println("➡ Switched to: " + logMsg);
            
        } catch (Exception e) {
            System.err.println("❌ Failed to load: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    // Optional: Method to hash plain text passwords in database
    private void hashAndUpdatePassword(String username, String plainPassword) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            
            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Successfully updated password to BCrypt hash for user: " + username);
            }
            
            pstmt.close();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to hash password for user: " + username);
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}