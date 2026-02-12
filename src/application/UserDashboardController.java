package application;

import util.DatabaseConnection;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;

import java.util.Collections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.util.Callback;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Font;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class UserDashboardController implements Initializable {

    // Navigation Buttons
    @FXML private Button homeBtn;
    @FXML private Button moviesBtn;
    @FXML private Button snacksDrinksBtn;
    @FXML private Button bookTicketsBtn;
    @FXML private Button myBookingsBtn;
    @FXML private Button aboutUsBtn;
    @FXML private Button helpBtn;
    @FXML private Button profileBtn;
    @FXML private Button logoutBtn;
    
    // Window Controls
    @FXML private Button closeBtn;
    @FXML private Button minimizeBtn;
    @FXML private Button maximizeBtn;
    
    // User Info
    @FXML private Label usernameLabel;
    @FXML private ImageView logoImageView;
    @FXML private FontAwesomeIcon userIcon;
    @FXML private StackPane profileIconContainer;
    @FXML private ImageView navProfileImageView;
    
    // Pages
    @FXML private AnchorPane homePageForm;
    @FXML private AnchorPane moviesPageForm;
    @FXML private AnchorPane snacksDrinksForm;
    @FXML private AnchorPane bookTicketsForm;
    @FXML private AnchorPane myBookingsForm;
    @FXML private AnchorPane aboutUsForm;
    @FXML private AnchorPane helpForm;
    @FXML private AnchorPane profileForm;
    @FXML private AnchorPane paymentForm;
    
    // Home Page Components
    @FXML private Button bookNowHeroBtn;
    @FXML private Button viewAllMoviesBtn;
    @FXML private Button viewAllSnacksBtn;
    @FXML private HBox nowShowingContainer;
    @FXML private HBox comingSoonContainer;
    @FXML private HBox popularSnacksContainer;
    
    // Movies Page Components
    @FXML private TextField movieSearchField;
    @FXML private ComboBox<String> movieCategoryFilter;
    @FXML private ComboBox<String> movieGenreFilter;
    @FXML private Button searchMovieBtn;
    @FXML private TilePane allMoviesTilePane;
    
    // Snacks & Drinks Page Components
    @FXML private Button allSnacksBtn;
    @FXML private Button snacksOnlyBtn;
    @FXML private Button drinksOnlyBtn;
    @FXML private Button combosBtn;
    @FXML private TextField snackSearchField;
    @FXML private Button searchSnackBtn;
    @FXML private TilePane snacksDrinksTilePane;
    
    // Book Tickets Page Components
    @FXML private ComboBox<String> bookMovieSelect;
    @FXML private DatePicker bookDateSelect;
    @FXML private ComboBox<String> bookTimeSelect;
    @FXML private Button findShowtimesBtn;
    @FXML private VBox showtimesContainer;
    @FXML private GridPane showtimesGrid;
    @FXML private VBox seatSelectionContainer;
    @FXML private ScrollPane seatSelectionScroll;
    
    @FXML private Label selectedSeatsLabel;
    @FXML private Label seatPriceLabel;
    @FXML private VBox snacksSelectionContainer;
    @FXML private TilePane snacksSelectionTile;
    @FXML private Label snacksTotalLabel;
    @FXML private VBox bookingSummaryContainer;
    @FXML private Label summaryMovieLabel;
    @FXML private Label summaryDateTimeLabel;
    @FXML private Label summarySeatsLabel;
    @FXML private Label summarySnacksLabel;
    @FXML private Label summaryTotalLabel;
    @FXML private Button proceedToPaymentBtn;
    @FXML private Button viewCartFromBookingBtn;
    
    // My Bookings Page Components
    @FXML private ComboBox<String> bookingStatusFilterUser;
    @FXML private DatePicker bookingDateFilter;
    @FXML private Button filterBookingsUserBtn;
    @FXML private TableView<Booking> myBookingsTable;
    @FXML private TableColumn<Booking, String> myBookingIdColumn;
    @FXML private TableColumn<Booking, String> myBookingMovieColumn;
    @FXML private TableColumn<Booking, String> myBookingDateColumn;
    @FXML private TableColumn<Booking, String> myBookingTimeColumn;
    @FXML private TableColumn<Booking, String> myBookingSeatsColumn;
    @FXML private TableColumn<Booking, String> myBookingAmountColumn;
    @FXML private TableColumn<Booking, String> myBookingStatusColumn;
    @FXML private TableColumn<Booking, String> myBookingActionColumn;
    @FXML private Button viewBookingDetailsBtn;
    @FXML private Button cancelBookingUserBtn;
    @FXML private Button printTicketBtn;
    @FXML private Button deleteBookingUserBtn;
    
    // Payment Page Components
    @FXML private Label paymentMovieLabel;
    @FXML private Label paymentSnacksLabel;
    @FXML private Label paymentDateTimeLabel;
    @FXML private Label paymentSeatsLabel;
    @FXML private Label paymentTicketsLabel;
    @FXML private Label paymentTicketsSubtotalLabel;
    @FXML private Label paymentSnacksSubtotalLabel;
    @FXML private Label paymentServiceChargeLabel;
    @FXML private Label paymentTotalAmountLabel;
    
    @FXML private RadioButton telebirrRadio;
    @FXML private RadioButton cbeRadio;
    @FXML private ToggleGroup paymentMethodGroup;
    
    @FXML private VBox paymentDetailsForm;
    @FXML private VBox otpSection;
    @FXML private TextField paymentPhoneField;
    @FXML private TextField otpField;
    @FXML private Button resendOtpBtn;
    @FXML private Label otpTimerLabel;
    @FXML private Button verifyOtpBtn;
    @FXML private Button requestOtpBtn;
    
    @FXML private CheckBox termsCheckBox;
    @FXML private Button backToBookingBtn;
    @FXML private Button makePaymentBtn;
    
    // Payment Processing Overlays
    @FXML private AnchorPane paymentProcessingPane;
    @FXML private ProgressIndicator paymentProgressIndicator;
    @FXML private Label paymentStatusLabel;
    @FXML private Label paymentMessageLabel;
    
    @FXML private AnchorPane paymentSuccessPane;
    @FXML private Label successMessageLabel;
    @FXML private Label bookingIdLabel;
    @FXML private Label paymentAmountLabel;
    @FXML private Button viewTicketBtn;
    @FXML private Button doneBtn;
    
    @FXML private AnchorPane paymentFailedPane;
    @FXML private Label errorMessageLabel;
    @FXML private Button tryAgainBtn;
    @FXML private Button cancelPaymentBtn;
    
    // Cart Components
    @FXML private Button cartBtn;
    @FXML private Circle cartBadge;
    @FXML private Label cartTotalLabel;
    
    // Cart Modal Components
    @FXML private AnchorPane cartModal;
    @FXML private VBox emptyCartMessage;
    @FXML private ListView<CartItem> cartItemsListView;
    @FXML private VBox cartSummaryBox;
    @FXML private Label cartSubtotalLabel;
    @FXML private Label cartTotalModalLabel;
    @FXML private Button closeCartBtn;
    @FXML private Button clearCartBtn;
    @FXML private Button checkoutBtn;
    @FXML private Button browseSnacksBtn;
    
    // Profile Page Components
    @FXML private ImageView profileImageView;
    @FXML private Button changeProfilePicBtn;
    @FXML private TextField profileNameField;
    @FXML private TextField profileEmailField;
    @FXML private TextField profilePhoneField;
    @FXML private PasswordField profilePasswordField;
    @FXML private PasswordField profileConfirmPasswordField;
    @FXML private Button saveProfileBtn;
    @FXML private Button cancelProfileBtn;
    
    // About Us Components
    @FXML private ImageView cinemaBannerImage;
    @FXML private Label cinemaNameLabel;
    @FXML private Label cinemaDescriptionLabel;
    @FXML private Label cinemaPhoneLabel;
    @FXML private Label cinemaEmailLabel;
    @FXML private Label cinemaAddressLabel;
    @FXML private Label cinemaHoursLabel;
    
    // Help Components
    @FXML private VBox faqContainer;
    @FXML private Label helpPhoneLabel;
    @FXML private Label helpEmailLabel;
    @FXML private Label helpHoursLabel;
    @FXML private Label helpInstructionsLabel;
    
    private Stage movieDetailStage;
    private Stage snackDetailStage;
    
    private String username = "";
    private int userId = 0;
    private String userFullName = "";
    
    // Data lists
    private ObservableList<Movie> nowShowingMovies = FXCollections.observableArrayList();
    private ObservableList<Movie> comingSoonMovies = FXCollections.observableArrayList();
    private ObservableList<Snack> allSnacks = FXCollections.observableArrayList();
    private ObservableList<Movie> allMoviesList = FXCollections.observableArrayList();
    
    // Cart management
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private double cartTotal = 0.0;
    
    // Seat selection variables
    private ObservableList<String> selectedSeats = FXCollections.observableArrayList();
    private Map<String, SeatSelection> seatSelections = new HashMap<>(); // Track seat type and price for each seat
    private Map<String, Pane> seatButtons = new HashMap<>();
    private double seatPremiumPrice = 20.0; // VIP seat premium price
    private double movieBasePrice = 0.0;
    private Movie selectedMovie;
    private int selectedShowtimeId = 0;
    private int selectedHallId = 0;
    private String selectedShowtime = "";
    private String selectedHall = "";
    
    // Profile image
    private String profileImagePath = "";
    
    // Animation
    private ScaleTransition scaleIn;
    private ScaleTransition scaleOut;
    private DropShadow hoverEffect;
    private DropShadow selectedEffect;
    private Glow glowEffect;
    
    // OTP Timer
    private Timeline otpTimer;
    private int otpSecondsRemaining = 120;
    private String generatedOtp = "";
    
    // Auto-refresh timer
    private Timeline dataRefreshTimer;
    private final int REFRESH_INTERVAL_SECONDS = 60;
    
    // Seat colors - Updated to match admin seat colors
    private final Color AVAILABLE_COLOR = Color.web("#2ecc71"); // Green - Available
    private final Color SELECTED_COLOR = Color.web("#3498db");  // Blue - Selected
    private final Color VIP_COLOR = Color.web("#f39c12");      // Orange - VIP
    private final Color BOOKED_COLOR = Color.web("#e74c3c");   // Red - Booked
    private final Color BLOCKED_COLOR = Color.web("#95a5a6");  // Gray - Blocked
    
    // Discount rates based on number of tickets
    private final Map<Integer, Double> discountRates = new HashMap<>();
    
    // Class to store seat selection info
    private class SeatSelection {
        String seatLabel;
        String seatType;
        double seatPrice;
        
        SeatSelection(String seatLabel, String seatType, double seatPrice) {
            this.seatLabel = seatLabel;
            this.seatType = seatType;
            this.seatPrice = seatPrice;
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ UserDashboardController initialized");
        
        // Initialize discount rates
        initializeDiscountRates();
        
        // Initialize animations and effects
        initializeAnimations();
        
        // Initialize navigation
        setupNavigation();
        
        // Initialize combo boxes
        initializeComboBoxes();
        
        // Initialize tables
        initializeTables();
        
        // Setup window controls
        setupWindowControls();
        
        // Setup payment method toggle
        setupPaymentMethods();
        
        // Setup cart
        setupCart();
        
        // Setup OTP timer
        setupOtpTimer();
        
        // Setup logout button properly
        setupLogoutButton();
        
        // Setup payment overlays
        setupPaymentOverlays();
        
        // Setup button hover effects
        setupButtonHoverEffects();
        
        // Setup auto-refresh timer
        setupDataRefreshTimer();
        
        // Setup profile image views
        setupNavProfileImageView();
        
        // Load data from database
        loadHomePageData();
        loadAllMovies();
        loadAllSnacks();
        loadAboutUsContent();
        loadHelpContent();
        loadMoviesForBooking();
        
        // Set default date to today
        if (bookDateSelect != null) {
            bookDateSelect.setValue(LocalDate.now());
        }
        
        // Setup search functionality
        setupSearchFunctionality();
        
        // Set initial page
        showHomePage();
    }
    
    private void initializeDiscountRates() {
        // Set discount rates based on number of tickets
        discountRates.put(1, 0.0);   // 0% discount for 1 ticket
        discountRates.put(2, 0.05);  // 5% discount for 2 tickets
        discountRates.put(3, 0.08);  // 8% discount for 3 tickets
        discountRates.put(4, 0.10);  // 10% discount for 4 tickets
        discountRates.put(5, 0.12);  // 12% discount for 5 tickets
        discountRates.put(6, 0.15);  // 15% discount for 6 or more tickets
    }
    
    private double calculateDiscountRate(int numberOfTickets) {
        // Get discount rate based on number of tickets
        if (numberOfTickets >= 6) {
            return discountRates.get(6); // 15% for 6 or more tickets
        } else if (discountRates.containsKey(numberOfTickets)) {
            return discountRates.get(numberOfTickets);
        }
        return 0.0; // No discount
    }
    
    private void initializeAnimations() {
        // Scale animations for buttons
        scaleIn = new ScaleTransition(Duration.millis(150));
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        
        scaleOut = new ScaleTransition(Duration.millis(150));
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        
        // Hover effect for cards
        hoverEffect = new DropShadow();
        hoverEffect.setColor(Color.rgb(174, 45, 60, 0.7));
        hoverEffect.setRadius(15);
        hoverEffect.setSpread(0.3);
        
        // Selected effect for seats
        selectedEffect = new DropShadow();
        selectedEffect.setColor(Color.rgb(52, 152, 219, 0.8));
        selectedEffect.setRadius(10);
        selectedEffect.setSpread(0.5);
        
        // Glow effect for important elements
        glowEffect = new Glow(0.8);
    }
    
    private void setupNavProfileImageView() {
        System.out.println("🔄 Setting up nav profile image view...");
        
        // First, check if userIcon exists and hide it
        if (userIcon != null) {
            userIcon.setVisible(false);
            System.out.println("✅ Hidden FontAwesome user icon");
        }
        
        // Create the profile image view if it doesn't exist
        if (navProfileImageView == null) {
            navProfileImageView = new ImageView();
            navProfileImageView.setPreserveRatio(true);
            navProfileImageView.setFitWidth(100); // INCREASED SIZE: 60px
            navProfileImageView.setFitHeight(100); // INCREASED SIZE: 60px
            
            // Make the image view circular with a nice border
            Circle clip = new Circle(30, 30, 30);
            navProfileImageView.setClip(clip);
            
            // Add border and shadow
            navProfileImageView.setStyle("-fx-border-color: #ae2d3c; -fx-border-width: 3; " +
                                       "-fx-border-radius: 30; " +
                                       "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);");
            
            System.out.println("✅ Created new navProfileImageView with larger size");
        }
        
        // Try to position the image view in the navigation area
        if (navProfileImageView != null) {
            // Remove from parent if already added
            if (navProfileImageView.getParent() != null) {
                ((Pane) navProfileImageView.getParent()).getChildren().remove(navProfileImageView);
            }
            
            // Try to find a good place in the navigation
            // Option 1: If there's a navigation container (like HBox or VBox for nav buttons)
            if (homeBtn != null && homeBtn.getParent() != null) {
                Pane parent = (Pane) homeBtn.getParent();
                
                // Position at the top-center of the navigation area
                // Adjust these values based on your actual UI layout
                double centerX = parent.getWidth() / 2 - navProfileImageView.getFitWidth() / 2;
                
                navProfileImageView.setLayoutX(70);
                navProfileImageView.setLayoutY(60); // 10px from top of navigation
                
                parent.getChildren().add(navProfileImageView);
                System.out.println("✅ Added profile image to navigation container");
                
                // Make sure it's visible
                
                navProfileImageView.setVisible(true);
                navProfileImageView.toFront();
                
            } else {
                // Fallback: Try to add to the main container
                System.out.println("⚠️ Could not find navigation container, trying fallback...");
                
                // Load the profile image anyway
                updateNavProfileImage();
            }
        }
        
        // Load the profile image
        updateNavProfileImage();
    }
    
    private void updateNavProfileImage() {
        System.out.println("🔄 Updating nav profile image...");
        
        if (navProfileImageView == null) {
            System.out.println("❌ navProfileImageView is null! Creating new one.");
            setupNavProfileImageView();
            return;
        }
        
        // If no profile image path, show default icon
        if (profileImagePath == null || profileImagePath.isEmpty()) {
            System.out.println("ℹ️ No profile image path, showing default icon");
            showDefaultUserIcon();
            return;
        }
        
        // Try to load the image with the new larger size
        Image profileImage = loadProfileImageFromPath(profileImagePath, 60, 60); // Updated size
        
        if (profileImage != null) {
            navProfileImageView.setImage(profileImage);
            navProfileImageView.setVisible(true);
            
            // Ensure FontAwesome icon is hidden when we have a profile image
            if (userIcon != null) {
                userIcon.setVisible(false);
            }
            
            System.out.println("✅ Nav profile image loaded successfully with larger size");
        } else {
            // If image loading fails, show default icon
            System.out.println("⚠️ Failed to load profile image, showing default icon");
            showDefaultUserIcon();
        }
    }
    
    private void showDefaultUserIcon() {
        System.out.println("👤 Showing default user icon");
        
        // Hide the profile image view
        if (navProfileImageView != null) {
            navProfileImageView.setVisible(false);
        }
        
        // Show the FontAwesome user icon instead
        if (userIcon != null) {
            userIcon.setVisible(true);
            userIcon.setStyle("-fx-fill: #ffffff; -fx-font-size: 24px;"); // Increased font size
        }
        
        // Also ensure the profile icon container is visible
        if (profileIconContainer != null) {
            profileIconContainer.setVisible(true);
        }
    }
    // Add this helper method
    private Image loadProfileImageFromPath(String imagePath, int width, int height) {
        try {
            // Try to load from file
            File file = new File(imagePath);
            if (file.exists()) {
                System.out.println("📁 Loading from file: " + file.getAbsolutePath());
                return new Image(file.toURI().toString(), width, height, true, true);
            }
            
            // Try to load from resources
            String[] possiblePaths = {
                imagePath,
                "/" + imagePath,
                "images/" + imagePath,
                "/images/" + imagePath,
                "/application/images/" + imagePath,
                "/resources/images/default-profile.png" // Add default profile image
            };
            
            for (String path : possiblePaths) {
                try {
                    InputStream is = getClass().getResourceAsStream(path);
                    if (is != null) {
                        System.out.println("📦 Loading from resources: " + path);
                        return new Image(is, width, height, true, true);
                    }
                } catch (Exception e) {
                    // Continue to next path
                }
            }
            
            // Try to load from URL (if it's a URL)
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                System.out.println("🌐 Loading from URL: " + imagePath);
                return new Image(imagePath, width, height, true, true);
            }
            
            // Return a colored circle as fallback
            System.out.println("⚠️ Creating fallback profile image");
            return createFallbackProfileImage(width, height);
            
        } catch (Exception e) {
            System.out.println("❌ Error loading image: " + e.getMessage());
            return createFallbackProfileImage(width, height);
        }
    }
    
    private Image createFallbackProfileImage(int width, int height) {
        try {
            // Create a simple colored circle with user initials
            WritableImage image = new WritableImage(width, height);
            PixelWriter writer = image.getPixelWriter();
            
            // Background color based on user name
            String colorHash = userFullName != null ? 
                String.valueOf(Math.abs(userFullName.hashCode() % 360)) : "0";
            
            // Draw a colored circle
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double distance = Math.sqrt(Math.pow(x - width/2, 2) + Math.pow(y - height/2, 2));
                    if (distance <= width/2) {
                        // Calculate color based on username hash
                        double hue = Double.parseDouble(colorHash);
                        Color color = Color.hsb(hue, 0.7, 0.9);
                        writer.setColor(x, y, color);
                    } else {
                        writer.setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }
            
            return image;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void setDefaultNavProfileImage() {
        if (navProfileImageView != null) {
            // Create a default circular image with user initials
            WritableImage defaultImage = new WritableImage(60, 60);
            PixelWriter writer = defaultImage.getPixelWriter();
            
            // Draw a purple circle as default
            for (int y = 0; y < 60; y++) {
                for (int x = 0; x < 60; x++) {
                    double distance = Math.sqrt(Math.pow(x - 30, 2) + Math.pow(y - 30, 2));
                    if (distance <= 30) {
                        writer.setColor(x, y, Color.web("#ae2d3c")); // Use your theme color
                    } else {
                        writer.setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }
            
            navProfileImageView.setImage(defaultImage);
            navProfileImageView.setVisible(true);
        }
        
        // Hide the FontAwesome icon
        if (userIcon != null) {
            userIcon.setVisible(false);
        }
        
        if (profileIconContainer != null) {
            profileIconContainer.setVisible(true);
        }
    }
    private void setupDataRefreshTimer() {
        dataRefreshTimer = new Timeline(
            new KeyFrame(Duration.seconds(REFRESH_INTERVAL_SECONDS), e -> {
                Platform.runLater(() -> {
                    System.out.println("🔄 Auto-refresh check at: " + LocalDateTime.now());
                    refreshCurrentPage();
                });
            })
        );
        dataRefreshTimer.setCycleCount(Timeline.INDEFINITE);
        dataRefreshTimer.play();
    }
    
    private void refreshCurrentPage() {
        try {
            if (homePageForm != null && homePageForm.isVisible()) {
                loadNowShowingMovies();
                loadComingSoonMovies();
                loadPopularSnacks();
            } else if (moviesPageForm != null && moviesPageForm.isVisible()) {
                loadAllMovies();
            } else if (myBookingsForm != null && myBookingsForm.isVisible()) {
                loadUserBookings();
            } else if (snacksDrinksForm != null && snacksDrinksForm.isVisible()) {
                loadAllSnacks();
            } else if (profileForm != null && profileForm.isVisible()) {
                loadUserProfile();
            }
        } catch (Exception e) {
            System.err.println("❌ Error during auto-refresh: " + e.getMessage());
        }
    }
    
    private void setupButtonHoverEffects() {
        // Apply hover effects to all navigation buttons
        Button[] navButtons = {homeBtn, moviesBtn, snacksDrinksBtn, bookTicketsBtn, 
                              myBookingsBtn, aboutUsBtn, helpBtn, profileBtn, logoutBtn};
        
        for (Button btn : navButtons) {
            if (btn != null) {
                setupButtonHover(btn);
            }
        }
        
        // Apply to other important buttons
        Button[] otherButtons = {bookNowHeroBtn, viewAllMoviesBtn, viewAllSnacksBtn,
                                findShowtimesBtn, proceedToPaymentBtn, makePaymentBtn,
                                saveProfileBtn, checkoutBtn, closeCartBtn, clearCartBtn,
                                browseSnacksBtn, searchMovieBtn, searchSnackBtn,
                                allSnacksBtn, snacksOnlyBtn, drinksOnlyBtn, combosBtn,
                                cancelBookingUserBtn, printTicketBtn, viewBookingDetailsBtn,
                                filterBookingsUserBtn, requestOtpBtn, verifyOtpBtn,
                                resendOtpBtn, backToBookingBtn, tryAgainBtn, cancelPaymentBtn,
                                viewTicketBtn, doneBtn, changeProfilePicBtn, cancelProfileBtn,
                                deleteBookingUserBtn};
        
        for (Button btn : otherButtons) {
            if (btn != null) {
                setupButtonHover(btn);
            }
        }
    }
    
    private void setupButtonHover(Button button) {
        if (button == null) return;
        
        button.setCursor(Cursor.HAND);
        
        button.setOnMouseEntered(e -> {
            scaleIn.setNode(button);
            scaleIn.play();
            button.setEffect(hoverEffect);
        });
        
        button.setOnMouseExited(e -> {
            scaleOut.setNode(button);
            scaleOut.play();
            button.setEffect(null);
        });
    }
    
    private void setupCardHover(Node card) {
        if (card == null) return;
        
        card.setCursor(Cursor.HAND);
        
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            
            FadeTransition ft = new FadeTransition(Duration.millis(200), card);
            ft.setToValue(0.95);
            ft.play();
            
            card.setEffect(hoverEffect);
        });
        
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            
            FadeTransition ft = new FadeTransition(Duration.millis(200), card);
            ft.setToValue(1.0);
            ft.play();
            
            card.setEffect(null);
        });
    }
    
    private void setupPaymentOverlays() {
        // Initialize payment overlays to hidden
        if (paymentProcessingPane != null) paymentProcessingPane.setVisible(false);
        if (paymentSuccessPane != null) paymentSuccessPane.setVisible(false);
        if (paymentFailedPane != null) paymentFailedPane.setVisible(false);
        
        // Setup overlay button actions
        if (viewTicketBtn != null) {
            viewTicketBtn.setOnAction(e -> {
                paymentSuccessPane.setVisible(false);
            });
        }
        
        if (doneBtn != null) {
            doneBtn.setOnAction(e -> {
                paymentSuccessPane.setVisible(false);
                showHomePage();
            });
        }
        
        if (tryAgainBtn != null) {
            tryAgainBtn.setOnAction(e -> {
                paymentFailedPane.setVisible(false);
                makePaymentBtn.setDisable(false);
            });
        }
        
        if (cancelPaymentBtn != null) {
            cancelPaymentBtn.setOnAction(e -> {
                paymentFailedPane.setVisible(false);
                showBookTicketsPage();
            });
        }
    }
    
    private void showPaymentProcessing(String message) {
        if (paymentProcessingPane != null && paymentMessageLabel != null) {
            paymentProcessingPane.setVisible(true);
            paymentMessageLabel.setText(message);
            paymentProcessingPane.toFront();
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), paymentProcessingPane);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }
    
    private void hidePaymentProcessing() {
        if (paymentProcessingPane != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), paymentProcessingPane);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> paymentProcessingPane.setVisible(false));
            ft.play();
        }
    }
    
    private void showPaymentSuccess(int bookingId, double amount) {
        if (paymentSuccessPane != null && bookingIdLabel != null && paymentAmountLabel != null) {
            paymentSuccessPane.setVisible(true);
            bookingIdLabel.setText("Booking ID: #" + bookingId);
            paymentAmountLabel.setText("Amount: ETB " + String.format("%.2f", amount));
            paymentSuccessPane.toFront();
            
            FadeTransition ft = new FadeTransition(Duration.millis(500), paymentSuccessPane);
            ft.setFromValue(0);
            ft.setToValue(1);
            
            ScaleTransition st = new ScaleTransition(Duration.millis(500), paymentSuccessPane);
            st.setFromX(0.8);
            st.setFromY(0.8);
            st.setToX(1);
            st.setToY(1);
            
            ParallelTransition pt = new ParallelTransition(ft, st);
            pt.play();
        }
    }
    
    private void showPaymentFailure(String errorMessage) {
        if (paymentFailedPane != null && errorMessageLabel != null) {
            paymentFailedPane.setVisible(true);
            errorMessageLabel.setText(errorMessage);
            paymentFailedPane.toFront();
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), paymentFailedPane);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }
    
    // ========== USER INFO SETTER METHODS ==========
    
    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("✅ UserDashboardController.setUserId(" + userId + ") called");
        
        if (userId > 0) {
            loadUserProfile();
            loadUserBookings();
            System.out.println("✅ User data loaded for ID: " + userId);
            
            // Ensure nav profile image is set up
            if (navProfileImageView == null) {
                setupNavProfileImageView();
            } else {
                updateNavProfileImage();
            }
        } else {
            System.out.println("⚠️ WARNING: UserDashboardController received userId = 0");
        }
    }
    
    public void setUsername(String username) {
        this.username = username;
        System.out.println("✅ UserDashboardController.setUsername(" + username + ") called");
        
        if (userId == 0) {
            System.out.println("⚠️ User ID is 0, attempting to load user ID from database");
            try {
                Connection conn = DatabaseConnection.getConnection();
                String sql = "SELECT user_id, first_name, last_name FROM users WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    this.userId = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    this.userFullName = firstName + " " + lastName;
                    
                    if (usernameLabel != null) {
                        usernameLabel.setText(this.userFullName);
                    }
                    
                    System.out.println("✅ Retrieved user info: ID=" + userId + ", Name=" + userFullName);
                    
                    loadUserProfile();
                    loadUserBookings();
                } else {
                    System.out.println("⚠️ User not found in database: " + username);
                }
                
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("❌ Error loading user info: " + e.getMessage());
            }
        }
    }
    
    public void setUserName(String fullName) {
        this.userFullName = fullName;
        System.out.println("✅ UserDashboardController.setUserName(" + fullName + ") called");
        
        if (usernameLabel != null) {
            usernameLabel.setText(fullName);
        }
    }
    
    public void setUser(model.User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.userFullName = user.getFullName();
            System.out.println("✅ UserDashboardController.setUser() called - ID: " + userId + ", Name: " + userFullName);
            
            if (userId > 0) {
                loadUserProfile();
                loadUserBookings();
                System.out.println("✅ User data loaded from User object");
            }
            
            if (usernameLabel != null) {
                usernameLabel.setText(userFullName);
            }
        }
    }
    
    private void updateUsernameLabel() {
        if (usernameLabel != null && userFullName != null && !userFullName.isEmpty()) {
            usernameLabel.setText(userFullName);
        }
    }
    
    private void setupNavigation() {
        // Add hover effects to all navigation buttons
        setupButtonHover(homeBtn);
        setupButtonHover(moviesBtn);
        setupButtonHover(snacksDrinksBtn);
        setupButtonHover(bookTicketsBtn);
        setupButtonHover(myBookingsBtn);
        setupButtonHover(aboutUsBtn);
        setupButtonHover(helpBtn);
        setupButtonHover(profileBtn);
        setupButtonHover(logoutBtn);
        
        // Set button actions
        homeBtn.setOnAction(e -> showHomePage());
        moviesBtn.setOnAction(e -> showMoviesPage()); 
        snacksDrinksBtn.setOnAction(e -> showSnacksDrinksPage());
        bookTicketsBtn.setOnAction(e -> showBookTicketsPage());
        myBookingsBtn.setOnAction(e -> showMyBookingsPage());
        aboutUsBtn.setOnAction(e -> showAboutUsPage());
        helpBtn.setOnAction(e -> showHelpPage());
        profileBtn.setOnAction(e -> showProfilePage());
        
        // Add hover to action buttons
        if (bookNowHeroBtn != null) setupButtonHover(bookNowHeroBtn);
        if (viewAllMoviesBtn != null) setupButtonHover(viewAllMoviesBtn);
        if (viewAllSnacksBtn != null) setupButtonHover(viewAllSnacksBtn);
        if (findShowtimesBtn != null) setupButtonHover(findShowtimesBtn);
        if (proceedToPaymentBtn != null) setupButtonHover(proceedToPaymentBtn);
        if (viewCartFromBookingBtn != null) setupButtonHover(viewCartFromBookingBtn);
        if (backToBookingBtn != null) setupButtonHover(backToBookingBtn);
        if (makePaymentBtn != null) setupButtonHover(makePaymentBtn);
        if (requestOtpBtn != null) setupButtonHover(requestOtpBtn);
        if (verifyOtpBtn != null) setupButtonHover(verifyOtpBtn);
        if (resendOtpBtn != null) setupButtonHover(resendOtpBtn);
        if (saveProfileBtn != null) setupButtonHover(saveProfileBtn);
        if (cancelProfileBtn != null) setupButtonHover(cancelProfileBtn);
        if (changeProfilePicBtn != null) setupButtonHover(changeProfilePicBtn);
        if (cancelBookingUserBtn != null) setupButtonHover(cancelBookingUserBtn);
        if (printTicketBtn != null) setupButtonHover(printTicketBtn);
        if (viewBookingDetailsBtn != null) setupButtonHover(viewBookingDetailsBtn);
        if (filterBookingsUserBtn != null) setupButtonHover(filterBookingsUserBtn);
        if (searchMovieBtn != null) setupButtonHover(searchMovieBtn);
        if (searchSnackBtn != null) setupButtonHover(searchSnackBtn);
        if (allSnacksBtn != null) setupButtonHover(allSnacksBtn);
        if (snacksOnlyBtn != null) setupButtonHover(snacksOnlyBtn);
        if (drinksOnlyBtn != null) setupButtonHover(drinksOnlyBtn);
        if (combosBtn != null) setupButtonHover(combosBtn);
        if (cartBtn != null) setupButtonHover(cartBtn);
        if (closeCartBtn != null) setupButtonHover(closeCartBtn);
        if (clearCartBtn != null) setupButtonHover(clearCartBtn);
        if (checkoutBtn != null) setupButtonHover(checkoutBtn);
        if (browseSnacksBtn != null) setupButtonHover(browseSnacksBtn);
        if (deleteBookingUserBtn != null) setupButtonHover(deleteBookingUserBtn);
        
        // Set button actions
        if (bookNowHeroBtn != null) bookNowHeroBtn.setOnAction(e -> handleBookNowHero());
        if (viewAllMoviesBtn != null) viewAllMoviesBtn.setOnAction(e -> handleViewAllMovies());
        if (viewAllSnacksBtn != null) viewAllSnacksBtn.setOnAction(e -> handleViewAllSnacks());
        if (findShowtimesBtn != null) findShowtimesBtn.setOnAction(e -> handleFindShowtimes());
        if (proceedToPaymentBtn != null) proceedToPaymentBtn.setOnAction(e -> handleProceedToPayment());
        if (viewCartFromBookingBtn != null) viewCartFromBookingBtn.setOnAction(e -> showCartModal());
        if (backToBookingBtn != null) backToBookingBtn.setOnAction(e -> handleBackToBooking());
        if (makePaymentBtn != null) makePaymentBtn.setOnAction(e -> handleMakePayment());
        if (requestOtpBtn != null) requestOtpBtn.setOnAction(e -> handleRequestOtp());
        if (verifyOtpBtn != null) verifyOtpBtn.setOnAction(e -> handleVerifyOtp());
        if (resendOtpBtn != null) resendOtpBtn.setOnAction(e -> handleResendOtp());
        if (saveProfileBtn != null) saveProfileBtn.setOnAction(e -> handleSaveProfile());
        if (cancelProfileBtn != null) cancelProfileBtn.setOnAction(e -> handleCancelProfile());
        if (changeProfilePicBtn != null) changeProfilePicBtn.setOnAction(e -> handleChangeProfileImage());
        if (cancelBookingUserBtn != null) cancelBookingUserBtn.setOnAction(e -> handleCancelBooking());
        if (printTicketBtn != null) printTicketBtn.setOnAction(e -> handlePrintTicket());
        if (viewBookingDetailsBtn != null) viewBookingDetailsBtn.setOnAction(e -> handleViewBookingDetails());
        if (filterBookingsUserBtn != null) filterBookingsUserBtn.setOnAction(e -> handleFilterBookings());
        if (searchMovieBtn != null) searchMovieBtn.setOnAction(e -> handleSearchMovies());
        if (searchSnackBtn != null) searchSnackBtn.setOnAction(e -> handleSearchSnacks());
        if (allSnacksBtn != null) allSnacksBtn.setOnAction(e -> handleAllSnacksFilter());
        if (snacksOnlyBtn != null) snacksOnlyBtn.setOnAction(e -> handleSnacksOnlyFilter());
        if (drinksOnlyBtn != null) drinksOnlyBtn.setOnAction(e -> handleDrinksOnlyFilter());
        if (combosBtn != null) combosBtn.setOnAction(e -> handleCombosFilter());
        if (cartBtn != null) cartBtn.setOnAction(e -> showCartModal());
        if (closeCartBtn != null) closeCartBtn.setOnAction(e -> hideCartModal());
        if (clearCartBtn != null) clearCartBtn.setOnAction(e -> handleClearCart());
        if (checkoutBtn != null) checkoutBtn.setOnAction(e -> handleCheckoutFromCart());
        if (browseSnacksBtn != null) browseSnacksBtn.setOnAction(e -> handleBrowseSnacksFromCart());
        if (deleteBookingUserBtn != null) deleteBookingUserBtn.setOnAction(e -> handleDeleteBooking());
    }
    
    private void setupWindowControls() {
        if (closeBtn != null) {
            setupButtonHover(closeBtn);
            closeBtn.setOnAction(e -> closeBtn.getScene().getWindow().hide());
        }
        
        if (minimizeBtn != null) {
            setupButtonHover(minimizeBtn);
            minimizeBtn.setOnAction(e -> {
                Stage stage = (Stage) minimizeBtn.getScene().getWindow();
                stage.setIconified(true);
            });
        }
        
        if (maximizeBtn != null) {
            setupButtonHover(maximizeBtn);
            maximizeBtn.setOnAction(e -> {
                Stage stage = (Stage) maximizeBtn.getScene().getWindow();
                stage.setMaximized(!stage.isMaximized());
            });
        }
    }
    
    private void setupPaymentMethods() {
        if (telebirrRadio != null) telebirrRadio.setSelected(true);
        if (telebirrRadio != null) telebirrRadio.setOnAction(e -> handlePaymentMethodChange());
        if (cbeRadio != null) cbeRadio.setOnAction(e -> handlePaymentMethodChange());
        handlePaymentMethodChange();
    }
    
    private void setupCart() {
        if (cartItemsListView != null) {
            cartItemsListView.setCellFactory(param -> new CartItemCell());
            cartItemsListView.setItems(cartItems);
        }
        updateCartDisplay();
    }
    
    private void setupOtpTimer() {
        otpTimer = new Timeline();
        otpTimer.setCycleCount(Timeline.INDEFINITE);
    }
    
    private void setupLogoutButton() {
        if (logoutBtn != null) {
            logoutBtn.setOnAction(e -> {
                try {
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Confirm Logout");
                    confirmAlert.setHeaderText("Logout");
                    confirmAlert.setContentText("Are you sure you want to logout?");
                    
                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        if (dataRefreshTimer != null) {
                            dataRefreshTimer.stop();
                        }
                        
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
                        Parent root = loader.load();
                        
                        Stage stage = (Stage) logoutBtn.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                        stage.show();
                        
                        System.out.println("👋 User logged out successfully: " + username);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Logout Error", "Failed to logout: " + ex.getMessage());
                }
            });
        }
    }
    
    private void setupSearchFunctionality() {
        if (movieSearchField != null) {
            movieSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterMovies(newValue);
            });
        }
        
        if (snackSearchField != null) {
            snackSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterSnacks(newValue);
            });
        }
    }
    
    private void initializeComboBoxes() {
        if (movieCategoryFilter != null) {
            movieCategoryFilter.setItems(FXCollections.observableArrayList(
                "All", "Now Showing", "Coming Soon", "Featured"
            ));
            movieCategoryFilter.setValue("All");
        }
        
        if (movieGenreFilter != null) {
            movieGenreFilter.setItems(FXCollections.observableArrayList(
                "All", "Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance"
            ));
            movieGenreFilter.setValue("All");
        }
        
        if (bookingStatusFilterUser != null) {
            bookingStatusFilterUser.setItems(FXCollections.observableArrayList(
                "All", "Upcoming", "Completed", "Cancelled", "Confirmed"
            ));
            bookingStatusFilterUser.setValue("All");
        }
        
        // Initialize bookTimeSelect with empty list
        if (bookTimeSelect != null) {
            bookTimeSelect.setItems(FXCollections.observableArrayList());
            System.out.println("✅ bookTimeSelect ComboBox initialized");
        } else {
            System.out.println("❌ bookTimeSelect is null!");
        }
    }
    private void initializeTables() {
        if (myBookingIdColumn != null) myBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (myBookingMovieColumn != null) myBookingMovieColumn.setCellValueFactory(new PropertyValueFactory<>("movie"));
        if (myBookingDateColumn != null) myBookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        if (myBookingTimeColumn != null) myBookingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        if (myBookingSeatsColumn != null) myBookingSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
        if (myBookingAmountColumn != null) myBookingAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        if (myBookingStatusColumn != null) myBookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        if (myBookingActionColumn != null) {
            myBookingActionColumn.setCellFactory(new Callback<TableColumn<Booking, String>, TableCell<Booking, String>>() {
                @Override
                public TableCell<Booking, String> call(TableColumn<Booking, String> param) {
                    return new TableCell<Booking, String>() {
                        private final Button viewBtn = new Button("View");
                        private final Button cancelBtn = new Button("Cancel");
                        private final Button printBtn = new Button("Print");
                        private final HBox hbox = new HBox(5);
                        
                        {
                            viewBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 10px;");
                            cancelBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 10px;");
                            printBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 10px;");
                            
                            setupButtonHover(viewBtn);
                            setupButtonHover(cancelBtn);
                            setupButtonHover(printBtn);
                            
                            viewBtn.setOnAction(e -> {
                                Booking booking = getTableView().getItems().get(getIndex());
                                viewBookingDetails(booking);
                            });
                            
                            cancelBtn.setOnAction(e -> {
                                Booking booking = getTableView().getItems().get(getIndex());
                                cancelBooking(booking);
                            });
                            
                            printBtn.setOnAction(e -> {
                                Booking booking = getTableView().getItems().get(getIndex());
                                printExistingTicket(booking);
                            });
                            
                            hbox.getChildren().addAll(viewBtn, printBtn);
                        }
                        
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                Booking booking = getTableView().getItems().get(getIndex());
                                hbox.getChildren().clear();
                                hbox.getChildren().addAll(viewBtn, printBtn);
                                
                                String status = booking.getStatus();
                                if (("Upcoming".equals(status) || "Confirmed".equals(status)) && 
                                    !isBookingPastShowtime(booking)) {
                                    hbox.getChildren().add(cancelBtn);
                                }
                                setGraphic(hbox);
                            }
                        }
                    };
                }
            });
        }
        
        loadUserBookings();
    }
    
    private boolean isBookingPastShowtime(Booking booking) {
        try {
            String dateStr = booking.getDate();
            String timeStr = booking.getTime();
            
            if (dateStr == null || timeStr == null) {
                return false;
            }
            
            LocalDate showDate = LocalDate.parse(dateStr);
            LocalDateTime showDateTime = LocalDateTime.of(showDate, 
                java.time.LocalTime.parse(timeStr.length() == 5 ? timeStr + ":00" : timeStr));
            
            return LocalDateTime.now().isAfter(showDateTime);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== DATABASE LOADING METHODS ====================
    
    private void loadHomePageData() {
        loadNowShowingMovies();
        loadComingSoonMovies();
        loadPopularSnacks();
    }
    
    private void loadNowShowingMovies() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, genre, duration_minutes, rating, poster_image, price, description " +
                        "FROM movies WHERE category = 'Now Showing' AND is_active = TRUE " +
                        "ORDER BY movie_id DESC LIMIT 5";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (nowShowingContainer != null) nowShowingContainer.getChildren().clear();
            nowShowingMovies.clear();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setRating(rs.getString("rating"));
                movie.setPosterImage(rs.getString("poster_image"));
                movie.setPrice(rs.getDouble("price"));
                movie.setDescription(rs.getString("description"));
                
                nowShowingMovies.add(movie);
                VBox movieCard = createMovieCard(movie);
                if (nowShowingContainer != null) nowShowingContainer.getChildren().add(movieCard);
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading now showing movies: " + e.getMessage());
        }
    }
    
    private void loadComingSoonMovies() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, genre, duration_minutes, rating, poster_image, release_date, description " +
                        "FROM movies WHERE category = 'Coming Soon' AND is_active = TRUE " +
                        "ORDER BY release_date ASC LIMIT 5";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (comingSoonContainer != null) comingSoonContainer.getChildren().clear();
            comingSoonMovies.clear();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setRating(rs.getString("rating"));
                movie.setPosterImage(rs.getString("poster_image"));
                movie.setDescription(rs.getString("description"));
                java.sql.Date releaseDate = rs.getDate("release_date");
                if (releaseDate != null) {
                    movie.setReleaseDate(releaseDate.toLocalDate());
                }
                
                comingSoonMovies.add(movie);
                VBox movieCard = createMovieCard(movie);
                if (comingSoonContainer != null) comingSoonContainer.getChildren().add(movieCard);
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading coming soon movies: " + e.getMessage());
        }
    }
    
    private void loadPopularSnacks() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                        "s.price, s.size, s.image_url " +
                        "FROM snack_items s " +
                        "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                        "WHERE s.is_available = TRUE " +
                        "ORDER BY s.snack_id DESC LIMIT 5";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (popularSnacksContainer != null) popularSnacksContainer.getChildren().clear();
            allSnacks.clear();
            
            while (rs.next()) {
                Snack snack = new Snack();
                snack.setSnackId(rs.getInt("snack_id"));
                snack.setItemName(rs.getString("item_name"));
                snack.setDescription(rs.getString("description"));
                snack.setCategory(rs.getString("category_name"));
                snack.setPrice(rs.getDouble("price"));
                snack.setSize(rs.getString("size"));
                snack.setImageUrl(rs.getString("image_url"));
                
                allSnacks.add(snack);
                VBox snackCard = createSnackCard(snack, false);
                if (popularSnacksContainer != null) popularSnacksContainer.getChildren().add(snackCard);
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading popular snacks: " + e.getMessage());
        }
    }
    
    private void loadAllMovies() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, genre, duration_minutes, rating, poster_image, price, category, description " +
                        "FROM movies WHERE is_active = TRUE ORDER BY title";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (allMoviesTilePane != null) allMoviesTilePane.getChildren().clear();
            allMoviesList.clear();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setRating(rs.getString("rating"));
                movie.setPosterImage(rs.getString("poster_image"));
                movie.setPrice(rs.getDouble("price"));
                movie.setCategory(rs.getString("category"));
                movie.setDescription(rs.getString("description"));
                
                allMoviesList.add(movie);
                VBox movieCard = createMovieCardForTile(movie);
                if (allMoviesTilePane != null) allMoviesTilePane.getChildren().add(movieCard);
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading all movies: " + e.getMessage());
        }
    }
    
    private void loadAllSnacks() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                        "s.price, s.size, s.image_url, s.calories " +
                        "FROM snack_items s " +
                        "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                        "WHERE s.is_available = TRUE ORDER BY s.item_name";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().clear();
            
            while (rs.next()) {
                Snack snack = new Snack();
                snack.setSnackId(rs.getInt("snack_id"));
                snack.setItemName(rs.getString("item_name"));
                snack.setDescription(rs.getString("description"));
                snack.setCategory(rs.getString("category_name"));
                snack.setPrice(rs.getDouble("price"));
                snack.setSize(rs.getString("size"));
                snack.setImageUrl(rs.getString("image_url"));
                snack.setCalories(rs.getInt("calories"));
                
                VBox snackCard = createSnackCardForTile(snack);
                if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().add(snackCard);
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading all snacks: " + e.getMessage());
        }
    }
    
    private void loadMoviesForBooking() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, price FROM movies WHERE is_active = TRUE AND category = 'Now Showing' ORDER BY title";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<String> movieTitles = FXCollections.observableArrayList();
            Map<String, Double> moviePrices = new HashMap<>();
            Map<String, Integer> movieIds = new HashMap<>();
            
            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String title = rs.getString("title");
                double price = rs.getDouble("price");
                
                movieTitles.add(title);
                moviePrices.put(title, price);
                movieIds.put(title, movieId);
            }
            
            if (bookMovieSelect != null) {
                bookMovieSelect.setItems(movieTitles);
                
                // Add listener to load showtimes when movie is selected
                bookMovieSelect.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && moviePrices.containsKey(newVal)) {
                        movieBasePrice = moviePrices.get(newVal);
                        int selectedMovieId = movieIds.get(newVal);
                        for (Movie m : allMoviesList) {
                            if (m.getMovieId() == selectedMovieId) {
                                selectedMovie = m;
                                break;
                            }
                        }
                        // Load showtimes for the selected movie
                        loadShowtimesForMovie(selectedMovieId);
                        updateBookingSummary();
                    }
                });
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading movies for booking: " + e.getMessage());
        }
    }
    // NEW METHOD: Load showtimes for selected movie
    private void loadShowtimesForMovie(int movieId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT DISTINCT s.show_time FROM showtimes s " +
                        "WHERE s.movie_id = ? AND s.is_active = TRUE " +
                        "AND s.show_date >= CURDATE() " +
                        "ORDER BY s.show_time";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<String> showtimes = FXCollections.observableArrayList();
            
            while (rs.next()) {
                String showTime = rs.getTime("show_time").toString();
                // Format time to HH:mm
                if (showTime.length() >= 5) {
                    showTime = showTime.substring(0, 5);
                    showtimes.add(showTime);
                }
            }
            
            if (bookTimeSelect != null) {
                bookTimeSelect.setItems(showtimes);
                // Clear the selection
                bookTimeSelect.getSelectionModel().clearSelection();
                bookTimeSelect.setValue(null);
                
                if (!showtimes.isEmpty()) {
                    System.out.println("✅ Loaded " + showtimes.size() + " showtimes for movie ID: " + movieId);
                } else {
                    System.out.println("⚠️ No showtimes available for movie ID: " + movieId);
                    // Show a message if no showtimes are available
                    bookTimeSelect.setPromptText("No showtimes available");
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading showtimes for movie: " + e.getMessage());
        }
    }
    
    private void loadAboutUsContent() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cinema_name, description, address, phone_numbers, emails, working_hours, banner_image " +
                        "FROM cinema_info LIMIT 1";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String cinemaName = rs.getString("cinema_name");
                String description = rs.getString("description");
                String address = rs.getString("address");
                String phoneJson = rs.getString("phone_numbers");
                String emailJson = rs.getString("emails");
                String hours = rs.getString("working_hours");
                String bannerImage = rs.getString("banner_image");
                
                if (cinemaNameLabel != null) cinemaNameLabel.setText(cinemaName);
                if (cinemaDescriptionLabel != null) cinemaDescriptionLabel.setText(description);
                if (cinemaAddressLabel != null) cinemaAddressLabel.setText(address);
                if (cinemaHoursLabel != null) cinemaHoursLabel.setText(hours);
                
                if (phoneJson != null && phoneJson.startsWith("[") && cinemaPhoneLabel != null) {
                    phoneJson = phoneJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!phoneJson.isEmpty()) {
                        String[] phones = phoneJson.split(",");
                        cinemaPhoneLabel.setText("Phone: " + (phones.length > 0 ? phones[0].trim() : ""));
                    }
                }
                
                if (emailJson != null && emailJson.startsWith("[") && cinemaEmailLabel != null) {
                    emailJson = emailJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!emailJson.isEmpty()) {
                        String[] emails = emailJson.split(",");
                        cinemaEmailLabel.setText("Email: " + (emails.length > 0 ? emails[0].trim() : ""));
                    }
                }
                
                if (bannerImage != null && !bannerImage.isEmpty() && cinemaBannerImage != null) {
                    try {
                        File file = new File(bannerImage);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            cinemaBannerImage.setImage(image);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading banner image: " + e.getMessage());
                    }
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading about us content: " + e.getMessage());
        }
    }
    
    private void loadHelpContent() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String faqSql = "SELECT question, answer FROM faqs WHERE is_active = TRUE ORDER BY display_order";
            PreparedStatement faqStmt = conn.prepareStatement(faqSql);
            ResultSet faqRs = faqStmt.executeQuery();
            
            if (faqContainer != null) faqContainer.getChildren().clear();
            
            while (faqRs.next()) {
                String question = faqRs.getString("question");
                String answer = faqRs.getString("answer");
                
                VBox faqItem = new VBox(5);
                faqItem.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 15;");
                
                Label questionLabel = new Label(question);
                questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
                questionLabel.setWrapText(true);
                
                Label answerLabel = new Label(answer);
                answerLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
                answerLabel.setWrapText(true);
                
                faqItem.getChildren().addAll(questionLabel, answerLabel);
                if (faqContainer != null) faqContainer.getChildren().add(faqItem);
            }
            
            faqRs.close();
            faqStmt.close();
            
            String contactSql = "SELECT phone_numbers, emails, working_hours FROM cinema_info LIMIT 1";
            PreparedStatement contactStmt = conn.prepareStatement(contactSql);
            ResultSet contactRs = contactStmt.executeQuery();
            
            if (contactRs.next()) {
                String phoneJson = contactRs.getString("phone_numbers");
                String emailJson = contactRs.getString("emails");
                String hours = contactRs.getString("working_hours");
                
                if (helpPhoneLabel != null && phoneJson != null && phoneJson.startsWith("[")) {
                    phoneJson = phoneJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!phoneJson.isEmpty()) {
                        String[] phones = phoneJson.split(",");
                        helpPhoneLabel.setText(phones.length > 0 ? phones[0].trim() : "");
                    }
                }
                
                if (helpEmailLabel != null && emailJson != null && emailJson.startsWith("[")) {
                    emailJson = emailJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!emailJson.isEmpty()) {
                        String[] emails = emailJson.split(",");
                        helpEmailLabel.setText(emails.length > 0 ? emails[0].trim() : "");
                    }
                }
                
                if (helpHoursLabel != null) {
                    helpHoursLabel.setText(hours);
                }
                
                if (helpInstructionsLabel != null) {
                    helpInstructionsLabel.setText("For assistance, please call us during business hours or send an email.");
                }
            }
            
            contactRs.close();
            contactStmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading help content: " + e.getMessage());
        }
    }
    
    private void loadUserBookings() {
        try {
            if (userId == 0) {
                System.out.println("⚠️ Cannot load bookings: userId is 0");
                return;
            }
        
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT b.booking_id, m.title, s.show_date, s.show_time, " +
                        "b.total_amount, b.booking_status, b.booking_date, " +
                        "GROUP_CONCAT(seats.seat_label ORDER BY seats.seat_label SEPARATOR ', ') as seat_labels " +
                        "FROM bookings b " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "LEFT JOIN booking_seats bs ON b.booking_id = bs.booking_id " +
                        "LEFT JOIN seats ON bs.seat_id = seats.seat_id " +
                        "WHERE b.user_id = ? " +
                        "GROUP BY b.booking_id " +
                        "ORDER BY b.booking_date DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Booking> userBookings = FXCollections.observableArrayList();
            
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String bookingIdStr = "#" + bookingId;
                String movie = rs.getString("title");
                String date = rs.getDate("show_date").toString();
                String time = rs.getTime("show_time").toString().substring(0, 5);
                String seats = rs.getString("seat_labels");
                if (seats == null) seats = "Not assigned";
                String amount = String.format("ETB %.2f", rs.getDouble("total_amount"));
                String status = rs.getString("booking_status");
                
                Booking booking = new Booking(bookingIdStr, movie, date, time, seats, amount, status);
                booking.setBookingIdDb(bookingId);
                userBookings.add(booking);
            }
            
            if (myBookingsTable != null) myBookingsTable.setItems(userBookings);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading user bookings: " + e.getMessage());
        }
    }
    
    private void loadUserProfile() {
        if (userId == 0) {
            System.out.println("⚠️ Cannot load profile: userId is 0");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT first_name, last_name, email, phone_number, profile_picture FROM users WHERE user_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                String profileImage = rs.getString("profile_picture");
                
                String fullName = firstName + " " + lastName;
                this.userFullName = fullName;
                
                if (profileNameField != null) profileNameField.setText(fullName);
                if (profileEmailField != null) profileEmailField.setText(email);
                if (profilePhoneField != null) profilePhoneField.setText(phone);
                
                if (profileImage != null && !profileImage.isEmpty()) {
                    this.profileImagePath = profileImage;
                    System.out.println("📸 Profile image path loaded from DB: " + profileImagePath);
                } else {
                    this.profileImagePath = "";
                    System.out.println("ℹ️ No profile image in database");
                }
                
                updateUsernameLabel();
                
                // Load profile image in profile page
                loadProfileImage(profileImage);
                
                // IMPORTANT: Update nav profile image
                updateNavProfileImage();
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading user profile: " + e.getMessage());
        }
    }
    
    private void loadProfileImage(String profileImagePath) {
        if (profileImageView == null) return;
        
        try {
            if (profileImagePath != null && !profileImagePath.isEmpty()) {
                File file = new File(profileImagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString(), 120, 120, true, true);
                    profileImageView.setImage(image);
                    System.out.println("✅ Profile image loaded from file: " + profileImagePath);
                } else {
                    // Try to load from resources
                    try {
                        // Try different resource paths
                        String[] possiblePaths = {
                            profileImagePath,
                            "/" + profileImagePath,
                            "images/" + profileImagePath,
                            "/images/" + profileImagePath
                        };
                        
                        Image image = null;
                        for (String path : possiblePaths) {
                            try {
                                java.io.InputStream is = getClass().getResourceAsStream(path);
                                if (is != null) {
                                    System.out.println("✅ Found resource at: " + path);
                                    image = new Image(is, 120, 120, true, true);
                                    break;
                                }
                            } catch (Exception e) {
                                // Try next path
                            }
                        }
                        
                        if (image != null) {
                            profileImageView.setImage(image);
                            System.out.println("✅ Profile image loaded from resources");
                        } else {
                            setDefaultProfileImage();
                            System.out.println("⚠️ Could not load profile image from resources, using default");
                        }
                    } catch (Exception e2) {
                        setDefaultProfileImage();
                        System.out.println("⚠️ Could not load profile image from resources, using default");
                    }
                }
            } else {
                setDefaultProfileImage();
                System.out.println("ℹ️ No profile image path, using default");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setDefaultProfileImage();
            System.out.println("❌ Error loading profile image, using default: " + e.getMessage());
        }
    }
    
    private void setDefaultProfileImage() {
        if (profileImageView != null) {
            try {
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-profile.png"), 120, 120, true, true);
                profileImageView.setImage(defaultImage);
            } catch (Exception e) {
                profileImageView.setImage(null);
                System.out.println("⚠️ Default profile image not found in resources");
            }
        }
    }
    
    // ==================== PAYMENT OTP METHODS ====================
    
    private void handlePaymentMethodChange() {
        resetOtpSection();
        
        if (telebirrRadio != null && telebirrRadio.isSelected()) {
            System.out.println("Telebirr payment selected");
        } else if (cbeRadio != null && cbeRadio.isSelected()) {
            System.out.println("CBE Birr payment selected");
        }
    }
    
    @FXML
    private void handleRequestOtp() {
        if (paymentPhoneField == null) return;
        
        String phone = paymentPhoneField.getText().trim();
        
        if (phone.isEmpty()) {
            showAlert("Phone Required", "Please enter your phone number");
            return;
        }
        
        if (!phone.matches("^09[0-9]{8}$")) {
            showAlert("Invalid Phone", "Please enter a valid Ethiopian phone number (09xxxxxxxx)");
            return;
        }
        
        generatedOtp = String.format("%06d", new Random().nextInt(999999));
        System.out.println("Generated OTP for " + phone + ": " + generatedOtp);
        
        if (otpSection != null) otpSection.setVisible(true);
        if (requestOtpBtn != null) requestOtpBtn.setDisable(true);
        startOtpTimer();
        
        showSuccessAlert("OTP Sent", "A 6-digit OTP has been sent to " + phone + 
                        "\nFor testing, use OTP: " + generatedOtp);
    }
    
    @FXML
    private void handleVerifyOtp() {
        if (otpField == null) {
            System.out.println("❌ otpField is null!");
            return;
        }
        
        String enteredOtp = otpField.getText().trim();
        
        if (enteredOtp.isEmpty()) {
            showAlert("OTP Required", "Please enter the OTP");
            return;
        }
        
        System.out.println("Entered OTP: " + enteredOtp);
        System.out.println("Generated OTP: " + generatedOtp);
        
        if (enteredOtp.equals(generatedOtp)) {
            if (makePaymentBtn != null) {
                makePaymentBtn.setDisable(false);
                System.out.println("✅ OTP verified, makePaymentBtn enabled");
            }
            if (verifyOtpBtn != null) verifyOtpBtn.setDisable(true);
            showSuccessAlert("OTP Verified", "Phone number verified successfully!");
        } else {
            showAlert("Invalid OTP", "The OTP you entered is incorrect. Please try again.");
        }
    }
    
    @FXML
    private void handleResendOtp() {
        stopOtpTimer();
        generatedOtp = String.format("%06d", new Random().nextInt(999999));
        System.out.println("Resent OTP: " + generatedOtp);
        
        otpSecondsRemaining = 120;
        startOtpTimer();
        
        showSuccessAlert("OTP Resent", "A new OTP has been sent to your phone" + 
                        "\nFor testing, use OTP: " + generatedOtp);
    }
    
    private void startOtpTimer() {
        stopOtpTimer();
        
        otpTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                otpSecondsRemaining--;
                updateOtpTimerDisplay();
                
                if (otpSecondsRemaining <= 0) {
                    stopOtpTimer();
                    if (otpSection != null) otpSection.setVisible(false);
                    if (requestOtpBtn != null) requestOtpBtn.setDisable(false);
                    showAlert("OTP Expired", "The OTP has expired. Please request a new one.");
                }
            })
        );
        otpTimer.setCycleCount(Timeline.INDEFINITE);
        otpTimer.play();
        
        updateOtpTimerDisplay();
    }
    
    private void stopOtpTimer() {
        if (otpTimer != null) {
            otpTimer.stop();
        }
    }
    
    private void updateOtpTimerDisplay() {
        int minutes = otpSecondsRemaining / 60;
        int seconds = otpSecondsRemaining % 60;
        if (otpTimerLabel != null) {
            otpTimerLabel.setText(String.format("OTP valid for: %02d:%02d", minutes, seconds));
        }
    }
    
    private void resetOtpSection() {
        stopOtpTimer();
        if (otpSection != null) otpSection.setVisible(false);
        if (otpField != null) otpField.clear();
        if (requestOtpBtn != null) requestOtpBtn.setDisable(false);
        if (verifyOtpBtn != null) verifyOtpBtn.setDisable(false);
        if (makePaymentBtn != null) makePaymentBtn.setDisable(true);
        otpSecondsRemaining = 120;
        if (otpTimerLabel != null) otpTimerLabel.setText("OTP valid for: 2:00");
    }
    
    // ==================== PAYMENT PROCESSING ====================
    
    @FXML
    private void handleMakePayment() {
        System.out.println("🔍 DEBUG: handleMakePayment called");
        System.out.println("🔍 DEBUG: userId = " + userId);
        System.out.println("🔍 DEBUG: username = " + username);
        System.out.println("🔍 DEBUG: userFullName = " + userFullName);
        
        if (userId == 0) {
            System.out.println("⚠️ User ID is 0 - attempting to reload user info");
            if (username != null && !username.isEmpty()) {
                setUsername(username);
            }
            
            if (userId == 0) {
                showAlert("Authentication Error", 
                    "User not properly authenticated.\n" +
                    "User ID: " + userId + "\n" +
                    "Username: " + username + "\n" +
                    "Please logout and login again.");
                return;
            }
        }
        
        if (selectedSeats.isEmpty()) {
            showAlert("Selection Required", "Please select at least one seat");
            return;
        }
        
        if (selectedShowtimeId == 0) {
            showAlert("Selection Required", "Please select a showtime");
            return;
        }
        
        if (termsCheckBox != null && !termsCheckBox.isSelected()) {
            showAlert("Terms Required", "Please accept the terms and conditions");
            return;
        }
        
        if (telebirrRadio != null && telebirrRadio.isSelected()) {
            if (paymentPhoneField != null && paymentPhoneField.getText().trim().isEmpty()) {
                showAlert("Payment Details Required", "Please enter phone number for Telebirr payment");
                return;
            }
            
            if (otpSection != null && otpSection.isVisible() && 
                (otpField != null && otpField.getText().trim().isEmpty() || 
                 makePaymentBtn != null && makePaymentBtn.isDisable())) {
                showAlert("OTP Required", "Please verify OTP first");
                return;
            }
        } else if (cbeRadio != null && cbeRadio.isSelected()) {
            if (makePaymentBtn != null) makePaymentBtn.setDisable(false);
        }
        
        showPaymentProcessing("Processing your payment...");
        
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    processBooking();
                });
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    hidePaymentProcessing();
                    showPaymentFailure("Payment processing failed: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void processBooking() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try {
                // Calculate total price with proper calculation
                int numberOfTickets = selectedSeats.size();
                double discountRate = calculateDiscountRate(numberOfTickets);
                
                // Calculate total seat price including VIP premiums
                double totalSeatPrice = 0.0;
                for (SeatSelection seatSelection : seatSelections.values()) {
                    totalSeatPrice += seatSelection.seatPrice;
                }
                
                // Apply discount to seat price
                double discountAmount = totalSeatPrice * discountRate;
                double discountedSeatPrice = totalSeatPrice - discountAmount;
                
                double snacksTotal = cartTotal;
                double subtotal = discountedSeatPrice + snacksTotal;
                double serviceCharge = subtotal * 0.05;
                double totalAmount = subtotal + serviceCharge;
                
                System.out.println("💰 Processing booking:");
                System.out.println("   Movie Base Price: " + movieBasePrice);
                System.out.println("   Number of tickets: " + numberOfTickets);
                System.out.println("   VIP Seat Premium: " + seatPremiumPrice);
                System.out.println("   Discount Rate: " + (discountRate * 100) + "%");
                System.out.println("   Discount Amount: " + discountAmount);
                System.out.println("   Total Seat Price (after discount): " + discountedSeatPrice);
                System.out.println("   Snacks Total: " + snacksTotal);
                System.out.println("   Subtotal: " + subtotal);
                System.out.println("   Service Charge (5%): " + serviceCharge);
                System.out.println("   Total Amount: " + totalAmount);
                
                String paymentMethod = (telebirrRadio != null && telebirrRadio.isSelected()) ? "telebirr" : "cbe_birr";
                String paymentStatus = "paid";
                
                String bookingSql = "INSERT INTO bookings (user_id, showtime_id, total_amount, booking_status, " +
                                  "payment_method, payment_status, booking_date) " +
                                  "VALUES (?, ?, ?, 'confirmed', ?, ?, NOW())";
                
                PreparedStatement bookingStmt = conn.prepareStatement(bookingSql, PreparedStatement.RETURN_GENERATED_KEYS);
                bookingStmt.setInt(1, userId);
                bookingStmt.setInt(2, selectedShowtimeId);
                bookingStmt.setDouble(3, totalAmount);
                bookingStmt.setString(4, paymentMethod);
                bookingStmt.setString(5, paymentStatus);
                
                int rowsAffected = bookingStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    ResultSet rs = bookingStmt.getGeneratedKeys();
                    int bookingId = 0;
                    if (rs.next()) {
                        bookingId = rs.getInt(1);
                    }
                    rs.close();
                    
                    System.out.println("✅ Booking created with ID: " + bookingId);
                    
                    String insertSeatSql = "INSERT INTO booking_seats (booking_id, seat_id, seat_price) " +
                                          "VALUES (?, ?, ?)";
                    PreparedStatement seatStmt = conn.prepareStatement(insertSeatSql);
                    
                    String getSeatIdSql = "SELECT seat_id, seat_type FROM seats WHERE seat_label = ? AND hall_id = ?";
                    PreparedStatement getSeatStmt = conn.prepareStatement(getSeatIdSql);
                    
                    for (SeatSelection seatSelection : seatSelections.values()) {
                        String seatLabel = seatSelection.seatLabel;
                        getSeatStmt.setString(1, seatLabel);
                        getSeatStmt.setInt(2, selectedHallId);
                        ResultSet seatRs = getSeatStmt.executeQuery();
                        
                        if (seatRs.next()) {
                            int seatId = seatRs.getInt("seat_id");
                            String seatType = seatRs.getString("seat_type");
                            double finalSeatPrice = seatSelection.seatPrice;
                            
                            seatStmt.setInt(1, bookingId);
                            seatStmt.setInt(2, seatId);
                            seatStmt.setDouble(3, finalSeatPrice);
                            seatStmt.addBatch();
                            System.out.println("✅ Adding seat: " + seatLabel + " (seat_id: " + seatId + ", type: " + seatType + ", price: " + finalSeatPrice + ")");
                        } else {
                            System.out.println("❌ Seat not found: " + seatLabel + " in hall " + selectedHallId);
                        }
                        seatRs.close();
                    }
                    
                    seatStmt.executeBatch();
                    seatStmt.close();
                    getSeatStmt.close();
                    
                    System.out.println("✅ Seats added: " + selectedSeats.size());
                    
                    if (!cartItems.isEmpty()) {
                        String snackSql = "INSERT INTO booking_snacks (booking_id, snack_id, quantity, item_price, total_price) " +
                                        "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement snackStmt = conn.prepareStatement(snackSql);
                        
                        for (CartItem cartItem : cartItems) {
                            snackStmt.setInt(1, bookingId);
                            snackStmt.setInt(2, cartItem.getSnack().getSnackId());
                            snackStmt.setInt(3, cartItem.getQuantity());
                            snackStmt.setDouble(4, cartItem.getSnack().getPrice());
                            snackStmt.setDouble(5, cartItem.getTotalPrice());
                            snackStmt.addBatch();
                        }
                        snackStmt.executeBatch();
                        snackStmt.close();
                        
                        System.out.println("✅ Snacks added: " + cartItems.size());
                    }
                    
                    String updateSql = "UPDATE showtimes SET available_seats = available_seats - ? WHERE showtime_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, selectedSeats.size());
                    updateStmt.setInt(2, selectedShowtimeId);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                    
                    conn.commit();
                    
                    System.out.println("✅ Transaction committed successfully");
                    
                    hidePaymentProcessing();
                    showPaymentSuccess(bookingId, totalAmount);
                    
                    generateAndShowTicket(bookingId, totalAmount);
                    resetBookingForm();
                    resetPaymentForm();
                    cartItems.clear();
                    updateCartDisplay();
                    
                    loadUserBookings();
                    
                } else {
                    conn.rollback();
                    hidePaymentProcessing();
                    showPaymentFailure("Failed to create booking");
                }
                
                bookingStmt.close();
                
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            hidePaymentProcessing();
            showPaymentFailure("Failed to process booking: " + e.getMessage());
        }
    }
    
    private double calculateSeatTotalPrice() {
        int numberOfTickets = selectedSeats.size();
        double discountRate = calculateDiscountRate(numberOfTickets);
        
        double totalSeatPrice = 0.0;
        for (SeatSelection seatSelection : seatSelections.values()) {
            totalSeatPrice += seatSelection.seatPrice;
        }
        
        // Apply discount
        double discountAmount = totalSeatPrice * discountRate;
        return totalSeatPrice - discountAmount;
    }
    
    private void generateAndShowTicket(int bookingId, double totalAmount) {
        Dialog<Void> ticketDialog = new Dialog<>();
        ticketDialog.setTitle("Movie Ticket");
        ticketDialog.setHeaderText("Booking Confirmation - Ticket #" + bookingId);
        
        ButtonType printButton = new ButtonType("Print", ButtonBar.ButtonData.OK_DONE);
        ButtonType savePDFButton = new ButtonType("Save as PDF", ButtonBar.ButtonData.OTHER);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        ticketDialog.getDialogPane().getButtonTypes().addAll(printButton, savePDFButton, closeButton);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white;");
        
        Label header = new Label("🎟️ ELIANA CINEMA TICKET 🎟️");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ae2d3c;");
        header.setAlignment(Pos.CENTER);
        
        VBox details = new VBox(10);
        details.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-border-color: #dee2e6; -fx-border-width: 1;");
        
        addTicketDetail(details, "Booking ID:", "#" + bookingId);
        if (bookMovieSelect != null) addTicketDetail(details, "Movie:", bookMovieSelect.getValue());
        if (bookDateSelect != null) addTicketDetail(details, "Date & Time:", bookDateSelect.getValue() + " " + selectedShowtime);
        addTicketDetail(details, "Hall:", selectedHall);
        
        // Add seat details with prices
        StringBuilder seatDetails = new StringBuilder();
        int numberOfTickets = selectedSeats.size();
        double discountRate = calculateDiscountRate(numberOfTickets);
        
        for (SeatSelection seatSelection : seatSelections.values()) {
            seatDetails.append(seatSelection.seatLabel)
                      .append(" (")
                      .append(seatSelection.seatType)
                      .append(" - ETB ")
                      .append(String.format("%.2f", seatSelection.seatPrice))
                      .append("), ");
        }
        if (seatDetails.length() > 0) {
            seatDetails.setLength(seatDetails.length() - 2);
        }
        addTicketDetail(details, "Seats:", seatDetails.toString());
        
        // Add discount information if applicable
        if (discountRate > 0) {
            addTicketDetail(details, "Discount:", String.format("%.0f%% off for %d tickets", discountRate * 100, numberOfTickets));
        }
        
        if (!cartItems.isEmpty()) {
            StringBuilder snacksText = new StringBuilder();
            for (CartItem item : cartItems) {
                snacksText.append(item.getSnack().getItemName())
                         .append(" (")
                         .append(item.getSnack().getSize())
                         .append(", x")
                         .append(item.getQuantity())
                         .append(" - ETB ")
                         .append(String.format("%.2f", item.getTotalPrice()))
                         .append("), ");
            }
            snacksText.setLength(snacksText.length() - 2);
            addTicketDetail(details, "Snacks:", snacksText.toString());
        }
        
        addTicketDetail(details, "Total Amount:", String.format("ETB %.2f", totalAmount));
        addTicketDetail(details, "Payment Method:", 
                       (telebirrRadio != null && telebirrRadio.isSelected()) ? "Telebirr" : "CBE Birr");
        
        Label terms = new Label("Terms: Please arrive 15 minutes before showtime. Present this ticket at the counter.");
        terms.setStyle("-fx-font-size: 11px; -fx-text-fill: #666; -fx-wrap-text: true;");
        
        content.getChildren().addAll(header, details, terms);
        ticketDialog.getDialogPane().setContent(content);
        
        Button printBtn = (Button) ticketDialog.getDialogPane().lookupButton(printButton);
        if (printBtn != null) {
            printBtn.setOnAction(e -> {
                printTicketContent(bookingId);
            });
        }
        
        Button savePDFBtn = (Button) ticketDialog.getDialogPane().lookupButton(savePDFButton);
        if (savePDFBtn != null) {
            savePDFBtn.setOnAction(e -> {
                saveTicketAsPDF(content, bookingId);
            });
        }
        
        ticketDialog.showAndWait();
    }
    
    private void saveTicketAsPDF(VBox ticketContent, int bookingId) {
        try {
            Stage stage;
            
            if (ticketContent.getScene() == null || ticketContent.getScene().getWindow() == null) {
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
            } else {
                stage = (Stage) ticketContent.getScene().getWindow();
            }
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Ticket as PDF");
            fileChooser.setInitialFileName("eliana_cinema_ticket_" + bookingId + ".html");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML Files", "*.html"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            
            File file = fileChooser.showSaveDialog(stage);
            
            if (file != null) {
                try {
                    String htmlContent = createTicketHTML(bookingId);
                    java.nio.file.Files.write(file.toPath(), htmlContent.getBytes());
                    showSuccessAlert("Ticket Saved", 
                        "Ticket saved successfully!\n" +
                        "File: " + file.getAbsolutePath() + "\n" +
                        "You can open this file in a browser and print it as PDF.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Save Error", "Failed to save ticket: " + e.getMessage());
                }
            }
            
            if (ticketContent.getScene() == null) {
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save ticket: " + e.getMessage());
        }
    }
    
    private String createTicketHTML(int bookingId) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <title>Eliana Cinema Ticket #").append(bookingId).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        .ticket { border: 2px solid #ae2d3c; border-radius: 10px; padding: 20px; max-width: 600px; margin: 0 auto; background-color: #f9f9f9; }\n");
        html.append("        .header { text-align: center; color: #ae2d3c; font-size: 24px; font-weight: bold; margin-bottom: 20px; }\n");
        html.append("        .details { background-color: white; padding: 15px; border-radius: 5px; border: 1px solid #ddd; }\n");
        html.append("        .row { display: flex; margin-bottom: 10px; }\n");
        html.append("        .label { font-weight: bold; width: 150px; color: #333; }\n");
        html.append("        .value { flex: 1; color: #333; }\n");
        html.append("        .footer { margin-top: 20px; font-size: 12px; color: #666; text-align: center; }\n");
        html.append("        .cinema-name { font-size: 18px; font-weight: bold; color: #1e242e; text-align: center; margin-bottom: 10px; }\n");
        html.append("        @media print {\n");
        html.append("            body { margin: 0; }\n");
        html.append("            .ticket { border: none; box-shadow: none; }\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"ticket\">\n");
        html.append("        <div class=\"cinema-name\">Eliana Cinema</div>\n");
        html.append("        <div class=\"header\">🎟️ ELIANA CINEMA TICKET 🎟️</div>\n");
        html.append("        <div class=\"details\">\n");
        html.append("            <div class=\"row\"><div class=\"label\">Booking ID:</div><div class=\"value\">#").append(bookingId).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Movie:</div><div class=\"value\">").append(bookMovieSelect != null ? bookMovieSelect.getValue() : "").append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Date & Time:</div><div class=\"value\">").append(bookDateSelect != null ? bookDateSelect.getValue() : "").append(" ").append(selectedShowtime).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Hall:</div><div class=\"value\">").append(selectedHall).append("</div></div>\n");
        
        // Seat details with prices
        StringBuilder seatDetails = new StringBuilder();
        for (SeatSelection seatSelection : seatSelections.values()) {
            seatDetails.append(seatSelection.seatLabel)
                      .append(" (")
                      .append(seatSelection.seatType)
                      .append(" - ETB ")
                      .append(String.format("%.2f", seatSelection.seatPrice))
                      .append("), ");
        }
        if (seatDetails.length() > 0) {
            seatDetails.setLength(seatDetails.length() - 2);
        }
        html.append("            <div class=\"row\"><div class=\"label\">Seats:</div><div class=\"value\">").append(seatDetails.toString()).append("</div></div>\n");
        
        // Add discount information
        int numberOfTickets = selectedSeats.size();
        double discountRate = calculateDiscountRate(numberOfTickets);
        if (discountRate > 0) {
            html.append("            <div class=\"row\"><div class=\"label\">Discount:</div><div class=\"value\">").append(String.format("%.0f%% off for %d tickets", discountRate * 100, numberOfTickets)).append("</div></div>\n");
        }
        
        if (!cartItems.isEmpty()) {
            StringBuilder snacksText = new StringBuilder();
            for (CartItem item : cartItems) {
                snacksText.append(item.getSnack().getItemName())
                         .append(" (")
                         .append(item.getSnack().getSize())
                         .append(", x")
                         .append(item.getQuantity())
                         .append(" - ETB ")
                         .append(String.format("%.2f", item.getTotalPrice()))
                         .append("), ");
            }
            snacksText.setLength(snacksText.length() - 2);
            html.append("            <div class=\"row\"><div class=\"label\">Snacks:</div><div class=\"value\">").append(snacksText.toString()).append("</div></div>\n");
        }
        
        double seatTotal = calculateSeatTotalPrice();
        double snacksTotal = cartTotal;
        double subtotal = seatTotal + snacksTotal;
        double serviceCharge = subtotal * 0.05;
        double total = subtotal + serviceCharge;
        
        html.append("            <div class=\"row\"><div class=\"label\">Movie Tickets:</div><div class=\"value\">ETB ").append(String.format("%.2f", seatTotal)).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Snacks & Drinks:</div><div class=\"value\">ETB ").append(String.format("%.2f", snacksTotal)).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Service Charge:</div><div class=\"value\">ETB ").append(String.format("%.2f", serviceCharge)).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Total Amount:</div><div class=\"value\">ETB ").append(String.format("%.2f", total)).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Payment Method:</div><div class=\"value\">").append((telebirrRadio != null && telebirrRadio.isSelected()) ? "Telebirr" : "CBE Birr").append("</div></div>\n");
        html.append("        </div>\n");
        html.append("        <div class=\"footer\">\n");
        html.append("            Please arrive 15 minutes before showtime. Present this ticket at the counter.\n");
        html.append("            <br>Valid for entry only. Late arrivals may not be admitted.\n");
        html.append("            <br><br>Thank you for choosing Eliana Cinema!\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("    <script>\n");
        html.append("        window.onload = function() {\n");
        html.append("            // Uncomment to auto-print\n");
        html.append("            // window.print();\n");
        html.append("        };\n");
        html.append("    </script>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    private void addTicketDetail(VBox container, String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelLbl = new Label(label);
        labelLbl.setStyle("-fx-font-weight: bold; -fx-min-width: 100; -fx-text-fill: #333;");
        
        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        
        row.getChildren().addAll(labelLbl, valueLbl);
        container.getChildren().add(row);
    }
    
    private void printTicketContent(int bookingId) {
        try {
            String htmlContent = createTicketHTML(bookingId);
            File tempFile = File.createTempFile("eliana_cinema_ticket_" + bookingId, ".html");
            java.nio.file.Files.write(tempFile.toPath(), htmlContent.getBytes());
            
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(tempFile.toURI());
            }
            
            showSuccessAlert("Print Ticket", 
                "Ticket #" + bookingId + " opened in browser.\n" +
                "Please use the browser's print function (Ctrl+P) to print your ticket.");
                
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Print Error", "Failed to generate print ticket: " + e.getMessage());
        }
    }
    
    private void printExistingTicket(Booking booking) {
        int bookingId = booking.getBookingIdDb();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.*, m.title, s.show_date, s.show_time, h.hall_name, " +
                        "GROUP_CONCAT(seats.seat_label ORDER BY seats.seat_label SEPARATOR ', ') as seat_labels, " +
                        "GROUP_CONCAT(CONCAT(sn.item_name, ' (', sn.size, ', x', bsn.quantity, ')') SEPARATOR ', ') as snack_items " +
                        "FROM bookings b " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                        "LEFT JOIN booking_seats bs ON b.booking_id = bs.booking_id " +
                        "LEFT JOIN seats ON bs.seat_id = seats.seat_id " +
                        "LEFT JOIN booking_snacks bsn ON b.booking_id = bsn.booking_id " +
                        "LEFT JOIN snack_items sn ON bsn.snack_id = sn.snack_id " +
                        "WHERE b.booking_id = ? " +
                        "GROUP BY b.booking_id";
            
            System.out.println("🔍 DEBUG: Executing query for ticket: " + sql);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String htmlContent = createExistingTicketHTML(rs, bookingId);
                File tempFile = File.createTempFile("eliana_cinema_ticket_" + bookingId, ".html");
                java.nio.file.Files.write(tempFile.toPath(), htmlContent.getBytes());
                
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().browse(tempFile.toURI());
                }
                
                showSuccessAlert("Print Ticket", 
                    "Ticket #" + bookingId + " opened in browser.\n" +
                    "Please use the browser's print function (Ctrl+P) to print your ticket.");
            } else {
                showAlert("Not Found", "Ticket not found for booking ID: " + bookingId);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Print Error", "Failed to load ticket details: " + e.getMessage());
        }
    }
    
    private String createExistingTicketHTML(ResultSet rs, int bookingId) throws Exception {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <title>Eliana Cinema Ticket #").append(bookingId).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        .ticket { border: 2px solid #ae2d3c; border-radius: 10px; padding: 20px; max-width: 600px; margin: 0 auto; background-color: #f9f9f9; }\n");
        html.append("        .header { text-align: center; color: #ae2d3c; font-size: 24px; font-weight: bold; margin-bottom: 20px; }\n");
        html.append("        .details { background-color: white; padding: 15px; border-radius: 5px; border: 1px solid #ddd; }\n");
        html.append("        .row { display: flex; margin-bottom: 10px; }\n");
        html.append("        .label { font-weight: bold; width: 150px; color: #333; }\n");
        html.append("        .value { flex: 1; color: #333; }\n");
        html.append("        .footer { margin-top: 20px; font-size: 12px; color: #666; text-align: center; }\n");
        html.append("        .cinema-name { font-size: 18px; font-weight: bold; color: #1e242e; text-align: center; margin-bottom: 10px; }\n");
        html.append("        @media print {\n");
        html.append("            body { margin: 0; }\n");
        html.append("            .ticket { border: none; box-shadow: none; }\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"ticket\">\n");
        html.append("        <div class=\"cinema-name\">Eliana Cinema</div>\n");
        html.append("        <div class=\"header\">🎟️ ELIANA CINEMA TICKET 🎟️</div>\n");
        html.append("        <div class=\"details\">\n");
        html.append("            <div class=\"row\"><div class=\"label\">Booking ID:</div><div class=\"value\">#").append(bookingId).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Movie:</div><div class=\"value\">").append(rs.getString("title")).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Date:</div><div class=\"value\">").append(rs.getDate("show_date")).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Time:</div><div class=\"value\">").append(rs.getTime("show_time")).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Hall:</div><div class=\"value\">").append(rs.getString("hall_name")).append("</div></div>\n");
        
        String seatLabels = rs.getString("seat_labels");
        html.append("            <div class=\"row\"><div class=\"label\">Seats:</div><div class=\"value\">").append(seatLabels != null ? seatLabels : "Not assigned").append("</div></div>\n");
        
        String snackItems = rs.getString("snack_items");
        if (snackItems != null) {
            html.append("            <div class=\"row\"><div class=\"label\">Snacks:</div><div class=\"value\">").append(snackItems).append("</div></div>\n");
        }
        
        html.append("            <div class=\"row\"><div class=\"label\">Total Amount:</div><div class=\"value\">ETB ").append(String.format("%.2f", rs.getDouble("total_amount"))).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Payment Method:</div><div class=\"value\">").append(rs.getString("payment_method")).append("</div></div>\n");
        html.append("            <div class=\"row\"><div class=\"label\">Status:</div><div class=\"value\">").append(rs.getString("booking_status")).append("</div></div>\n");
        html.append("        </div>\n");
        html.append("    <div class=\"footer\">\n");
        html.append("        Please arrive 15 minutes before showtime. Present this ticket at the counter.\n");
        html.append("        <br>Valid for entry only. Late arrivals may not be admitted.\n");
        html.append("        <br><br>Thank you for choosing Eliana Cinema!\n");
        html.append("    </div>\n");
        html.append("</div>\n");
        html.append("<script>\n");
        html.append("    window.onload = function() {\n");
        html.append("        // window.print(); // Uncomment to auto-print\n");
        html.append("    };\n");
        html.append("</script>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    private void viewBookingDetails(Booking booking) {
        int bookingId = booking.getBookingIdDb();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.*, m.title, m.genre, m.duration_minutes, s.show_date, s.show_time, " +
                        "h.hall_name, u.first_name, u.last_name, u.email, u.phone_number, " +
                        "GROUP_CONCAT(seats.seat_label ORDER BY seats.seat_label SEPARATOR ', ') as seat_labels, " +
                        "GROUP_CONCAT(CONCAT(sn.item_name, ' (', sn.size, ', x', bsn.quantity, ') - ETB ', bsn.item_price*bsn.quantity) SEPARATOR '\n') as snack_details " +
                        "FROM bookings b " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "LEFT JOIN booking_seats bs ON b.booking_id = bs.booking_id " +
                        "LEFT JOIN seats ON bs.seat_id = seats.seat_id " +
                        "LEFT JOIN booking_snacks bsn ON b.booking_id = bsn.booking_id " +
                        "LEFT JOIN snack_items sn ON bsn.snack_id = sn.snack_id " +
                        "WHERE b.booking_id = ? " +
                        "GROUP BY b.booking_id";
            
            System.out.println("🔍 DEBUG: Executing query: " + sql);
            System.out.println("🔍 DEBUG: For booking_id: " + bookingId);
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
                detailsAlert.setTitle("Booking Details");
                detailsAlert.setHeaderText("Booking #" + bookingId + " Details");
                
                StringBuilder details = new StringBuilder();
                details.append("Booking ID: #").append(bookingId).append("\n");
                details.append("Movie: ").append(rs.getString("title")).append("\n");
                details.append("Genre: ").append(rs.getString("genre")).append("\n");
                details.append("Duration: ").append(rs.getInt("duration_minutes")).append(" minutes\n");
                details.append("Date: ").append(rs.getDate("show_date")).append("\n");
                details.append("Time: ").append(rs.getString("show_time")).append("\n");
                details.append("Hall: ").append(rs.getString("hall_name")).append("\n");
                details.append("Seats: ").append(rs.getString("seat_labels")).append("\n");
                
                String snacks = rs.getString("snack_details");
                if (snacks != null) {
                    details.append("\nSnacks & Drinks:\n").append(snacks).append("\n");
                }
                
                details.append("\nCustomer: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name")).append("\n");
                details.append("Email: ").append(rs.getString("email")).append("\n");
                details.append("Phone: ").append(rs.getString("phone_number")).append("\n");
                details.append("\nPayment Details:\n");
                details.append("Total Amount: ETB ").append(String.format("%.2f", rs.getDouble("total_amount"))).append("\n");
                details.append("Payment Method: ").append(rs.getString("payment_method")).append("\n");
                details.append("Payment Status: ").append(rs.getString("payment_status")).append("\n");
                details.append("Booking Status: ").append(rs.getString("booking_status")).append("\n");
                details.append("Booking Date: ").append(rs.getTimestamp("booking_date")).append("\n");
                
                detailsAlert.setContentText(details.toString());
                detailsAlert.showAndWait();
            } else {
                showAlert("Not Found", "Booking details not found for ID: " + bookingId);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Details Error", "Failed to load booking details: " + e.getMessage());
        }
    }
    
    private void resetPaymentForm() {
        if (paymentPhoneField != null) paymentPhoneField.clear();
        resetOtpSection();
        if (termsCheckBox != null) {
            termsCheckBox.setSelected(false);
        }
        if (makePaymentBtn != null) makePaymentBtn.setDisable(true);
    }
    
    // ==================== CART MANAGEMENT METHODS ====================
    
    private void addToCart(Snack snack) {
        addToCart(snack, 1);
    }
    
    private void addToCart(Snack snack, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getSnack().getSnackId() == snack.getSnackId()) {
                item.setQuantity(item.getQuantity() + quantity);
                updateCartDisplay();
                showSuccessAnimation(snack.getItemName() + " added to cart!");
                return;
            }
        }
        
        cartItems.add(new CartItem(snack, quantity));
        updateCartDisplay();
        showSuccessAnimation(snack.getItemName() + " added to cart!");
    }
    
    private void updateCartDisplay() {
        cartTotal = 0.0;
        int totalItems = 0;
        
        for (CartItem item : cartItems) {
            cartTotal += item.getTotalPrice();
            totalItems += item.getQuantity();
        }
        
        if (cartBadge != null) cartBadge.setVisible(totalItems > 0);
        if (cartTotalLabel != null) cartTotalLabel.setText(String.format("ETB %.2f", cartTotal));
        
        if (cartItems.isEmpty()) {
            if (emptyCartMessage != null) emptyCartMessage.setVisible(true);
            if (cartSummaryBox != null) cartSummaryBox.setVisible(false);
        } else {
            if (emptyCartMessage != null) emptyCartMessage.setVisible(false);
            if (cartSummaryBox != null) cartSummaryBox.setVisible(true);
            if (cartSubtotalLabel != null) cartSubtotalLabel.setText(String.format("ETB %.2f", cartTotal));
            if (cartTotalModalLabel != null) cartTotalModalLabel.setText(String.format("ETB %.2f", cartTotal));
        }
        
        if (cartItemsListView != null) cartItemsListView.refresh();
        updateBookingSummary();
        updateSnacksSelectionTile();
    }
    
    private void updateSnacksSelectionTile() {
        if (snacksSelectionTile != null) {
            snacksSelectionTile.getChildren().clear();
            for (Snack snack : allSnacks) {
                VBox snackCard = createSnackCardForSelection(snack);
                snacksSelectionTile.getChildren().add(snackCard);
            }
            
            if (snacksTotalLabel != null) {
                snacksTotalLabel.setText("Cart Total: " + String.format("ETB %.2f", cartTotal));
                snacksTotalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 16px;");
            }
        }
    }
    
    private void showCartModal() {
        if (cartModal != null) {
            cartModal.setVisible(true);
            cartModal.toFront();
            updateCartDisplay();
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), cartModal);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }
    
    private void hideCartModal() {
        if (cartModal != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), cartModal);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> cartModal.setVisible(false));
            ft.play();
        }
    }
    
    @FXML
    private void handleClearCart() {
        cartItems.clear();
        updateCartDisplay();
        showSuccessAlert("Cart Cleared", "Your cart has been cleared.");
    }
    
    @FXML
    private void handleCheckoutFromCart() {
        hideCartModal();
        if (selectedSeats.isEmpty()) {
            showAlert("Seat Selection Required", "Please select seats before checking out.");
            showBookTicketsPage();
        } else {
            showPaymentPage();
        }
    }
    
    @FXML
    private void handleBrowseSnacksFromCart() {
        hideCartModal();
        showSnacksDrinksPage();
    }
    
    // ==================== PAGE NAVIGATION METHODS ====================
    
    private void showHomePage() {
        hideAllPages();
        if (homePageForm != null) {
            homePageForm.setVisible(true);
            homePageForm.setManaged(true);
            homePageForm.toFront();
        }
        loadHomePageData();
        System.out.println("📊 Showing Home Page (Refreshed)");
    }
    
    private void showMoviesPage() {
        hideAllPages();
        if (moviesPageForm != null) {
            moviesPageForm.setVisible(true);
            moviesPageForm.setManaged(true);
            moviesPageForm.toFront();
        }
        loadAllMovies();
        System.out.println("🎬 Showing Movies Page (Refreshed)");
    }
    
    private void showSnacksDrinksPage() {
        hideAllPages();
        if (snacksDrinksForm != null) {
            snacksDrinksForm.setVisible(true);
            snacksDrinksForm.setManaged(true);
            snacksDrinksForm.toFront();
        }
        loadAllSnacks();
        updateSnackFilterButtons(allSnacksBtn);
        System.out.println("🍿 Showing Snacks & Drinks Page (Refreshed)");
    }
    
    private void showBookTicketsPage() {
        hideAllPages();
        if (bookTicketsForm != null) {
            bookTicketsForm.setVisible(true);
            bookTicketsForm.setManaged(true);
            bookTicketsForm.toFront();
        }
        
        if (snacksSelectionContainer != null) {
            snacksSelectionContainer.setVisible(true);
            snacksSelectionContainer.setManaged(true);
        }
        updateSnacksSelectionTile();
        
        System.out.println("🎟️ Showing Book Tickets Page");
    }
    
    private void showMyBookingsPage() {
        hideAllPages();
        if (myBookingsForm != null) {
            myBookingsForm.setVisible(true);
            myBookingsForm.setManaged(true);
            myBookingsForm.toFront();
        }
        loadUserBookings();
        System.out.println("📋 Showing My Bookings Page (Refreshed)");
    }
    
    private void showAboutUsPage() {
        hideAllPages();
        if (aboutUsForm != null) {
            aboutUsForm.setVisible(true);
            aboutUsForm.setManaged(true);
            aboutUsForm.toFront();
        }
        loadAboutUsContent();
        System.out.println("🏢 Showing About Us Page");
    }
    
    private void showHelpPage() {
        hideAllPages();
        if (helpForm != null) {
            helpForm.setVisible(true);
            helpForm.setManaged(true);
            helpForm.toFront();
        }
        loadHelpContent();
        System.out.println("❓ Showing Help & Support Page");
    }
    
    private void showProfilePage() {
        hideAllPages();
        if (profileForm != null) {
            profileForm.setVisible(true);
            profileForm.setManaged(true);
            profileForm.toFront();
        }
        loadUserProfile();
        System.out.println("👤 Showing Profile Page");
    }
    
    private void showPaymentPage() {
        hideAllPages();
        if (paymentForm != null) {
            paymentForm.setVisible(true);
            paymentForm.setManaged(true);
            paymentForm.toFront();
        }
        
        if (paymentMovieLabel != null && bookMovieSelect != null) paymentMovieLabel.setText(bookMovieSelect.getValue());
        if (paymentDateTimeLabel != null && bookDateSelect != null) paymentDateTimeLabel.setText(bookDateSelect.getValue().toString() + " " + selectedShowtime);
        if (paymentSeatsLabel != null) paymentSeatsLabel.setText(String.join(", ", selectedSeats));
        if (paymentTicketsLabel != null) paymentTicketsLabel.setText(String.valueOf(selectedSeats.size()));
        
        if (paymentMovieLabel != null) paymentMovieLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentDateTimeLabel != null) paymentDateTimeLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentSeatsLabel != null) paymentSeatsLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentTicketsLabel != null) paymentTicketsLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentTicketsSubtotalLabel != null) paymentTicketsSubtotalLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentSnacksSubtotalLabel != null) paymentSnacksSubtotalLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentServiceChargeLabel != null) paymentServiceChargeLabel.setStyle("-fx-text-fill: #333333;");
        if (paymentTotalAmountLabel != null) paymentTotalAmountLabel.setStyle("-fx-text-fill: #ae2d3c; -fx-font-weight: bold;");
        
        if (cartItems.isEmpty()) {
            if (paymentSnacksLabel != null) {
                paymentSnacksLabel.setText("No snacks added");
                paymentSnacksLabel.setStyle("-fx-text-fill: #333333;");
            }
        } else {
            StringBuilder snacksText = new StringBuilder();
            for (CartItem item : cartItems) {
                snacksText.append(item.getSnack().getItemName())
                         .append(" (")
                         .append(item.getSnack().getSize())
                         .append(", x")
                         .append(item.getQuantity())
                         .append("), ");
            }
            snacksText.setLength(snacksText.length() - 2);
            if (paymentSnacksLabel != null) {
                paymentSnacksLabel.setText(snacksText.toString());
                paymentSnacksLabel.setStyle("-fx-text-fill: #333333;");
            }
        }
        
        // Calculate total with proper discount
        double seatTotal = calculateSeatTotalPrice();
        double snacksTotal = cartTotal;
        double subtotal = seatTotal + snacksTotal;
        double serviceCharge = subtotal * 0.05;
        double total = subtotal + serviceCharge;
        
        if (paymentTicketsSubtotalLabel != null) {
            paymentTicketsSubtotalLabel.setText(String.format("ETB %.2f", seatTotal));
        }
        if (paymentSnacksSubtotalLabel != null) paymentSnacksSubtotalLabel.setText(String.format("ETB %.2f", snacksTotal));
        if (paymentServiceChargeLabel != null) paymentServiceChargeLabel.setText(String.format("ETB %.2f", serviceCharge));
        if (paymentTotalAmountLabel != null) paymentTotalAmountLabel.setText(String.format("ETB %.2f", total));
        
        if (makePaymentBtn != null) makePaymentBtn.setDisable(true);
        if (cbeRadio != null && cbeRadio.isSelected() && makePaymentBtn != null) {
            makePaymentBtn.setDisable(false);
        }
        
        System.out.println("💳 Showing Payment Page");
    }
    
    private void hideAllPages() {
        AnchorPane[] pages = {homePageForm, moviesPageForm, snacksDrinksForm, 
                             bookTicketsForm, myBookingsForm, aboutUsForm, 
                             helpForm, profileForm, paymentForm};
        
        for (AnchorPane page : pages) {
            if (page != null) {
                page.setVisible(false);
                page.setManaged(false);
            }
        }
    }
    
    // ==================== EVENT HANDLERS ====================
    
    @FXML
    private void handleBookNowHero() {
        showBookTicketsPage();
        System.out.println("🎟️ Book Now clicked");
    }
    
    @FXML
    private void handleFindShowtimes() {
        String selectedMovieTitle = bookMovieSelect != null ? bookMovieSelect.getValue() : null;
        LocalDate selectedDate = bookDateSelect != null ? bookDateSelect.getValue() : null;
        String selectedTime = bookTimeSelect != null ? bookTimeSelect.getValue() : null;
        
        System.out.println("🔍 Finding showtimes for:");
        System.out.println("   Movie: " + selectedMovieTitle);
        System.out.println("   Date: " + selectedDate);
        System.out.println("   Time: " + selectedTime);
        
        if (selectedMovieTitle == null || selectedMovieTitle.isEmpty()) {
            showAlert("Selection Required", "Please select a movie");
            return;
        }
        
        if (selectedDate == null) {
            showAlert("Selection Required", "Please select a date");
            return;
        }
        
        if (selectedTime == null || selectedTime.isEmpty()) {
            showAlert("Selection Required", "Please select a showtime from the list");
            return;
        }
        
        if (showtimesContainer != null) {
            showtimesContainer.setVisible(true);
            showtimesContainer.setManaged(true);
        }
        loadShowtimesForSelection();
        System.out.println("🕐 Finding showtimes...");
    }
    
    @FXML
    private void handleProceedToPayment() {
        if (selectedSeats.isEmpty()) {
            showAlert("Selection Required", "Please select at least one seat");
            return;
        }
        
        if (selectedShowtimeId == 0) {
            showAlert("Selection Required", "Please select a showtime");
            return;
        }
        
        showPaymentPage();
        System.out.println("💳 Proceeding to payment...");
    }
    
    @FXML
    private void handleBackToBooking() {
        showBookTicketsPage();
        System.out.println("↩️ Returning to booking...");
    }
    
    @FXML
    private void handleViewAllMovies() {
        showMoviesPage();
        System.out.println("🎬 Viewing all movies...");
    }
    
    @FXML
    private void handleViewAllSnacks() {
        showSnacksDrinksPage();
        System.out.println("🍿 Viewing all snacks...");
    }
    
    @FXML
    private void handleSearchMovies() {
        filterMovies(movieSearchField != null ? movieSearchField.getText() : "");
        System.out.println("🔍 Searching movies...");
    }
    
    @FXML
    private void handleSearchSnacks() {
        filterSnacks(snackSearchField != null ? snackSearchField.getText() : "");
        System.out.println("🔍 Searching snacks...");
    }
    
    @FXML
    private void handleLogout() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Logout");
            confirmAlert.setHeaderText("Logout");
            confirmAlert.setContentText("Are you sure you want to logout?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (dataRefreshTimer != null) {
                    dataRefreshTimer.stop();
                }
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) logoutBtn.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                
                System.out.println("👋 User logged out: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Logout Error", "Failed to logout: " + e.getMessage());
        }
    }
    
    // ==================== UI HELPER METHODS ====================
    
    private void updateBookingSummary() {
        if (summaryMovieLabel != null && bookMovieSelect != null) {
            summaryMovieLabel.setText(bookMovieSelect.getValue());
        }
        if (summaryDateTimeLabel != null && bookDateSelect != null) {
            summaryDateTimeLabel.setText(bookDateSelect.getValue() + " " + selectedShowtime);
        }
        if (summarySeatsLabel != null) {
            summarySeatsLabel.setText(selectedSeats.isEmpty() ? "No seats selected" : String.join(", ", selectedSeats));
        }
        
        if (summarySnacksLabel != null) {
            if (cartItems.isEmpty()) {
                summarySnacksLabel.setText("No snacks");
            } else {
                StringBuilder snacksText = new StringBuilder();
                for (CartItem item : cartItems) {
                    snacksText.append(item.getSnack().getItemName())
                             .append(" (")
                             .append(item.getSnack().getSize())
                             .append(", x")
                             .append(item.getQuantity())
                             .append("), ");
                }
                snacksText.setLength(snacksText.length() - 2);
                summarySnacksLabel.setText(snacksText.toString());
            }
        }
        
        // Calculate total with proper discount
        double seatTotal = calculateSeatTotalPrice();
        double snacksTotal = cartTotal;
        double subtotal = seatTotal + snacksTotal;
        double serviceCharge = subtotal * 0.05;
        double total = subtotal + serviceCharge;
        
        if (summaryTotalLabel != null) {
            summaryTotalLabel.setText(String.format("ETB %.2f", total));
            summaryTotalLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 18px;");
        }
        
        if (bookingSummaryContainer != null) {
            bookingSummaryContainer.setVisible(true);
        }
    }
    
    private void resetBookingForm() {
        selectedSeats.clear();
        seatSelections.clear();
        selectedShowtimeId = 0;
        selectedHallId = 0;
        selectedShowtime = "";
        selectedHall = "";
        seatPremiumPrice = 20.0;
        movieBasePrice = 0.0;
        selectedMovie = null;
        
        if (bookMovieSelect != null) bookMovieSelect.setValue(null);
        if (bookDateSelect != null) bookDateSelect.setValue(LocalDate.now());
        if (bookTimeSelect != null) bookTimeSelect.setValue(null);
        
        if (showtimesContainer != null) showtimesContainer.setVisible(false);
        if (seatSelectionContainer != null) seatSelectionContainer.setVisible(false);
        if (snacksSelectionContainer != null) snacksSelectionContainer.setVisible(false);
        if (bookingSummaryContainer != null) bookingSummaryContainer.setVisible(false);
        
        if (showtimesGrid != null) showtimesGrid.getChildren().clear();
        if (snacksSelectionTile != null) snacksSelectionTile.getChildren().clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    
    private void showSuccessAnimation(String message) {
        try {
            Label successLabel = new Label(message);
            successLabel.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; " +
                                "-fx-padding: 10 20 10 20; -fx-background-radius: 20;");
            
            successLabel.setLayoutX(400);
            successLabel.setLayoutY(600);
            
            if (homePageForm != null && homePageForm.getParent() != null) {
                AnchorPane root = (AnchorPane) homePageForm.getParent();
                root.getChildren().add(successLabel);
                
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), successLabel);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), successLabel);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setDelay(Duration.seconds(2));
                
                fadeIn.setOnFinished(e -> fadeOut.play());
                fadeOut.setOnFinished(e -> root.getChildren().remove(successLabel));
                
                fadeIn.play();
            }
        } catch (Exception e) {
            // Silently fail if animation fails
        }
    }
    
    // ==================== SEAT SELECTION METHODS ====================
    
    private void loadShowtimesForSelection() {
        String selectedMovieTitle = bookMovieSelect != null ? bookMovieSelect.getValue() : null;
        LocalDate selectedDate = bookDateSelect != null ? bookDateSelect.getValue() : null;
        String selectedTime = bookTimeSelect != null ? bookTimeSelect.getValue() : null;
        
        if (selectedMovieTitle == null || selectedDate == null || selectedTime == null) {
            showAlert("Selection Required", "Please select movie, date, and time");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get the showtime ID for the selected movie, date, and time
            String sql = "SELECT s.showtime_id, s.show_time, h.hall_name, " +
                        "s.available_seats, h.hall_id, m.price as movie_price " +
                        "FROM showtimes s " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                        "WHERE m.title = ? AND s.show_date = ? AND s.show_time = ? " +
                        "AND s.is_active = TRUE AND s.available_seats > 0";
            
            System.out.println("🔍 SQL Query: " + sql);
            System.out.println("🔍 Parameters: movie=" + selectedMovieTitle + ", date=" + selectedDate + ", time=" + selectedTime + ":00");
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selectedMovieTitle);
            pstmt.setDate(2, java.sql.Date.valueOf(selectedDate));
            pstmt.setString(3, selectedTime + ":00");
            ResultSet rs = pstmt.executeQuery();
            
            if (showtimesGrid != null) showtimesGrid.getChildren().clear();
            
            if (rs.next()) {
                int showtimeId = rs.getInt("showtime_id");
                String showTime = rs.getTime("show_time").toString().substring(0, 5);
                String hallName = rs.getString("hall_name");
                double moviePriceFromDb = rs.getDouble("movie_price");
                int availableSeats = rs.getInt("available_seats");
                int hallId = rs.getInt("hall_id");
                
                System.out.println("✅ Found showtime: " + showTime + " in " + hallName + " (" + availableSeats + " seats available)");
                
                this.selectedShowtimeId = showtimeId;
                this.selectedHallId = hallId;
                this.selectedShowtime = showTime;
                this.selectedHall = hallName;
                this.movieBasePrice = moviePriceFromDb;
                
                // Create showtime card
                VBox showtimeCard = createShowtimeCard(showTime, hallName, moviePriceFromDb, availableSeats, showtimeId, hallId);
                if (showtimesGrid != null) showtimesGrid.add(showtimeCard, 0, 0);
                
                // Automatically load seats
                if (seatSelectionContainer != null) {
                    seatSelectionContainer.setVisible(true);
                    seatSelectionContainer.setManaged(true);
                }
                
                loadSeatsForShowtime(showtimeId, hallId);
                updateBookingSummary();
                
                showSuccessAlert("Showtime Selected", "Showtime selected successfully! You can now select seats.");
            } else {
                Label noShowtimes = new Label("No showtimes available for selected time");
                noShowtimes.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
                if (showtimesGrid != null) showtimesGrid.add(noShowtimes, 0, 0);
                showAlert("No Showtimes", "No showtimes found for the selected movie, date, and time.");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load showtimes: " + e.getMessage());
        }
    }
    private VBox createShowtimeCard(String time, String hall, double price, int availableSeats, 
                                    int showtimeId, int hallId) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(200);
        
        setupCardHover(card);
        
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        Label hallLabel = new Label(hall);
        hallLabel.setStyle("-fx-text-fill: #666;");
        
        Label priceLabel = new Label(String.format("ETB %.2f", price));
        priceLabel.setStyle("-fx-text-fill: #ae2d3c; -fx-font-weight: bold;");
        
        Label seatsLabel = new Label(availableSeats + " seats available");
        seatsLabel.setStyle("-fx-text-fill: " + (availableSeats > 10 ? "#28a745" : "#dc3545") + ";");
        
        Button selectBtn = new Button("Select");
        selectBtn.setStyle("-fx-background-color: #ae2d3c; -fx-text-fill: white;");
        selectBtn.setCursor(Cursor.HAND);
        setupButtonHover(selectBtn);
        selectBtn.setOnAction(e -> {
            this.selectedShowtimeId = showtimeId;
            this.selectedHallId = hallId;
            this.selectedShowtime = time;
            this.selectedHall = hall;
            this.movieBasePrice = price;
            
            if (seatSelectionContainer != null) {
                seatSelectionContainer.setVisible(true);
                seatSelectionContainer.setManaged(true);
            }
            
            loadSeatsForShowtime(showtimeId, hallId);
            updateBookingSummary();
        });
        
        card.getChildren().addAll(timeLabel, hallLabel, priceLabel, seatsLabel, selectBtn);
        return card;
    }
    
    private void loadSeatsForShowtime(int showtimeId, int hallId) {
        System.out.println("DEBUG: Loading seats for showtimeId=" + showtimeId + ", hallId=" + hallId);
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            if (seatSelectionContainer == null) {
                System.err.println("ERROR: seatSelectionContainer is null!");
                return;
            }
            
            seatSelectionContainer.getChildren().clear();
            
            selectedSeats.clear();
            seatSelections.clear();
            seatButtons.clear();
            updateSeatSelectionInfo();
            
            Pane screen = createCinemaScreen();
            seatSelectionContainer.getChildren().add(screen);
            
            // FIXED: Updated SQL query to include price_multiplier
            String seatSql = "SELECT s.seat_id, s.seat_label, s.seat_type, s.is_blocked, s.seat_row, s.seat_number, " +
                           "s.price_multiplier, " + // ADDED THIS LINE
                           "CASE WHEN bs.booking_id IS NOT NULL AND b.showtime_id = ? THEN 1 ELSE 0 END as is_booked " +
                           "FROM seats s " +
                           "LEFT JOIN booking_seats bs ON s.seat_id = bs.seat_id " +
                           "LEFT JOIN bookings b ON bs.booking_id = b.booking_id " +
                           "WHERE s.hall_id = ? " +
                           "ORDER BY s.seat_row, s.seat_number";
            
            PreparedStatement seatStmt = conn.prepareStatement(seatSql);
            seatStmt.setInt(1, showtimeId);
            seatStmt.setInt(2, hallId);
            
            ResultSet seatRs = seatStmt.executeQuery();
            
            Map<Character, List<SeatInfo>> rows = new HashMap<>();
            
            while (seatRs.next()) {
                SeatInfo seat = new SeatInfo();
                seat.label = seatRs.getString("seat_label");
                seat.type = seatRs.getString("seat_type");
                seat.isBlocked = seatRs.getBoolean("is_blocked");
                seat.isBooked = seatRs.getInt("is_booked") == 1;
                seat.row = seatRs.getString("seat_row").charAt(0);
                seat.col = seatRs.getInt("seat_number");
                seat.priceMultiplier = seatRs.getDouble("price_multiplier"); // ADDED THIS LINE
                
                char rowChar = seat.row;
                if (!rows.containsKey(rowChar)) {
                    rows.put(rowChar, new ArrayList<>());
                }
                rows.get(rowChar).add(seat);
            }
            
            seatRs.close();
            seatStmt.close();
            
            List<Character> sortedRows = new ArrayList<>(rows.keySet());
            Collections.sort(sortedRows);
            
            for (char rowChar : sortedRows) {
                HBox seatRow = new HBox(5);
                seatRow.setAlignment(Pos.CENTER);
                
                Label rowLabel = new Label(String.valueOf(rowChar) + ": ");
                rowLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-min-width: 30px; -fx-text-fill: #333;");
                seatRow.getChildren().add(rowLabel);
                
                List<SeatInfo> seatsInRow = rows.get(rowChar);
                seatsInRow.sort((s1, s2) -> Integer.compare(s1.col, s2.col));
                
                for (SeatInfo seat : seatsInRow) {
                    Pane seatPane = createSeatPane(seat);
                    seatButtons.put(seat.label, seatPane);
                    seatRow.getChildren().add(seatPane);
                }
                
                seatSelectionContainer.getChildren().add(seatRow);
            }
            
            VBox legend = createSeatLegend();
            seatSelectionContainer.getChildren().add(legend);
            
            System.out.println("DEBUG: Total rows loaded: " + sortedRows.size());
            
        } catch (Exception e) {
            System.err.println("ERROR loading seats: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load seats: " + e.getMessage());
        }
    }
    
    private Pane createCinemaScreen() {
        VBox screenContainer = new VBox(10);
        screenContainer.setAlignment(Pos.CENTER);
        
        Label screenLabel = new Label("SCREEN");
        screenLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #666;");
        
        Pane screen = new Pane();
        screen.setPrefSize(500, 30);
        screen.setStyle("-fx-background-color: linear-gradient(to bottom, #1e242e, #3a4559); " +
                       "-fx-background-radius: 10; -fx-border-color: #666; -fx-border-width: 2;");
        
        Rectangle reflection = new Rectangle(500, 10);
        reflection.setStyle("-fx-fill: linear-gradient(to bottom, rgba(255,255,255,0.3), transparent);");
        reflection.setArcWidth(10);
        reflection.setArcHeight(10);
        
        screen.getChildren().add(reflection);
        screenContainer.getChildren().addAll(screenLabel, screen);
        
        return screenContainer;
    }
    
    private Pane createSeatPane(SeatInfo seat) {
        StackPane seatPane = new StackPane();
        seatPane.setPrefSize(40, 40);
        
        Rectangle bg = new Rectangle(35, 35);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        
        // Apply colors based on seat status (matching admin colors)
        if (seat.isBlocked) {
            bg.setFill(BLOCKED_COLOR); // Gray for blocked
        } else if (seat.isBooked) {
            bg.setFill(BOOKED_COLOR); // Red for booked
        } else if (seat.type.equals("premium")) {
            bg.setFill(Color.GOLD); // Gold for premium
        } else if (seat.type.equals("vip")) {
            bg.setFill(VIP_COLOR); // Orange for VIP
        } else if (seat.type.equals("disabled")) {
            bg.setFill(Color.LIGHTGRAY); // Light gray for disabled
        } else {
            bg.setFill(AVAILABLE_COLOR); // Green for standard/available
        }
        
        Label label = new Label(seat.label);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: white;");
        
        seatPane.getChildren().addAll(bg, label);
        
        // Only make seat clickable if it's available and not blocked/booked
        if (!seat.isBlocked && !seat.isBooked && !seat.type.equals("disabled")) {
            seatPane.setCursor(Cursor.HAND);
            
            seatPane.setOnMouseEntered(e -> {
                if (!selectedSeats.contains(seat.label)) {
                    ScaleTransition st = new ScaleTransition(Duration.millis(200), seatPane);
                    st.setToX(1.2);
                    st.setToY(1.2);
                    st.play();
                    
                    seatPane.setEffect(glowEffect);
                }
            });
            
            seatPane.setOnMouseExited(e -> {
                if (!selectedSeats.contains(seat.label)) {
                    ScaleTransition st = new ScaleTransition(Duration.millis(200), seatPane);
                    st.setToX(1.0);
                    st.setToY(1.0);
                    st.play();
                    
                    seatPane.setEffect(null);
                }
            });
            
            seatPane.setOnMouseClicked(e -> handleSeatSelection(seatPane, seat.label, bg, seat.type));
        } else {
            seatPane.setCursor(Cursor.DEFAULT);
        }
        
        return seatPane;
    }
    
    private void handleSeatSelection(StackPane seatPane, String seatLabel, Rectangle bg, String seatType) {
        if (selectedSeats.contains(seatLabel)) {
            // Deselect seat
            selectedSeats.remove(seatLabel);
            seatSelections.remove(seatLabel);
            
            // Restore original color
            if (seatType.equals("vip")) {
                bg.setFill(VIP_COLOR); // Orange for VIP
            } else if (seatType.equals("premium")) {
                bg.setFill(Color.GOLD); // Gold for premium
            } else {
                bg.setFill(AVAILABLE_COLOR); // Green for available/standard
            }
            seatPane.setEffect(null);
            
            ScaleTransition st = new ScaleTransition(Duration.millis(200), seatPane);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        } else {
            // Select seat
            selectedSeats.add(seatLabel);
            
            // Calculate seat price based on type with multipliers
            double seatPrice = movieBasePrice; // Base movie price
            
            // Apply seat type multipliers (matching admin multipliers)
            switch (seatType) {
                case "vip":
                    seatPrice *= 1.5; // 1.5x multiplier for VIP
                    break;
                case "premium":
                    seatPrice *= 2.0; // 2.0x multiplier for premium
                    break;
                case "disabled":
                    seatPrice = 0.0; // Free for disabled seats
                    break;
                // standard seat type uses base price (1.0x)
            }
            
            // Store seat selection with price
            seatSelections.put(seatLabel, new SeatSelection(seatLabel, seatType, seatPrice));
            
            // Set selected color (Blue)
            bg.setFill(SELECTED_COLOR);
            seatPane.setEffect(selectedEffect);
            
            // Add animation for selection
            ScaleTransition st1 = new ScaleTransition(Duration.millis(100), seatPane);
            st1.setToX(1.3);
            st1.setToY(1.3);
            
            ScaleTransition st2 = new ScaleTransition(Duration.millis(100), seatPane);
            st2.setToX(1.2);
            st2.setToY(1.2);
            st2.setDelay(Duration.millis(100));
            
            ScaleTransition st3 = new ScaleTransition(Duration.millis(100), seatPane);
            st3.setToX(1.0);
            st3.setToY(1.0);
            st3.setDelay(Duration.millis(200));
            
            st1.setOnFinished(e -> st2.play());
            st2.setOnFinished(e -> st3.play());
            st1.play();
        }
        
        updateSeatSelectionInfo();
        updateBookingSummary();
    }
    
    private VBox createSeatLegend() {
        VBox legend = new VBox(10);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(20));
        legend.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        
        Label title = new Label("Seat Legend");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        
        HBox legendItems = new HBox(20);
        legendItems.setAlignment(Pos.CENTER);
        
        legendItems.getChildren().addAll(
            createLegendItem("Standard", AVAILABLE_COLOR, "#333"),
            createLegendItem("VIP (1.5x)", VIP_COLOR, "#333"),
            createLegendItem("Premium (2.0x)", Color.GOLD, "#333"),
            createLegendItem("Selected", SELECTED_COLOR, "#333"),
            createLegendItem("Booked", BOOKED_COLOR, "#333"),
            createLegendItem("Blocked", BLOCKED_COLOR, "#333"),
            createLegendItem("Disabled", Color.LIGHTGRAY, "#333")
        );
        
        legend.getChildren().addAll(title, legendItems);
        return legend;
    }
    
    private HBox createLegendItem(String text, Color color, String textColor) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Rectangle colorBox = new Rectangle(20, 20);
        colorBox.setFill(color);
        colorBox.setArcWidth(5);
        colorBox.setArcHeight(5);
        
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textColor + ";");
        
        item.getChildren().addAll(colorBox, label);
        return item;
    }
    
    private void updateSeatSelectionInfo() {
        if (selectedSeatsLabel != null) {
            if (selectedSeats.isEmpty()) {
                selectedSeatsLabel.setText("Selected Seats: None");
            } else {
                selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", selectedSeats));
            }
            selectedSeatsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333; -fx-font-size: 14px;");
        }
        
        if (seatPriceLabel != null) {
            double totalSeatPrice = calculateSeatTotalPrice();
            seatPriceLabel.setText(String.format("Total: ETB %.2f", totalSeatPrice));
            seatPriceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 16px;");
        }
    }
    
    // ==================== FILTER METHODS ====================
    
    private void filterMovies(String searchText) {
        String category = movieCategoryFilter != null ? movieCategoryFilter.getValue() : null;
        String genre = movieGenreFilter != null ? movieGenreFilter.getValue() : null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT movie_id, title, genre, duration_minutes, rating, poster_image, price, category, description " +
                "FROM movies WHERE is_active = TRUE "
            );
            
        List<String> params = new ArrayList<>();
        
        if (searchText != null && !searchText.isEmpty()) {
            sql.append("AND (title LIKE ? OR genre LIKE ?) ");
            String searchPattern = "%" + searchText + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        if (category != null && !category.equals("All")) {
            sql.append("AND category = ? ");
            params.add(category);
        }
        
        if (genre != null && !genre.equals("All")) {
            sql.append("AND genre LIKE ? ");
            params.add("%" + genre + "%");
        }
        
        sql.append("ORDER BY title");
        
        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            pstmt.setString(i + 1, params.get(i));
        }
        
        ResultSet rs = pstmt.executeQuery();
        
        if (allMoviesTilePane != null) allMoviesTilePane.getChildren().clear();
        
        while (rs.next()) {
            Movie movie = new Movie();
            movie.setMovieId(rs.getInt("movie_id"));
            movie.setTitle(rs.getString("title"));
            movie.setGenre(rs.getString("genre"));
            movie.setDurationMinutes(rs.getInt("duration_minutes"));
            movie.setRating(rs.getString("rating"));
            movie.setPosterImage(rs.getString("poster_image"));
            movie.setPrice(rs.getDouble("price"));
            movie.setCategory(rs.getString("category"));
            movie.setDescription(rs.getString("description"));
            
            VBox movieCard = createMovieCardForTile(movie);
            if (allMoviesTilePane != null) allMoviesTilePane.getChildren().add(movieCard);
        }
        
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error filtering movies: " + e.getMessage());
    }
}

