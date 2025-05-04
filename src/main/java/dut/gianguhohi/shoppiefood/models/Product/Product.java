package dut.gianguhohi.shoppiefood.models.Product;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Orders.OrderItem;

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    public Product() {
        this.postedAt = LocalDateTime.now();
        this.isAvailable = true;
    }

    public Product(Restaurant restaurant, Category category, String imageUrl, String name, 
                  String description, long price, float rating) {
        this.restaurant = restaurant;
        this.category = category;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.postedAt = LocalDateTime.now();
        this.isAvailable = true;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
