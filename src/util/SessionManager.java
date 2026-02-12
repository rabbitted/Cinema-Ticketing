package util;

import model.User;

public class SessionManager {
    private static User currentUser = null;
    
    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            System.out.println("✅ SessionManager: User set - ID: " + user.getUserId() + ", Name: " + user.getFullName());
        }
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUserId(int userId) {
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for ID: " + userId);
        }
        currentUser.setUserId(userId);
    }
    
    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : 0;
    }
    
    public static void setCurrentUsername(String username) {
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for username: " + username);
        }
        currentUser.setUsername(username);
    }
    
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "";
    }
    
    public static void setCurrentFullName(String fullName) {
        // This is a computed property, so we can't set it directly
        // We'll split the full name into first and last names
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for full name: " + fullName);
        }
        
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] nameParts = fullName.trim().split("\\s+", 2);
            if (nameParts.length >= 1) {
                currentUser.setFirstName(nameParts[0]);
            }
            if (nameParts.length >= 2) {
                currentUser.setLastName(nameParts[1]);
            }
        }
    }
    
    public static String getCurrentFullName() {
        return currentUser != null ? currentUser.getFullName() : "";
    }
    
    public static void setCurrentUserType(String userType) {
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for user type: " + userType);
        }
        currentUser.setUserType(userType);
    }
    
    public static String getCurrentUserType() {
        return currentUser != null ? currentUser.getUserType() : "";
    }
    
    public static void setCurrentUserEmail(String email) {
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for email: " + email);
        }
        currentUser.setEmail(email);
    }
    
    public static String getCurrentUserEmail() {
        return currentUser != null ? currentUser.getEmail() : "";
    }
    
    public static void setCurrentUserPhone(String phone) {
        if (currentUser == null) {
            currentUser = new User();
            System.out.println("⚠️ SessionManager: Created new User object for phone: " + phone);
        }
        currentUser.setPhoneNumber(phone);
    }
    
    public static String getCurrentUserPhone() {
        return currentUser != null ? currentUser.getPhoneNumber() : "";
    }
    
    public static void clearSession() {
        System.out.println("🔒 SessionManager: Clearing session");
        currentUser = null;
    }
    
    public static boolean isLoggedIn() {
        boolean loggedIn = currentUser != null && currentUser.getUserId() > 0;
        System.out.println("🔍 SessionManager: isLoggedIn = " + loggedIn + ", User ID = " + getCurrentUserId());
        return loggedIn;
    }
    
    public static void printSessionInfo() {
        if (currentUser != null) {
            System.out.println("🔍 SESSION INFO:");
            System.out.println("  User ID: " + currentUser.getUserId());
            System.out.println("  Username: " + currentUser.getUsername());
            System.out.println("  Full Name: " + currentUser.getFullName());
            System.out.println("  User Type: " + currentUser.getUserType());
            System.out.println("  Email: " + currentUser.getEmail());
            System.out.println("  Phone: " + currentUser.getPhoneNumber());
        } else {
            System.out.println("🔍 SessionManager: No user session active");
        }
    }
}