private void filterSnacks(String searchText) {
    try {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                    "s.price, s.size, s.image_url, s.calories " +
                    "FROM snack_items s " +
                    "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                    "WHERE s.is_available = TRUE " +
                    "AND (s.item_name LIKE ? OR c.category_name LIKE ?) " +
                    "ORDER BY s.item_name";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        String searchPattern = "%" + searchText + "%";
        pstmt.setString(1, searchPattern);
        pstmt.setString(2, searchPattern);
        
        ResultSet rs = pstmt.executeQuery();
        
        if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().clear();
        
        while (rs.next()) {
            Snack snack = new Snack();
            snack.setSnackId(rs.getInt("snack_id"));
            snack.setItemName(rs.getString("item_name"));
            snack.setDescription(rs.getString("description"));
            snack.setCategory(rs.getString("category_name"));
            snack.setPrice(rs.getDouble("price"));
            snack.setSize(rs.getString("size"));
            snack.setImageUrl(rs.getString("image_url"));
            snack.setCalories(rs.getInt("calories"));
            
            VBox snackCard = createSnackCardForTile(snack);
            if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().add(snackCard);
        }
        
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error filtering snacks: " + e.getMessage());
    }
}

@FXML
private void handleFilterBookings() {
    String status = bookingStatusFilterUser != null ? bookingStatusFilterUser.getValue() : null;
    LocalDate date = bookingDateFilter != null ? bookingDateFilter.getValue() : null;
    
    try {
        Connection conn = DatabaseConnection.getConnection();
        StringBuilder sql = new StringBuilder(
            "SELECT b.booking_id, m.title, s.show_date, s.show_time, " +
            "b.total_amount, b.booking_status, b.booking_date, " +
            "GROUP_CONCAT(seats.seat_label ORDER BY seats.seat_label SEPARATOR ', ') as seat_labels " +
            "FROM bookings b " +
            "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "LEFT JOIN booking_seats bs ON b.booking_id = bs.booking_id " +
            "LEFT JOIN seats ON bs.seat_id = seats.seat_id " +
            "WHERE b.user_id = ? "
        );
        
        List<Object> params = new ArrayList<>();
        params.add(userId);
        
        if (status != null && !status.equals("All")) {
            sql.append("AND b.booking_status = ? ");
            params.add(status);
        }
        
        if (date != null) {
            sql.append("AND DATE(s.show_date) = ? ");
            params.add(java.sql.Date.valueOf(date));
        }
        
        sql.append("GROUP BY b.booking_id ORDER BY b.booking_date DESC");
        
        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }
        
        ResultSet rs = pstmt.executeQuery();
        
        ObservableList<Booking> userBookings = FXCollections.observableArrayList();
        
        while (rs.next()) {
            int bookingId = rs.getInt("booking_id");
            String bookingIdStr = "#" + bookingId;
            String movie = rs.getString("title");
            String dateStr = rs.getDate("show_date").toString();
            String time = rs.getTime("show_time").toString().substring(0, 5);
            String seats = rs.getString("seat_labels");
            if (seats == null) seats = "Not assigned";
            String amount = String.format("ETB %.2f", rs.getDouble("total_amount"));
            String statusStr = rs.getString("booking_status");
            
            Booking booking = new Booking(bookingIdStr, movie, dateStr, time, seats, amount, statusStr);
            booking.setBookingIdDb(bookingId);
            userBookings.add(booking);
        }
        
        if (myBookingsTable != null) myBookingsTable.setItems(userBookings);
        
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error filtering bookings: " + e.getMessage());
    }
}

