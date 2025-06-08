package dut.gianguhohi.shoppiefood.init;

import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
public class ProductRatingInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductRatingInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        RestTemplate restTemplate = new RestTemplate();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            try {
                String url = "http://157.245.52.20:9357/api/feedbacks/list/" + product.getProductId();
                List<Map<String, Object>> feedbacks = restTemplate.getForObject(url, List.class);

                if (feedbacks != null && !feedbacks.isEmpty()) {
                    double avg = feedbacks.stream()
                        .mapToInt(fb -> (int) fb.getOrDefault("rating", 0))
                        .average()
                        .orElse(0.0);
                    product.setRating((float) avg); // Cast to float
                } else {
                    product.setRating(0.0f); // Use float literal
                }
                productRepository.save(product);
                System.out.println("Updated rating for product " + product.getProductId() + ": " + product.getRating());
            } catch (Exception e) {
                System.err.println("Failed to update rating for product " + product.getProductId() + ": " + e.getMessage());
            }
        }
    }
}