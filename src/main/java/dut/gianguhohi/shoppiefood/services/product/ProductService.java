package dut.gianguhohi.shoppiefood.services.product;

import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Product.Category;
import dut.gianguhohi.shoppiefood.models.Product.ProductCategory;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private static final int MAX_CATEGORIES = 3;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CategoryService categoryService;


    // ==========================
    // == Product Queries      ==
    // ==========================

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
        }
        return product;
    }

    public Page<Product> getProductsByRestaurant(Restaurant restaurant, int page, int size) {
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà hàng không hợp lệ");
        }
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByRestaurant(restaurant, pageable);
    }

    public List<String> getProductCategories(int productId) {
        Product product = readById(productId);
        List<ProductCategory> links = productCategoryRepository.findByProduct(product);
        return links.stream()
                .map(pc -> pc.getCategory().getName())
                .collect(Collectors.toList());
    }

    public List<String> getProductCategories(Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm không hợp lệ");
        }
        List<ProductCategory> links = productCategoryRepository.findByProduct(product);
        return links.stream()
                .map(pc -> pc.getCategory().getName())
                .collect(Collectors.toList());
    }

    // ==========================
    // == Product CRUD         ==
    // ==========================

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

        if (categories == null) categories = List.of();
        if (categories.size() > MAX_CATEGORIES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng danh mục không hợp lệ, tối đa là " + MAX_CATEGORIES);
        }
        if (remainingQuantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm phải lớn hơn 0");
        }
        if (productRepository.existsByNameAndRestaurant(name, restaurant)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm với tên này đã tồn tại trong nhà hàng");
        }

        float rating = 0.0f;
        long ratingNumber = 0;

        Product product = new Product(restaurant, imageUrl, name, description, price, rating, ratingNumber, remainingQuantity);
        product = productRepository.save(product);

        // Handle categories using CategoryService
        for (String catName : categories) {
            if (catName == null || catName.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục không hợp lệ");
            }
            Category category = categoryService.getByName(catName.trim());
            // Only create if not exists
            if (!productCategoryRepository.findByProduct(product).stream()
                    .anyMatch(pc -> pc.getCategory().getId() == category.getId())) {
                ProductCategory pc = new ProductCategory(product, category);
                productCategoryRepository.save(pc);
            }
        }

        return product;
    }

    public Product update(
        int id,
        Restaurant restaurant,
        String imageUrl,
        String name,
        String description,
        long price,
        List<String> categories,
        long remainingQuantity
    ) {
        Product existingProduct = readById(id);
        validateProduct(restaurant, name, description, price);

        existingProduct.setRestaurant(restaurant);
        existingProduct.setImageUrl(imageUrl);
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setRemainingQuantity(remainingQuantity);

        existingProduct.setAllow(null);  // Chuyển sang chờ duyệt.

        // Get current and new category sets
        List<ProductCategory> oldLinks = productCategoryRepository.findByProduct(existingProduct);
        Set<String> oldCategoryNames = oldLinks.stream()
                .map(pc -> pc.getCategory().getName())
                .collect(Collectors.toSet());
        Set<String> newCategoryNames = categories == null ? Set.of() :
                categories.stream().filter(s -> s != null && !s.trim().isEmpty())
                        .map(String::trim).collect(Collectors.toSet());

        // Remove ProductCategory links for removed categories
        for (ProductCategory pc : oldLinks) {
            if (!newCategoryNames.contains(pc.getCategory().getName())) {
                productCategoryRepository.delete(pc);
            }
        }

        // Add ProductCategory links for new categories
        if (newCategoryNames.size() > MAX_CATEGORIES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng danh mục không hợp lệ, tối đa là " + MAX_CATEGORIES);
        }
        for (String catName : newCategoryNames) {
            Category category = categoryService.getByName(catName);
            boolean exists = oldLinks.stream()
                    .anyMatch(pc -> pc.getCategory().getId() == category.getId());
            if (!exists) {
                ProductCategory pc = new ProductCategory(existingProduct, category);
                productCategoryRepository.save(pc);
            }
        }

        return productRepository.save(existingProduct);
    }

    public void delete(int id) {
        Product product = readById(id);
        // Delete all ProductCategory links
        List<ProductCategory> links = productCategoryRepository.findByProduct(product);
        productCategoryRepository.deleteAll(links);
        productRepository.delete(product);
    }

    // ==========================
    // == Product Actions      ==
    // ==========================

    public float rate(int id, int rating) {
        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đánh giá phải từ 1 đến 5 sao");
        }

        Product product = readById(id);
        float currentRating = product.getRating();
        long ratingCount = product.getRatingNumber();

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

    public void enable(int id) {
        Product product = readById(id);
        product.setAllow(true);
        productRepository.save(product);
    }

    public void changeQuantity(int id, long newQuantity) {
        Product product = readById(id);
        if (newQuantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm không hợp lệ");
        }
        product.setRemainingQuantity(newQuantity);
        productRepository.save(product);
    }

    // ==========================
    // == Validation Methods   ==
    // ==========================

    private void validateProduct(Restaurant restaurant, String name, String description, long price) {
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà hàng không hợp lệ");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sản phẩm không được để trống");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sản phẩm phải từ 2 đến 100 ký tự");
        }
        if (description != null && description.length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mô tả sản phẩm không được vượt quá 1000 ký tự");
        }
        if (price < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá sản phẩm không hợp lệ");
        }
    }

    private void validateSearchPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Từ khóa tìm kiếm không được để trống");
        }
    }
}