@FXML
private void handleAllSnacksFilter() {
    loadAllSnacks();
    updateSnackFilterButtons(allSnacksBtn);
}

@FXML
private void handleSnacksOnlyFilter() {
    filterSnacksByCategory("Snack");
    updateSnackFilterButtons(snacksOnlyBtn);
}

@FXML
private void handleDrinksOnlyFilter() {
    filterSnacksByCategory("Drink");
    updateSnackFilterButtons(drinksOnlyBtn);
}

@FXML
private void handleCombosFilter() {
    filterSnacksByCategory("Combo");
    updateSnackFilterButtons(combosBtn);
}

private void filterSnacksByCategory(String category) {
    try {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                    "s.price, s.size, s.image_url, s.calories " +
                    "FROM snack_items s " +
                    "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                    "WHERE s.is_available = TRUE AND c.category_name = ? " +
                    "ORDER BY s.item_name";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, category);
        ResultSet rs = pstmt.executeQuery();
        
        if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().clear();
        
        while (rs.next()) {
            Snack snack = new Snack();
            snack.setSnackId(rs.getInt("snack_id"));
            snack.setItemName(rs.getString("item_name"));
            snack.setDescription(rs.getString("description"));
            snack.setCategory(rs.getString("category_name"));
            snack.setPrice(rs.getDouble("price"));
            snack.setSize(rs.getString("size"));
            snack.setImageUrl(rs.getString("image_url"));
            snack.setCalories(rs.getInt("calories"));
            
            VBox snackCard = createSnackCardForTile(snack);
            if (snacksDrinksTilePane != null) snacksDrinksTilePane.getChildren().add(snackCard);
        }
        
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("❌ Error filtering snacks by category: " + e.getMessage());
    }
}

