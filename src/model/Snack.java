// SnackItem.java - Model class
package model;

import javafx.beans.property.*;

public class Snack{
    private final IntegerProperty snackId;
    private final StringProperty itemName;
    private final StringProperty description;
    private final StringProperty category;
    private final DoubleProperty price;
    private final StringProperty size;
    private final IntegerProperty calories;
    private final BooleanProperty available;
    private final StringProperty imageUrl;
    
    public Snack() {
        this.snackId = new SimpleIntegerProperty();
        this.itemName = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.category = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.size = new SimpleStringProperty();
        this.calories = new SimpleIntegerProperty();
        this.available = new SimpleBooleanProperty();
        this.imageUrl = new SimpleStringProperty();
    }
    
    public Snack(int snackId, String itemName, String description, String category, 
                     double price, String size, int calories, boolean available, String imageUrl) {
        this.snackId = new SimpleIntegerProperty(snackId);
        this.itemName = new SimpleStringProperty(itemName);
        this.description = new SimpleStringProperty(description);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.size = new SimpleStringProperty(size);
        this.calories = new SimpleIntegerProperty(calories);
        this.available = new SimpleBooleanProperty(available);
        this.imageUrl = new SimpleStringProperty(imageUrl);
    }
    
    // Getters and Setters
    public int getSnackId() { return snackId.get(); }
    public void setSnackId(int snackId) { this.snackId.set(snackId); }
    public IntegerProperty snackIdProperty() { return snackId; }
    
    public String getItemName() { return itemName.get(); }
    public void setItemName(String itemName) { this.itemName.set(itemName); }
    public StringProperty itemNameProperty() { return itemName; }
    
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }
    
    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }
    
    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }
    
    public String getSize() { return size.get(); }
    public void setSize(String size) { this.size.set(size); }
    public StringProperty sizeProperty() { return size; }
    
    public int getCalories() { return calories.get(); }
    public void setCalories(int calories) { this.calories.set(calories); }
    public IntegerProperty caloriesProperty() { return calories; }
    
    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean available) { this.available.set(available); }
    public BooleanProperty availableProperty() { return available; }
    
    public String getImageUrl() { return imageUrl.get(); }
    public void setImageUrl(String imageUrl) { this.imageUrl.set(imageUrl); }
    public StringProperty imageUrlProperty() { return imageUrl; }
    
    @Override
    public String toString() {
        return getItemName() + " (" + getSize() + ") - ETB " + getPrice();
    }
}