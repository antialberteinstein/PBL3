package dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos;

public class RestaurantRequest {
    private String name;
    private String description;
    private String backgroundUrl;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBackgroundUrl() { return backgroundUrl; }
    public void setBackgroundUrl(String backgroundUrl) { this.backgroundUrl = backgroundUrl; }
}