private void updateSnackFilterButtons(Button activeButton) {
    if (allSnacksBtn != null) allSnacksBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333;");
    if (snacksOnlyBtn != null) snacksOnlyBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333;");
    if (drinksOnlyBtn != null) drinksOnlyBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333;");
    if (combosBtn != null) combosBtn.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333;");
    
    if (activeButton != null) activeButton.setStyle("-fx-background-color: #ae2d3c; -fx-text-fill: white;");
}

// ==================== PROFILE METHODS ====================

@FXML
private void handleSaveProfile() {
    String name = profileNameField != null ? profileNameField.getText().trim() : "";
    String email = profileEmailField != null ? profileEmailField.getText().trim() : "";
    String phone = profilePhoneField != null ? profilePhoneField.getText().trim() : "";
    String password = profilePasswordField != null ? profilePasswordField.getText() : "";
    String confirmPassword = profileConfirmPasswordField != null ? profileConfirmPasswordField.getText() : "";
    
    if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
        showAlert("Validation Error", "Please fill all required fields");
        return;
    }
    
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        showAlert("Invalid Email", "Please enter a valid email address");
        return;
    }
    
    if (!phone.matches("^09[0-9]{8}$")) {
        showAlert("Invalid Phone", "Please enter a valid Ethiopian phone number (09xxxxxxxx)");
        return;
    }
    
    if (!password.isEmpty() && !password.equals(confirmPassword)) {
        showAlert("Password Mismatch", "Passwords do not match!");
        return;
    }
    
    if (!password.isEmpty() && password.length() < 6) {
        showAlert("Password Too Short", "Password must be at least 6 characters long");
        return;
    }
    
    try {
        Connection conn = DatabaseConnection.getConnection();
        
        if (password.isEmpty()) {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, profile_picture = ? WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            String[] nameParts = name.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, profileImagePath);
            pstmt.setInt(6, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                this.userFullName = name;
                updateUsernameLabel();
                updateNavProfileImage();
                showSuccessAlert("Profile Updated", "Your profile has been updated successfully!");
                loadUserProfile();
            }
            
            pstmt.close();
        } else {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, password = ?, profile_picture = ? WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            String[] nameParts = name.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, password);
            pstmt.setString(6, profileImagePath);
            pstmt.setInt(7, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                this.userFullName = name;
                updateUsernameLabel();
                updateNavProfileImage();
                showSuccessAlert("Profile Updated", "Your profile and password have been updated successfully!");
                loadUserProfile();
                if (profilePasswordField != null) profilePasswordField.clear();
                if (profileConfirmPasswordField != null) profileConfirmPasswordField.clear();
            }
            
            pstmt.close();
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Database Error", "Failed to update profile: " + e.getMessage());
    }
}

