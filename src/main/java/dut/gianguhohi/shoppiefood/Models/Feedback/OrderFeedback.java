package dut.gianguhohi.shoppiefood.models.Feedback;

import jakarta.persistence.*;
import dut.gianguhohi.shoppiefood.models.Orders.Order;

@Entity
@DiscriminatorValue("ORDER")
public class OrderFeedback extends Feedback {
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderFeedback() {
        super();
    }

    public OrderFeedback(int type, String content, Order order) {
        super(type, content);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
