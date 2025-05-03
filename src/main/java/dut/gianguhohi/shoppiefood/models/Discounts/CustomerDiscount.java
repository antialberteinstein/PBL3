package dut.gianguhohi.shoppiefood.models.Discounts;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import dut.gianguhohi.shoppiefood.models.Users.User;
import java.util.Set;


@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerDiscount extends Discount {
    @ManyToMany
    @JoinTable(
        name = "customer_discount_mapping",
        joinColumns = @JoinColumn(name = "discount_id"),
        inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<User> customers;

    public CustomerDiscount() {
        super();
    }

    public CustomerDiscount(String type, LocalDateTime timeStart, LocalDateTime timeFinish, 
                          int status, int discountValue, boolean isPercentage, 
                          int maxDiscountValue, int minOrderValue, int quantityLimit,
                          Set<User> customers) {
        super(type, timeStart, timeFinish, status, discountValue, isPercentage, 
              maxDiscountValue, minOrderValue, quantityLimit);
        this.customers = customers;
    }

    public Set<User> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<User> customers) {
        this.customers = customers;
    }
}
