package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.utils.OrderFeedbackType;

@Entity
@DiscriminatorValue("PRODUCT")
public class ProductFeedback extends OrderFeedback {
    
    // How many stars?
    @Column(name = "star_rating")
    private float starRating;

    public ProductFeedback() {
        super();
    }

    public ProductFeedback(String content, User whoFeedback, Order order, float starRating) {
        super(content, whoFeedback, order, OrderFeedbackType.PRODUCT);
        this.starRating = starRating;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }
}
