package dut.gianguhohi.shoppiefood.repositories.Products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Product> findByNameContains(String pattern);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pattern, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    Page<Product> findByNameOrDescriptionContains(String pattern, Pageable pageable);

    Product findByProductId(int productId);

    Page<Product> findByRestaurant(Restaurant restaurant, Pageable pageable);
    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);
    boolean existsByProductId(int productId);
}