@FXML
private void handleCancelProfile() {
    loadUserProfile();
    showHomePage();
    System.out.println("❌ Cancelling profile changes...");
}

@FXML
private void handleChangeProfileImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Profile Picture");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
    );
    
    Stage stage = (Stage) changeProfilePicBtn.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);
    
    if (selectedFile != null) {
        try {
            String userDir = System.getProperty("user.dir");
            String destDir = userDir + File.separator + "profile_images";
            File destFolder = new File(destDir);
            if (!destFolder.exists()) {
                boolean created = destFolder.mkdirs();
                System.out.println("Created profile_images directory: " + created);
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "user_" + userId + "_" + timestamp + "_" + selectedFile.getName();
            String destPath = destDir + File.separator + fileName;
            
            java.nio.file.Files.copy(selectedFile.toPath(), new File(destPath).toPath(), 
                                   java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            
            profileImagePath = destPath;
            
            // Update profile page image
            Image image = new Image("file:" + destPath);
            if (profileImageView != null) {
                profileImageView.setImage(image);
            }
            
            // IMPORTANT: Update nav profile image
            updateNavProfileImage();
            
            // Save to database
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET profile_picture = ? WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, destPath);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            pstmt.close();
            
            showSuccessAlert("Profile Picture", "Profile picture updated successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Image Error", "Failed to update profile picture: " + e.getMessage());
        }
    }
}

