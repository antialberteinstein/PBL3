package dut.gianguhohi.shoppiefood.controller.rest.user;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import dut.gianguhohi.shoppiefood.dtos.ProductDTO;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.services.product.ProductService;
import java.util.Map;
import java.util.stream.Collectors;

import dut.gianguhohi.shoppiefood.repositories.Products.ProductCategoryRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products")
public class ProductUserController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) Float minRating,
            @RequestParam(defaultValue = "postedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Page<Product> products = productService.filterProducts(
                categoryId, search, minPrice, maxPrice, minRating, sortBy, sortDir, page, size
        );
        RestTemplate restTemplate = new RestTemplate();

        List<ProductDTO> dtoList = products.getContent().stream().map(product -> {
            List<String> categories = productCategoryRepository.findByProduct(product)
                    .stream().map(pc -> pc.getCategory().getName()).collect(Collectors.toList());

            // Fetch feedbacks and calculate rating
            float avgRating = 0.0f;
            int ratingNumber = 0;
            try {
                String url = "http://157.245.52.20:9357/api/feedbacks/list/" + product.getProductId();
                List<Map<String, Object>> feedbacks = restTemplate.getForObject(url, List.class);
                if (feedbacks != null && !feedbacks.isEmpty()) {
                    double avg = feedbacks.stream()
                            .mapToInt(fb -> (int) fb.getOrDefault("rating", 0))
                            .average()
                            .orElse(0.0);
                    avgRating = (float) avg;
                    ratingNumber = feedbacks.size();
                }
            } catch (Exception e) {
                // Optionally log error
            }

            ProductDTO dto = new ProductDTO(product, categories);
            dto.setRating(avgRating);
            dto.setRatingNumber((long) ratingNumber);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "content", dtoList,
                "totalPages", products.getTotalPages(),
                "totalElements", products.getTotalElements(),
                "page", products.getNumber()
        ));
    }
}