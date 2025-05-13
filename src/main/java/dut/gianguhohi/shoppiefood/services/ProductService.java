package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService extends BaseService<Product, Integer> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected ProductRepository getRepository() {
        return productRepository;
    }

    public List<Product> search(String pattern) {
        return productRepository.findByNameContains(pattern);
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableTrue();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByRestaurant(Integer restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }

    public Product create(Product product) {
        validateEntity(product);
        try {
            return save(product);
        } catch (Exception e) {
            throw new ProductRelatedException("Cannot create " + product.getName() + " as it already exists in the system");
        }
    }

    public Product update(Integer id, Product product) {
        validateEntity(product);
        Product existingProduct = findById(id)
            .orElseThrow(() -> new ProductRelatedException("Product not found"));
        
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setAvailable(product.isAvailable());
        existingProduct.setImageUrl(product.getImageUrl());
        
        return save(existingProduct);
    }

    public Product toggleAvailability(Integer id) {
        Product product = findById(id)
            .orElseThrow(() -> new ProductRelatedException("Product not found"));
        
        product.setAvailable(!product.isAvailable());
        return save(product);
    }

    public void delete(Integer id) {
        Product product = findById(id)
            .orElseThrow(() -> new ProductRelatedException("Product not found"));
        delete(product);
    }

    public static class ProductRelatedException extends RuntimeException {
        public ProductRelatedException(String message) {
            super(message);
        }
    }
}
