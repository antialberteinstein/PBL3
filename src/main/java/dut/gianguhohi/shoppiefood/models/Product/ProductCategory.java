package dut.gianguhohi.shoppiefood.models.Product;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Product.Category;
import dut.gianguhohi.shoppiefood.models.Product.Product;




@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Constructors
    public ProductCategory() {}

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}