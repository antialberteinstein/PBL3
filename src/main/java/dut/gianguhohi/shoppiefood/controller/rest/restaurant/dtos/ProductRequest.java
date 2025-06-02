package dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos;

import java.util.List;

public class ProductRequest {
    private String imageUrl;
    private String name;
    private String description;
    private long price;
    private List<String> categories;
    private long remainingQuantity;

    // Getters and Setters
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    public long getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(long remainingQuantity) { this.remainingQuantity = remainingQuantity; }
}