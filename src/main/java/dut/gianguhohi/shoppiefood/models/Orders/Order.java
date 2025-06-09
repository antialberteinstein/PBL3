package dut.gianguhohi.shoppiefood.models.Orders;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.utils.OrderStatusType;

import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "shipper_id", nullable = true)
    private Shipper shipper;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "time_confirmed")
    private LocalDateTime timeConfirmed;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "time_delivered")
    private LocalDateTime timeDelivered;

    @Column(name = "estimated_delivery_time")
    private Date estimatedDeliveryTime;

    @Column(nullable = false)
    private String status;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "note")
    private String note;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "order_date")
    private Date orderDate;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.orderItems = new ArrayList<>();
    }

    public Order(User customer, Shipper shipper, Branch branch,
                String status, double totalAmount, String deliveryAddress) {
        this();
        this.customer = customer;
        this.shipper = shipper;
        this.branch = branch;
        this.status = status;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;

    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setStatus(OrderStatusType statusType) {
    this.status = statusType.toString();
}

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

public void setBranch(Branch branch) {
    this.branch = branch;
}

public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        // Cập nhật reference trong từng OrderItem
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                item.setOrder(this);
            }
        }
    }

    public void appendOrderItem(OrderItem orderItem) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); // Set the order reference in the OrderItem
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (this.orderItems != null) {
            this.orderItems.remove(orderItem);
        }
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

public Branch getBranch() {
    return branch;
}

    public LocalDateTime getTimeConfirmed() {
        return timeConfirmed;
    }

    public void setTimeConfirmed(LocalDateTime timeConfirmed) {
        this.timeConfirmed = timeConfirmed;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public Date getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }
    public void setEstimatedDeliveryTime(Date estimatedDelivery) {
        this.estimatedDeliveryTime = estimatedDelivery;
    }
    public String getCancelReason() {
        return cancelReason;
    }
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setCancelledDate(Date date) {
    if (date != null) {
        this.cancelledDate = LocalDateTime.ofInstant(
            date.toInstant(), ZoneId.systemDefault()
        );
    } else {
        this.cancelledDate = null;
    }
}
    public LocalDateTime getCancelledDate() {
        return cancelledDate;
    }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDelivery) {
    this.estimatedDeliveryTime = new Date(
        estimatedDelivery.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    );
}
    public void setCancelledDate(LocalDateTime cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
    public Date getOrderDate() {
    return this.orderDate;
}

    public void setOrderDate(Date date) {
        this.orderDate = date;
    }
}