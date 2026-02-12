package application;

import util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.fxml.FXMLLoader;

public class Admin_DashboardController implements Initializable {
    
    // Navigation buttons
    @FXML private Button homeBtn, moviesBtn, snacksDrinksBtn, showtimesBtn;
    @FXML private Button bookingsBtn, customersBtn, seatsBtn, contentBtn, logoutBtn;
    @FXML private Button closeBtn, minimizeBtn, maximizeBtn;
    
    // Dashboard forms
    @FXML private ScrollPane dashboardScrollPane, moviesScrollPane, snacksScrollPane;
    @FXML private ScrollPane showtimesScrollPane, bookingsScrollPane, customersScrollPane;
    @FXML private ScrollPane seatsScrollPane, contentScrollPane;
    
    // Movie management components
    @FXML private TextField movieTitleField, movieGenreField, movieDurationField;
    @FXML private TextField movieDirectorField, movieCastField, moviePriceField;
    @FXML private TextField movieRatingField, movieTrailerField, searchMovieField;
    @FXML private TextArea movieDescriptionArea;
    @FXML private ComboBox<String> movieCategoryBox;
    @FXML private DatePicker movieReleaseDate;
    @FXML private TableView<Movie> movieTableView;
    
    // Movie Table Columns
    @FXML private TableColumn<Movie, Integer> movieIdColumn;
    @FXML private TableColumn<Movie, String> movieTitleColumn;
    @FXML private TableColumn<Movie, String> movieCategoryColumn;
    @FXML private TableColumn<Movie, String> movieGenreColumn;
    @FXML private TableColumn<Movie, Integer> movieDurationColumn;
    @FXML private TableColumn<Movie, Double> moviePriceColumn;
    @FXML private TableColumn<Movie, String> movieStatusColumn;
    
    // IMAGE COMPONENTS FOR MOVIES
    @FXML private ImageView movieImageView;
    @FXML private Button importMovieImageBtn;
    @FXML private Button addMovieBtn, updateMovieBtn, deleteMovieBtn, clearMovieBtn;
    
    // SNACKS COMPONENTS
    @FXML private ImageView snackImageView;
    @FXML private Button importSnackImageBtn;
    @FXML private ComboBox<String> snackTypeBox;
    @FXML private TextField snackNameField, snackSizeField, snackPriceField;
    @FXML private TextArea snackDescriptionArea;
    @FXML private TextField snackCaloriesField;
    @FXML private CheckBox snackAvailableCheck;
    @FXML private Button addSnackBtn, updateSnackBtn, deleteSnackBtn, clearSnackBtn;
    @FXML private TableView<Snack> snackTableView;
    
    // Snack Table Columns
    @FXML private TableColumn<Snack, Integer> snackIdColumn;
    @FXML private TableColumn<Snack, String> snackNameColumn;
    @FXML private TableColumn<Snack, String> snackTypeColumn;
    @FXML private TableColumn<Snack, String> snackSizeColumn;
    @FXML private TableColumn<Snack, Double> snackPriceColumn;
    @FXML private TableColumn<Snack, String> snackAvailableColumn;
    @FXML private TableColumn<Snack, Integer> snackCaloriesColumn;
    @FXML private TextField searchSnackField;
    
    // CONTENT MANAGEMENT COMPONENTS
    @FXML private ImageView aboutUsImageView;
    @FXML private Button importAboutUsImageBtn;
    @FXML private TextField cinemaNameField, cinemaAddressField, cinemaPhoneField;
    @FXML private TextField cinemaEmailField, cinemaHoursField;
    @FXML private TextArea cinemaDescriptionArea;
    @FXML private Button saveAboutUsBtn;
    
    // FAQ Components
    @FXML private TextField faqQuestionField;
    @FXML private TextArea faqAnswerArea;
    @FXML private Button addFaqBtn;
    @FXML private TableView<FAQ> faqTableView;
    @FXML private TableColumn<FAQ, Integer> faqIdColumn;
    @FXML private TableColumn<FAQ, String> faqQuestionColumn;
    @FXML private TableColumn<FAQ, String> faqAnswerColumn;
    @FXML private TableColumn<FAQ, String> faqActionColumn;
    
    // Help Components
    @FXML private TextField helpPhoneField;
    @FXML private TextField helpEmailField;
    @FXML private TextArea helpInstructionsArea;
    @FXML private Button saveHelpBtn;
    
    // SEAT MANAGEMENT COMPONENTS
    @FXML private TextField seatRowsField;
    @FXML private TextField seatColumnsField;
    @FXML private TextField seatPriceField;
    @FXML private ComboBox<String> seatHallBox;
    @FXML private Button generateSeatsBtn;
    @FXML private GridPane seatGridPane;
    @FXML private Button saveSeatsBtn;
    @FXML private Button blockSeatBtn;
    @FXML private Button unblockSeatBtn;
    @FXML private Button resetSeatsBtn;
    @FXML private TableView<SeatData> seatTableView;
    @FXML private TableColumn<SeatData, Integer> seatIdColumn;
    @FXML private TableColumn<SeatData, String> seatLabelColumn;
    @FXML private TableColumn<SeatData, String> seatRowColumn;
    @FXML private TableColumn<SeatData, Integer> seatNumberColumn;
    @FXML private TableColumn<SeatData, String> seatTypeColumn;
    @FXML private TableColumn<SeatData, String> seatStatusColumn;
    @FXML private TableColumn<SeatData, Double> seatPriceColumn;
    @FXML private ComboBox<String> seatHallBox2;
    @FXML private Button loadSeat1;
    @FXML private Button updateHall;
    @FXML private Button deleteSeat;
    
    // NEW SEAT CONFIGURATION FIELDS FOR HALL UPDATE
    @FXML private TextField newSeatRowsField;
    @FXML private TextField newSeatColumnsField;
    @FXML private ComboBox<String> newSeatTypeBox;
    @FXML private CheckBox keepExistingBlockedCheck;
    
    // DASHBOARD COMPONENTS
    @FXML private Label totalSalesLabel, ticketsSoldLabel, activeMoviesLabel, totalCustomersLabel;
    @FXML private Label usernameLabel;
    
    // DASHBOARD CHART COMPONENTS
    @FXML private javafx.scene.chart.BarChart<String, Number> salesChart;
    @FXML private TableView<RecentBooking> recentBookingsTable;
    @FXML private TableColumn<RecentBooking, Integer> bookingIdColumn;
    @FXML private TableColumn<RecentBooking, String> bookingMovieColumn;
    @FXML private TableColumn<RecentBooking, String> bookingTimeColumn;
    @FXML private Button viewAllBookingsBtn;
    
    // SHOWTIME MANAGEMENT COMPONENTS
    @FXML private ComboBox<String> showtimeMovieBox;
    @FXML private DatePicker showtimeDate;
    @FXML private TextField showtimeTimeField;
    @FXML private TextField showtimeHallField;
    @FXML private TextField showtimePriceField;
    @FXML private TextArea showtimeNotesArea;
    @FXML private TableView<Showtime> showtimeTableView;
    @FXML private TableColumn<Showtime, Integer> showtimeIdColumn;
    @FXML private TableColumn<Showtime, String> showtimeMovieColumn;
    @FXML private TableColumn<Showtime, String> showtimeDateColumn;
    @FXML private TableColumn<Showtime, String> showtimeTimeColumn;
    @FXML private TableColumn<Showtime, String> showtimeHallColumn;
    @FXML private TableColumn<Showtime, Double> showtimePriceColumn;
    @FXML private TableColumn<Showtime, Integer> showtimeSeatsColumn;
    @FXML private DatePicker showtimeFilterDate;
    @FXML private ComboBox<String> showtimeFilterMovie;
    @FXML private Button addShowtimeBtn, updateShowtimeBtn, deleteShowtimeBtn, filterShowtimesBtn;
    
    // CUSTOMER MANAGEMENT COMPONENTS
    @FXML private TextField customerSearchField;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerEmailColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerJoinDateColumn;
    @FXML private TableColumn<Customer, Integer> customerBookingsColumn;
    @FXML private TableColumn<Customer, Double> customerTotalSpentColumn;
    @FXML private TableColumn<Customer, String> customerStatusColumn;
    @FXML private Button viewCustomerBtn, editCustomerBtn, deactivateCustomerBtn, exportCustomersBtn;
    
    // BOOKING MANAGEMENT COMPONENTS
    @FXML private DatePicker bookingFilterDate;
    @FXML private TextField bookingFilterId;
    @FXML private ComboBox<String> bookingStatusFilter;
    @FXML private Button filterBookingsBtn, exportBookingsBtn;
    @FXML private TableView<Booking> bookingTableView;
    @FXML private TableColumn<Booking, Integer> bookingIdColumn2;
    @FXML private TableColumn<Booking, String> bookingCustomerColumn;
    @FXML private TableColumn<Booking, String> bookingMovieColumn2;
    @FXML private TableColumn<Booking, String> bookingDateColumn;
    @FXML private TableColumn<Booking, String> bookingTimeColumn2;
    @FXML private TableColumn<Booking, String> bookingSeatsColumn;
    @FXML private TableColumn<Booking, Double> bookingTotalColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    @FXML private TableColumn<Booking, String> bookingPaymentColumn;
    @FXML private Button confirmBookingBtn, cancelBookingBtn, viewBookingDetailsBtn, sendBookingSMSBtn;
    
    // User info
    private int adminId;
    private String adminName;
    private String selectedMovieImagePath = "";
    private String selectedSnackImagePath = "";
    private String selectedAboutUsImagePath = "";
    
    // Seat selection variables
    private ObservableList<SeatButton> seatButtons = FXCollections.observableArrayList();
    private ObservableList<SeatData> seatDataList = FXCollections.observableArrayList();
    private Map<String, Integer> hallMap = new HashMap<>(); // hall name -> hall id
    private SeatData selectedSeatData = null;
    
    // Current hall information for update
    private String currentHallName = "";
    private int currentHallId = -1;
    private int currentSeatRows = 0;
    private int currentSeatCols = 0;
    
    // Time formatter for showtimes
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Dashboard data
    private ObservableList<RecentBooking> recentBookings = FXCollections.observableArrayList();
    
    // For animations
    private DropShadow hoverEffect = new DropShadow(15, Color.web("#4a90e2"));
    private Glow glowEffect = new Glow(0.3);
    private DropShadow cardShadow = new DropShadow(20, Color.color(0, 0, 0, 0.3));
    
    // Price multipliers for seat types
    private static final Map<String, Double> SEAT_MULTIPLIERS = Map.of(
        "standard", 1.0,
        "vip", 1.5,
        "premium", 2.0,
        "disabled", 0.0
    );
    
    // VIP seat configuration
    private int vipRows = 2; // First 2 rows are VIP
    private int premiumRows = 1; // Row after VIP is Premium
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Show dashboard first
            showForm(dashboardScrollPane);
            animateFormTransition(dashboardScrollPane);
            
            setupNavigation();
            initializeMovieCategoryBox();
            initializeSnackTypeBox();
            initializeSeatHallBoxes();
            initializeShowtimeComponents();
            setupImageButtons();
            setupTableColumns();
            setupShowtimeTableColumns();
            setupDashboardChart();
            setupRecentBookingsTable();
            loadDashboardStats();
            loadRecentBookings();
            loadChartData();
            
            // Load initial data
            loadMovies();
            loadSnacks();
            loadContent();
            loadFAQs();
            loadHelpContent();
            
            // Initialize search functionality
            initializeSearchFunctionality();
            
            // Setup seat table selection listener
            setupSeatSelectionListener();
            
            // Setup hover effects for navigation buttons
            setupHoverEffects();
            
            // Setup animations for dashboard cards
            setupDashboardAnimations();
            
            // Initialize seat form components
            initializeSeatFormComponents();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setAdminInfo(int adminId, String firstName) {
        this.adminId = adminId;
        this.adminName = firstName;
        if (usernameLabel != null) {
            usernameLabel.setText("Admin: " + firstName);
        }
    }
    
