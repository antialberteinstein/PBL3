package dut.gianguhohi.shoppiefood.repositories.misc;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Page<Branch> findByRestaurant(Restaurant restaurant, Pageable pageable);
    Branch findByBranchId(int branchId);
    boolean existsByBranchId(int branchId);
    boolean existsByRestaurantAndBranchName(Restaurant restaurant, String branchName);
    boolean existsByRestaurantAndBranchId(Restaurant restaurant, int branchId);
    
}
