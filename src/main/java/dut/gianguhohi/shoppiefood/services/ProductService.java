package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Product.Category;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import java.util.List;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;

@Transactional
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> search(String pattern) {
        validateSearchPattern(pattern);
        return productRepository.findByNameContains(pattern);
    }

    public Product readById(int id) {
        if (id <= 0) {
            throw new AppServiceException("ID sản phẩm không hợp lệ");
        }
        Product product = productRepository.findByProductId(id);
        if (product == null) {
            throw new AppServiceException("Không tìm thấy sản phẩm");
        }
        return product;
    }

    public Product create(
        Restaurant restaurant,
        Category category,
        String imageUrl,
        String name,
        String description,
        long price,
        float rating
    ) {
        validateProduct(restaurant, category, name, description, price);

        // Optional: Check for duplicate product name in the same restaurant
        if (productRepository.existsByNameAndRestaurant(name, restaurant)) {
            throw new AppServiceException("Sản phẩm với tên này đã tồn tại trong nhà hàng");
        }

        Product product = new Product(restaurant, category, imageUrl, name, description, price, rating);
        return productRepository.save(product);
    }

    public Product update(
        int id,
        Restaurant restaurant,
        Category category,
        String imageUrl,
        String name,
        String description,
        long price,
        float rating,
        boolean isAvailable
    ) {
        Product existingProduct = readById(id);
        validateProduct(restaurant, category, name, description, price);

        existingProduct.setRestaurant(restaurant);
        existingProduct.setCategory(category);
        existingProduct.setImageUrl(imageUrl);
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setRating(rating);
        existingProduct.setAvailable(isAvailable);

        return productRepository.save(existingProduct);
    }

    public void delete(int id) {
        Product product = readById(id);
        productRepository.delete(product);
    }

    /* Validation phase */
    private void validateProduct(Restaurant restaurant, Category category, String name, String description, long price) {
        if (restaurant == null) {
            throw new AppServiceException("Nhà hàng không hợp lệ");
        }
        if (category == null) {
            throw new AppServiceException("Danh mục không hợp lệ");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new AppServiceException("Tên sản phẩm không được để trống");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new AppServiceException("Tên sản phẩm phải từ 2 đến 100 ký tự");
        }
        if (description != null && description.length() > 1000) {
            throw new AppServiceException("Mô tả sản phẩm không được vượt quá 1000 ký tự");
        }
        if (price < 0) {
            throw new AppServiceException("Giá sản phẩm không hợp lệ");
        }
    }

    private void validateSearchPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new AppServiceException("Từ khóa tìm kiếm không được để trống");
        }
    }
}