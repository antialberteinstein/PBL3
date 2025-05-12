package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Entity
@DiscriminatorValue("ORDER")
public class OrderFeedback extends Feedback {
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // What do you want to feedback about?
    @Column(name = "order_feedback_type")
    private String type;


    public OrderFeedback() {
        super();
    }

    public OrderFeedback(String content, User whoFeedback, Order order, String type) {
        super(content, whoFeedback);
        this.order = order;
        this.type = type;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
