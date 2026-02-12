package application;

import util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Reset_PasswordController {

    @FXML
    private TextField verificationCodeField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField newPasswordFieldVisible;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordFieldVisible;

    @FXML
    private ImageView toggleNewPasswordIcon;

    @FXML
    private ImageView toggleConfirmPasswordIcon;

    @FXML
    private Button resetpasswordButton;

    @FXML
    private Button backButton;
    
    @FXML
    private ImageView backButtonIcon;
    
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    
    private Image eyeOpenIcon;
    private Image eyeClosedIcon;

    @FXML
    private void initialize() {
        System.out.println("\n🔄 Reset_PasswordController initialized");
        System.out.println("   Expected code: " + Forgot_PasswordController.VerificationData.code);
        System.out.println("   For user: " + Forgot_PasswordController.VerificationData.username);
        System.out.println("   Email: " + Forgot_PasswordController.VerificationData.email);
        
        // Initialize eye icons
        try {
            eyeOpenIcon = new Image(getClass().getResourceAsStream("/images/open eye.png"));
            eyeClosedIcon = new Image(getClass().getResourceAsStream("/images/eye-disabled_icon-icons.com_50399.png"));
        } catch (Exception e) {
            System.err.println("⚠️ Eye icons not found: " + e.getMessage());
        }
        
        // Bind text fields
        newPasswordFieldVisible.textProperty().bindBidirectional(newPasswordField.textProperty());
        confirmPasswordFieldVisible.textProperty().bindBidirectional(confirmPasswordField.textProperty());
        
        updatePasswordToggleIcons();
    }

    @FXML
    private void toggleNewPassword() {
        isNewPasswordVisible = !isNewPasswordVisible;
        updatePasswordVisibility();
    }

    @FXML
    private void toggleConfirmPassword() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        updatePasswordVisibility();
    }
    
    private void updatePasswordVisibility() {
        if (isNewPasswordVisible) {
            newPasswordField.setVisible(false);
            newPasswordField.setManaged(false);
            newPasswordFieldVisible.setVisible(true);
            newPasswordFieldVisible.setManaged(true);
        } else {
            newPasswordField.setVisible(true);
            newPasswordField.setManaged(true);
            newPasswordFieldVisible.setVisible(false);
            newPasswordFieldVisible.setManaged(false);
        }
        
        if (isConfirmPasswordVisible) {
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            confirmPasswordFieldVisible.setVisible(true);
            confirmPasswordFieldVisible.setManaged(true);
        } else {
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            confirmPasswordFieldVisible.setVisible(false);
            confirmPasswordFieldVisible.setManaged(false);
        }
        
        updatePasswordToggleIcons();
    }
    
    private void updatePasswordToggleIcons() {
        if (eyeOpenIcon != null && eyeClosedIcon != null) {
            if (isNewPasswordVisible) {
                toggleNewPasswordIcon.setImage(eyeOpenIcon);
            } else {
                toggleNewPasswordIcon.setImage(eyeClosedIcon);
            }
            
            if (isConfirmPasswordVisible) {
                toggleConfirmPasswordIcon.setImage(eyeOpenIcon);
            } else {
                toggleConfirmPasswordIcon.setImage(eyeClosedIcon);
            }
        }
    }

    @FXML
    private void handleResetPassword() {
        String verificationCode = verificationCodeField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        System.out.println("\n🔄 Processing password reset...");
        System.out.println("   Entered code: " + verificationCode);
        
        // Validate inputs
        if (verificationCode.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter the verification code.");
            return;
        }
        
        if (newPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter a new password.");
            return;
        }
        
        if (confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please confirm your new password.");
            return;
        }
        
        // Validate verification code
        String expectedCode = Forgot_PasswordController.VerificationData.code;
        if (expectedCode == null || !verificationCode.equals(expectedCode)) {
            System.out.println("❌ Code mismatch!");
            System.out.println("   Expected: " + expectedCode);
            System.out.println("   Got: " + verificationCode);
            
            showAlert(AlertType.ERROR, "Invalid Code", 
                "The verification code is incorrect.\n\n" +
                "Please check the code and try again.");
            return;
        }
        
        System.out.println("✅ Verification code matched!");
        
        // Validate password strength
        if (newPassword.length() < 6) {
            showAlert(AlertType.ERROR, "Weak Password", 
                "Password must be at least 6 characters long.\n" +
                "Current length: " + newPassword.length());
            return;
        }
        
        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Password Mismatch", 
                "Passwords do not match.\n\n" +
                "Please make sure both passwords are identical.");
            return;
        }
        
        System.out.println("✅ Passwords match!");
        
        // Get user info
        String email = Forgot_PasswordController.VerificationData.email;
        String username = Forgot_PasswordController.VerificationData.username;
        
        System.out.println("📧 Updating password for:");
        System.out.println("   Email: " + email);
        System.out.println("   Username: " + username);
        
        // Try to update password using email first
        boolean passwordUpdated = DatabaseConnection.resetPassword(email, newPassword);
        
        if (!passwordUpdated) {
            // If email update fails, try username
            System.out.println("⚠️ Email update failed, trying username...");
            passwordUpdated = DatabaseConnection.updatePasswordByUsername(username, newPassword);
        }
        
        if (passwordUpdated) {
            System.out.println("✅ Password successfully updated in database!");
            
            showAlert(AlertType.INFORMATION, "Success!", 
                "✅ Password has been reset successfully!\n\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n\n" +
                "You can now log in with your new password.");
            
            // Clear verification data
            Forgot_PasswordController.VerificationData.code = null;
            Forgot_PasswordController.VerificationData.email = null;
            Forgot_PasswordController.VerificationData.username = null;
            Forgot_PasswordController.VerificationData.input = null;
            
            // Return to login screen
            returnToLoginScreen();
            
        } else {
            System.out.println("❌ All password update attempts failed!");
            
            showAlert(AlertType.ERROR, "Database Error", 
                "Failed to update password in database.\n\n" +
                "Possible reasons:\n" +
                "1. User not found: " + username + " / " + email + "\n" +
                "2. Database connection issue\n" +
                "3. User account is inactive\n\n" +
                "Please try again or contact support.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            System.out.println("↩ Going back to forgot password screen...");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/forgot_password.fxml"));
            Parent root = loader.load();
            
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            returnToLoginScreen();
        }
    }
    
    private void returnToLoginScreen() {
        try {
            System.out.println("➡ Returning to login screen...");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage currentStage = (Stage) resetpasswordButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Stage stage = (Stage) resetpasswordButton.getScene().getWindow();
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