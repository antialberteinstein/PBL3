package dut.gianguhohi.shoppiefood.models.Discounts;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discount_type", discriminatorType = DiscriminatorType.STRING)
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private int discountId;

    @Column(nullable = false)
    private String type;

    @Column(name = "time_start", nullable = false)
    private LocalDateTime timeStart;

    @Column(name = "time_finish", nullable = false)
    private LocalDateTime timeFinish;

    @Column(nullable = false)
    private int status;

    @Column(name = "discount_value", nullable = false)
    private int discountValue;

    @Column(name = "is_percentage", nullable = false)
    private boolean isPercentage;

    @Column(name = "max_discount_value")
    private int maxDiscountValue;

    @Column(name = "min_order_value")
    private int minOrderValue;

    @Column(name = "quantity_limit")
    private int quantityLimit;

    public Discount() {
    }

    public Discount(String type, LocalDateTime timeStart, LocalDateTime timeFinish, 
                   int status, int discountValue, boolean isPercentage, 
                   int maxDiscountValue, int minOrderValue, int quantityLimit) {
        this.type = type;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.status = status;
        this.discountValue = discountValue;
        this.isPercentage = isPercentage;
        this.maxDiscountValue = maxDiscountValue;
        this.minOrderValue = minOrderValue;
        this.quantityLimit = quantityLimit;
    }

    // Getters and setters
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(LocalDateTime timeFinish) {
        this.timeFinish = timeFinish;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public int getMaxDiscountValue() {
        return maxDiscountValue;
    }

    public void setMaxDiscountValue(int maxDiscountValue) {
        this.maxDiscountValue = maxDiscountValue;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getQuantityLimit() {
        return quantityLimit;
    }

    public void setQuantityLimit(int quantityLimit) {
        this.quantityLimit = quantityLimit;
    }
}