// ==================== BOOKING MANAGEMENT ====================

@FXML
private void handleCancelBooking() {
    if (myBookingsTable == null) return;
    
    Booking selectedBooking = myBookingsTable.getSelectionModel().getSelectedItem();
    if (selectedBooking != null) {
        cancelBooking(selectedBooking);
    } else {
        showAlert("No Selection", "Please select a booking to cancel.");
    }
}

private void cancelBooking(Booking booking) {
    int bookingId = booking.getBookingIdDb();
    
    if (bookingId == 0) {
        showAlert("Error", "Invalid booking ID");
        return;
    }
    
    if ("cancelled".equalsIgnoreCase(booking.getStatus()) || 
        "completed".equalsIgnoreCase(booking.getStatus())) {
        showAlert("Cannot Cancel", 
            "This booking is already " + booking.getStatus() + " and cannot be cancelled.");
        return;
    }
    
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirm Cancel");
    confirmAlert.setHeaderText("Cancel Booking");
    confirmAlert.setContentText("Are you sure you want to cancel booking " + booking.getId() + "?\n" +
                              "A cancellation fee may apply if within 24 hours of showtime.");
    
    confirmAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false);
                
                try {
                    // Check if booking is within 2 hours of showtime
                    String checkSql = "SELECT s.show_date, s.show_time, s.showtime_id FROM bookings b " +
                                     "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                                     "WHERE b.booking_id = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setInt(1, bookingId);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (rs.next()) {
                        java.sql.Date showDate = rs.getDate("show_date");
                        String showTime = rs.getString("show_time");
                        int showtimeId = rs.getInt("showtime_id");
                        
                        LocalDateTime showDateTime = LocalDateTime.of(
                            showDate.toLocalDate(),
                            java.time.LocalTime.parse(showTime.length() == 5 ? showTime + ":00" : showTime)
                        );
                        
                        if (LocalDateTime.now().plusHours(2).isAfter(showDateTime)) {
                            showAlert("Cannot Cancel", 
                                "Cannot cancel booking within 2 hours of showtime.\n" +
                                "Showtime: " + showDate + " " + showTime);
                            return;
                        }
                        
                        // Get number of seats in this booking
                        String seatsSql = "SELECT COUNT(*) as seat_count FROM booking_seats WHERE booking_id = ?";
                        PreparedStatement seatsStmt = conn.prepareStatement(seatsSql);
                        seatsStmt.setInt(1, bookingId);
                        ResultSet seatsRs = seatsStmt.executeQuery();
                        int seatCount = 0;
                        if (seatsRs.next()) {
                            seatCount = seatsRs.getInt("seat_count");
                        }
                        seatsRs.close();
                        seatsStmt.close();
                        
                        // Update booking status
                        String updateSql = "UPDATE bookings SET booking_status = 'cancelled' WHERE booking_id = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, bookingId);
                        
                        int rowsAffected = updateStmt.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            // Free up seats by making them available again
                            String freeSeatsSql = "DELETE FROM booking_seats WHERE booking_id = ?";
                            PreparedStatement freeSeatsStmt = conn.prepareStatement(freeSeatsSql);
                            freeSeatsStmt.setInt(1, bookingId);
                            freeSeatsStmt.executeUpdate();
                            freeSeatsStmt.close();
                            
                            // Update available seats count in showtimes
                            String updateShowtimeSql = "UPDATE showtimes SET available_seats = available_seats + ? WHERE showtime_id = ?";
                            PreparedStatement updateShowtimeStmt = conn.prepareStatement(updateShowtimeSql);
                            updateShowtimeStmt.setInt(1, seatCount);
                            updateShowtimeStmt.setInt(2, showtimeId);
                            updateShowtimeStmt.executeUpdate();
                            updateShowtimeStmt.close();
                            
                            conn.commit();
                            
                            booking.setStatus("cancelled");
                            myBookingsTable.refresh();
                            
                            showSuccessAlert("Booking Cancelled", 
                                "Booking " + booking.getId() + " has been cancelled successfully.\n" +
                                "Seats have been made available again.\n" +
                                "Refund will be processed within 3-5 business days.");
                            
                            String currentFilter = bookingStatusFilterUser != null ? bookingStatusFilterUser.getValue() : null;
                            if (currentFilter != null && !currentFilter.equals("All") && !currentFilter.equals("Cancelled")) {
                                myBookingsTable.getItems().remove(booking);
                            }
                        }
                        
                        updateStmt.close();
                    }
                    
                    checkStmt.close();
                    
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Cancellation Error", "Failed to cancel booking: " + e.getMessage());
            }
        }
    });
}

