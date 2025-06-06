package dut.gianguhohi.shoppiefood.repositories.Products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT p, COUNT(oi) as popularity
    FROM Product p
    LEFT JOIN OrderItem oi ON oi.product = p
    WHERE (:categoryId IS NULL OR EXISTS (
        SELECT 1 FROM ProductCategory pc WHERE pc.product = p AND pc.category.id = :categoryId
    ))
    AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
    AND (:minPrice IS NULL OR p.price >= :minPrice)
    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    AND (:minRating IS NULL OR p.rating >= :minRating)
    GROUP BY p
    """)
    Page<Object[]> findProductsWithPopularity(
        @Param("categoryId") Integer categoryId,
        @Param("search") String search,
        @Param("minPrice") Long minPrice,
        @Param("maxPrice") Long maxPrice,
        @Param("minRating") Float minRating,
        Pageable pageable
    );

        @Query("""
    SELECT p FROM Product p
    LEFT JOIN ProductCategory pc ON pc.product = p
    LEFT JOIN Category c ON pc.category = c
    WHERE (:categoryId IS NULL OR c.id = :categoryId)
    AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))
    AND (:minPrice IS NULL OR p.price >= :minPrice)
    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    AND (:minRating IS NULL OR p.rating >= :minRating)
    """)
    Page<Product> filterProducts(
        @Param("categoryId") Integer categoryId,
        @Param("search") String search,
        @Param("minPrice") Long minPrice,
        @Param("maxPrice") Long maxPrice,
        @Param("minRating") Float minRating,
        Pageable pageable
    );
}
