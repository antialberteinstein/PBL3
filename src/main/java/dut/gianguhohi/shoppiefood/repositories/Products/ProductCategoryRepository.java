package dut.gianguhohi.shoppiefood.repositories.Products;

import dut.gianguhohi.shoppiefood.models.Product.ProductCategory;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    List<ProductCategory> findByProduct(Product product);
    List<ProductCategory> findByCategory(Category category);
}