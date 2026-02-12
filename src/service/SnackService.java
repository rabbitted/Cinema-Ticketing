// SnackService.java
package service;

import model.SnackItem;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SnackService {
    
    public boolean addSnackItem(SnackItem snack) {
        String query = "INSERT INTO snack_items (item_name, description, category_id, " +
                      "price, size, calories, image_url, is_available) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, snack.getItemName());
            pstmt.setString(2, snack.getDescription());
            pstmt.setInt(3, snack.getCategoryId());
            pstmt.setDouble(4, snack.getPrice());
            pstmt.setString(5, snack.getSize());
            
            if (snack.getCalories() != null) {
                pstmt.setInt(6, snack.getCalories());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            pstmt.setString(7, snack.getImageUrl());
            pstmt.setBoolean(8, snack.isAvailable());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<SnackItem> getAllSnackItems() {
        List<SnackItem> snacks = new ArrayList<>();
        String query = "SELECT * FROM snack_items ORDER BY category_id, item_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                SnackItem snack = extractSnackFromResultSet(rs);
                snacks.add(snack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return snacks;
    }
    
    public List<SnackItem> getSnacksByCategory(int categoryId) {
        List<SnackItem> snacks = new ArrayList<>();
        String query = "SELECT * FROM snack_items WHERE category_id = ? AND is_available = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SnackItem snack = extractSnackFromResultSet(rs);
                snacks.add(snack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return snacks;
    }
    
    public SnackItem getSnackById(int snackId) {
        String query = "SELECT * FROM snack_items WHERE snack_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, snackId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSnackFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateSnackItem(SnackItem snack) {
        String query = "UPDATE snack_items SET item_name = ?, description = ?, " +
                      "category_id = ?, price = ?, size = ?, calories = ?, " +
                      "image_url = ?, is_available = ?, updated_at = CURRENT_TIMESTAMP " +
                      "WHERE snack_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, snack.getItemName());
            pstmt.setString(2, snack.getDescription());
            pstmt.setInt(3, snack.getCategoryId());
            pstmt.setDouble(4, snack.getPrice());
            pstmt.setString(5, snack.getSize());
            
            if (snack.getCalories() != null) {
                pstmt.setInt(6, snack.getCalories());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            pstmt.setString(7, snack.getImageUrl());
            pstmt.setBoolean(8, snack.isAvailable());
            pstmt.setInt(9, snack.getSnackId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteSnackItem(int snackId) {
        String query = "UPDATE snack_items SET is_available = FALSE WHERE snack_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, snackId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private SnackItem extractSnackFromResultSet(ResultSet rs) throws SQLException {
        SnackItem snack = new SnackItem();
        snack.setSnackId(rs.getInt("snack_id"));
        snack.setItemName(rs.getString("item_name"));
        snack.setDescription(rs.getString("description"));
        snack.setCategoryId(rs.getInt("category_id"));
        snack.setPrice(rs.getDouble("price"));
        snack.setSize(rs.getString("size"));
        snack.setCalories(rs.getInt("calories"));
        if (rs.wasNull()) snack.setCalories(null);
        snack.setImageUrl(rs.getString("image_url"));
        snack.setAvailable(rs.getBoolean("is_available"));
        
        return snack;
    }
}