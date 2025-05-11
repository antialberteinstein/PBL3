package dut.gianguhohi.shoppiefood.services.Products;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> search(String pattern) {
        return productRepository.findByNameContains(pattern);
    }

    public Product create(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new ProductRelatedException("Không thể tạo " + product.getName() + " vì đã tồn tại trong hệ thống");
        }
    }

    public static class ProductRelatedException extends RuntimeException {
        public ProductRelatedException(String message) {
            super(message);
        }
    }
}
