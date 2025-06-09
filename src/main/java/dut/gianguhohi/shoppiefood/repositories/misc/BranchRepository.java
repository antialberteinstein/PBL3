package dut.gianguhohi.shoppiefood.repositories.misc;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dut.gianguhohi.shoppiefood.models.misc.Branch;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Page<Branch> findByRestaurant(Restaurant restaurant, Pageable pageable);
    Branch findByBranchId(int branchId);
    Branch findByRestaurantAndBranchName(Restaurant restaurant, String branchName);
    boolean existsByBranchId(int branchId);

    boolean existsByRestaurantAndBranchName(Restaurant restaurant, String branchName);
    boolean existsByRestaurantAndBranchId(Restaurant restaurant, int branchId);
    Optional<Branch> findByRestaurantAndDefaultBranch(Restaurant restaurant, boolean defaultBranch);
    
    // Hoặc giữ nguyên tên phương thức nhưng sử dụng @Query
    @Query("SELECT b FROM Branch b WHERE b.restaurant = :restaurant AND b.defaultBranch = :isDefault")
    Optional<Branch> findByRestaurantAndIsDefault(
        @Param("restaurant") Restaurant restaurant, 
        @Param("isDefault") boolean isDefault);
    Optional<Branch> findFirstByRestaurant(Restaurant restaurant);
}
