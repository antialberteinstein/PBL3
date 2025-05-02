package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Product.Product;

@Entity
@DiscriminatorValue("PRODUCT")
public class ProductFeedback extends Feedback {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductFeedback() {
        super();
    }

    public ProductFeedback(int type, String content, Product product) {
        super(type, content);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
