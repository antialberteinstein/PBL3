package dut.gianguhohi.shoppiefood.models.Orders;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.Users;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "time_delivered")
    private LocalDateTime timeDelivered;

    @Column(nullable = false)
    private int status;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(Users user, LocalDateTime timeStart, LocalDateTime timeDelivered, 
                int status, long totalAmount, String shippingAddress) {
        this.user = user;
        this.timeStart = timeStart;
        this.timeDelivered = timeDelivered;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(LocalDateTime timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
