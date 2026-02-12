package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.util.Objects;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        // Load FXML for login/selection screen
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_selection.fxml"));
        Scene scene = new Scene(root);
        
        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        // Configure stage
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Eliana Cinema - Dashboard");
        
        // Set stage to fill screen
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        
        // Add window dragging capability
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        
        // Make scene fill the stage and be responsive
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Scene width changed: " + newVal);
        });
        
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Scene height changed: " + newVal);
        });
        
        // Optional: Add icon
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }
        
        stage.setScene(scene);
        stage.show();
        
        System.out.println("✅ Application started in fullscreen mode");
        System.out.println("Screen dimensions: " + screenBounds.getWidth() + "x" + screenBounds.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}