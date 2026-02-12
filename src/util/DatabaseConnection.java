package util;

import model.User;
import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/eliana";
    private static final String USER = "root";
    private static final String PASSWORD = "myonlyted";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC Driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    // ========== GET USER BY USERNAME ==========
    public static User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, email, first_name, last_name, phone_number, user_type, profile_picture " +
                    "FROM users WHERE username = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setUserType(rs.getString("user_type"));
                user.setProfilePicture(rs.getString("profile_picture"));
                
                System.out.println("✅ Retrieved complete user info for: " + username + ", ID: " + user.getUserId());
                return user;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting user by username: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    // ========== GET USER BY ID ==========
    public static User getUserById(int userId) {
        String sql = "SELECT user_id, username, email, first_name, last_name, phone_number, user_type, profile_picture " +
                    "FROM users WHERE user_id = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setUserType(rs.getString("user_type"));
                user.setProfilePicture(rs.getString("profile_picture"));
                
                System.out.println("✅ Retrieved user by ID: " + userId + ", Name: " + user.getFullName());
                return user;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting user by ID: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    // ========== GET USER ID ==========
    public static int getUserId(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                System.out.println("✅ Retrieved user_id for " + username + ": " + userId);
                return userId;
            } else {
                System.out.println("❌ No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting user_id for " + username + ": " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return 0;
    }
    
    // ========== LOGIN METHOD ==========
    public static boolean login(String username, String password) {
        String sql = "SELECT user_id FROM users WHERE (username = ? OR email = ?) AND password = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            rs = pstmt.executeQuery();
            
            boolean success = rs.next();
            if (success) {
                int userId = rs.getInt("user_id");
                System.out.println("🔍 Login successful. User ID: " + userId);
            }
            return success;
            
        } catch (SQLException e) {
            System.err.println("❌ Error during login: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }
    
    // ========== GET USER TYPE ==========
    public static String getUserType(String username) {
        String sql = "SELECT user_type FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String userType = rs.getString("user_type");
                System.out.println("🔍 User type for " + username + ": " + userType);
                return userType;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting user type: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return "customer";
    }
    
    // ========== GET USER FULL NAME ==========
    public static String getUserFullName(String username) {
        String sql = "SELECT first_name, last_name FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String fullName = (firstName + " " + lastName).trim();
                System.out.println("🔍 Full name for " + username + ": " + fullName);
                return fullName;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting user full name: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return username;
    }
    
    // ========== REGISTER NEW USER ==========
    public static boolean registerUser(String username, String password, String email, 
                                       String firstName, String lastName, String phone) {
        String sql = "INSERT INTO users (username, password, email, first_name, last_name, " +
                    "phone_number, user_type) VALUES (?, ?, ?, ?, ?, ?, 'customer')";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, phone);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Registered new user: " + username + ", Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ SQL Error registering user: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ========== CHECK IF USER EXISTS ==========
    public static boolean userExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🔍 User exists check for " + username + ": " + (count > 0));
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking if user exists: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }
    
    // ========== RESET PASSWORD ==========
    public static boolean resetPassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Password reset for email: " + email + ", Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error resetting password: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ========== UPDATE USER PROFILE ==========
    public static boolean updateUserProfile(int userId, String email, String firstName, 
                                           String lastName, String phone, String profilePicture) {
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, " +
                    "phone_number = ?, profile_picture = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, phone);
            pstmt.setString(5, profilePicture);
            pstmt.setInt(6, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Updated profile for user ID: " + userId + ", Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating user profile: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ========== VALIDATE PASSWORD ==========
    public static boolean validatePassword(int userId, String password) {
        String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("❌ Error validating password: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }
    
    // ========== CHANGE PASSWORD ==========
    public static boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Changed password for user ID: " + userId + ", Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error changing password: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ========== TEST CONNECTION ==========
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();
            System.out.println("✅ Database connection test: " + (isValid ? "SUCCESS" : "FAILED"));
            return isValid;
        } catch (SQLException e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    // ========== CHECK USER EXISTS (USERNAME OR EMAIL) ==========
    public static boolean checkUserExists(String usernameOrEmail) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE (username = ? OR email = ?) AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("🔍 User exists check for " + usernameOrEmail + ": " + (count > 0));
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking user existence: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }
    
    // ========== GET EMAIL FROM USERNAME ==========
    public static String getEmailFromUsername(String username) {
        String sql = "SELECT email FROM users WHERE username = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String email = rs.getString("email");
                System.out.println("📧 Email for " + username + ": " + email);
                return email;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting email from username: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        System.out.println("⚠️ No email found for username: " + username);
        return null;
    }
    
    // ========== GET USERNAME FROM EMAIL ==========
    public static String getUsernameFromEmail(String email) {
        String sql = "SELECT username FROM users WHERE email = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String username = rs.getString("username");
                System.out.println("👤 Username for " + email + ": " + username);
                return username;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting username from email: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        System.out.println("⚠️ No username found for email: " + email);
        return null;
    }
    
    // ========== UPDATE PASSWORD WITH USERNAME ==========
    public static boolean updatePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE username = ? AND is_active = TRUE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Updated password for username: " + username + ", Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating password by username: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ========== TEST PASSWORD RESET ==========
    public static void testPasswordReset(String email, String newPassword) {
        System.out.println("\n🔧 Testing password reset for: " + email);
        
        // Test 1: Check if user exists
        boolean exists = checkUserExists(email);
        System.out.println("1. User exists: " + exists);
        
        if (exists) {
            // Test 2: Get current password hash (for comparison)
            String sql = "SELECT password FROM users WHERE email = ? AND is_active = TRUE";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String oldHash = rs.getString("password");
                    System.out.println("2. Current password hash: " + (oldHash != null ? "[HIDDEN]" : "NULL"));
                }
                
                // Test 3: Reset password
                boolean reset = resetPassword(email, newPassword);
                System.out.println("3. Reset password: " + reset);
                
                // Test 4: Try to login with new password
                boolean canLogin = login(email, newPassword);
                System.out.println("4. Can login with new password: " + canLogin);
                
            } catch (SQLException e) {
                System.err.println("❌ Test error: " + e.getMessage());
            }
        }
    }
}