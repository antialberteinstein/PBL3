package dut.gianguhohi.shoppiefood.models.Discounts;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import java.util.Set;

@Entity
@DiscriminatorValue("PRODUCT")
public class ProductDiscount extends Discount {
    @ManyToMany
    @JoinTable(
        name = "product_discount_mapping",
        joinColumns = @JoinColumn(name = "discount_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    public ProductDiscount() {
        super();
    }

    public ProductDiscount(String type, LocalDateTime timeStart, LocalDateTime timeFinish, 
                         int status, int discountValue, boolean isPercentage, 
                         int maxDiscountValue, int minOrderValue, int quantityLimit,
                         Set<Product> products) {
        super(type, timeStart, timeFinish, status, discountValue, isPercentage, 
              maxDiscountValue, minOrderValue, quantityLimit);
        this.products = products;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
