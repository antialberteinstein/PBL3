package dut.gianguhohi.shoppiefood.repositories.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Feedback.Feedback;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    
    // Feedback what? The order related to shipper and restaurant.
    List<Feedback> findByOrder_OrderId(Integer orderId);

    // Specific product feedback.
    // The feedback will be shown on the product detail page.
    List<Feedback> findByProduct_ProductId(Integer productId);

    // Who feedback?
    List<Feedback> findByUser_UserId(Integer userId);
    
    
}
