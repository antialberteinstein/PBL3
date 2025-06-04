package dut.gianguhohi.shoppiefood.repositories.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import dut.gianguhohi.shoppiefood.models.misc.Branch;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Customer history: sort by timeDelivered DESC
    @Query("SELECT o FROM Order o WHERE o.customer = :user AND o.status IN ('delivered', 'cancelled', 'refunded') ORDER BY o.timeDelivered DESC")
    Page<Order> getCustomerHistory(User user, Pageable pageable);

    // Customer active: sort by createdAt DESC
    @Query("SELECT o FROM Order o WHERE o.customer = :user AND o.status IN ('confirmed', 'in_progress') ORDER BY o.createdAt DESC")
    Page<Order> getCustomerActiveOrders(User user, Pageable pageable);

    // Restaurant history: sort by timeDelivered DESC
    @Query("SELECT o FROM Order o WHERE o.branch = :branch AND o.status IN ('delivered', 'cancelled', 'refunded', 'in_progress') ORDER BY o.timeDelivered DESC")
    Page<Order> getBranchHistory(Branch branch, Pageable pageable);

    // Restaurant active: sort by createdAt DESC
    @Query("SELECT o FROM Order o WHERE o.branch = :branch AND o.status IN ('pending', 'confirmed', 'in_progress') ORDER BY o.createdAt DESC")
    Page<Order> getBranchActiveOrders(Branch branch, Pageable pageable);

    // Shipper history: sort by timeDelivered DESC
    @Query("SELECT o FROM Order o WHERE o.shipper = :shipper AND o.status IN ('delivered', 'cancelled', 'refunded') ORDER BY o.timeDelivered DESC")
    Page<Order> getShipperHistory(Shipper shipper, Pageable pageable);

    // Shipper active: sort by timeConfirmed DESC
    @Query("SELECT o FROM Order o WHERE o.shipper = :shipper AND o.status IN ('confirmed', 'in_progress') ORDER BY o.timeConfirmed DESC")
    Page<Order> getShipperActiveOrders(Shipper shipper, Pageable pageable);

    Order findByOrderId(int orderId);

    // Thay các phương thức sau
    List<Order> findByBranch_Restaurant_RestaurantIdAndStatusOrderByCreatedAtDesc(int restaurantId, String status);
    List<Order> findByStatusAndShipperIsNullOrderByCreatedAtAsc(String status);
    List<Order> findByShipperAndStatusOrderByCreatedAtDesc(Shipper shipper, String status);
    List<Order> findByShipperAndStatusOrderByTimeDeliveredDesc(Shipper shipper, String status);

    // Hoặc sửa @Query
    @Query("SELECT o FROM Order o WHERE o.customer = :user AND o.status IN (:statusList) ORDER BY o.timeDelivered DESC")
Page<Order> getCustomerHistory(User user, @Param("statusList") List<String> statusList, Pageable pageable);
}