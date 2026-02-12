package application;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button continueButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private ImageView backButtonIcon;

    private String username = "";
    private int userId = 0;
    private String fullName = "";

    @FXML
    public void initialize() {
        System.out.println("✅ WelcomeController initialized");
        System.out.println("🔍 backButton is: " + (backButton != null ? "FOUND" : "NULL"));
        System.out.println("🔍 continueButton is: " + (continueButton != null ? "FOUND" : "NULL"));
        
        // Try to get user from SessionManager first
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getUserId() > 0) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
            
            System.out.println("✅ WelcomeController: Retrieved from SessionManager - ID: " + userId + ", Name: " + fullName);
            
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + this.fullName + "!");
                System.out.println("✅ Welcome page set for: " + this.fullName);
            }
        }
        
        // Add hover effects to the continue button
        if (continueButton != null) {
            addHoverEffect(continueButton);
        } else {
            System.err.println("⚠️ continueButton is null in initialize()");
        }
        
        // Add hover effects to the back button
        if (backButton != null) {
            addBackButtonHoverEffect();
        } else {
            System.err.println("⚠️ backButton is null in initialize()");
        }
        
        // Apply fade-in animation to welcome label
        if (welcomeLabel != null) {
            FadeTransition ft = new FadeTransition(Duration.seconds(1.5), welcomeLabel);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
        
        // Add a subtle pulse animation to continue button
        if (continueButton != null) {
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), continueButton);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.05);
            pulse.setToY(1.05);
            pulse.setCycleCount(ScaleTransition.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
        }
    }

    // ========== SETTER METHODS ==========
    
    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("✅ WelcomeController.setUserId(" + userId + ") called");
        
        // If we have user ID but no name, try to get user from SessionManager
        if (userId > 0 && (fullName == null || fullName.isEmpty())) {
            User user = SessionManager.getCurrentUser();
            if (user != null && user.getUserId() == userId) {
                this.fullName = user.getFullName();
                this.username = user.getUsername();
                
                if (welcomeLabel != null) {
                    welcomeLabel.setText("Welcome, " + this.fullName + "!");
                }
            }
        }
    }
    
    public void setUsername(String username) {
        this.username = username;
        System.out.println("✅ WelcomeController.setUsername(" + username + ") called");
    }
    
    public void setUser(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
            System.out.println("✅ WelcomeController.setUser() called - ID: " + userId + ", Name: " + fullName);
            
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + this.fullName + "!");
            }
        }
    }
    
    public void setUserName(String fullName) {
        this.fullName = fullName;
        System.out.println("✅ WelcomeController.setUserName(" + fullName + ") called");
        
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + this.fullName + "!");
            System.out.println("✅ Welcome page set for: " + this.fullName);
        }
        
        // Apply fade-in animation to welcome label
        if (welcomeLabel != null) {
            FadeTransition ft = new FadeTransition(Duration.seconds(1.5), welcomeLabel);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }

    // -------------------------------
    // CONTINUE BUTTON - GO TO DASHBOARD
    // -------------------------------
    @FXML
    private void goToDashboard(ActionEvent event) {
        System.out.println("🚀 Continuing to dashboard for username: " + username + ", ID: " + userId);
        
        // If we don't have user ID, try to get it from SessionManager
        if (userId == 0) {
            User user = SessionManager.getCurrentUser();
            if (user != null && user.getUserId() > 0) {
                this.userId = user.getUserId();
                this.username = user.getUsername();
                this.fullName = user.getFullName();
                System.out.println("✅ Retrieved user from SessionManager - ID: " + userId);
            }
        }
        
        if (userId == 0) {
            System.out.println("⚠️ WARNING: User ID is 0! Dashboard will not work properly.");
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userdashboard.fxml"));
            Parent root = loader.load();

            // Pass user information to dashboard controller
            Object controller = loader.getController();
            if (controller != null) {
                System.out.println("🔍 Dashboard controller loaded. Passing user data...");
                
                // Try to pass user ID (most important)
                try {
                    controller.getClass().getMethod("setUserId", int.class)
                              .invoke(controller, userId);
                    System.out.println("✅ User ID passed to dashboard: " + userId);
                } catch (NoSuchMethodException e) {
                    System.out.println("❌ Dashboard doesn't have setUserId method");
                }
                
                // Try to pass username
                try {
                    controller.getClass().getMethod("setUsername", String.class)
                              .invoke(controller, username);
                    System.out.println("✅ Username passed to dashboard: " + username);
                } catch (NoSuchMethodException e) {
                    System.out.println("❌ Dashboard doesn't have setUsername method");
                }
                
                // Try to pass full name
                try {
                    controller.getClass().getMethod("setUserName", String.class)
                              .invoke(controller, fullName);
                    System.out.println("✅ Full name passed to dashboard: " + fullName);
                } catch (NoSuchMethodException e) {
                    System.out.println("❌ Dashboard doesn't have setUserName method");
                }
                
                // Try to pass User object
                try {
                    controller.getClass().getMethod("setUser", User.class)
                              .invoke(controller, SessionManager.getCurrentUser());
                    System.out.println("✅ User object passed to dashboard");
                } catch (NoSuchMethodException e) {
                    System.out.println("❌ Dashboard doesn't have setUser method");
                }
            } else {
                System.out.println("⚠️ Dashboard controller is null!");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            
            // Add fade transition
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            stage.setScene(newScene);
            stage.show();

            System.out.println("✅ Dashboard loaded successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ ERROR: Could not load /fxml/userdashboard.fxml");
            
            // Fallback to login page
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // -------------------------------
    // BACK BUTTON - WITH ROTATION ANIMATION
    // -------------------------------
    @FXML
    private void handleBackButton(ActionEvent event) {
        System.out.println("↩️ Going back to main selection page");
        System.out.println("🔍 backButton reference: " + backButton);
        
        if (backButton != null) {
            System.out.println("🎬 Playing back button animation");
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

            st.setOnFinished(e -> {
                System.out.println("🎬 Animation finished, loading scene...");
                loadScene("/fxml/main_selection.fxml", "Main Selection Page");
            });
        } else {
            System.err.println("❌ backButton is null, loading scene directly");
            // Use the event source to get the stage
            loadSceneFromEvent(event, "/fxml/main_selection.fxml", "Main Selection Page");
        }
    }
    
    // -------------------------------
    // LOAD SCENE METHOD (USING BACKBUTTON)
    // -------------------------------
    private void loadScene(String fxmlPath, String logMsg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (backButton != null && backButton.getScene() != null) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene newScene = new Scene(root);
                
                // Add fade transition
                FadeTransition ft = new FadeTransition(Duration.millis(500), root);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                
                stage.setScene(newScene);
                stage.show();
                
                System.out.println("➡ Switched to: " + logMsg);
            } else {
                System.err.println("❌ Cannot get stage from backButton");
            }
            
        } catch (IOException e) {
            System.err.println("❌ Failed to load: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    // -------------------------------
    // LOAD SCENE FROM EVENT (FALLBACK)
    // -------------------------------
    private void loadSceneFromEvent(ActionEvent event, String fxmlPath, String logMsg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            
            // Add fade transition
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
    // HOVER EFFECTS
    // -------------------------------
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.08);
            st.setToY(1.08);
            st.play();
            
            // Change button color on hover
            if (button == continueButton) {
                button.setStyle("-fx-background-color: #039BE5; -fx-background-radius: 18; " +
                               "-fx-text-fill: white; -fx-font-weight: bold;");
            }
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            
            // Revert to original style
            if (button == continueButton) {
                button.setStyle("-fx-background-color: #0288d1; -fx-background-radius: 18; " +
                               "-fx-text-fill: white; -fx-font-weight: bold;");
            }
        });
    }
    
    private void addBackButtonHoverEffect() {
        if (backButton == null) return;
        
        backButton.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
            st.setToX(1.2);
            st.setToY(1.2);
            st.play();
        });
        
        backButton.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
    
    // -------------------------------
    // GETTERS
    // -------------------------------
    public String getUsername() {
        return this.username;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public String getFullName() {
        return this.fullName;
    }
}