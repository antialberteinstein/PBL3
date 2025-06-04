package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.Users.Restaurant;

public class RestaurantDTO {
    private int restaurantId;
    private String restaurantName;
    private String description;
    private int sellerId;
    private String sellerName;
    private String backgroundUrl;

    public RestaurantDTO(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.description = restaurant.getDescription();
        this.backgroundUrl = restaurant.getBackgroundUrl();
        if (restaurant.getSeller() != null) {
            this.sellerId = restaurant.getSeller().getUserId();
            this.sellerName = restaurant.getSeller().getName();
        }
    }

    // Getters and setters

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
}