@FXML
private void handleDeleteBooking() {
    if (myBookingsTable == null) return;
    
    Booking selectedBooking = myBookingsTable.getSelectionModel().getSelectedItem();
    if (selectedBooking != null) {
        deleteBooking(selectedBooking);
    } else {
        showAlert("No Selection", "Please select a booking to delete.");
    }
}

private void deleteBooking(Booking booking) {
    int bookingId = booking.getBookingIdDb();
    
    if (bookingId == 0) {
        showAlert("Error", "Invalid booking ID");
        return;
    }
    
    // Only allow deletion of cancelled or completed bookings
    if (!"cancelled".equalsIgnoreCase(booking.getStatus()) && 
        !"completed".equalsIgnoreCase(booking.getStatus())) {
        showAlert("Cannot Delete", 
            "You can only delete cancelled or completed bookings.\n" +
            "Current status: " + booking.getStatus());
        return;
    }
    
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirm Delete");
    confirmAlert.setHeaderText("Delete Booking History");
    confirmAlert.setContentText("Are you sure you want to permanently delete booking " + booking.getId() + "?\n" +
                              "This action cannot be undone. This will only remove the booking from your history,\n" +
                              "not from the cinema's records.");
    
    confirmAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // For now, just remove from the table view
            myBookingsTable.getItems().remove(booking);
            
            showSuccessAlert("Booking Deleted", 
                "Booking " + booking.getId() + " has been removed from your history.");
        }
    });
}

@FXML
private void handlePrintTicket() {
    if (myBookingsTable == null) return;
    
    Booking selectedBooking = myBookingsTable.getSelectionModel().getSelectedItem();
    if (selectedBooking != null) {
        printExistingTicket(selectedBooking);
    } else {
        showAlert("No Selection", "Please select a booking to print.");
    }
}

@FXML
private void handleViewBookingDetails() {
    if (myBookingsTable == null) return;
    
    Booking selectedBooking = myBookingsTable.getSelectionModel().getSelectedItem();
    if (selectedBooking != null) {
        viewBookingDetails(selectedBooking);
    } else {
        showAlert("No Selection", "Please select a booking to view details.");
    }
}

// ==================== UI CARD CREATION METHODS ====================

