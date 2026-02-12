package application;

import util.DatabaseConnection;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Forgot_PasswordController {

    @FXML
    private TextField usernameoremailField;

    @FXML
    private Button sendcodeBtn;

    @FXML
    private Button backButton;
    
    // Store the verification code and user info for the next screen
    public static class VerificationData {
        public static String code;
        public static String email;
        public static String username;
        public static String input; // Store the original input
    }

    @FXML
    private void initialize() {
        // Clear any previous verification data
        VerificationData.code = null;
        VerificationData.email = null;
        VerificationData.username = null;
        VerificationData.input = null;
    }

    @FXML
    private void handleSendCode() {
        String usernameOrEmail = usernameoremailField.getText().trim();
        
        if (usernameOrEmail.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter your username or email address.");
            return;
        }
        
        System.out.println("\n🔐 Password Reset Request:");
        System.out.println("   Input: " + usernameOrEmail);
        
        // Check if user exists in database by checking both username and email
        boolean userExists = DatabaseConnection.checkUserExists(usernameOrEmail);
        
        if (!userExists) {
            showAlert(AlertType.ERROR, "User Not Found", 
                "No active account found with that username or email. Please try again.");
            return;
        }
        
        // Generate verification code
        String generatedCode = generateVerificationCode();
        
        String userEmail;
        String username;
        
        // Determine if input is email or username
        if (usernameOrEmail.contains("@")) {
            userEmail = usernameOrEmail;
            username = DatabaseConnection.getUsernameFromEmail(userEmail);
            if (username == null) {
                // Fallback: extract username from email
                username = userEmail.substring(0, userEmail.indexOf("@"));
            }
        } else {
            username = usernameOrEmail;
            userEmail = DatabaseConnection.getEmailFromUsername(username);
            if (userEmail == null) {
                // Fallback for testing
                userEmail = username + "@example.com";
            }
        }
        
        System.out.println("   Username: " + username);
        System.out.println("   Email: " + userEmail);
        System.out.println("   Verification Code: " + generatedCode);
        
        // Save data for next screen
        VerificationData.code = generatedCode;
        VerificationData.email = userEmail;
        VerificationData.username = username;
        VerificationData.input = usernameOrEmail;
        
        // Show success message
        showAlert(AlertType.INFORMATION, "Verification Code", 
            "A verification code has been generated.\n\n" +
            "Code: " + generatedCode + "\n" +
            "For: " + username + "\n" +
            "Email: " + userEmail + "\n\n" +
            "Please enter this code on the next screen to reset your password.");
        
        // Open reset password screen
        openResetPasswordScreen();
    }
    
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }
    
    private void openResetPasswordScreen() {
        try {
            System.out.println("\n➡ Opening reset password screen...");
            System.out.println("   Code: " + VerificationData.code);
            System.out.println("   Email: " + VerificationData.email);
            System.out.println("   Username: " + VerificationData.username);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reset_password.fxml"));
            Parent root = loader.load();
            
            Stage currentStage = (Stage) sendcodeBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
            
            System.out.println("✅ Reset password screen loaded successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", 
                "Failed to open reset password screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            // Go back to login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            // Close window if can't go back
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}