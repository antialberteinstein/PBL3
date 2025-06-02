package dut.gianguhohi.shoppiefood.models.Product;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Orders.OrderItem;
import java.util.ArrayList;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private long price;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    @Column
    private float rating;

    @Column
    private long ratingNumber;

    @Column(name = "remaining_quantity")
    private long remainingQuantity;

    // Có vượt qua kiểm duyệt không?
    @Column(name = "is_alow")
    private boolean isAllow;

    public Product() {
        this.postedAt = LocalDateTime.now();
        this.isAllow = true;  // Chưa có chức năng kiểm duyệt.
    }

    public Product(Restaurant restaurant, String imageUrl, String name, 
                  String description, long price, float rating, long ratingNumber,
                  long remaininngQuantity) {
        this();
        this.restaurant = restaurant;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.remainingQuantity = remainingQuantity;
        this.ratingNumber = ratingNumber;
    }

    // Getters and setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(long remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public void setAllow(boolean allow) {
        isAllow = allow;
    }

    public long getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(long ratingNumber) {
        this.ratingNumber = ratingNumber;
    }
}
