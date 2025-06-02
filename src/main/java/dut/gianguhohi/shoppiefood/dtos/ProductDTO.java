package dut.gianguhohi.shoppiefood.dtos;

import java.time.LocalDateTime;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Product.Product;

public class ProductDTO {
    private Integer productId;
    private Integer restaurantId;
    private List<String> categories;
    private String imageUrl;
    private String name;
    private String description;
    private Long price;
    private LocalDateTime postedAt;
    private Float rating;
    private Long ratingNumber;
    private Long remainingQuantity;
    private Boolean isAllow;

    public ProductDTO() {}

    // New constructor: pass in category names
    public ProductDTO(Product product, List<String> categoryNames) {
        this.productId = product.getProductId();
        this.restaurantId = product.getRestaurant() != null ? product.getRestaurant().getRestaurantId() : null;
        this.categories = categoryNames;
        this.imageUrl = product.getImageUrl();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.postedAt = product.getPostedAt();
        this.rating = product.getRating();
        this.remainingQuantity = product.getRemainingQuantity();
        this.isAllow = product.isAllow();
        this.ratingNumber = product.getRatingNumber();
    }

    // Getters and setters
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }

    public Long getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(Long remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public Boolean getIsAllow() { return isAllow; }
    public void setIsAllow(Boolean isAllow) { this.isAllow = isAllow; }

    public Long getRatingNumber() { return ratingNumber; }
    public void setRatingNumber(Long ratingNumber) { this.ratingNumber = ratingNumber; }
}