private VBox createMovieCard(Movie movie) {
    VBox card = new VBox(10);
    card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; " +
                 "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 15;");
    card.setPrefWidth(200);
    card.setPrefHeight(320);
    
    setupCardHover(card);
    
    StackPane posterContainer = new StackPane();
    posterContainer.setStyle("-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
    
    ImageView poster = new ImageView();
    poster.setFitWidth(170);
    poster.setFitHeight(220);
    poster.setPreserveRatio(true);
    poster.setStyle("-fx-background-radius: 10;");
    
    if (movie.getPosterImage() != null && !movie.getPosterImage().isEmpty()) {
        try {
            File file = new File(movie.getPosterImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                poster.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    if (movie.getRating() != null && !movie.getRating().isEmpty()) {
        Label ratingBadge = new Label(movie.getRating());
        ratingBadge.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold; " +
                           "-fx-padding: 3 8 3 8; -fx-background-radius: 10; -fx-font-size: 12px;");
        StackPane.setAlignment(ratingBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(ratingBadge, new Insets(5));
        posterContainer.getChildren().addAll(poster, ratingBadge);
    } else {
        posterContainer.getChildren().add(poster);
    }
    
    Label titleLabel = new Label(movie.getTitle());
    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
    titleLabel.setWrapText(true);
    
    String duration = String.format("%dh %dm", movie.getDurationMinutes() / 60, movie.getDurationMinutes() % 60);
    Label infoLabel = new Label(movie.getGenre() + " • " + duration);
    infoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
    
    Label priceLabel = new Label(String.format("ETB %.2f", movie.getPrice()));
    priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 16px;");
    
    Button bookBtn = new Button("Book Now");
    bookBtn.setStyle("-fx-background-color: linear-gradient(to right, #ae2d3c, #d9534f); " +
                    "-fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 20;");
    bookBtn.setCursor(Cursor.HAND);
    bookBtn.setPrefWidth(170);
    setupButtonHover(bookBtn);
    bookBtn.setOnAction(e -> {
        showBookTicketsPage();
        if (bookMovieSelect != null) {
            bookMovieSelect.setValue(movie.getTitle());
        }
    });
    
    posterContainer.setOnMouseClicked(e -> {
        if (e.getClickCount() == 1) {
            showMovieDetailsDialog(movie);
        }
    });
    
    card.getChildren().addAll(posterContainer, titleLabel, infoLabel, priceLabel, bookBtn);
    return card;
}

private VBox createMovieCardForTile(Movie movie) {
    VBox card = new VBox(10);
    card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; " +
                 "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 15;");
    card.setPrefWidth(240);
    card.setPrefHeight(380);
    
    setupCardHover(card);
    
    StackPane posterContainer = new StackPane();
    posterContainer.setStyle("-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
    
    ImageView poster = new ImageView();
    poster.setFitWidth(210);
    poster.setFitHeight(280);
    poster.setPreserveRatio(true);
    poster.setStyle("-fx-background-radius: 10;");
    
    if (movie.getPosterImage() != null && !movie.getPosterImage().isEmpty()) {
        try {
            File file = new File(movie.getPosterImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                poster.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    if (movie.getRating() != null && !movie.getRating().isEmpty()) {
        Label ratingBadge = new Label(movie.getRating());
        ratingBadge.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold; " +
                           "-fx-padding: 3 8 3 8; -fx-background-radius: 10; -fx-font-size: 12px;");
        StackPane.setAlignment(ratingBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(ratingBadge, new Insets(5));
        posterContainer.getChildren().addAll(poster, ratingBadge);
    } else {
        posterContainer.getChildren().add(poster);
    }
    
    Label titleLabel = new Label(movie.getTitle());
    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
    titleLabel.setWrapText(true);
    
    String duration = String.format("%dh %dm", movie.getDurationMinutes() / 60, movie.getDurationMinutes() % 60);
    Label infoLabel = new Label(movie.getGenre() + " • " + duration);
    infoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
    
    Label priceLabel = new Label(String.format("ETB %.2f", movie.getPrice()));
    priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 18px;");
    
    Label categoryLabel = new Label(movie.getCategory());
    categoryLabel.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333; -fx-padding: 3 8 3 8; -fx-background-radius: 10;");
    
    Button bookBtn = new Button("Book Tickets");
    bookBtn.setStyle("-fx-background-color: linear-gradient(to right, #ae2d3c, #d9534f); " +
                    "-fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 210; -fx-background-radius: 20;");
    bookBtn.setCursor(Cursor.HAND);
    setupButtonHover(bookBtn);
    bookBtn.setOnAction(e -> {
        showBookTicketsPage();
        if (bookMovieSelect != null) {
            bookMovieSelect.setValue(movie.getTitle());
        }
    });
    
    posterContainer.setOnMouseClicked(e -> {
        if (e.getClickCount() == 1) {
            showMovieDetailsDialog(movie);
        }
    });
    
    card.setOnMouseClicked(e -> {
        if (e.getClickCount() == 2) {
            showMovieDetailsDialog(movie);
        }
    });
    
    card.getChildren().addAll(posterContainer, titleLabel, infoLabel, priceLabel, categoryLabel, bookBtn);
    return card;
}

private VBox createSnackCard(Snack snack, boolean forHome) {
    VBox card = new VBox(10);
    card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; " +
                 "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 15;");
    card.setPrefWidth(180);
    card.setPrefHeight(240);
    
    setupCardHover(card);
    
    StackPane imageContainer = new StackPane();
    imageContainer.setStyle("-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
    
    ImageView imageView = new ImageView();
    imageView.setFitWidth(150);
    imageView.setFitHeight(120);
    imageView.setPreserveRatio(true);
    imageView.setStyle("-fx-background-radius: 10;");
    
    if (snack.getImageUrl() != null && !snack.getImageUrl().isEmpty()) {
        try {
            File file = new File(snack.getImageUrl());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    if (snack.getCategory() != null) {
        Label categoryBadge = new Label(snack.getCategory());
        categoryBadge.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; " +
                             "-fx-padding: 3 8 3 8; -fx-background-radius: 10; -fx-font-size: 10px;");
        StackPane.setAlignment(categoryBadge, Pos.TOP_LEFT);
        StackPane.setMargin(categoryBadge, new Insets(5));
        imageContainer.getChildren().addAll(imageView, categoryBadge);
    } else {
        imageContainer.getChildren().add(imageView);
    }
    
    Label nameLabel = new Label(snack.getItemName());
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
    nameLabel.setWrapText(true);
    
    Label infoLabel = new Label(snack.getSize() + " • " + snack.getCategory());
    infoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
    
    Label priceLabel = new Label(String.format("ETB %.2f", snack.getPrice()));
    priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 14px;");
    
    Button addBtn = new Button(forHome ? "View" : "Add");
    addBtn.setStyle("-fx-background-color: " + (forHome ? "#007bff" : "#28a745") + 
                   "; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 20;");
    addBtn.setCursor(Cursor.HAND);
    setupButtonHover(addBtn);
    addBtn.setOnAction(e -> {
        if (forHome) {
            showSnacksDrinksPage();
        } else {
            addToCart(snack);
        }
    });
    
    imageContainer.setOnMouseClicked(e -> {
        if (e.getClickCount() == 1) {
            showSnackDetailsDialog(snack);
        }
    });
    
    card.getChildren().addAll(imageContainer, nameLabel, infoLabel, priceLabel, addBtn);
    return card;
}

private VBox createSnackCardForTile(Snack snack) {
    VBox card = new VBox(10);
    card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; " +
                 "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 15;");
    card.setPrefWidth(240);
    card.setPrefHeight(320);
    
    setupCardHover(card);
    
    StackPane imageContainer = new StackPane();
    imageContainer.setStyle("-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
    
    ImageView imageView = new ImageView();
    imageView.setFitWidth(210);
    imageView.setFitHeight(160);
    imageView.setPreserveRatio(true);
    imageView.setStyle("-fx-background-radius: 10;");
    
    if (snack.getImageUrl() != null && !snack.getImageUrl().isEmpty()) {
        try {
            File file = new File(snack.getImageUrl());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    Label categoryBadge = new Label(snack.getCategory());
    categoryBadge.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; " +
                         "-fx-padding: 3 8 3 8; -fx-background-radius: 10; -fx-font-size: 12px;");
    StackPane.setAlignment(categoryBadge, Pos.TOP_LEFT);
    StackPane.setMargin(categoryBadge, new Insets(5));
    imageContainer.getChildren().addAll(imageView, categoryBadge);
    
    Label nameLabel = new Label(snack.getItemName());
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
    nameLabel.setWrapText(true);
    
    Label descLabel = new Label(snack.getDescription());
    descLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
    descLabel.setWrapText(true);
    descLabel.setMaxHeight(40);
    
    HBox infoBox = new HBox(10);
    infoBox.setAlignment(Pos.CENTER_LEFT);
    
    Label sizeLabel = new Label(snack.getSize());
    sizeLabel.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333; -fx-padding: 3 8 3 8; " +
                      "-fx-background-radius: 10; -fx-font-size: 12px;");
    
    if (snack.getCalories() > 0) {
        Label calLabel = new Label(snack.getCalories() + " cal");
        calLabel.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 3 8 3 8; " +
                        "-fx-background-radius: 10; -fx-font-size: 12px;");
        infoBox.getChildren().addAll(sizeLabel, calLabel);
    } else {
        infoBox.getChildren().add(sizeLabel);
    }
    
    HBox priceBox = new HBox();
    priceBox.setAlignment(Pos.CENTER_LEFT);
    
    Label priceLabel = new Label(String.format("ETB %.2f", snack.getPrice()));
    priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 18px;");
    
    Button addBtn = new Button("Add to Cart");
    addBtn.setStyle("-fx-background-color: linear-gradient(to right, #28a745, #20c997); " +
                   "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-pref-width: 210;");
    addBtn.setCursor(Cursor.HAND);
    setupButtonHover(addBtn);
    addBtn.setOnAction(e -> {
        addToCart(snack);
        ScaleTransition st1 = new ScaleTransition(Duration.millis(100), addBtn);
        st1.setToX(0.9);
        st1.setToY(0.9);
        
        ScaleTransition st2 = new ScaleTransition(Duration.millis(100), addBtn);
        st2.setToX(1.1);
        st2.setToY(1.1);
        st2.setDelay(Duration.millis(100));
        
        ScaleTransition st3 = new ScaleTransition(Duration.millis(100), addBtn);
        st3.setToX(1.0);
        st3.setToY(1.0);
        st3.setDelay(Duration.millis(200));
        
        st1.setOnFinished(e1 -> st2.play());
        st2.setOnFinished(e2 -> st3.play());
        st1.play();
    });
    
    priceBox.getChildren().add(priceLabel);
    
    imageContainer.setOnMouseClicked(e -> {
        if (e.getClickCount() == 1) {
            showSnackDetailsDialog(snack);
        }
    });
    
    card.setOnMouseClicked(e -> {
        if (e.getClickCount() == 2) {
            showSnackDetailsDialog(snack);
        }
    });
    
    card.getChildren().addAll(imageContainer, nameLabel, descLabel, infoBox, priceBox, addBtn);
    return card;
}

private VBox createSnackCardForSelection(Snack snack) {
    VBox card = new VBox(10);
    card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
    card.setPrefWidth(200);
    card.setPrefHeight(250);
    
    setupCardHover(card);
    
    ImageView imageView = new ImageView();
    imageView.setFitWidth(170);
    imageView.setFitHeight(120);
    imageView.setPreserveRatio(true);
    imageView.setStyle("-fx-background-radius: 5;");
    
    if (snack.getImageUrl() != null && !snack.getImageUrl().isEmpty()) {
        try {
            File file = new File(snack.getImageUrl());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    Label nameLabel = new Label(snack.getItemName());
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");
    nameLabel.setWrapText(true);
    
    Label infoLabel = new Label(snack.getSize() + " • " + snack.getCategory());
    infoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
    
    Label priceLabel = new Label(String.format("ETB %.2f", snack.getPrice()));
    priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c; -fx-font-size: 14px;");
    
    Button addBtn = new Button("Add to Cart");
    addBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 20;");
    addBtn.setCursor(Cursor.HAND);
    setupButtonHover(addBtn);
    addBtn.setOnAction(e -> addToCart(snack));
    
    imageView.setOnMouseClicked(e -> {
        if (e.getClickCount() == 1) {
            showSnackDetailsDialog(snack);
        }
    });
    
    card.getChildren().addAll(imageView, nameLabel, infoLabel, priceLabel, addBtn);
    return card;
}

private void showMovieDetailsDialog(Movie movie) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Movie Details");
    dialog.setHeaderText(movie.getTitle());
    
    ButtonType bookButton = new ButtonType("Book Tickets", ButtonBar.ButtonData.OK_DONE);
    ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(bookButton, closeButton);
    
    VBox content = new VBox(20);
    content.setPadding(new Insets(20));
    content.setStyle("-fx-background-color: white;");
    
    HBox topSection = new HBox(20);
    topSection.setAlignment(Pos.CENTER_LEFT);
    
    ImageView poster = new ImageView();
    poster.setFitWidth(200);
    poster.setFitHeight(300);
    poster.setPreserveRatio(true);
    
    if (movie.getPosterImage() != null && !movie.getPosterImage().isEmpty()) {
        try {
            File file = new File(movie.getPosterImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                poster.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    VBox details = new VBox(10);
    details.setPrefWidth(300);
    
    Label titleLabel = new Label(movie.getTitle());
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e242e;");
    
    Label genreLabel = new Label("Genre: " + movie.getGenre());
    genreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
    
    String duration = String.format("%dh %dm", movie.getDurationMinutes() / 60, movie.getDurationMinutes() % 60);
    Label durationLabel = new Label("Duration: " + duration);
    durationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
    
    Label ratingLabel = new Label("Rating: " + movie.getRating());
    ratingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
    
    Label priceLabel = new Label("Price: " + String.format("ETB %.2f", movie.getPrice()));
    priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ae2d3c;");
    
    if (movie.getCategory() != null) {
        Label categoryLabel = new Label("Category: " + movie.getCategory());
        categoryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        details.getChildren().add(categoryLabel);
    }
    
    if (movie.getReleaseDate() != null) {
        Label releaseLabel = new Label("Release Date: " + movie.getReleaseDate());
        releaseLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        details.getChildren().add(releaseLabel);
    }
    
    TextArea descriptionArea = new TextArea(movie.getDescription());
    descriptionArea.setEditable(false);
    descriptionArea.setWrapText(true);
    descriptionArea.setPrefHeight(100);
    descriptionArea.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
    
    details.getChildren().addAll(titleLabel, genreLabel, durationLabel, ratingLabel, priceLabel, descriptionArea);
    
    topSection.getChildren().addAll(poster, details);
    
    VBox showtimesSection = new VBox(10);
    showtimesSection.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-border-radius: 5;");
    
    Label showtimesTitle = new Label("Available Showtimes");
    showtimesTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
    
    VBox showtimesList = new VBox(10);
    showtimesList.setPrefHeight(150);
    
    try {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT s.showtime_id, s.show_date, s.show_time, h.hall_name, s.available_seats " +
                    "FROM showtimes s " +
                    "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                    "WHERE s.movie_id = ? AND s.is_active = TRUE AND s.show_date >= CURDATE() " +
                    "ORDER BY s.show_date, s.show_time";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, movie.getMovieId());
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            HBox showtimeItem = new HBox(15);
            showtimeItem.setAlignment(Pos.CENTER_LEFT);
            showtimeItem.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5;");
            
            Label dateLabel = new Label(rs.getDate("show_date").toString());
            dateLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 100; -fx-text-fill: #333;");
            
            Label timeLabel = new Label(rs.getTime("show_time").toString().substring(0, 5));
            timeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
            
            Label hallLabel = new Label("Hall: " + rs.getString("hall_name"));
            hallLabel.setStyle("-fx-text-fill: #333;");
            
            Label seatsLabel = new Label("Seats: " + rs.getInt("available_seats"));
            seatsLabel.setStyle("-fx-text-fill: " + (rs.getInt("available_seats") > 10 ? "#28a745" : "#dc3545") + ";");
            
            Label priceShowtimeLabel = new Label(String.format("ETB %.2f", movie.getPrice()));
            priceShowtimeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ae2d3c;");
            
            showtimeItem.getChildren().addAll(dateLabel, timeLabel, hallLabel, seatsLabel, priceShowtimeLabel);
            showtimesList.getChildren().add(showtimeItem);
        }
        
        rs.close();
        pstmt.close();
        
    } catch (Exception e) {
        e.printStackTrace();
        Label noShowtimes = new Label("No showtimes available");
        noShowtimes.setStyle("-fx-text-fill: #666;");
        showtimesList.getChildren().add(noShowtimes);
    }
    
    showtimesSection.getChildren().addAll(showtimesTitle, showtimesList);
    
    content.getChildren().addAll(topSection, showtimesSection);
    dialog.getDialogPane().setContent(content);
    
    Button bookBtn = (Button) dialog.getDialogPane().lookupButton(bookButton);
    if (bookBtn != null) {
        bookBtn.setOnAction(e -> {
            showBookTicketsPage();
            if (bookMovieSelect != null) {
                bookMovieSelect.setValue(movie.getTitle());
            }
            dialog.close();
        });
    }
    
    dialog.showAndWait();
}

private void showSnackDetailsDialog(Snack snack) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Snack Details");
    dialog.setHeaderText(snack.getItemName());
    
    ButtonType addButton = new ButtonType("Add to Cart", ButtonBar.ButtonData.OK_DONE);
    ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(addButton, closeButton);
    
    VBox content = new VBox(20);
    content.setPadding(new Insets(20));
    content.setStyle("-fx-background-color: white;");
    
    HBox topSection = new HBox(20);
    topSection.setAlignment(Pos.CENTER_LEFT);
    
    ImageView imageView = new ImageView();
    imageView.setFitWidth(200);
    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);
    
    if (snack.getImageUrl() != null && !snack.getImageUrl().isEmpty()) {
        try {
            File file = new File(snack.getImageUrl());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            // Use default image
        }
    }
    
    VBox details = new VBox(10);
    details.setPrefWidth(300);
    
    Label nameLabel = new Label(snack.getItemName());
    nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e242e;");
    
    Label categoryLabel = new Label("Category: " + snack.getCategory());
    categoryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
    
    Label sizeLabel = new Label("Size: " + snack.getSize());
    sizeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
    
    Label priceLabel = new Label("Price: " + String.format("ETB %.2f", snack.getPrice()));
    priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ae2d3c;");
    
    if (snack.getCalories() > 0) {
        Label calLabel = new Label("Calories: " + snack.getCalories() + " cal");
        calLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        details.getChildren().add(calLabel);
    }
    
    TextArea descriptionArea = new TextArea(snack.getDescription());
    descriptionArea.setEditable(false);
    descriptionArea.setWrapText(true);
    descriptionArea.setPrefHeight(100);
    descriptionArea.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
    
    details.getChildren().addAll(nameLabel, categoryLabel, sizeLabel, priceLabel, descriptionArea);
    
    topSection.getChildren().addAll(imageView, details);
    
    content.getChildren().add(topSection);
    dialog.getDialogPane().setContent(content);
    
    Button addBtn = (Button) dialog.getDialogPane().lookupButton(addButton);
    if (addBtn != null) {
        addBtn.setOnAction(e -> {
            addToCart(snack);
            dialog.close();
        });
    }
    
    dialog.showAndWait();
}

// ==================== INNER CLASSES ====================

public static class CartItemCell extends ListCell<CartItem> {
    private final HBox hbox = new HBox(10);
    private final Label nameLabel = new Label();
    private final Label priceLabel = new Label();
    private final Label quantityLabel = new Label();
    private final Button removeBtn = new Button("×");
    
    public CartItemCell() {
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5));
        
        nameLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 150; -fx-text-fill: #333;");
        priceLabel.setStyle("-fx-text-fill: #ae2d3c; -fx-min-width: 80;");
        quantityLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 30; -fx-text-fill: #333;");
        removeBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        removeBtn.setCursor(Cursor.HAND);
        removeBtn.setPrefSize(25, 25);
        
        removeBtn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), removeBtn);
            st.setToX(1.2);
            st.setToY(1.2);
            st.play();
        });
        
        removeBtn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), removeBtn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        hbox.getChildren().addAll(nameLabel, quantityLabel, priceLabel, removeBtn);
        
        removeBtn.setOnAction(e -> {
            CartItem item = getItem();
            if (item != null) {
                getListView().getItems().remove(item);
            }
        });
    }
    
    @Override
    protected void updateItem(CartItem item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            nameLabel.setText(item.getSnack().getItemName() + " (" + item.getSnack().getSize() + ")");
            quantityLabel.setText("x" + item.getQuantity());
            priceLabel.setText(String.format("ETB %.2f", item.getTotalPrice()));
            setGraphic(hbox);
        }
    }
}

// ==================== DATA MODEL CLASSES ====================

public static class Movie {
    private int movieId;
    private String title;
    private String genre;
    private int durationMinutes;
    private String rating;
    private String posterImage;
    private double price;
    private LocalDate releaseDate;
    private String category;
    private String description;
    
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getPosterImage() { return posterImage; }
    public void setPosterImage(String posterImage) { this.posterImage = posterImage; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

public static class Snack {
    private int snackId;
    private String itemName;
    private String description;
    private String category;
    private double price;
    private String size;
    private String imageUrl;
    private int calories;
    
    public int getSnackId() { return snackId; }
    public void setSnackId(int snackId) { this.snackId = snackId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
}

public static class Booking {
    private final SimpleStringProperty id;
    private final SimpleStringProperty movie;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty seats;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty status;
    
    private int bookingIdDb;
    
    public Booking(String id, String movie, String date, String time, 
                  String seats, String amount, String status) {
        this.id = new SimpleStringProperty(id);
        this.movie = new SimpleStringProperty(movie);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.seats = new SimpleStringProperty(seats);
        this.amount = new SimpleStringProperty(amount);
        this.status = new SimpleStringProperty(status);
        
        if (id.startsWith("#")) {
            try {
                this.bookingIdDb = Integer.parseInt(id.substring(1));
            } catch (NumberFormatException e) {
                this.bookingIdDb = 0;
            }
        } else {
            try {
                this.bookingIdDb = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                this.bookingIdDb = 0;
            }
        }
    }
    
    public String getId() { return id.get(); }
    public String getMovie() { return movie.get(); }
    public String getDate() { return date.get(); }
    public String getTime() { return time.get(); }
    public String getSeats() { return seats.get(); }
    public String getAmount() { return amount.get(); }
    public String getStatus() { return status.get(); }
    public int getBookingIdDb() { return bookingIdDb; }
    
    public void setBookingIdDb(int bookingIdDb) {
        this.bookingIdDb = bookingIdDb;
    }
    
    public void setStatus(String newStatus) {
        this.status.set(newStatus);
    }
}

public static class CartItem {
    private Snack snack;
    private int quantity;
    
    public CartItem(Snack snack, int quantity) {
        this.snack = snack;
        this.quantity = quantity;
    }
    
    public Snack getSnack() { return snack; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return snack.getPrice() * quantity; }
}

private static class SeatInfo {
    String label;
    String type;
    boolean isBlocked;
    boolean isBooked;
    char row;
    int col;
    double priceMultiplier = 1.0; // ADD THIS LINE - default to 1.0
    
    // Optional: Add constructor
    SeatInfo() {}
}
}