    private void setupNavigation() {
        homeBtn.setOnAction(e -> {
            showForm(dashboardScrollPane);
            animateFormTransition(dashboardScrollPane);
            loadDashboardStats();
            loadRecentBookings();
            loadChartData();
        });
        moviesBtn.setOnAction(e -> {
            showForm(moviesScrollPane);
            animateFormTransition(moviesScrollPane);
            loadMovies();
        });
        snacksDrinksBtn.setOnAction(e -> {
            showForm(snacksScrollPane);
            animateFormTransition(snacksScrollPane);
            loadSnacks();
        });
        showtimesBtn.setOnAction(e -> {
            showForm(showtimesScrollPane);
            animateFormTransition(showtimesScrollPane);
            loadShowtimes();
        });
        bookingsBtn.setOnAction(e -> {
            showForm(bookingsScrollPane);
            animateFormTransition(bookingsScrollPane);
            loadBookings();
        });
        customersBtn.setOnAction(e -> {
            showForm(customersScrollPane);
            animateFormTransition(customersScrollPane);
            loadCustomers();
        });
        seatsBtn.setOnAction(e -> {
            showForm(seatsScrollPane);
            animateFormTransition(seatsScrollPane);
            initializeSeatHallBoxes(); // Reload halls
            clearHallConfigurationForm();
        });
        contentBtn.setOnAction(e -> {
            showForm(contentScrollPane);
            animateFormTransition(contentScrollPane);
            loadContent();
            loadFAQs();
            loadHelpContent();
        });
        logoutBtn.setOnAction(e -> logout());
        
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());
        minimizeBtn.setOnAction(e -> ((Stage) minimizeBtn.getScene().getWindow()).setIconified(true));
        maximizeBtn.setOnAction(e -> {
            Stage stage = (Stage) maximizeBtn.getScene().getWindow();
            stage.setMaximized(!stage.isMaximized());
        });
    }
    
    private void animateFormTransition(ScrollPane formToShow) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formToShow);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
    
    private void showForm(ScrollPane formToShow) {
        dashboardScrollPane.setVisible(false);
        moviesScrollPane.setVisible(false);
        snacksScrollPane.setVisible(false);
        showtimesScrollPane.setVisible(false);
        bookingsScrollPane.setVisible(false);
        customersScrollPane.setVisible(false);
        seatsScrollPane.setVisible(false);
        contentScrollPane.setVisible(false);
        formToShow.setVisible(true);
    }
    
    private void setupTableColumns() {
        // Movie table columns
        movieIdColumn.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        movieGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieDurationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        moviePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        movieStatusColumn.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue();
            String status = movie.isActive() ? "Active" : "Inactive";
            return new SimpleStringProperty(status);
        });
        
        moviePriceColumn.setCellFactory(tc -> new TableCell<Movie, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %.2f", price));
                }
            }
        });
        
        movieDurationColumn.setCellFactory(tc -> new TableCell<Movie, Integer>() {
            @Override
            protected void updateItem(Integer minutes, boolean empty) {
                super.updateItem(minutes, empty);
                if (empty || minutes == null) {
                    setText(null);
                } else {
                    int hours = minutes / 60;
                    int mins = minutes % 60;
                    setText(String.format("%dh %dm", hours, mins));
                }
            }
        });
        
        movieTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillMovieForm(newSelection);
            }
        });
        
        // Snack table columns
        snackIdColumn.setCellValueFactory(new PropertyValueFactory<>("snackId"));
        snackNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        snackTypeColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        snackSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        snackPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        snackAvailableColumn.setCellValueFactory(cellData -> {
            Snack snack = cellData.getValue();
            String status = snack.isAvailable() ? "Yes" : "No";
            return new SimpleStringProperty(status);
        });
        snackCaloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        
        snackPriceColumn.setCellFactory(tc -> new TableCell<Snack, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %.2f", price));
                }
            }
        });
        
        snackTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillSnackForm(newSelection);
            }
        });
        
        // FAQ table columns
        faqIdColumn.setCellValueFactory(new PropertyValueFactory<>("faqId"));
        faqQuestionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        faqAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
        faqActionColumn.setCellFactory(param -> new TableCell<FAQ, String>() {
            private final Button deleteBtn = new Button("Delete");
            
            {
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5;");
                setupButtonHoverEffect(deleteBtn);
                deleteBtn.setOnAction(event -> {
                    FAQ faq = getTableView().getItems().get(getIndex());
                    handleDeleteFAQ(faq);
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
        
        // Customer table columns
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerJoinDateColumn.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        customerBookingsColumn.setCellValueFactory(new PropertyValueFactory<>("totalBookings"));
        customerTotalSpentColumn.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));
        customerStatusColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue();
            String status = customer.isActive() ? "Active" : "Inactive";
            return new SimpleStringProperty(status);
        });
        
        customerTotalSpentColumn.setCellFactory(tc -> new TableCell<Customer, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %.2f", amount));
                }
            }
        });
        
        // Booking table columns
        bookingIdColumn2.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        bookingMovieColumn2.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        bookingTimeColumn2.setCellValueFactory(new PropertyValueFactory<>("bookingTime"));
        bookingSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumbers"));
        bookingTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookingPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        
        bookingTotalColumn.setCellFactory(tc -> new TableCell<Booking, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %.2f", amount));
                }
            }
        });
        
        // Seat table columns
        seatIdColumn.setCellValueFactory(new PropertyValueFactory<>("seatId"));
        seatLabelColumn.setCellValueFactory(new PropertyValueFactory<>("seatLabel"));
        seatRowColumn.setCellValueFactory(new PropertyValueFactory<>("seatRow"));
        seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        seatTypeColumn.setCellValueFactory(new PropertyValueFactory<>("seatType"));
        seatStatusColumn.setCellValueFactory(cellData -> {
            SeatData seat = cellData.getValue();
            String status = seat.isBlocked() ? "Blocked" : "Available";
            return new SimpleStringProperty(status);
        });
        seatPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        seatPriceColumn.setCellFactory(tc -> new TableCell<SeatData, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1fx", price)); // Display as multiplier (1.5x, 2.0x, etc.)
                }
            }
        });
        
        seatTypeColumn.setCellFactory(tc -> new TableCell<SeatData, String>() {
            @Override
            protected void updateItem(String seatType, boolean empty) {
                super.updateItem(seatType, empty);
                if (empty || seatType == null) {
                    setText(null);
                } else {
                    String displayText = seatType.toUpperCase();
                    double multiplier = SEAT_MULTIPLIERS.getOrDefault(seatType, 1.0);
                    if (multiplier != 1.0) {
                        displayText += String.format(" (%.1fx)", multiplier);
                    }
                    setText(displayText);
                }
            }
        });
        
        // Setup seat table selection listener
        seatTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSeatData = newSelection;
                highlightSeatInGrid(newSelection.getSeatLabel());
            } else {
                selectedSeatData = null;
            }
        });
    }
    
    private void initializeSeatFormComponents() {
        // Initialize seat type combo boxes
        if (newSeatTypeBox != null) {
            newSeatTypeBox.getItems().addAll("standard", "vip", "premium", "disabled");
            newSeatTypeBox.setValue("standard");
        }
        
        // Setup update hall button
        if (updateHall != null) {
            updateHall.setOnAction(e -> handleUpdateHallSeats());
        }
        
        // Setup delete seat button
        if (deleteSeat != null) {
            deleteSeat.setOnAction(e -> handleDeleteSeat());
        }
    }
    
    private void setupDashboardChart() {
        // Style the chart
        salesChart.setTitle("Weekly Sales Overview");
        salesChart.setAnimated(true);
        salesChart.setLegendVisible(false);
        salesChart.setStyle("-fx-background-color: transparent;");
        
        // Add hover effect to entire chart
        salesChart.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), salesChart);
            st.setToX(1.01);
            st.setToY(1.01);
            st.play();
        });
        
        salesChart.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), salesChart);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        // Load chart data
        loadChartData();
    }
    
    private void loadChartData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get last 7 days sales data
            String sql = "SELECT DATE(booking_date) as day, SUM(total_amount) as total " +
                        "FROM bookings " +
                        "WHERE booking_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                        "AND booking_status = 'confirmed' " +
                        "GROUP BY DATE(booking_date) " +
                        "ORDER BY day";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Sales");
            
            // Generate last 7 days dates
            LocalDate today = LocalDate.now();
            Map<String, Double> salesMap = new HashMap<>();
            
            while (rs.next()) {
                java.sql.Date sqlDate = rs.getDate("day");
                double total = rs.getDouble("total");
                String day = sqlDate.toLocalDate().getDayOfWeek().toString().substring(0, 3);
                salesMap.put(day, total);
            }
            
            // Add data for last 7 days
            String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (String day : daysOfWeek) {
                double total = salesMap.getOrDefault(day, 0.0);
                
                XYChart.Data<String, Number> data = new XYChart.Data<>(day, total);
                
                // Customize bar appearance
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        setupChartBar(newNode, day, total);
                    }
                });
                
                series.getData().add(data);
            }
            
            salesChart.getData().clear();
            salesChart.getData().add(series);
            
            // Animate chart
            animateChartSeries(series);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupChartBar(Node bar, String day, double value) {
        // Set bar color based on value
        String style = "-fx-bar-fill: ";
        if (value < 5000) {
            style += "#3498db;"; // Blue for low values
        } else if (value < 15000) {
            style += "#2ecc71;"; // Green for medium values
        } else {
            style += "#e74c3c;"; // Red for high values
        }
        
        bar.setStyle(style);
        
        // Create and install tooltip
        Tooltip tooltip = new Tooltip(String.format("%s\nETB %,.0f", day, value));
        tooltip.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        Tooltip.install(bar, tooltip);
        
        // Add hover effect
        bar.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), bar);
            st.setToX(1.15);
            st.setToY(1.15);
            st.play();
            
            // Add glow effect
            Glow glow = new Glow(0.5);
            bar.setEffect(glow);
        });
        
        bar.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), bar);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            
            bar.setEffect(null);
        });
    }
    
    private void animateChartSeries(XYChart.Series<String, Number> series) {
        int delay = 0;
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setScaleY(0);
                data.getNode().setTranslateY(50);
                data.getNode().setOpacity(0);
                
                SequentialTransition seq = new SequentialTransition();
                
                FadeTransition ft = new FadeTransition(Duration.millis(300), data.getNode());
                ft.setDelay(Duration.millis(delay));
                ft.setToValue(1);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(400), data.getNode());
                tt.setDelay(Duration.millis(delay));
                tt.setToY(0);
                
                ScaleTransition st = new ScaleTransition(Duration.millis(400), data.getNode());
                st.setDelay(Duration.millis(delay));
                st.setToY(1);
                
                seq.getChildren().addAll(ft, tt, st);
                seq.play();
                
                delay += 100;
            }
        }
    }
    
    private void setupRecentBookingsTable() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingMovieColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        bookingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingTime"));
        
        // Style the table
        recentBookingsTable.setRowFactory(tv -> {
            TableRow<RecentBooking> row = new TableRow<RecentBooking>() {
                @Override
                protected void updateItem(RecentBooking item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        // Add animation on row hover
                        this.setOnMouseEntered(e -> {
                            ScaleTransition st = new ScaleTransition(Duration.millis(100), this);
                            st.setToX(1.01);
                            st.play();
                            this.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #d1e7ff; -fx-border-width: 0 0 1 0;");
                        });
                        this.setOnMouseExited(e -> {
                            ScaleTransition st = new ScaleTransition(Duration.millis(100), this);
                            st.setToX(1.0);
                            st.play();
                            this.setStyle("");
                        });
                    }
                }
            };
            return row;
        });
        
        // Setup view all bookings button
        viewAllBookingsBtn.setOnAction(e -> {
            showForm(bookingsScrollPane);
            animateFormTransition(bookingsScrollPane);
        });
        
        // Add hover effect to button
        setupButtonHoverEffect(viewAllBookingsBtn);
    }
    
    private void loadRecentBookings() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT b.booking_id, m.title as movie_name, b.booking_date " +
                        "FROM bookings b " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "WHERE b.booking_status = 'confirmed' " +
                        "ORDER BY b.booking_date DESC " +
                        "LIMIT 10";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            recentBookings.clear();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
            
            while (rs.next()) {
                RecentBooking booking = new RecentBooking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setMovieName(rs.getString("movie_name"));
                
                java.sql.Timestamp bookingTime = rs.getTimestamp("booking_date");
                if (bookingTime != null) {
                    booking.setBookingTime(bookingTime.toLocalDateTime().format(formatter));
                }
                
                recentBookings.add(booking);
            }
            
            recentBookingsTable.setItems(recentBookings);
            
            // Animate table rows
            animateTableRows();
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void animateTableRows() {
        for (int i = 0; i < recentBookingsTable.getItems().size(); i++) {
            TableRow<RecentBooking> row = getTableRow(i);
            if (row != null) {
                row.setOpacity(0);
                row.setTranslateX(-50);
                
                FadeTransition ft = new FadeTransition(Duration.millis(300), row);
                ft.setDelay(Duration.millis(i * 50));
                ft.setToValue(1);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(300), row);
                tt.setDelay(Duration.millis(i * 50));
                tt.setToX(0);
                
                ParallelTransition pt = new ParallelTransition(ft, tt);
                pt.play();
            }
        }
    }
    
    private TableRow<RecentBooking> getTableRow(int index) {
        return (TableRow<RecentBooking>) recentBookingsTable.lookup(".table-row-cell:nth-child(" + (index + 1) + ")");
    }
    
    private void setupHoverEffects() {
        // Setup hover effects for navigation buttons
        Button[] navButtons = {homeBtn, moviesBtn, snacksDrinksBtn, showtimesBtn, 
                              bookingsBtn, customersBtn, seatsBtn, contentBtn};
        
        for (Button btn : navButtons) {
            if (btn != null) {
                setupNavigationButtonHover(btn);
            }
        }
        
        // Setup hover effects for all buttons
        setupButtonHoverEffect(addMovieBtn);
        setupButtonHoverEffect(updateMovieBtn);
        setupButtonHoverEffect(deleteMovieBtn);
        setupButtonHoverEffect(clearMovieBtn);
        setupButtonHoverEffect(importMovieImageBtn);
        setupButtonHoverEffect(addSnackBtn);
        setupButtonHoverEffect(updateSnackBtn);
        setupButtonHoverEffect(deleteSnackBtn);
        setupButtonHoverEffect(clearSnackBtn);
        setupButtonHoverEffect(importSnackImageBtn);
        setupButtonHoverEffect(generateSeatsBtn);
        setupButtonHoverEffect(saveSeatsBtn);
        setupButtonHoverEffect(blockSeatBtn);
        setupButtonHoverEffect(unblockSeatBtn);
        setupButtonHoverEffect(resetSeatsBtn);
        setupButtonHoverEffect(loadSeat1);
        setupButtonHoverEffect(updateHall);
        setupButtonHoverEffect(deleteSeat);
        setupButtonHoverEffect(addShowtimeBtn);
        setupButtonHoverEffect(updateShowtimeBtn);
        setupButtonHoverEffect(deleteShowtimeBtn);
        setupButtonHoverEffect(filterShowtimesBtn);
        setupButtonHoverEffect(addFaqBtn);
        setupButtonHoverEffect(saveAboutUsBtn);
        setupButtonHoverEffect(importAboutUsImageBtn);
        setupButtonHoverEffect(saveHelpBtn);
        setupButtonHoverEffect(viewCustomerBtn);
        setupButtonHoverEffect(editCustomerBtn);
        setupButtonHoverEffect(deactivateCustomerBtn);
        setupButtonHoverEffect(exportCustomersBtn);
        setupButtonHoverEffect(filterBookingsBtn);
        setupButtonHoverEffect(exportBookingsBtn);
        setupButtonHoverEffect(confirmBookingBtn);
        setupButtonHoverEffect(cancelBookingBtn);
        setupButtonHoverEffect(viewBookingDetailsBtn);
        setupButtonHoverEffect(sendBookingSMSBtn);
        setupButtonHoverEffect(closeBtn);
        setupButtonHoverEffect(minimizeBtn);
        setupButtonHoverEffect(maximizeBtn);
    }
    
    private void setupNavigationButtonHover(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            
            TranslateTransition tt = new TranslateTransition(Duration.millis(150), button);
            tt.setToX(5);
            tt.play();
            
            button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); -fx-text-fill: #fff; -fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-width: 0 0 0 3;");
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            
            TranslateTransition tt = new TranslateTransition(Duration.millis(150), button);
            tt.setToX(0);
            tt.play();
            
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #fff; -fx-border-width: 0;");
        });
    }
    
    private void setupButtonHoverEffect(Button button) {
        if (button == null) return;
        
        // Store original style
        final String originalStyle = button.getStyle();
        
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            
            button.setEffect(hoverEffect);
            
            // Add pulse animation
            ScaleTransition pulse = new ScaleTransition(Duration.millis(500), button);
            pulse.setToX(1.07);
            pulse.setToY(1.07);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(2);
            pulse.play();
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            
            button.setEffect(null);
        });
    }
    
    private void setupDashboardAnimations() {
        // Animate dashboard cards on load
        if (totalSalesLabel != null && totalSalesLabel.getParent() != null && totalSalesLabel.getParent().getParent() != null) {
            Node salesCard = totalSalesLabel.getParent().getParent();
            Node ticketsCard = ticketsSoldLabel != null && ticketsSoldLabel.getParent() != null && ticketsSoldLabel.getParent().getParent() != null ? 
                              ticketsSoldLabel.getParent().getParent() : null;
            Node moviesCard = activeMoviesLabel != null && activeMoviesLabel.getParent() != null && activeMoviesLabel.getParent().getParent() != null ? 
                             activeMoviesLabel.getParent().getParent() : null;
            Node customersCard = totalCustomersLabel != null && totalCustomersLabel.getParent() != null && totalCustomersLabel.getParent().getParent() != null ? 
                                totalCustomersLabel.getParent().getParent() : null;
            
            Node[] dashboardCards = {salesCard, ticketsCard, moviesCard, customersCard};
            
            for (int i = 0; i < dashboardCards.length; i++) {
                if (dashboardCards[i] != null) {
                    dashboardCards[i].setOpacity(0);
                    dashboardCards[i].setScaleX(0.8);
                    dashboardCards[i].setScaleY(0.8);
                    dashboardCards[i].setTranslateY(20);
                    
                    SequentialTransition seq = new SequentialTransition();
                    
                    FadeTransition ft = new FadeTransition(Duration.millis(400), dashboardCards[i]);
                    ft.setDelay(Duration.millis(i * 150));
                    ft.setToValue(1);
                    
                    TranslateTransition tt = new TranslateTransition(Duration.millis(400), dashboardCards[i]);
                    tt.setDelay(Duration.millis(i * 150));
                    tt.setToY(0);
                    
                    ScaleTransition st = new ScaleTransition(Duration.millis(400), dashboardCards[i]);
                    st.setDelay(Duration.millis(i * 150));
                    st.setToX(1);
                    st.setToY(1);
                    
                    seq.getChildren().addAll(ft, tt, st);
                    seq.play();
                    
                    // Add hover effect to cards
                    final Node card = dashboardCards[i];
                    card.setOnMouseEntered(e -> {
                        ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
                        scale.setToX(1.03);
                        scale.setToY(1.03);
                        scale.play();
                        
                        card.setEffect(cardShadow);
                    });
                    
                    card.setOnMouseExited(e -> {
                        ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
                        scale.setToX(1.0);
                        scale.setToY(1.0);
                        scale.play();
                        
                        card.setEffect(null);
                    });
                }
            }
        }
    }
    
    // ==================== DASHBOARD STATS ====================
    
    private void loadDashboardStats() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Total Sales
            String salesSql = "SELECT COALESCE(SUM(total_amount), 0) as total_sales FROM bookings WHERE booking_status = 'confirmed'";
            PreparedStatement salesStmt = conn.prepareStatement(salesSql);
            ResultSet salesRs = salesStmt.executeQuery();
            if (salesRs.next() && totalSalesLabel != null) {
                animateLabelValue(totalSalesLabel, String.format("ETB %,.0f", salesRs.getDouble("total_sales")));
            }
            salesRs.close();
            salesStmt.close();
            
            // Tickets Sold
            String ticketsSql = "SELECT COUNT(*) as total_tickets FROM bookings WHERE booking_status = 'confirmed'";
            PreparedStatement ticketsStmt = conn.prepareStatement(ticketsSql);
            ResultSet ticketsRs = ticketsStmt.executeQuery();
            if (ticketsRs.next() && ticketsSoldLabel != null) {
                animateLabelValue(ticketsSoldLabel, String.format("%,d", ticketsRs.getInt("total_tickets")));
            }
            ticketsRs.close();
            ticketsStmt.close();
            
            // Active Movies
            String moviesSql = "SELECT COUNT(*) as active_movies FROM movies WHERE is_active = TRUE";
            PreparedStatement moviesStmt = conn.prepareStatement(moviesSql);
            ResultSet moviesRs = moviesStmt.executeQuery();
            if (moviesRs.next() && activeMoviesLabel != null) {
                animateLabelValue(activeMoviesLabel, String.format("%d", moviesRs.getInt("active_movies")));
            }
            moviesRs.close();
            moviesStmt.close();
            
            // Total Customers
            String customersSql = "SELECT COUNT(*) as total_customers FROM users WHERE user_type = 'customer' AND is_active = TRUE";
            PreparedStatement customersStmt = conn.prepareStatement(customersSql);
            ResultSet customersRs = customersStmt.executeQuery();
            if (customersRs.next() && totalCustomersLabel != null) {
                animateLabelValue(totalCustomersLabel, String.format("%,d", customersRs.getInt("total_customers")));
            }
            customersRs.close();
            customersStmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void animateLabelValue(Label label, String newValue) {
        ScaleTransition st1 = new ScaleTransition(Duration.millis(150), label);
        st1.setToX(1.2);
        st1.setToY(1.2);
        
        ScaleTransition st2 = new ScaleTransition(Duration.millis(150), label);
        st2.setToX(1.0);
        st2.setToY(1.0);
        
        st1.setOnFinished(e -> {
            label.setText(newValue);
            st2.play();
        });
        
        st1.play();
    }
    
    // ==================== RECENT BOOKING DATA CLASS ====================
    
    public static class RecentBooking {
        private int bookingId;
        private String movieName;
        private String bookingTime;
        
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }
        public String getMovieName() { return movieName; }
        public void setMovieName(String movieName) { this.movieName = movieName; }
        public String getBookingTime() { return bookingTime; }
        public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
    }
    
    private void fillMovieForm(Movie movie) {
        movieTitleField.setText(movie.getTitle());
        movieGenreField.setText(movie.getGenre());
        movieDurationField.setText(String.valueOf(movie.getDurationMinutes()) + "m");
        movieDirectorField.setText(movie.getDirector());
        movieCastField.setText(movie.getCast());
        movieDescriptionArea.setText(movie.getDescription());
        movieCategoryBox.setValue(movie.getCategory());
        moviePriceField.setText(String.valueOf(movie.getPrice()));
        movieRatingField.setText(movie.getRating());
        movieTrailerField.setText(movie.getTrailerUrl());
        if (movie.getReleaseDate() != null) {
            movieReleaseDate.setValue(movie.getReleaseDate());
        }
        
        if (movie.getPosterImage() != null && !movie.getPosterImage().isEmpty()) {
            try {
                java.io.File file = new java.io.File(movie.getPosterImage());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    movieImageView.setImage(image);
                    selectedMovieImagePath = movie.getPosterImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void fillSnackForm(Snack snack) {
        snackTypeBox.setValue(snack.getCategory());
        snackNameField.setText(snack.getItemName());
        snackSizeField.setText(snack.getSize());
        snackPriceField.setText(String.valueOf(snack.getPrice()));
        snackDescriptionArea.setText(snack.getDescription());
        snackCaloriesField.setText(String.valueOf(snack.getCalories()));
        snackAvailableCheck.setSelected(snack.isAvailable());
        
        if (snack.getImageUrl() != null && !snack.getImageUrl().isEmpty()) {
            try {
                java.io.File file = new java.io.File(snack.getImageUrl());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    snackImageView.setImage(image);
                    selectedSnackImagePath = snack.getImageUrl();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void setupShowtimeTableColumns() {
        showtimeIdColumn.setCellValueFactory(new PropertyValueFactory<>("showtimeId"));
        showtimeMovieColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        showtimeDateColumn.setCellValueFactory(cellData -> {
            Showtime showtime = cellData.getValue();
            return new SimpleStringProperty(showtime.getShowDate().toString());
        });
        showtimeTimeColumn.setCellValueFactory(new PropertyValueFactory<>("showTime"));
        showtimeHallColumn.setCellValueFactory(new PropertyValueFactory<>("hallName"));
        showtimePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        showtimeSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        
        showtimePriceColumn.setCellFactory(tc -> new TableCell<Showtime, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %.2f", price));
                }
            }
        });
        
        showtimeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillShowtimeForm(newSelection);
            }
        });
    }
    
    private void initializeMovieCategoryBox() {
        if (movieCategoryBox != null) {
            movieCategoryBox.getItems().addAll("Now Showing", "Coming Soon", "Featured");
        }
    }
    
    private void initializeSnackTypeBox() {
        if (snackTypeBox != null) {
            snackTypeBox.getItems().addAll("Snack", "Drink", "Combo", "Dessert");
        }
    }
    
    private void initializeSeatHallBoxes() {
        // Clear existing items
        if (seatHallBox != null) seatHallBox.getItems().clear();
        if (seatHallBox2 != null) seatHallBox2.getItems().clear();
        
        hallMap.clear();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT hall_id, hall_name, seat_rows, seat_columns FROM cinema_halls ORDER BY hall_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<String> halls = FXCollections.observableArrayList();
            while (rs.next()) {
                String hallName = rs.getString("hall_name");
                int hallId = rs.getInt("hall_id");
                halls.add(hallName);
                hallMap.put(hallName, hallId);
            }
            
            if (seatHallBox != null) {
                seatHallBox.setItems(halls);
                if (!halls.isEmpty()) {
                    seatHallBox.setValue(halls.get(0));
                }
            }
            
            if (seatHallBox2 != null) {
                seatHallBox2.setItems(halls);
                if (!halls.isEmpty()) {
                    seatHallBox2.setValue(halls.get(0));
                    currentHallName = halls.get(0);
                    currentHallId = hallMap.get(currentHallName);
                    
                    // Load current hall configuration
                    loadCurrentHallConfiguration();
                }
                
                // Add listener to load hall configuration when hall is selected
                seatHallBox2.setOnAction(e -> {
                    String selectedHall = seatHallBox2.getValue();
                    if (selectedHall != null && hallMap.containsKey(selectedHall)) {
                        currentHallId = hallMap.get(selectedHall);
                        currentHallName = selectedHall;
                        loadCurrentHallConfiguration();
                    }
                });
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadCurrentHallConfiguration() {
        if (currentHallId == -1) return;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT seat_rows, seat_columns FROM cinema_halls WHERE hall_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentHallId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                currentSeatRows = rs.getInt("seat_rows");
                currentSeatCols = rs.getInt("seat_columns");
                
                // Fill the configuration form with current values
                if (seatRowsField != null) {
                    seatRowsField.setText(String.valueOf(currentSeatRows));
                }
                if (seatColumnsField != null) {
                    seatColumnsField.setText(String.valueOf(currentSeatCols));
                }
                
                // Fill the update form as well
                if (newSeatRowsField != null) {
                    newSeatRowsField.setText(String.valueOf(currentSeatRows));
                }
                if (newSeatColumnsField != null) {
                    newSeatColumnsField.setText(String.valueOf(currentSeatCols));
                }
                if (newSeatTypeBox != null) {
                    newSeatTypeBox.setValue("standard");
                }
                if (keepExistingBlockedCheck != null) {
                    keepExistingBlockedCheck.setSelected(true);
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clearHallConfigurationForm() {
        if (newSeatRowsField != null) newSeatRowsField.clear();
        if (newSeatColumnsField != null) newSeatColumnsField.clear();
        if (newSeatTypeBox != null) newSeatTypeBox.setValue("standard");
        if (keepExistingBlockedCheck != null) keepExistingBlockedCheck.setSelected(true);
    }
    
    private void initializeShowtimeComponents() {
        loadMoviesForShowtime();
        loadMoviesForFilter();
        
        if (addShowtimeBtn != null) addShowtimeBtn.setOnAction(e -> handleAddShowtime());
        if (updateShowtimeBtn != null) updateShowtimeBtn.setOnAction(e -> handleUpdateShowtime());
        if (deleteShowtimeBtn != null) deleteShowtimeBtn.setOnAction(e -> handleDeleteShowtime());
        if (filterShowtimesBtn != null) filterShowtimesBtn.setOnAction(e -> filterShowtimes());
        
        if (showtimeDate != null) showtimeDate.setValue(LocalDate.now());
        if (showtimeFilterDate != null) showtimeFilterDate.setValue(LocalDate.now());
        if (showtimeTimeField != null) showtimeTimeField.setText(LocalTime.now().format(timeFormatter));
        
        // Add listener to auto-fill showtime price when movie is selected
        showtimeMovieBox.setOnAction(e -> {
            String selectedMovie = showtimeMovieBox.getValue();
            if (selectedMovie != null && !selectedMovie.isEmpty()) {
                double moviePrice = getMoviePriceByTitle(selectedMovie);
                if (moviePrice > 0) {
                    showtimePriceField.setText(String.format("%.2f", moviePrice));
                    
                    ScaleTransition st = new ScaleTransition(Duration.millis(200), showtimePriceField);
                    st.setFromX(1.1);
                    st.setToX(1.0);
                    st.play();
                }
            }
        });
    }
    
    private void setupImageButtons() {
        // Movie image button
        if (importMovieImageBtn != null) importMovieImageBtn.setOnAction(e -> importMovieImage());
        
        // Snack image button
        if (importSnackImageBtn != null) importSnackImageBtn.setOnAction(e -> importSnackImage());
        
        // About Us image button
        if (importAboutUsImageBtn != null) importAboutUsImageBtn.setOnAction(e -> importAboutUsImage());
        
        // Movie CRUD buttons
        if (addMovieBtn != null) addMovieBtn.setOnAction(e -> handleAddMovie());
        if (updateMovieBtn != null) updateMovieBtn.setOnAction(e -> handleUpdateMovie());
        if (deleteMovieBtn != null) deleteMovieBtn.setOnAction(e -> handleDeleteMovie());
        if (clearMovieBtn != null) clearMovieBtn.setOnAction(e -> clearMovieForm());
        
        // Snack CRUD buttons
        if (addSnackBtn != null) addSnackBtn.setOnAction(e -> handleAddSnack());
        if (updateSnackBtn != null) updateSnackBtn.setOnAction(e -> handleUpdateSnack());
        if (deleteSnackBtn != null) deleteSnackBtn.setOnAction(e -> handleDeleteSnack());
        if (clearSnackBtn != null) clearSnackBtn.setOnAction(e -> clearSnackForm());
        
        // About Us save button
        if (saveAboutUsBtn != null) saveAboutUsBtn.setOnAction(e -> saveAboutUsContent());
        
        // FAQ button
        if (addFaqBtn != null) addFaqBtn.setOnAction(e -> handleAddFAQ());
        
        // Help button
        if (saveHelpBtn != null) saveHelpBtn.setOnAction(e -> saveHelpContent());
        
        // Seat management buttons
        if (generateSeatsBtn != null) generateSeatsBtn.setOnAction(e -> handleGenerateSeats());
        if (saveSeatsBtn != null) saveSeatsBtn.setOnAction(e -> handleSaveSeats());
        if (blockSeatBtn != null) blockSeatBtn.setOnAction(e -> handleBlockSeat());
        if (unblockSeatBtn != null) unblockSeatBtn.setOnAction(e -> handleUnblockSeat());
        if (resetSeatsBtn != null) resetSeatsBtn.setOnAction(e -> handleResetSeats());
        
        // Load seats button
        if (loadSeat1 != null) loadSeat1.setOnAction(e -> handleLoadSeats());
        
        // Customer buttons
        if (viewCustomerBtn != null) viewCustomerBtn.setOnAction(e -> handleViewCustomer());
        if (editCustomerBtn != null) editCustomerBtn.setOnAction(e -> handleEditCustomer());
        if (deactivateCustomerBtn != null) deactivateCustomerBtn.setOnAction(e -> handleDeactivateCustomer());
        if (exportCustomersBtn != null) exportCustomersBtn.setOnAction(e -> handleExportCustomers());
        
        // Booking buttons
        if (confirmBookingBtn != null) confirmBookingBtn.setOnAction(e -> handleConfirmBooking());
        if (cancelBookingBtn != null) cancelBookingBtn.setOnAction(e -> handleCancelBooking());
        if (viewBookingDetailsBtn != null) viewBookingDetailsBtn.setOnAction(e -> handleViewBookingDetails());
        if (sendBookingSMSBtn != null) sendBookingSMSBtn.setOnAction(e -> handleSendBookingSMS());
        if (exportBookingsBtn != null) exportBookingsBtn.setOnAction(e -> handleExportBookings());
        
        // Seat table buttons
        if (updateHall != null) updateHall.setOnAction(e -> handleUpdateHallSeats());
        if (deleteSeat != null) deleteSeat.setOnAction(e -> handleDeleteSeat());
    }
    
    // ==================== IMAGE IMPORT METHODS ====================
    
    @FXML
    private void importMovieImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Movie Poster");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        java.io.File selectedFile = fileChooser.showOpenDialog(importMovieImageBtn.getScene().getWindow());
        
        if (selectedFile != null) {
            try {
                selectedMovieImagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                movieImageView.setImage(image);
                
                ScaleTransition st = new ScaleTransition(Duration.millis(300), movieImageView);
                st.setFromX(0.8);
                st.setFromY(0.8);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            } catch (Exception e) {
                showAlert("Image Error", "Failed to load image: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void importSnackImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Snack/Drink Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        java.io.File selectedFile = fileChooser.showOpenDialog(importSnackImageBtn.getScene().getWindow());
        
        if (selectedFile != null) {
            try {
                selectedSnackImagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                snackImageView.setImage(image);
                
                ScaleTransition st = new ScaleTransition(Duration.millis(300), snackImageView);
                st.setFromX(0.8);
                st.setFromY(0.8);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            } catch (Exception e) {
                showAlert("Image Error", "Failed to load image: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void importAboutUsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select About Us Banner Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        java.io.File selectedFile = fileChooser.showOpenDialog(importAboutUsImageBtn.getScene().getWindow());
        
        if (selectedFile != null) {
            try {
                selectedAboutUsImagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                aboutUsImageView.setImage(image);
                
                FadeTransition ft = new FadeTransition(Duration.millis(500), aboutUsImageView);
                ft.setFromValue(0.5);
                ft.setToValue(1.0);
                ft.play();
            } catch (Exception e) {
                showAlert("Image Error", "Failed to load image: " + e.getMessage());
            }
        }
    }
    
    // ==================== MOVIE CRUD OPERATIONS ====================
    
    private void loadMovies() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, genre, duration_minutes, director, category, " +
                        "price, rating, trailer_url, poster_image, release_date, is_active, " +
                        "description, cast " +
                        "FROM movies ORDER BY movie_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Movie> movies = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setDirector(rs.getString("director"));
                movie.setCategory(rs.getString("category"));
                movie.setPrice(rs.getDouble("price"));
                movie.setRating(rs.getString("rating"));
                movie.setTrailerUrl(rs.getString("trailer_url"));
                movie.setPosterImage(rs.getString("poster_image"));
                java.sql.Date releaseDate = rs.getDate("release_date");
                if (releaseDate != null) {
                    movie.setReleaseDate(releaseDate.toLocalDate());
                }
                movie.setActive(rs.getBoolean("is_active"));
                movie.setDescription(rs.getString("description"));
                movie.setCast(rs.getString("cast"));
                
                movies.add(movie);
            }
            
            movieTableView.setItems(movies);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load movies: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddMovie() {
        String title = movieTitleField.getText().trim();
        String genre = movieGenreField.getText().trim();
        String durationText = movieDurationField.getText().trim();
        String director = movieDirectorField.getText().trim();
        String cast = movieCastField.getText().trim();
        String description = movieDescriptionArea.getText().trim();
        String category = movieCategoryBox.getValue();
        String priceText = moviePriceField.getText().trim();
        String rating = movieRatingField.getText().trim();
        String trailerUrl = movieTrailerField.getText().trim();
        LocalDate releaseDate = movieReleaseDate.getValue();
        
        if (title.isEmpty() || genre.isEmpty() || durationText.isEmpty() || priceText.isEmpty() || 
            category == null || releaseDate == null) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        try {
            int durationMinutes = convertDurationToMinutes(durationText);
            double price = Double.parseDouble(priceText);
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO movies (title, description, genre, duration_minutes, director, " +
                        "cast, category, price, rating, trailer_url, release_date, poster_image, is_active, added_by_admin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, genre);
            pstmt.setInt(4, durationMinutes);
            pstmt.setString(5, director);
            pstmt.setString(6, cast);
            pstmt.setString(7, category);
            pstmt.setDouble(8, price);
            pstmt.setString(9, rating);
            pstmt.setString(10, trailerUrl);
            pstmt.setDate(11, java.sql.Date.valueOf(releaseDate));
            pstmt.setString(12, selectedMovieImagePath);
            pstmt.setBoolean(13, true);
            pstmt.setInt(14, adminId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Movie added successfully!");
                clearMovieForm();
                loadMovies();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price and duration");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to add movie: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateMovie() {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("Selection Error", "Please select a movie to update");
            return;
        }
        
        String title = movieTitleField.getText().trim();
        String genre = movieGenreField.getText().trim();
        String durationText = movieDurationField.getText().trim();
        String director = movieDirectorField.getText().trim();
        String cast = movieCastField.getText().trim();
        String description = movieDescriptionArea.getText().trim();
        String category = movieCategoryBox.getValue();
        String priceText = moviePriceField.getText().trim();
        String rating = movieRatingField.getText().trim();
        String trailerUrl = movieTrailerField.getText().trim();
        LocalDate releaseDate = movieReleaseDate.getValue();
        
        if (title.isEmpty() || genre.isEmpty() || durationText.isEmpty() || priceText.isEmpty() || 
            category == null || releaseDate == null) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        try {
            int durationMinutes = convertDurationToMinutes(durationText);
            double price = Double.parseDouble(priceText);
            double oldPrice = selectedMovie.getPrice();
            
            Connection conn = DatabaseConnection.getConnection();
            
            String sql = "UPDATE movies SET title = ?, description = ?, genre = ?, duration_minutes = ?, " +
                        "director = ?, cast = ?, category = ?, price = ?, rating = ?, trailer_url = ?, " +
                        "release_date = ?, poster_image = ? WHERE movie_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, genre);
            pstmt.setInt(4, durationMinutes);
            pstmt.setString(5, director);
            pstmt.setString(6, cast);
            pstmt.setString(7, category);
            pstmt.setDouble(8, price);
            pstmt.setString(9, rating);
            pstmt.setString(10, trailerUrl);
            pstmt.setDate(11, java.sql.Date.valueOf(releaseDate));
            pstmt.setString(12, selectedMovieImagePath.isEmpty() ? selectedMovie.getPosterImage() : selectedMovieImagePath);
            pstmt.setInt(13, selectedMovie.getMovieId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                if (price != oldPrice) {
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Update Showtimes");
                    confirmAlert.setHeaderText("Movie Price Changed");
                    confirmAlert.setContentText(
                        "Movie price changed from ETB " + String.format("%.2f", oldPrice) + 
                        " to ETB " + String.format("%.2f", price) + 
                        "\n\nDo you want to update all FUTURE showtimes for this movie?\n" +
                        "(This will affect showtimes starting from tomorrow)"
                    );
                    
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            updateFutureShowtimesPrice(selectedMovie.getMovieId(), price);
                        }
                    });
                }
                
                showSuccessAlert("Success", "Movie updated successfully!");
                clearMovieForm();
                loadMovies();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price and duration");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update movie: " + e.getMessage());
        }
    }
    
    private void updateFutureShowtimesPrice(int movieId, double newPrice) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE showtimes SET price = ? WHERE movie_id = ? AND show_date >= CURDATE()";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, movieId);
            
            int updatedCount = pstmt.executeUpdate();
            pstmt.close();
            
            showSuccessAlert("Success", "Updated " + updatedCount + " future showtimes to new price: ETB " + String.format("%.2f", newPrice));
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update showtimes: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteMovie() {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("Selection Error", "Please select a movie to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Movie");
        confirmAlert.setContentText("Are you sure you want to delete '" + selectedMovie.getTitle() + "'?\nThis will also delete all associated showtimes and bookings.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM movies WHERE movie_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedMovie.getMovieId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        showSuccessAlert("Success", "Movie deleted successfully!");
                        loadMovies();
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to delete movie: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void clearMovieForm() {
        movieTitleField.clear();
        movieGenreField.clear();
        movieDurationField.clear();
        movieDirectorField.clear();
        movieCastField.clear();
        movieDescriptionArea.clear();
        moviePriceField.clear();
        movieRatingField.clear();
        movieTrailerField.clear();
        movieCategoryBox.setValue(null);
        movieReleaseDate.setValue(null);
        movieImageView.setImage(null);
        selectedMovieImagePath = "";
        movieTableView.getSelectionModel().clearSelection();
    }
    
    // ==================== SNACKS CRUD OPERATIONS ====================
    
    private void loadSnacks() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                        "s.price, s.size, s.calories, s.image_url, s.is_available " +
                        "FROM snack_items s " +
                        "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                        "ORDER BY s.snack_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Snack> snacks = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Snack snack = new Snack();
                snack.setSnackId(rs.getInt("snack_id"));
                snack.setItemName(rs.getString("item_name"));
                snack.setDescription(rs.getString("description"));
                snack.setCategory(rs.getString("category_name"));
                snack.setPrice(rs.getDouble("price"));
                snack.setSize(rs.getString("size"));
                snack.setCalories(rs.getInt("calories"));
                snack.setImageUrl(rs.getString("image_url"));
                snack.setAvailable(rs.getBoolean("is_available"));
                
                snacks.add(snack);
            }
            
            snackTableView.setItems(snacks);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load snacks: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddSnack() {
        String type = snackTypeBox.getValue();
        String name = snackNameField.getText().trim();
        String size = snackSizeField.getText().trim();
        String priceText = snackPriceField.getText().trim();
        String description = snackDescriptionArea.getText().trim();
        String caloriesText = snackCaloriesField.getText().trim();
        boolean available = snackAvailableCheck.isSelected();
        
        if (type == null || name.isEmpty() || size.isEmpty() || priceText.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            Integer calories = caloriesText.isEmpty() ? null : Integer.parseInt(caloriesText);
            
            int categoryId = 1; // Default to Snack
            
            switch (type) {
                case "Drink": categoryId = 2; break;
                case "Combo": categoryId = 3; break;
                case "Dessert": categoryId = 4; break;
            }
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO snack_items (item_name, description, category_id, price, " +
                        "size, calories, image_url, is_available, added_by_admin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, categoryId);
            pstmt.setDouble(4, price);
            pstmt.setString(5, size);
            
            if (calories != null) {
                pstmt.setInt(6, calories);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(7, selectedSnackImagePath);
            pstmt.setBoolean(8, available);
            pstmt.setInt(9, adminId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Snack/Drink added successfully!");
                clearSnackForm();
                loadSnacks();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price and calories");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to add snack/drink: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateSnack() {
        Snack selectedSnack = snackTableView.getSelectionModel().getSelectedItem();
        if (selectedSnack == null) {
            showAlert("Selection Error", "Please select a snack/drink to update");
            return;
        }
        
        String type = snackTypeBox.getValue();
        String name = snackNameField.getText().trim();
        String size = snackSizeField.getText().trim();
        String priceText = snackPriceField.getText().trim();
        String description = snackDescriptionArea.getText().trim();
        String caloriesText = snackCaloriesField.getText().trim();
        boolean available = snackAvailableCheck.isSelected();
        
        if (type == null || name.isEmpty() || size.isEmpty() || priceText.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            Integer calories = caloriesText.isEmpty() ? null : Integer.parseInt(caloriesText);
            
            int categoryId = 1; // Default to Snack
            
            switch (type) {
                case "Drink": categoryId = 2; break;
                case "Combo": categoryId = 3; break;
                case "Dessert": categoryId = 4; break;
            }
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE snack_items SET item_name = ?, description = ?, category_id = ?, price = ?, " +
                        "size = ?, calories = ?, image_url = ?, is_available = ? WHERE snack_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, categoryId);
            pstmt.setDouble(4, price);
            pstmt.setString(5, size);
            
            if (calories != null) {
                pstmt.setInt(6, calories);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(7, selectedSnackImagePath.isEmpty() ? selectedSnack.getImageUrl() : selectedSnackImagePath);
            pstmt.setBoolean(8, available);
            pstmt.setInt(9, selectedSnack.getSnackId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Snack/Drink updated successfully!");
                clearSnackForm();
                loadSnacks();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price and calories");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update snack/drink: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteSnack() {
        Snack selectedSnack = snackTableView.getSelectionModel().getSelectedItem();
        if (selectedSnack == null) {
            showAlert("Selection Error", "Please select a snack/drink to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Snack/Drink");
        confirmAlert.setContentText("Are you sure you want to delete '" + selectedSnack.getItemName() + "'?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM snack_items WHERE snack_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedSnack.getSnackId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        showSuccessAlert("Success", "Snack/Drink deleted successfully!");
                        loadSnacks();
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to delete snack/drink: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void clearSnackForm() {
        snackTypeBox.setValue(null);
        snackNameField.clear();
        snackSizeField.clear();
        snackPriceField.clear();
        snackDescriptionArea.clear();
        snackCaloriesField.clear();
        snackAvailableCheck.setSelected(false);
        snackImageView.setImage(null);
        selectedSnackImagePath = "";
        snackTableView.getSelectionModel().clearSelection();
    }
    
    // ==================== SEAT MANAGEMENT ====================
    
    private void setupSeatSelectionListener() {
        seatTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSeatData = newSelection;
                highlightSeatInGrid(newSelection.getSeatLabel());
            } else {
                selectedSeatData = null;
            }
        });
    }
    
    @FXML
    private void handleGenerateSeats() {
        String rowsText = seatRowsField.getText().trim();
        String colsText = seatColumnsField.getText().trim();
        String hallName = seatHallBox.getValue();
        
        if (rowsText.isEmpty() || colsText.isEmpty() || hallName == null) {
            showAlert("Validation Error", "Please fill all fields and select a hall");
            return;
        }
        
        try {
            int rows = Integer.parseInt(rowsText);
            int cols = Integer.parseInt(colsText);
            
            if (rows <= 0 || cols <= 0 || rows > 20 || cols > 30) {
                showAlert("Input Error", "Rows must be 1-20 and columns 1-30");
                return;
            }
            
            generateSeatGrid(rows, cols);
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for rows and columns");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate seats: " + e.getMessage());
        }
    }
    
    private void generateSeatGrid(int rows, int cols) {
        // Clear existing grid
        seatGridPane.getChildren().clear();
        seatGridPane.getRowConstraints().clear();
        seatGridPane.getColumnConstraints().clear();
        
        // Set up grid constraints
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(40);
            seatGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(40);
            seatGridPane.getRowConstraints().add(rowConst);
        }
        
        // Create seats
        seatButtons.clear();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String seatLabel = (char) ('A' + row) + String.valueOf(col + 1);
                SeatButton seatBtn = new SeatButton(seatLabel, row, col);
                
                // Determine seat type based on row
                String seatType = getSeatTypeForRow(row, rows);
                
                // Set seat button color based on type
                switch (seatType) {
                    case "premium":
                        seatBtn.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold;");
                        break;
                    case "vip":
                        seatBtn.setStyle("-fx-background-color: #ff9900; -fx-text-fill: black; -fx-font-weight: bold;");
                        break;
                    case "standard":
                        seatBtn.setStyle("-fx-background-color: #239f1a; -fx-text-fill: white;");
                        break;
                    default:
                        seatBtn.setStyle("-fx-background-color: #239f1a; -fx-text-fill: white;");
                }
                
                seatBtn.setSeatType(seatType);
                seatBtn.setOnAction(e -> handleSeatClick(seatBtn));
                seatButtons.add(seatBtn);
                
                seatGridPane.add(seatBtn, col, row);
            }
        }
        
        // Add screen label
        Label screenLabel = new Label("S C R E E N");
        screenLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #666;");
        screenLabel.setAlignment(Pos.CENTER);
        seatGridPane.add(screenLabel, 0, rows, cols, 1);
        
        // Show seat type legend
        showSeatTypeLegend();
    }
    
    private String getSeatTypeForRow(int row, int totalRows) {
        // First row is premium
        if (row == 0) {
            return "premium";
        }
        // Next vipRows are VIP
        else if (row < vipRows + 1) { // +1 because row 0 is premium
            return "vip";
        }
        // Remaining rows are standard
        else {
            return "standard";
        }
    }
    
    private void showSeatTypeLegend() {
        // You can add a legend box here to show seat type colors
        // This is optional but helpful for users
    }
    
    private void handleSeatClick(SeatButton seatBtn) {
        seatBtn.setSelected(!seatBtn.isSelected());
        
        if (seatBtn.isSelected()) {
            seatBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        } else {
            updateSeatButtonStyle(seatBtn);
        }
    }
    
    private void updateSeatButtonStyle(SeatButton seatBtn) {
        if (seatBtn.isBlocked()) {
            seatBtn.setStyle("-fx-background-color: #939698; -fx-text-fill: white;");
        } else {
            String seatType = seatBtn.getSeatType();
            switch (seatType) {
                case "premium":
                    seatBtn.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold;");
                    break;
                case "vip":
                    seatBtn.setStyle("-fx-background-color: #ff9900; -fx-text-fill: black; -fx-font-weight: bold;");
                    break;
                case "standard":
                    seatBtn.setStyle("-fx-background-color: #239f1a; -fx-text-fill: white;");
                    break;
                default:
                    seatBtn.setStyle("-fx-background-color: #239f1a; -fx-text-fill: white;");
            }
        }
    }
    
    @FXML
    private void handleSaveSeats() {
        String hallName = seatHallBox.getValue();
        if (hallName == null || seatButtons.isEmpty()) {
            showAlert("Error", "Please generate seats first and select a hall");
            return;
        }
        
        if (!hallMap.containsKey(hallName)) {
            // Create new hall if it doesn't exist
            createNewHall(hallName);
        }
        
        int hallId = hallMap.get(hallName);
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Delete existing seats for this hall
            String deleteSql = "DELETE FROM seats WHERE hall_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, hallId);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            
            // Insert new seats
            String insertSql = "INSERT INTO seats (hall_id, seat_row, seat_number, seat_label, seat_type, is_blocked, price_multiplier) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            
            for (SeatButton seatBtn : seatButtons) {
                String seatLabel = seatBtn.getSeatLabel();
                char rowChar = seatLabel.charAt(0);
                int colNum = Integer.parseInt(seatLabel.substring(1));
                
                double priceMultiplier = SEAT_MULTIPLIERS.getOrDefault(seatBtn.getSeatType(), 1.0);
                
                insertStmt.setInt(1, hallId);
                insertStmt.setString(2, String.valueOf(rowChar));
                insertStmt.setInt(3, colNum);
                insertStmt.setString(4, seatLabel);
                insertStmt.setString(5, seatBtn.getSeatType());
                insertStmt.setBoolean(6, seatBtn.isBlocked());
                insertStmt.setDouble(7, priceMultiplier);
                insertStmt.addBatch();
            }
            
            insertStmt.executeBatch();
            insertStmt.close();
            
            // Update seat count in cinema_halls table
            int totalSeats = seatButtons.size();
            int seatRows = seatButtons.stream().mapToInt(SeatButton::getRow).max().orElse(0) + 1;
            int seatCols = seatButtons.stream().mapToInt(SeatButton::getCol).max().orElse(0) + 1;
            
            updateHallSeatConfiguration(hallId, seatRows, seatCols, totalSeats);
            
            // Load the saved seats into the table view
            handleLoadSeats();
            
            showSuccessAlert("Success", "Seat layout saved successfully for " + hallName + "!");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save seats: " + e.getMessage());
        }
    }
    
    private void createNewHall(String hallName) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO cinema_halls (hall_name, seat_rows, seat_columns, total_seats) VALUES (?, 0, 0, 0)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, hallName);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int hallId = rs.getInt(1);
                hallMap.put(hallName, hallId);
                
                // Update combo boxes
                if (!seatHallBox.getItems().contains(hallName)) {
                    seatHallBox.getItems().add(hallName);
                    seatHallBox.setValue(hallName);
                }
                if (!seatHallBox2.getItems().contains(hallName)) {
                    seatHallBox2.getItems().add(hallName);
                    seatHallBox2.setValue(hallName);
                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateHallSeatConfiguration(int hallId, int rows, int cols, int totalSeats) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE cinema_halls SET seat_rows = ?, seat_columns = ?, total_seats = ? WHERE hall_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rows);
            pstmt.setInt(2, cols);
            pstmt.setInt(3, totalSeats);
            pstmt.setInt(4, hallId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLoadSeats() {
        String hallName = seatHallBox2.getValue();
        if (hallName == null) {
            showAlert("Error", "Please select a hall to load seats from");
            return;
        }
        
        if (!hallMap.containsKey(hallName)) {
            showAlert("Error", "Selected hall does not exist");
            return;
        }
        
        try {
            int hallId = hallMap.get(hallName);
            Connection conn = DatabaseConnection.getConnection();
            
            // First, get hall configuration
            String hallSql = "SELECT seat_rows, seat_columns FROM cinema_halls WHERE hall_id = ?";
            PreparedStatement hallStmt = conn.prepareStatement(hallSql);
            hallStmt.setInt(1, hallId);
            ResultSet hallRs = hallStmt.executeQuery();
            
            if (hallRs.next()) {
                currentSeatRows = hallRs.getInt("seat_rows");
                currentSeatCols = hallRs.getInt("seat_columns");
                
                // Update the form fields with the loaded values
                seatRowsField.setText(String.valueOf(currentSeatRows));
                seatColumnsField.setText(String.valueOf(currentSeatCols));
            }
            hallRs.close();
            hallStmt.close();
            
            // Load seat data
            String seatSql = "SELECT seat_id, seat_row, seat_number, seat_label, seat_type, is_blocked, price_multiplier as price " +
                        "FROM seats WHERE hall_id = ? ORDER BY seat_row, seat_number";
            
            PreparedStatement seatStmt = conn.prepareStatement(seatSql);
            seatStmt.setInt(1, hallId);
            ResultSet rs = seatStmt.executeQuery();
            
            seatDataList.clear();
            
            while (rs.next()) {
                SeatData seatData = new SeatData();
                seatData.setSeatId(rs.getInt("seat_id"));
                seatData.setSeatRow(rs.getString("seat_row"));
                seatData.setSeatNumber(rs.getInt("seat_number"));
                seatData.setSeatLabel(rs.getString("seat_label"));
                seatData.setSeatType(rs.getString("seat_type"));
                seatData.setBlocked(rs.getBoolean("is_blocked"));
                seatData.setPrice(rs.getDouble("price"));
                
                seatDataList.add(seatData);
            }
            
            seatTableView.setItems(seatDataList);
            
            // Load seats into the grid for visualization
            loadSeatsIntoGrid(hallId);
            
            // Update current configuration
            currentHallId = hallId;
            currentHallName = hallName;
            
            rs.close();
            seatStmt.close();
            
            showSuccessAlert("Success", "Loaded " + seatDataList.size() + " seats from " + hallName + 
                " (Configuration: " + currentSeatRows + " rows × " + currentSeatCols + " columns)");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load seats: " + e.getMessage());
        }
    }
    
    private void loadSeatsIntoGrid(int hallId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT seat_row, seat_number, seat_label, seat_type, is_blocked " +
                        "FROM seats WHERE hall_id = ? ORDER BY seat_row, seat_number";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, hallId);
            ResultSet rs = pstmt.executeQuery();
            
            // Clear existing grid
            seatGridPane.getChildren().clear();
            seatGridPane.getRowConstraints().clear();
            seatGridPane.getColumnConstraints().clear();
            
            seatButtons.clear();
            
            // Find max rows and columns
            int maxRow = 0;
            int maxCol = 0;
            List<SeatInfo> seatInfos = new java.util.ArrayList<>();
            
            while (rs.next()) {
                String seatRow = rs.getString("seat_row");
                int seatNumber = rs.getInt("seat_number");
                String seatLabel = rs.getString("seat_label");
                String seatType = rs.getString("seat_type");
                boolean isBlocked = rs.getBoolean("is_blocked");
                
                seatInfos.add(new SeatInfo(seatRow, seatNumber, seatLabel, seatType, isBlocked));
                
                int rowNum = seatRow.charAt(0) - 'A';
                int colNum = seatNumber - 1;
                
                if (rowNum > maxRow) maxRow = rowNum;
                if (colNum > maxCol) maxCol = colNum;
            }
            
            // Set up grid constraints
            for (int i = 0; i <= maxCol; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPrefWidth(40);
                seatGridPane.getColumnConstraints().add(colConst);
            }
            for (int i = 0; i <= maxRow; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPrefHeight(40);
                seatGridPane.getRowConstraints().add(rowConst);
            }
            
            // Create seat buttons
            for (SeatInfo seatInfo : seatInfos) {
                int rowNum = seatInfo.seatRow.charAt(0) - 'A';
                int colNum = seatInfo.seatNumber - 1;
                
                SeatButton seatBtn = new SeatButton(seatInfo.seatLabel, rowNum, colNum);
                seatBtn.setSeatType(seatInfo.seatType);
                seatBtn.setBlocked(seatInfo.isBlocked);
                
                // Set style based on seat type and status
                updateSeatButtonStyle(seatBtn);
                
                seatBtn.setOnAction(e -> handleSeatClick(seatBtn));
                seatButtons.add(seatBtn);
                
                seatGridPane.add(seatBtn, colNum, rowNum);
            }
            
            // Add screen label
            Label screenLabel = new Label("S C R E E N");
            screenLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #666;");
            screenLabel.setAlignment(Pos.CENTER);
            seatGridPane.add(screenLabel, 0, maxRow + 1, maxCol + 1, 1);
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void highlightSeatInGrid(String seatLabel) {
        // Reset all seats to their original colors
        for (SeatButton seatBtn : seatButtons) {
            updateSeatButtonStyle(seatBtn);
        }
        
        // Highlight the selected seat
        for (SeatButton seatBtn : seatButtons) {
            if (seatBtn.getSeatLabel().equals(seatLabel)) {
                seatBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
                break;
            }
        }
    }
    
    @FXML
    private void handleBlockSeat() {
        boolean anySeatSelected = false;
        
        for (SeatButton seatBtn : seatButtons) {
            if (seatBtn.isSelected() && !seatBtn.isBlocked()) {
                seatBtn.setBlocked(true);
                seatBtn.setSelected(false);
                seatBtn.setStyle("-fx-background-color: #939698; -fx-text-fill: white;");
                anySeatSelected = true;
            }
        }
        
        if (!anySeatSelected) {
            showAlert("Info", "Please select a seat to block");
        }
    }
    
    @FXML
    private void handleUnblockSeat() {
        boolean anySeatSelected = false;
        
        for (SeatButton seatBtn : seatButtons) {
            if (seatBtn.isSelected() && seatBtn.isBlocked()) {
                seatBtn.setBlocked(false);
                seatBtn.setSelected(false);
                updateSeatButtonStyle(seatBtn);
                anySeatSelected = true;
            }
        }
        
        if (!anySeatSelected) {
            showAlert("Info", "Please select a blocked seat to unblock");
        }
    }
    
    @FXML
    private void handleResetSeats() {
        for (SeatButton seatBtn : seatButtons) {
            seatBtn.setBlocked(false);
            seatBtn.setSelected(false);
            updateSeatButtonStyle(seatBtn);
        }
    }
    
    // ==================== UPDATED HALL SEAT CONFIGURATION ====================
    
    @FXML
    private void handleUpdateHallSeats() {
        if (currentHallId == -1 || currentHallName == null || currentHallName.isEmpty()) {
            showAlert("Selection Error", "Please select a hall from the dropdown first (use Load button)");
            return;
        }
        
        // Get new configuration from form
        String newRowsText = newSeatRowsField.getText().trim();
        String newColsText = newSeatColumnsField.getText().trim();
        String seatType = newSeatTypeBox.getValue();
        boolean keepBlocked = keepExistingBlockedCheck.isSelected();
        
        if (newRowsText.isEmpty() || newColsText.isEmpty() || seatType == null) {
            showAlert("Validation Error", "Please fill all required configuration fields");
            return;
        }
        
        try {
            int newRows = Integer.parseInt(newRowsText);
            int newCols = Integer.parseInt(newColsText);
            
            if (newRows <= 0 || newCols <= 0 || newRows > 20 || newCols > 30) {
                showAlert("Input Error", "Rows must be 1-20 and columns 1-30");
                return;
            }
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Update Hall Configuration");
            confirmAlert.setHeaderText("Update Hall Seat Layout");
            confirmAlert.setContentText(
                "Are you sure you want to update the seat layout for '" + currentHallName + "'?\n\n" +
                "Current: " + currentSeatRows + " rows × " + currentSeatCols + " columns\n" +
                "New: " + newRows + " rows × " + newCols + " columns\n\n" +
                "This will regenerate ALL seats in this hall.\n" +
                (keepBlocked ? "Existing blocked seats will be preserved where possible.\n" : "") +
                "WARNING: This action cannot be undone!"
            );
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    updateHallSeatLayout(newRows, newCols, seatType, keepBlocked);
                }
            });
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for rows and columns");
        }
    }
    
    private void updateHallSeatLayout(int newRows, int newCols, String seatType, boolean keepBlocked) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get existing blocked seats if we need to preserve them
            Map<String, Boolean> existingBlockedSeats = new HashMap<>();
            if (keepBlocked) {
                String getBlockedSql = "SELECT seat_label, is_blocked FROM seats WHERE hall_id = ? AND is_blocked = TRUE";
                PreparedStatement getBlockedStmt = conn.prepareStatement(getBlockedSql);
                getBlockedStmt.setInt(1, currentHallId);
                ResultSet rs = getBlockedStmt.executeQuery();
                
                while (rs.next()) {
                    existingBlockedSeats.put(rs.getString("seat_label"), rs.getBoolean("is_blocked"));
                }
                rs.close();
                getBlockedStmt.close();
            }
            
            // Delete all existing seats for this hall
            String deleteSql = "DELETE FROM seats WHERE hall_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, currentHallId);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            
            // Insert new seats with the updated configuration
            String insertSql = "INSERT INTO seats (hall_id, seat_row, seat_number, seat_label, seat_type, is_blocked, price_multiplier) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            
            for (int row = 0; row < newRows; row++) {
                for (int col = 0; col < newCols; col++) {
                    String seatLabel = (char) ('A' + row) + String.valueOf(col + 1);
                    
                    // Check if this seat was blocked in the old layout
                    boolean isBlocked = existingBlockedSeats.containsKey(seatLabel) && existingBlockedSeats.get(seatLabel);
                    
                    // Determine seat type based on row
                    String actualSeatType = getSeatTypeForRow(row, newRows);
                    
                    // Calculate price multiplier based on seat type
                    double priceMultiplier = SEAT_MULTIPLIERS.getOrDefault(actualSeatType, 1.0);
                    
                    insertStmt.setInt(1, currentHallId);
                    insertStmt.setString(2, String.valueOf((char) ('A' + row)));
                    insertStmt.setInt(3, col + 1);
                    insertStmt.setString(4, seatLabel);
                    insertStmt.setString(5, actualSeatType);
                    insertStmt.setBoolean(6, isBlocked);
                    insertStmt.setDouble(7, priceMultiplier);
                    insertStmt.addBatch();
                }
            }
            
            insertStmt.executeBatch();
            insertStmt.close();
            
            // Update hall configuration in cinema_halls table
            int totalSeats = newRows * newCols;
            updateHallSeatConfiguration(currentHallId, newRows, newCols, totalSeats);
            
            // Update current configuration
            currentSeatRows = newRows;
            currentSeatCols = newCols;
            
            // Reload the seats
            handleLoadSeats();
            
            // Update the configuration form
            newSeatRowsField.setText(String.valueOf(newRows));
            newSeatColumnsField.setText(String.valueOf(newCols));
            
            showSuccessAlert("Success", 
                "Hall seat layout updated successfully!\n" +
                "New configuration: " + newRows + " rows × " + newCols + " columns\n" +
                "Total seats: " + totalSeats + "\n" +
                "Seat types: Premium (Row A), VIP (Rows B-C), Standard (remaining rows)");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update hall seat layout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteSeat() {
        if (selectedSeatData == null) {
            showAlert("Selection Error", "Please select a seat from the table to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Seat");
        confirmAlert.setContentText("Are you sure you want to delete seat '" + selectedSeatData.getSeatLabel() + "'?\nThis action cannot be undone.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM seats WHERE seat_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedSeatData.getSeatId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        seatDataList.remove(selectedSeatData);
                        
                        for (SeatButton seatBtn : seatButtons) {
                            if (seatBtn.getSeatLabel().equals(selectedSeatData.getSeatLabel())) {
                                seatGridPane.getChildren().remove(seatBtn);
                                seatButtons.remove(seatBtn);
                                break;
                            }
                        }
                        
                        // Update total seats in hall
                        updateHallSeatConfiguration(currentHallId, currentSeatRows, currentSeatCols, seatButtons.size());
                        
                        showSuccessAlert("Success", "Seat deleted successfully!");
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to delete seat: " + e.getMessage());
                }
            }
        });
    }
    
    // ==================== CONTENT MANAGEMENT ====================
    
    private void loadContent() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cinema_name, description, address, phone_numbers, emails, working_hours, banner_image " +
                        "FROM cinema_info LIMIT 1";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                cinemaNameField.setText(rs.getString("cinema_name"));
                cinemaDescriptionArea.setText(rs.getString("description"));
                cinemaAddressField.setText(rs.getString("address"));
                
                String phoneJson = rs.getString("phone_numbers");
                if (phoneJson != null && phoneJson.startsWith("[")) {
                    phoneJson = phoneJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!phoneJson.isEmpty()) {
                        String[] phones = phoneJson.split(",");
                        cinemaPhoneField.setText(phones.length > 0 ? phones[0].trim() : "");
                    }
                }
                
                String emailJson = rs.getString("emails");
                if (emailJson != null && emailJson.startsWith("[")) {
                    emailJson = emailJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!emailJson.isEmpty()) {
                        String[] emails = emailJson.split(",");
                        cinemaEmailField.setText(emails.length > 0 ? emails[0].trim() : "");
                    }
                }
                
                cinemaHoursField.setText(rs.getString("working_hours"));
                
                String bannerImagePath = rs.getString("banner_image");
                if (bannerImagePath != null && !bannerImagePath.isEmpty()) {
                    try {
                        java.io.File file = new java.io.File(bannerImagePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            aboutUsImageView.setImage(image);
                            selectedAboutUsImagePath = bannerImagePath;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void saveAboutUsContent() {
        String cinemaName = cinemaNameField.getText().trim();
        String description = cinemaDescriptionArea.getText().trim();
        String address = cinemaAddressField.getText().trim();
        String phone = cinemaPhoneField.getText().trim();
        String email = cinemaEmailField.getText().trim();
        String hours = cinemaHoursField.getText().trim();
        
        if (cinemaName.isEmpty() || description.isEmpty() || address.isEmpty() || 
            phone.isEmpty() || email.isEmpty() || hours.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String checkSql = "SELECT COUNT(*) FROM cinema_info";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            String sql;
            if (count > 0) {
                sql = "UPDATE cinema_info SET cinema_name = ?, description = ?, address = ?, " +
                      "phone_numbers = ?, emails = ?, working_hours = ?, banner_image = ? " +
                      "WHERE info_id = 1";
            } else {
                sql = "INSERT INTO cinema_info (cinema_name, description, address, phone_numbers, " +
                      "emails, working_hours, banner_image) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
            }
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cinemaName);
            pstmt.setString(2, description);
            pstmt.setString(3, address);
            pstmt.setString(4, "[\"" + phone + "\"]");
            pstmt.setString(5, "[\"" + email + "\"]");
            pstmt.setString(6, hours);
            pstmt.setString(7, selectedAboutUsImagePath);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "About Us content saved successfully!");
            }
            
            pstmt.close();
            checkStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save About Us content: " + e.getMessage());
        }
    }
    
    // ==================== FAQ MANAGEMENT ====================
    
    private void loadFAQs() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT faq_id, question, answer FROM faqs WHERE is_active = TRUE ORDER BY display_order";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<FAQ> faqs = FXCollections.observableArrayList();
            
            while (rs.next()) {
                FAQ faq = new FAQ();
                faq.setFaqId(rs.getInt("faq_id"));
                faq.setQuestion(rs.getString("question"));
                faq.setAnswer(rs.getString("answer"));
                
                faqs.add(faq);
            }
            
            faqTableView.setItems(faqs);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load FAQs: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddFAQ() {
        String question = faqQuestionField.getText().trim();
        String answer = faqAnswerArea.getText().trim();
        
        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Validation Error", "Please fill both question and answer");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO faqs (question, answer, category, display_order, is_active) " +
                        "VALUES (?, ?, 'General', 0, TRUE)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, question);
            pstmt.setString(2, answer);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "FAQ added successfully!");
                faqQuestionField.clear();
                faqAnswerArea.clear();
                loadFAQs();
            }
            
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to add FAQ: " + e.getMessage());
        }
    }
    
    private void handleDeleteFAQ(FAQ faq) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete FAQ");
        confirmAlert.setContentText("Are you sure you want to delete this FAQ?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM faqs WHERE faq_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, faq.getFaqId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        showSuccessAlert("Success", "FAQ deleted successfully!");
                        loadFAQs();
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to delete FAQ: " + e.getMessage());
                }
            }
        });
    }
    
    // ==================== SHOWTIME MANAGEMENT ====================
    
    private void loadMoviesForShowtime() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, price FROM movies WHERE is_active = TRUE AND category = 'Now Showing' ORDER BY title";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<String> movies = FXCollections.observableArrayList();
            
            while (rs.next()) {
                String movieTitle = rs.getString("title");
                double moviePrice = rs.getDouble("price");
                movies.add(movieTitle + " (ETB " + String.format("%.2f", moviePrice) + ")");
            }
            
            showtimeMovieBox.setItems(movies);
            
            // Also update the filter combo box to only show "Now Showing" movies
            if (showtimeFilterMovie != null) {
                ObservableList<String> filterMovies = FXCollections.observableArrayList();
                filterMovies.add("All Movies");
                
                // Reload the movies for the filter
                sql = "SELECT title FROM movies WHERE is_active = TRUE AND category = 'Now Showing' ORDER BY title";
                PreparedStatement filterStmt = conn.prepareStatement(sql);
                ResultSet filterRs = filterStmt.executeQuery();
                
                while (filterRs.next()) {
                    filterMovies.add(filterRs.getString("title"));
                }
                
                showtimeFilterMovie.setItems(filterMovies);
                showtimeFilterMovie.setValue("All Movies");
                
                filterRs.close();
                filterStmt.close();
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load movies for showtime: " + e.getMessage());
        }
    }
    
    private void loadMoviesForFilter() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT title FROM movies WHERE is_active = TRUE ORDER BY title";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<String> movies = FXCollections.observableArrayList();
            movies.add("All Movies");
            
            while (rs.next()) {
                movies.add(rs.getString("title"));
            }
            
            showtimeFilterMovie.setItems(movies);
            showtimeFilterMovie.setValue("All Movies");
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadShowtimes() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.showtime_id, s.movie_id, m.title, s.show_date, s.show_time, " +
                        "h.hall_name, s.price, s.available_seats, s.notes, s.is_active " +
                        "FROM showtimes s " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                        "WHERE s.is_active = TRUE " +
                        "ORDER BY s.show_date DESC, s.show_time DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Showtime> showtimes = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Showtime showtime = new Showtime();
                showtime.setShowtimeId(rs.getInt("showtime_id"));
                showtime.setMovieId(rs.getInt("movie_id"));
                showtime.setMovieTitle(rs.getString("title"));
                
                java.sql.Date sqlDate = rs.getDate("show_date");
                if (sqlDate != null) {
                    showtime.setShowDate(sqlDate.toLocalDate());
                }
                
                showtime.setShowTime(rs.getString("show_time"));
                showtime.setHallName(rs.getString("hall_name"));
                showtime.setPrice(rs.getDouble("price"));
                showtime.setAvailableSeats(rs.getInt("available_seats"));
                showtime.setNotes(rs.getString("notes"));
                showtime.setActive(rs.getBoolean("is_active"));
                
                showtimes.add(showtime);
            }
            
            showtimeTableView.setItems(showtimes);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load showtimes: " + e.getMessage());
        }
    }
    
    private void fillShowtimeForm(Showtime showtime) {
        // Format movie display with price
        String movieDisplay = showtime.getMovieTitle() + " (ETB " + String.format("%.2f", showtime.getPrice()) + ")";
        showtimeMovieBox.setValue(movieDisplay);
        showtimeDate.setValue(showtime.getShowDate());
        showtimeTimeField.setText(showtime.getShowTime());
        showtimeHallField.setText(showtime.getHallName());
        showtimePriceField.setText(String.format("%.2f", showtime.getPrice()));
        showtimeNotesArea.setText(showtime.getNotes() != null ? showtime.getNotes() : "");
    }
    
    @FXML
    private void handleAddShowtime() {
        String movieDisplay = showtimeMovieBox.getValue();
        LocalDate date = showtimeDate.getValue();
        String time = showtimeTimeField.getText().trim();
        String hallName = showtimeHallField.getText().trim();
        String priceText = showtimePriceField.getText().trim();
        String notes = showtimeNotesArea.getText().trim();
        
        if (movieDisplay == null || date == null || time.isEmpty() || hallName.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        if (!isValidTimeFormat(time)) {
            showAlert("Time Format Error", "Please enter time in HH:mm format (e.g., 14:30)");
            return;
        }
        
        try {
        	String movieTitle = movieDisplay.split(" \\(")[0];
            
            double moviePrice = getMoviePriceByTitle(movieTitle);
            if (moviePrice == -1) {
                showAlert("Error", "Selected movie not found");
                return;
            }
            
            double price;
            if (priceText.isEmpty()) {
                price = moviePrice;
                showtimePriceField.setText(String.format("%.2f", price));
            } else {
                price = Double.parseDouble(priceText);
            }
            
            int movieId = getMovieIdByTitle(movieTitle);
            if (movieId == -1) {
                showAlert("Error", "Selected movie not found");
                return;
            }
            
            if (!hallMap.containsKey(hallName)) {
                showAlert("Error", "Hall '" + hallName + "' not found. Please create it first in Seat Management.");
                return;
            }
            int hallId = hallMap.get(hallName);
            
            int totalSeats = getTotalSeatsForHall(hallId);
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO showtimes (movie_id, hall_id, show_date, show_time, price, " +
                        "available_seats, notes, is_active) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, hallId);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setString(4, time);
            pstmt.setDouble(5, price);
            pstmt.setInt(6, totalSeats);
            pstmt.setString(7, notes);
            pstmt.setBoolean(8, true);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Showtime added successfully!");
                clearShowtimeForm();
                loadShowtimes();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to add showtime: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateShowtime() {
        Showtime selectedShowtime = showtimeTableView.getSelectionModel().getSelectedItem();
        if (selectedShowtime == null) {
            showAlert("Selection Error", "Please select a showtime to update");
            return;
        }
        
        String movieDisplay = showtimeMovieBox.getValue();
        LocalDate date = showtimeDate.getValue();
        String time = showtimeTimeField.getText().trim();
        String hallName = showtimeHallField.getText().trim();
        String priceText = showtimePriceField.getText().trim();
        String notes = showtimeNotesArea.getText().trim();
        
        if (movieDisplay == null || date == null || time.isEmpty() || hallName.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return;
        }
        
        if (!isValidTimeFormat(time)) {
            showAlert("Time Format Error", "Please enter time in HH:mm format (e.g., 14:30)");
            return;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            
            String movieTitle = movieDisplay.split(" \\(")[0];
            
            int movieId = getMovieIdByTitle(movieTitle);
            if (movieId == -1) {
                showAlert("Error", "Selected movie not found");
                return;
            }
            
            if (!hallMap.containsKey(hallName)) {
                showAlert("Error", "Hall '" + hallName + "' not found");
                return;
            }
            int hallId = hallMap.get(hallName);
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE showtimes SET movie_id = ?, hall_id = ?, show_date = ?, show_time = ?, " +
                        "price = ?, notes = ? WHERE showtime_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, hallId);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setString(4, time);
            pstmt.setDouble(5, price);
            pstmt.setString(6, notes);
            pstmt.setInt(7, selectedShowtime.getShowtimeId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Showtime updated successfully!");
                clearShowtimeForm();
                loadShowtimes();
            }
            
            pstmt.close();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid price");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update showtime: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteShowtime() {
        Showtime selectedShowtime = showtimeTableView.getSelectionModel().getSelectedItem();
        if (selectedShowtime == null) {
            showAlert("Selection Error", "Please select a showtime to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Showtime");
        confirmAlert.setContentText("Are you sure you want to delete this showtime?\nThis will also delete all associated bookings.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    
                    String deleteSql = "DELETE FROM showtimes WHERE showtime_id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    deleteStmt.setInt(1, selectedShowtime.getShowtimeId());
                    
                    int rowsAffected = deleteStmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        showSuccessAlert("Success", "Showtime deleted successfully!");
                        clearShowtimeForm();
                        loadShowtimes();
                    }
                    
                    deleteStmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to delete showtime: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void filterShowtimes() {
        LocalDate filterDate = showtimeFilterDate.getValue();
        String filterMovie = showtimeFilterMovie.getValue();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT s.showtime_id, s.movie_id, m.title, s.show_date, s.show_time, " +
                "h.hall_name, s.price, s.available_seats, s.notes, s.is_active " +
                "FROM showtimes s " +
                "JOIN movies m ON s.movie_id = m.movie_id " +
                "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                "WHERE s.is_active = TRUE "
            );
            
            if (filterDate != null) {
                sql.append("AND s.show_date = ? ");
            }
            
            if (filterMovie != null && !filterMovie.equals("All Movies")) {
                sql.append("AND m.title = ? ");
            }
            
            sql.append("ORDER BY s.show_date DESC, s.show_time DESC");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (filterDate != null) {
                pstmt.setDate(paramIndex++, java.sql.Date.valueOf(filterDate));
            }
            
            if (filterMovie != null && !filterMovie.equals("All Movies")) {
                pstmt.setString(paramIndex++, filterMovie);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Showtime> showtimes = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Showtime showtime = new Showtime();
                showtime.setShowtimeId(rs.getInt("showtime_id"));
                showtime.setMovieId(rs.getInt("movie_id"));
                showtime.setMovieTitle(rs.getString("title"));
                
                java.sql.Date sqlDate = rs.getDate("show_date");
                if (sqlDate != null) {
                    showtime.setShowDate(sqlDate.toLocalDate());
                }
                
                showtime.setShowTime(rs.getString("show_time"));
                showtime.setHallName(rs.getString("hall_name"));
                showtime.setPrice(rs.getDouble("price"));
                showtime.setAvailableSeats(rs.getInt("available_seats"));
                showtime.setNotes(rs.getString("notes"));
                showtime.setActive(rs.getBoolean("is_active"));
                
                showtimes.add(showtime);
            }
            
            showtimeTableView.setItems(showtimes);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to filter showtimes: " + e.getMessage());
        }
    }
    
    private void clearShowtimeForm() {
        showtimeMovieBox.setValue(null);
        showtimeDate.setValue(LocalDate.now());
        showtimeTimeField.setText(LocalTime.now().format(timeFormatter));
        showtimeHallField.clear();
        showtimePriceField.clear();
        showtimeNotesArea.clear();
        showtimeTableView.getSelectionModel().clearSelection();
    }
    
    // ==================== CUSTOMER MANAGEMENT ====================
    
    private void loadCustomers() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.phone_number as phone, " +
                        "u.created_at, u.is_active, " +
                        "COUNT(b.booking_id) as total_bookings, " +
                        "COALESCE(SUM(b.total_amount), 0) as total_spent " +
                        "FROM users u " +
                        "LEFT JOIN bookings b ON u.user_id = b.user_id " +
                        "WHERE u.user_type = 'customer' " +
                        "GROUP BY u.user_id " +
                        "ORDER BY u.user_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("user_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                
                java.sql.Timestamp joinTimestamp = rs.getTimestamp("created_at");
                if (joinTimestamp != null) {
                    customer.setJoinDate(joinTimestamp.toLocalDateTime().toLocalDate().toString());
                }
                
                customer.setTotalBookings(rs.getInt("total_bookings"));
                customer.setTotalSpent(rs.getDouble("total_spent"));
                customer.setActive(rs.getBoolean("is_active"));
                
                customers.add(customer);
            }
            
            customerTableView.setItems(customers);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load customers: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleViewCustomer() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("Selection Error", "Please select a customer to view");
            return;
        }
        
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Customer Details");
        infoAlert.setHeaderText("Customer Profile: " + selectedCustomer.getFullName());
        infoAlert.setContentText(
            "Customer ID: " + selectedCustomer.getCustomerId() + "\n" +
            "Name: " + selectedCustomer.getFullName() + "\n" +
            "Email: " + selectedCustomer.getEmail() + "\n" +
            "Phone: " + selectedCustomer.getPhone() + "\n" +
            "Join Date: " + selectedCustomer.getJoinDate() + "\n" +
            "Total Bookings: " + selectedCustomer.getTotalBookings() + "\n" +
            "Total Spent: ETB " + String.format("%.2f", selectedCustomer.getTotalSpent()) + "\n" +
            "Status: " + (selectedCustomer.isActive() ? "Active" : "Inactive")
        );
        infoAlert.showAndWait();
    }
    
    @FXML
    private void handleEditCustomer() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("Selection Error", "Please select a customer to edit");
            return;
        }
        
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");
        dialog.setHeaderText("Edit Customer: " + selectedCustomer.getFullName());
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField firstNameField = new TextField(selectedCustomer.getFirstName());
        TextField lastNameField = new TextField(selectedCustomer.getLastName());
        TextField emailField = new TextField(selectedCustomer.getEmail());
        TextField phoneField = new TextField(selectedCustomer.getPhone());
        CheckBox activeCheck = new CheckBox("Active");
        activeCheck.setSelected(selectedCustomer.isActive());
        
        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(activeCheck, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedCustomer.setFirstName(firstNameField.getText());
                selectedCustomer.setLastName(lastNameField.getText());
                selectedCustomer.setEmail(emailField.getText());
                selectedCustomer.setPhone(phoneField.getText());
                selectedCustomer.setActive(activeCheck.isSelected());
                return selectedCustomer;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(result -> {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, is_active = ? WHERE user_id = ?";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, result.getFirstName());
                pstmt.setString(2, result.getLastName());
                pstmt.setString(3, result.getEmail());
                pstmt.setString(4, result.getPhone());
                pstmt.setBoolean(5, result.isActive());
                pstmt.setInt(6, result.getCustomerId());
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    showSuccessAlert("Success", "Customer updated successfully!");
                    loadCustomers();
                }
                
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Database Error", "Failed to update customer: " + e.getMessage());
            }
        });
    }
    
    @FXML
    private void handleDeactivateCustomer() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("Selection Error", "Please select a customer to deactivate");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deactivation");
        confirmAlert.setHeaderText("Deactivate Customer");
        confirmAlert.setContentText("Are you sure you want to deactivate '" + selectedCustomer.getFullName() + "'?\nThey will no longer be able to log in.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setBoolean(1, !selectedCustomer.isActive());
                    pstmt.setInt(2, selectedCustomer.getCustomerId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        String action = selectedCustomer.isActive() ? "deactivated" : "activated";
                        showSuccessAlert("Success", "Customer " + action + " successfully!");
                        loadCustomers();
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to update customer status: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleExportCustomers() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Customers to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fileChooser.setInitialFileName("customers_export_" + LocalDate.now() + ".csv");
            
            java.io.File file = fileChooser.showSaveDialog(exportCustomersBtn.getScene().getWindow());
            if (file != null) {
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                writer.println("Customer ID,First Name,Last Name,Email,Phone,Join Date,Total Bookings,Total Spent,Status");
                
                for (Customer customer : customerTableView.getItems()) {
                    writer.println(
                        customer.getCustomerId() + "," +
                        customer.getFirstName() + "," +
                        customer.getLastName() + "," +
                        customer.getEmail() + "," +
                        customer.getPhone() + "," +
                        customer.getJoinDate() + "," +
                        customer.getTotalBookings() + "," +
                        customer.getTotalSpent() + "," +
                        (customer.isActive() ? "Active" : "Inactive")
                    );
                }
                
                writer.close();
                showSuccessAlert("Export Successful", "Customers exported to: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Export Error", "Failed to export customers: " + e.getMessage());
        }
    }
    
    // ==================== BOOKING MANAGEMENT ====================
    
    private void loadBookings() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.booking_id, CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                        "m.title as movie_title, DATE(b.booking_date) as booking_date, " +
                        "s.show_time, " +
                        "COALESCE((SELECT GROUP_CONCAT(se.seat_label SEPARATOR ', ') FROM booking_seats bs " +
                        "JOIN seats se ON bs.seat_id = se.seat_id WHERE bs.booking_id = b.booking_id), 'No seats') as seat_numbers, " +
                        "b.total_amount, b.booking_status as status, b.payment_method " +
                        "FROM bookings b " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "ORDER BY b.booking_date DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Booking> bookings = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setMovieTitle(rs.getString("movie_title"));
                booking.setBookingDate(rs.getDate("booking_date").toString());
                booking.setBookingTime(rs.getString("show_time"));
                
                String seatNumbers = rs.getString("seat_numbers");
                if ("No seats".equals(seatNumbers)) {
                    booking.setSeatNumbers("Not assigned");
                } else {
                    booking.setSeatNumbers(seatNumbers);
                }
                
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setStatus(rs.getString("status"));
                booking.setPaymentMethod(rs.getString("payment_method"));
                
                bookings.add(booking);
            }
            
            bookingTableView.setItems(bookings);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load bookings: " + e.getMessage());
        }
    }
    @FXML
    private void handleConfirmBooking() {
        Booking selectedBooking = bookingTableView.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Selection Error", "Please select a booking to confirm");
            return;
        }
        
        if (selectedBooking.getStatus().equals("confirmed") || selectedBooking.getStatus().equals("Confirmed")) {
            showAlert("Info", "This booking is already confirmed");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Booking");
        confirmAlert.setHeaderText("Confirm Booking");
        confirmAlert.setContentText("Are you sure you want to confirm booking #" + selectedBooking.getBookingId() + "?\nThis will update the booking status to 'confirmed'.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "UPDATE bookings SET booking_status = 'confirmed' WHERE booking_id = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedBooking.getBookingId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        showSuccessAlert("Success", "Booking #" + selectedBooking.getBookingId() + " confirmed successfully!");
                        loadBookings();
                    }
                    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to confirm booking: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleCancelBooking() {
        Booking selectedBooking = bookingTableView.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Selection Error", "Please select a booking to cancel");
            return;
        }
        
        if (selectedBooking.getStatus().equals("cancelled") || selectedBooking.getStatus().equals("Cancelled")) {
            showAlert("Info", "This booking is already cancelled");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Booking");
        confirmAlert.setHeaderText("Cancel Booking");
        confirmAlert.setContentText("Are you sure you want to cancel booking #" + selectedBooking.getBookingId() + "?\nThis action cannot be undone.");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    
                    // Get seat IDs from booking_seats
                    String getSeatsSql = "SELECT seat_id FROM booking_seats WHERE booking_id = ?";
                    PreparedStatement getSeatsStmt = conn.prepareStatement(getSeatsSql);
                    getSeatsStmt.setInt(1, selectedBooking.getBookingId());
                    ResultSet seatsRs = getSeatsStmt.executeQuery();
                    
                    List<Integer> seatIds = new java.util.ArrayList<>();
                    while (seatsRs.next()) {
                        seatIds.add(seatsRs.getInt("seat_id"));
                    }
                    seatsRs.close();
                    getSeatsStmt.close();
                    
                    // Update booking status
                    String updateBookingSql = "UPDATE bookings SET booking_status = 'cancelled' WHERE booking_id = ?";
                    PreparedStatement updateBookingStmt = conn.prepareStatement(updateBookingSql);
                    updateBookingStmt.setInt(1, selectedBooking.getBookingId());
                    updateBookingStmt.executeUpdate();
                    updateBookingStmt.close();
                    
                    showSuccessAlert("Success", "Booking #" + selectedBooking.getBookingId() + " cancelled successfully!");
                    loadBookings();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Database Error", "Failed to cancel booking: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleViewBookingDetails() {
        Booking selectedBooking = bookingTableView.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Selection Error", "Please select a booking to view details");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.*, u.email, u.phone_number as phone, m.title, s.show_date, s.show_time, h.hall_name, " +
                        "COALESCE((SELECT GROUP_CONCAT(se.seat_label SEPARATOR ', ') FROM booking_seats bs " +
                        "JOIN seats se ON bs.seat_id = se.seat_id WHERE bs.booking_id = b.booking_id), 'No seats assigned') as seat_numbers " +
                        "FROM bookings b " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                        "JOIN movies m ON s.movie_id = m.movie_id " +
                        "JOIN cinema_halls h ON s.hall_id = h.hall_id " +
                        "WHERE b.booking_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, selectedBooking.getBookingId());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Booking Details");
                infoAlert.setHeaderText("Booking #" + selectedBooking.getBookingId() + " Details");
                
                String seatNumbers = rs.getString("seat_numbers");
                if ("No seats assigned".equals(seatNumbers)) {
                    seatNumbers = "Not assigned";
                }
                
                String details = "Booking ID: " + rs.getInt("booking_id") + "\n" +
                               "Customer: " + selectedBooking.getCustomerName() + "\n" +
                               "Email: " + rs.getString("email") + "\n" +
                               "Phone: " + rs.getString("phone") + "\n" +
                               "Movie: " + rs.getString("title") + "\n" +
                               "Date: " + rs.getDate("show_date") + "\n" +
                               "Time: " + rs.getString("show_time") + "\n" +
                               "Hall: " + rs.getString("hall_name") + "\n" +
                               "Seats: " + seatNumbers + "\n" +
                               "Total Amount: ETB " + String.format("%.2f", rs.getDouble("total_amount")) + "\n" +
                               "Status: " + rs.getString("booking_status") + "\n" +
                               "Payment Method: " + rs.getString("payment_method") + "\n" +
                               "Payment Status: " + rs.getString("payment_status") + "\n" +
                               "Booking Date: " + rs.getTimestamp("booking_date");
                
                infoAlert.setContentText(details);
                infoAlert.showAndWait();
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load booking details: " + e.getMessage());
        }
    }
    @FXML
    private void handleSendBookingSMS() {
        Booking selectedBooking = bookingTableView.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Selection Error", "Please select a booking to send SMS");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.phone_number FROM bookings b JOIN users u ON b.user_id = u.user_id WHERE b.booking_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, selectedBooking.getBookingId());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String phoneNumber = rs.getString("phone_number");
                
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("SMS Sent");
                infoAlert.setHeaderText("SMS Notification");
                infoAlert.setContentText("Booking confirmation SMS has been sent to:\n" + phoneNumber + "\n\nMessage: Your booking #" + 
                    selectedBooking.getBookingId() + " for '" + selectedBooking.getMovieTitle() + "' is confirmed. Seats: " + 
                    selectedBooking.getSeatNumbers() + ". Thank you!");
                infoAlert.showAndWait();
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("SMS Error", "Failed to send SMS: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleExportBookings() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Bookings to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fileChooser.setInitialFileName("bookings_export_" + LocalDate.now() + ".csv");
            
            java.io.File file = fileChooser.showSaveDialog(exportBookingsBtn.getScene().getWindow());
            if (file != null) {
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                writer.println("Booking ID,Customer,Movie,Date,Time,Seats,Total Amount,Status,Payment Method");
                
                for (Booking booking : bookingTableView.getItems()) {
                    writer.println(
                        booking.getBookingId() + "," +
                        booking.getCustomerName() + "," +
                        booking.getMovieTitle() + "," +
                        booking.getBookingDate() + "," +
                        booking.getBookingTime() + "," +
                        (booking.getSeatNumbers() != null ? booking.getSeatNumbers() : "") + "," +
                        booking.getTotalAmount() + "," +
                        booking.getStatus() + "," +
                        booking.getPaymentMethod()
                    );
                }
                
                writer.close();
                showSuccessAlert("Export Successful", "Bookings exported to: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Export Error", "Failed to export bookings: " + e.getMessage());
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    private int getMovieIdByTitle(String title) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id FROM movies WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
            
            rs.close();
            pstmt.close();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    private double getMoviePriceByTitle(String displayText) {
        try {
            // Extract movie title from display text (remove price part)
            String title = displayText;
            if (displayText.contains("(ETB")) {
                title = displayText.split(" \\(")[0];
            }
            
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT price FROM movies WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("price");
            }
            
            rs.close();
            pstmt.close();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    private int getTotalSeatsForHall(int hallId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) as total_seats FROM seats WHERE hall_id = ? AND is_blocked = FALSE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, hallId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total_seats");
            }
            
            rs.close();
            pstmt.close();
            return 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 100;
        }
    }
    
    private int convertDurationToMinutes(String duration) {
        int minutes = 0;
        try {
            if (duration.contains("h")) {
                String[] parts = duration.split("h");
                int hours = Integer.parseInt(parts[0].trim());
                minutes = hours * 60;
                
                if (parts.length > 1 && parts[1].contains("m")) {
                    String minutePart = parts[1].replace("m", "").trim();
                    if (!minutePart.isEmpty()) {
                        minutes += Integer.parseInt(minutePart);
                    }
                }
            } else if (duration.contains("m")) {
                minutes = Integer.parseInt(duration.replace("m", "").trim());
            } else {
                minutes = Integer.parseInt(duration);
            }
        } catch (NumberFormatException e) {
            showAlert("Duration Error", "Please enter duration in format: '2h 30m' or '150m'");
        }
        return minutes;
    }
    
    private void loadHelpContent() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT phone_numbers, emails, working_hours FROM cinema_info LIMIT 1";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String phoneJson = rs.getString("phone_numbers");
                String emailJson = rs.getString("emails");
                String hours = rs.getString("working_hours");
                
                if (helpPhoneField != null && phoneJson != null && phoneJson.startsWith("[")) {
                    phoneJson = phoneJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!phoneJson.isEmpty()) {
                        String[] phones = phoneJson.split(",");
                        helpPhoneField.setText(phones.length > 0 ? phones[0].trim() : "");
                    }
                }
                
                if (helpEmailField != null && emailJson != null && emailJson.startsWith("[")) {
                    emailJson = emailJson.replace("[", "").replace("]", "").replace("\"", "");
                    if (!emailJson.isEmpty()) {
                        String[] emails = emailJson.split(",");
                        helpEmailField.setText(emails.length > 0 ? emails[0].trim() : "");
                    }
                }
                
                if (helpInstructionsArea != null) {
                    helpInstructionsArea.setText("For assistance, please call us during business hours or send an email.");
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void saveHelpContent() {
        String phone = helpPhoneField.getText().trim();
        String email = helpEmailField.getText().trim();
        String instructions = helpInstructionsArea.getText().trim();
        
        if (phone.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Please fill phone and email fields");
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE cinema_info SET phone_numbers = ?, emails = ? WHERE info_id = 1";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "[\"" + phone + "\"]");
            pstmt.setString(2, "[\"" + email + "\"]");
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showSuccessAlert("Success", "Help content saved successfully!");
            }
            
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save help content: " + e.getMessage());
        }
    }
    
    private void initializeSearchFunctionality() {
        if (searchMovieField != null) {
            searchMovieField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterMovies(newValue);
            });
        }
        
        if (searchSnackField != null) {
            searchSnackField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterSnacks(newValue);
            });
        }
        
        if (customerSearchField != null) {
            customerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterCustomers(newValue);
            });
        }
        
        if (filterBookingsBtn != null) {
            filterBookingsBtn.setOnAction(e -> filterBookings());
        }
    }
    
    private void filterMovies(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadMovies();
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT movie_id, title, genre, duration_minutes, director, category, " +
                        "price, rating, trailer_url, poster_image, release_date, is_active, " +
                        "description, cast " +
                        "FROM movies WHERE " +
                        "(title LIKE ? OR genre LIKE ? OR director LIKE ? OR category LIKE ? OR cast LIKE ?) " +
                        "ORDER BY movie_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Movie> movies = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setDirector(rs.getString("director"));
                movie.setCategory(rs.getString("category"));
                movie.setPrice(rs.getDouble("price"));
                movie.setRating(rs.getString("rating"));
                movie.setTrailerUrl(rs.getString("trailer_url"));
                movie.setPosterImage(rs.getString("poster_image"));
                java.sql.Date releaseDate = rs.getDate("release_date");
                if (releaseDate != null) {
                    movie.setReleaseDate(releaseDate.toLocalDate());
                }
                movie.setActive(rs.getBoolean("is_active"));
                movie.setDescription(rs.getString("description"));
                movie.setCast(rs.getString("cast"));
                
                movies.add(movie);
            }
            
            movieTableView.setItems(movies);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterSnacks(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadSnacks();
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.snack_id, s.item_name, s.description, c.category_name, " +
                        "s.price, s.size, s.calories, s.image_url, s.is_available " +
                        "FROM snack_items s " +
                        "LEFT JOIN snack_categories c ON s.category_id = c.category_id " +
                        "WHERE s.item_name LIKE ? OR c.category_name LIKE ? OR s.description LIKE ? " +
                        "ORDER BY s.snack_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Snack> snacks = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Snack snack = new Snack();
                snack.setSnackId(rs.getInt("snack_id"));
                snack.setItemName(rs.getString("item_name"));
                snack.setDescription(rs.getString("description"));
                snack.setCategory(rs.getString("category_name"));
                snack.setPrice(rs.getDouble("price"));
                snack.setSize(rs.getString("size"));
                snack.setCalories(rs.getInt("calories"));
                snack.setImageUrl(rs.getString("image_url"));
                snack.setAvailable(rs.getBoolean("is_available"));
                
                snacks.add(snack);
            }
            
            snackTableView.setItems(snacks);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterCustomers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadCustomers();
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.phone_number as phone, " +
                        "u.created_at, u.is_active, " +
                        "COUNT(b.booking_id) as total_bookings, " +
                        "COALESCE(SUM(b.total_amount), 0) as total_spent " +
                        "FROM users u " +
                        "LEFT JOIN bookings b ON u.user_id = b.user_id " +
                        "WHERE (u.first_name LIKE ? OR u.last_name LIKE ? OR u.email LIKE ? OR u.phone_number LIKE ?) " +
                        "AND u.user_type = 'customer' " +
                        "GROUP BY u.user_id " +
                        "ORDER BY u.user_id DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("user_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                
                java.sql.Timestamp joinTimestamp = rs.getTimestamp("created_at");
                if (joinTimestamp != null) {
                    customer.setJoinDate(joinTimestamp.toLocalDateTime().toLocalDate().toString());
                }
                
                customer.setTotalBookings(rs.getInt("total_bookings"));
                customer.setTotalSpent(rs.getDouble("total_spent"));
                customer.setActive(rs.getBoolean("is_active"));
                
                customers.add(customer);
            }
            
            customerTableView.setItems(customers);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterBookings() {
        String bookingId = bookingFilterId.getText().trim();
        LocalDate filterDate = bookingFilterDate.getValue();
        String status = bookingStatusFilter.getValue();
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT b.booking_id, CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                "m.title as movie_title, DATE(b.booking_date) as booking_date, " +
                "s.show_time, " +
                "(SELECT GROUP_CONCAT(se.seat_label) FROM booking_seats bs " +
                "JOIN seats se ON bs.seat_id = se.seat_id WHERE bs.booking_id = b.booking_id) as seat_numbers, " +
                "b.total_amount, b.booking_status as status, b.payment_method " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.user_id " +
                "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                "JOIN movies m ON s.movie_id = m.movie_id " +
                "WHERE 1=1"
            );
            
            List<Object> params = new java.util.ArrayList<>();
            
            if (!bookingId.isEmpty()) {
                sql.append(" AND b.booking_id = ?");
                try {
                    params.add(Integer.parseInt(bookingId));
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "Please enter a valid booking ID");
                    return;
                }
            }
            
            if (filterDate != null) {
                sql.append(" AND DATE(b.booking_date) = ?");
                params.add(java.sql.Date.valueOf(filterDate));
            }
            
            if (status != null && !status.equals("All")) {
                sql.append(" AND b.booking_status = ?");
                params.add(status.toLowerCase());
            }
            
            sql.append(" ORDER BY b.booking_date DESC");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            ObservableList<Booking> bookings = FXCollections.observableArrayList();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerName(rs.getString("customer_name"));
                booking.setMovieTitle(rs.getString("movie_title"));
                booking.setBookingDate(rs.getDate("booking_date").toString());
                booking.setBookingTime(rs.getString("show_time"));
                booking.setSeatNumbers(rs.getString("seat_numbers"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setStatus(rs.getString("status"));
                booking.setPaymentMethod(rs.getString("payment_method"));
                
                bookings.add(booking);
            }
            
            bookingTableView.setItems(bookings);
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Search Error", "Failed to filter bookings: " + e.getMessage());
        }
    }
    
    @FXML
    private void logout() {
        try {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), logoutBtn.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                Stage stage = (Stage) logoutBtn.getScene().getWindow();
                stage.close();
            });
            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        alert.getDialogPane().setScaleX(0.8);
        alert.getDialogPane().setScaleY(0.8);
        alert.show();
        
        ScaleTransition st = new ScaleTransition(Duration.millis(200), alert.getDialogPane());
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        alert.getDialogPane().setScaleX(0.8);
        alert.getDialogPane().setScaleY(0.8);
        alert.show();
        
        ScaleTransition st = new ScaleTransition(Duration.millis(200), alert.getDialogPane());
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }
    
    // ==================== DATA MODEL CLASSES ====================
    
    public static class Movie {
        private int movieId;
        private String title;
        private String description;
        private String genre;
        private int durationMinutes;
        private String director;
        private String cast;
        private String category;
        private double price;
        private String rating;
        private String trailerUrl;
        private String posterImage;
        private LocalDate releaseDate;
        private boolean isActive;
        
        public int getMovieId() { return movieId; }
        public void setMovieId(int movieId) { this.movieId = movieId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }
        public int getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
        public String getDirector() { return director; }
        public void setDirector(String director) { this.director = director; }
        public String getCast() { return cast; }
        public void setCast(String cast) { this.cast = cast; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getRating() { return rating; }
        public void setRating(String rating) { this.rating = rating; }
        public String getTrailerUrl() { return trailerUrl; }
        public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
        public String getPosterImage() { return posterImage; }
        public void setPosterImage(String posterImage) { this.posterImage = posterImage; }
        public LocalDate getReleaseDate() { return releaseDate; }
        public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
    }
    
    public static class Snack {
        private int snackId;
        private String itemName;
        private String description;
        private String category;
        private double price;
        private String size;
        private int calories;
        private String imageUrl;
        private boolean available;
        
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
        public int getCalories() { return calories; }
        public void setCalories(int calories) { this.calories = calories; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }
    
    public static class FAQ {
        private int faqId;
        private String question;
        private String answer;
        
        public int getFaqId() { return faqId; }
        public void setFaqId(int faqId) { this.faqId = faqId; }
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }
    
    public static class Showtime {
        private int showtimeId;
        private int movieId;
        private String movieTitle;
        private LocalDate showDate;
        private String showTime;
        private String hallName;
        private double price;
        private int availableSeats;
        private String notes;
        private boolean isActive;
        
        public int getShowtimeId() { return showtimeId; }
        public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
        public int getMovieId() { return movieId; }
        public void setMovieId(int movieId) { this.movieId = movieId; }
        public String getMovieTitle() { return movieTitle; }
        public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
        public LocalDate getShowDate() { return showDate; }
        public void setShowDate(LocalDate showDate) { this.showDate = showDate; }
        public String getShowTime() { return showTime; }
        public void setShowTime(String showTime) { this.showTime = showTime; }
        public String getHallName() { return hallName; }
        public void setHallName(String hallName) { this.hallName = hallName; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getAvailableSeats() { return availableSeats; }
        public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
    }
    
    public static class Customer {
        private int customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String joinDate;
        private int totalBookings;
        private double totalSpent;
        private boolean active;
        
        public int getCustomerId() { return customerId; }
        public void setCustomerId(int customerId) { this.customerId = customerId; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getFullName() { return firstName + " " + lastName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getJoinDate() { return joinDate; }
        public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
        public double getTotalSpent() { return totalSpent; }
        public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    public static class Booking {
        private int bookingId;
        private String customerName;
        private String movieTitle;
        private String bookingDate;
        private String bookingTime;
        private String seatNumbers;
        private double totalAmount;
        private String status;
        private String paymentMethod;
        
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getMovieTitle() { return movieTitle; }
        public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
        public String getBookingDate() { return bookingDate; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
        public String getBookingTime() { return bookingTime; }
        public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
        public String getSeatNumbers() { return seatNumbers; }
        public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }
    
    public static class SeatButton extends Button {
        private String seatLabel;
        private int row;
        private int col;
        private String seatType;
        private boolean isBlocked = false;
        private boolean isSelected = false;
        
        public SeatButton(String seatLabel, int row, int col) {
            super(seatLabel);
            this.seatLabel = seatLabel;
            this.row = row;
            this.col = col;
            this.setPrefSize(35, 35);
            this.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        }
        
        public String getSeatLabel() { return seatLabel; }
        public void setSeatLabel(String seatLabel) { this.seatLabel = seatLabel; }
        public int getRow() { return row; }
        public int getCol() { return col; }
        public String getSeatType() { return seatType; }
        public void setSeatType(String seatType) { this.seatType = seatType; }
        public boolean isBlocked() { return isBlocked; }
        public void setBlocked(boolean blocked) { isBlocked = blocked; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { isSelected = selected; }
    }
    
    public static class SeatData {
        private int seatId;
        private String seatRow;
        private int seatNumber;
        private String seatLabel;
        private String seatType;
        private boolean isBlocked;
        private double price;
        
        public int getSeatId() { return seatId; }
        public void setSeatId(int seatId) { this.seatId = seatId; }
        public String getSeatRow() { return seatRow; }
        public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
        public int getSeatNumber() { return seatNumber; }
        public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
        public String getSeatLabel() { return seatLabel; }
        public void setSeatLabel(String seatLabel) { this.seatLabel = seatLabel; }
        public String getSeatType() { return seatType; }
        public void setSeatType(String seatType) { this.seatType = seatType; }
        public boolean isBlocked() { return isBlocked; }
        public void setBlocked(boolean blocked) { isBlocked = blocked; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
    
    private static class SeatInfo {
        String seatRow;
        int seatNumber;
        String seatLabel;
        String seatType;
        boolean isBlocked;
        
        SeatInfo(String seatRow, int seatNumber, String seatLabel, String seatType, boolean isBlocked) {
            this.seatRow = seatRow;
            this.seatNumber = seatNumber;
            this.seatLabel = seatLabel;
            this.seatType = seatType;
            this.isBlocked = isBlocked;
        }
    }
}