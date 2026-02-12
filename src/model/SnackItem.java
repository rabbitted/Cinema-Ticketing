// SnackItem.java
package model;

public class SnackItem {
    private int snackId;
    private String itemName;
    private String description;
    private int categoryId;
    private double price;
    private String size;
    private Integer calories;
    private String imageUrl;
    private boolean isAvailable;
    
    // Constructors
    public SnackItem() {}
    
    public SnackItem(String itemName, String description, int categoryId, 
                     double price, String size, boolean isAvailable) {
        this.itemName = itemName;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.size = size;
        this.isAvailable = isAvailable;
    }
    
    // Getters and Setters
    public int getSnackId() { return snackId; }
    public void setSnackId(int snackId) { this.snackId = snackId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getCategoryName() {
        switch (categoryId) {
            case 1: return "Snack";
            case 2: return "Drink";
            case 3: return "Combo";
            case 4: return "Dessert";
            default: return "Other";
        }
    }
}