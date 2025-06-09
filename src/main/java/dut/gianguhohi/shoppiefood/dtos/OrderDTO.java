package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.Orders.Order;
import java.time.LocalDateTime;

public class OrderDTO {
    private int orderId;
    private int customerId;
    private String firstProductName;
    private String firstProductImageUrl;
    private String customerName;
    private Integer shipperId;
    private String shipperName;
    private int restaurantId;
    private String restaurantName;
    private LocalDateTime createdAt;
    private LocalDateTime timeConfirmed;
    private LocalDateTime timeStart;
    private LocalDateTime timeDelivered;
    private String status;
    private double totalAmount;
    private String pickupAddress;
    private String deliveryAddress;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.customerName = order.getCustomer() != null ? order.getCustomer().getName() : "Unknown";
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.timeConfirmed = order.getTimeConfirmed();
        this.timeStart = order.getTimeStart();
        this.timeDelivered = order.getTimeDelivered();
        this.deliveryAddress = order.getDeliveryAddress();
        this.note = order.getNote();
        if (order.getCustomer() != null) {
            this.customerId = order.getCustomer().getUserId();
        }
        if (order.getShipper() != null) {
            this.shipperId = order.getShipper().getShipperId();
            this.shipperName = order.getShipper().getUser().getName();
        }
        if (order.getBranch() != null) {
            if (order.getBranch().getRestaurant() != null) {
                this.restaurantId = order.getBranch().getRestaurant().getRestaurantId();
                this.restaurantName = order.getBranch().getRestaurant().getRestaurantName();
            }
            this.pickupAddress = order.getBranch().getAddress().getFullAddress();
        }
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getShipperId() { return shipperId; }
    public void setShipperId(Integer shipperId) { this.shipperId = shipperId; }

    public String getShipperName() { return shipperName; }
    public void setShipperName(String shipperName) { this.shipperName = shipperName; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getTimeConfirmed() { return timeConfirmed; }
    public void setTimeConfirmed(LocalDateTime timeConfirmed) { this.timeConfirmed = timeConfirmed; }

    public LocalDateTime getTimeStart() { return timeStart; }
    public void setTimeStart(LocalDateTime timeStart) { this.timeStart = timeStart; }

    public LocalDateTime getTimeDelivered() { return timeDelivered; }
    public void setTimeDelivered(LocalDateTime timeDelivered) { this.timeDelivered = timeDelivered; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPickupAddress() { return pickupAddress; }

    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }

    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getFirstProductName() {
        return firstProductName;
    }

    public void setFirstProductName(String firstProductName) {
        this.firstProductName = firstProductName;
    }

    public String getFirstProductImageUrl() {
        return firstProductImageUrl;
    }

    public void setFirstProductImageUrl(String firstProductImageUrl) {
        this.firstProductImageUrl = firstProductImageUrl;
    }
}