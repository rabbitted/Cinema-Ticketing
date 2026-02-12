package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Main_SelectionController {

    @FXML
    private Button userButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button closeBtn;

    // Initialize method for hover effects
    @FXML
    public void initialize() {
        // Setup hover effects for buttons
        setupButtonHoverEffect(userButton, Color.rgb(0, 150, 255)); // Lighter blue
        setupButtonHoverEffect(adminButton, Color.rgb(155, 100, 255)); // Lighter purple
        
        // Setup close button with complete functionality
        setupCloseButton();
    }

    @FXML
    private void handleUserLogin() {
        // Add click effect
        applyClickEffect(userButton);
        loadPage("/fxml/login.fxml");
    }

    @FXML
    private void handleAdminLogin() {
        // Add click effect
        applyClickEffect(adminButton);
        loadPage("/fxml/admin_login.fxml");
    }

    @FXML
    private void handleCloseButton() {
        // Apply click effect before closing
        applyClickEffect(closeBtn);
        
        // Add fade out animation for the entire scene
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), closeBtn.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });
        fadeOut.play();
    }

    @FXML
    private void handleCloseButtonHover(MouseEvent event) {
        // Scale effect on hover
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), closeBtn);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        scaleIn.play();
        
        // Add glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(20);
        glow.setSpread(0.3);
        closeBtn.setEffect(glow);
    }

    @FXML
    private void handleCloseButtonExit(MouseEvent event) {
        // Reset scale with animation
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), closeBtn);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        scaleOut.play();
        
        // Remove effect
        closeBtn.setEffect(null);
    }

    private void loadPage(String fxmlPath) {
        try {
            // Fade out current scene
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), userButton.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                    Stage stage = (Stage) userButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    
                    // Fade in new scene
                    root.setOpacity(0);
                    stage.setScene(scene);
                    
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to setup hover effects for regular buttons
    private void setupButtonHoverEffect(Button button, Color hoverColor) {
        final DropShadow shadow = new DropShadow();
        shadow.setColor(hoverColor);
        shadow.setRadius(15);
        shadow.setSpread(0.3);

        // Store original style to revert back
        final String originalStyle = button.getStyle();

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), button);
            scaleIn.setToX(1.05);
            scaleIn.setToY(1.05);
            scaleIn.play();
            
            button.setEffect(shadow);
            
            // Slightly brighten the button on hover
            if (originalStyle.contains("-fx-background-color")) {
                button.setStyle(originalStyle.replaceAll("-fx-background-color: #[0-9a-fA-F]{6}", 
                    String.format("-fx-background-color: #%02x%02x%02x;", 
                        (int)(hoverColor.getRed() * 255), 
                        (int)(hoverColor.getGreen() * 255), 
                        (int)(hoverColor.getBlue() * 255))));
            }
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), button);
            scaleOut.setToX(1.0);
            scaleOut.setToY(1.0);
            scaleOut.play();
            
            button.setEffect(null);
            button.setStyle(originalStyle);
        });
    }

    // Setup complete close button functionality
    private void setupCloseButton() {
        // Add mouse pressed effect
        closeBtn.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), closeBtn);
            scaleDown.setToX(0.9);
            scaleDown.setToY(0.9);
            scaleDown.play();
        });
        
        // Add mouse released effect
        closeBtn.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), closeBtn);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });
        
        // Add click effect with animation
        closeBtn.setOnMouseClicked(event -> {
            ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), closeBtn);
            clickScale.setToX(0.8);
            clickScale.setToY(0.8);
            clickScale.setAutoReverse(true);
            clickScale.setCycleCount(2);
            clickScale.setOnFinished(e -> handleCloseButton());
            clickScale.play();
        });
    }

    // Method to apply click effect with animation
    private void applyClickEffect(Button button) {
        // Scale down effect on click with animation
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(0.95);
        scaleDown.setToY(0.95);
        scaleDown.setAutoReverse(true);
        scaleDown.setCycleCount(2);
        scaleDown.play();
    }
}