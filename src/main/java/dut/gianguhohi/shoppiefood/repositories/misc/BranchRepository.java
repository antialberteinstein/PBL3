package dut.gianguhohi.shoppiefood.repositories.misc;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByRestaurant(Restaurant restaurant);
    Branch findByBranchId(int branchId);
    boolean existsByBranchId(int branchId);
    boolean existsByRestaurantAndBranchName(Restaurant restaurant, String branchName);
    boolean existsByRestaurantAndBranchId(Restaurant restaurant, int branchId);
    
}
