package dut.gianguhohi.shoppiefood.models.Discounts;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import dut.gianguhohi.shoppiefood.models.Users.User;
import java.util.Set;

@Entity
@DiscriminatorValue("FREE_SHIP")
public class FreeShip extends Discount {
    @Column(name = "min_order_value", nullable = false)
    private int minOrderValue;

    @ManyToMany
    @JoinTable(
        name = "freeship_customer_mapping",
        joinColumns = @JoinColumn(name = "discount_id"),
        inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<User> customers;

    public FreeShip() {
        super();
    }

    public FreeShip(String type, LocalDateTime timeStart, LocalDateTime timeFinish, 
                   int status, int discountValue, boolean isPercentage, 
                   int maxDiscountValue, int minOrderValue, int quantityLimit,
                   Set<User> customers) {
        super(type, timeStart, timeFinish, status, discountValue, isPercentage, 
              maxDiscountValue, minOrderValue, quantityLimit);
        this.minOrderValue = minOrderValue;
        this.customers = customers;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Set<User> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<User> customers) {
        this.customers = customers;
    }
}
