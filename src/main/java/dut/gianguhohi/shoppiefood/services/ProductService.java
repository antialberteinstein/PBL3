package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import java.util.List;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;

@Transactional
@Service
public class ProductService {

    private static final int MAX_CATEGORIES = 3;

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
        Product product = productRepository.findByProductId(id);
        if (product == null) {
            throw new AppServiceException("Không tìm thấy sản phẩm");
        }
        return product;
    }

    public Product create(
        Restaurant restaurant,
        String imageUrl,
        String name,
        String description,
        long price,
        List<String> categories,
        long remainingQuantity
    ) {
        validateProduct(restaurant, name, description, price);
        if (categories == null) {
            categories = List.of(); // Default to empty list if null
        }
        if (categories.size() > MAX_CATEGORIES) {
            throw new AppServiceException("Số lượng danh mục không hợp lệ, tối đa là " + MAX_CATEGORIES);
        }

        if (remainingQuantity < 0) {
            throw new AppServiceException("Số lượng sản phẩm không hợp lệ");
        }

        // Optional: Check for duplicate product name in the same restaurant
        if (productRepository.existsByNameAndRestaurant(name, restaurant)) {
            throw new AppServiceException("Sản phẩm với tên này đã tồn tại trong nhà hàng");
        }

        float rating = 0.0f; // Default rating for new products
        long ratingNumber = 0; // Default rating number for new products

        Product product = new Product(restaurant, imageUrl, name, description, price, rating, ratingNumber, categories, remainingQuantity);
        product.setAllow(true); // Assuming new products are allowed by default
        return productRepository.save(product);
    }

    public Product update(
        int id,
        Restaurant restaurant,
        String imageUrl,
        String name,
        String description,
        long price
    ) {
        Product existingProduct = readById(id);
        validateProduct(restaurant, name, description, price);

        existingProduct.setRestaurant(restaurant);
        existingProduct.setImageUrl(imageUrl);
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);

        return productRepository.save(existingProduct);
    }

    public float rate(int id, int rating) {
        if (rating < 1 || rating > 5) {
            throw new AppServiceException("Đánh giá phải từ 1 đến 5 sao");
        }

        Product product = readById(id);
        float currentRating = product.getRating();
        long ratingCount = product.getRatingNumber();

        // Calculate new rating
        float newRating = ((currentRating * ratingCount) + rating) / (ratingCount + 1);
        product.setRating(newRating);
        product.setRatingNumber(ratingCount + 1);

        productRepository.save(product);
        return newRating;
    }

    public void disable(int id) {
        Product product = readById(id);
        product.setAllow(false);
        productRepository.save(product);
    }

    public void changeQuantity(int id, long newQuantity) {
        Product product = readById(id);
        if (newQuantity < 0) {
            throw new AppServiceException("Số lượng sản phẩm không hợp lệ");
        }
        product.setRemainingQuantity(newQuantity);
        productRepository.save(product);
    }

    public void enable(int id) {
        Product product = readById(id);
        product.setAllow(true);
        productRepository.save(product);
    }

    public void addCategory(int id, String category) {
        Product product = readById(id);
        if (product.getCategories().size() >= MAX_CATEGORIES) {
            throw new AppServiceException("Sản phẩm đã có đủ danh mục");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new AppServiceException("Danh mục không được để trống");
        }
        product.getCategories().add(category);
        productRepository.save(product);
    }

    public void removeCategory(int id, String category) {
        Product product = readById(id);
        if (!product.getCategories().remove(category)) {
            throw new AppServiceException("Danh mục không tồn tại trong sản phẩm");
        }
        productRepository.save(product);
    }

    public void delete(int id) {
        Product product = readById(id);
        productRepository.delete(product);
    }

    /* Validation phase */
    private void validateProduct(Restaurant restaurant, String name, String description, long price) {
        if (restaurant == null) {
            throw new AppServiceException("Nhà hàng không hợp lệ");
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