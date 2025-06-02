package dut.gianguhohi.shoppiefood.controller.rest.restaurant;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;
import dut.gianguhohi.shoppiefood.services.product.ProductService;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.dtos.ProductDTO;
import dut.gianguhohi.shoppiefood.controller.rest.restaurant.dtos.ProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurant")
public class ProductRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ProductService productService;

    // ==========================
    // == Product APIs         ==
    // ==========================

    @PostMapping("/{restaurantId}/products")
    public ResponseEntity<?> createProduct(
            @PathVariable int restaurantId,
            @RequestBody ProductRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Product product = productService.create(
                restaurant,
                req.getImageUrl(),
                req.getName(),
                req.getDescription(),
                req.getPrice(),
                req.getCategories(),
                req.getRemainingQuantity()
        );
        return ResponseEntity.ok(
            new ProductDTO(
                product,
                productService.getProductCategories(product)
            )
        );
    }

    @PutMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int restaurantId,
            @PathVariable int productId,
            @RequestBody ProductRequest req
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        Product product = productService.update(
                productId,
                restaurant,
                req.getImageUrl(),
                req.getName(),
                req.getDescription(),
                req.getPrice(),
                req.getCategories(),
                req.getRemainingQuantity()
        );
        return ResponseEntity.ok(
            new ProductDTO(
                product,
                productService.getProductCategories(product)
            )
        );
    }

    @GetMapping("/{restaurantId}/products")
    public ResponseEntity<?> getProductsByRestaurant(
            @PathVariable int restaurantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        int exactPage = (page > 0) ? page - 1 : 0;
        Page<Product> products = productService.getProductsByRestaurant(restaurant, exactPage, size);
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product, productService.getProductCategories(product)))
                .toList();
        return ResponseEntity.ok(
                Map.of(
                        "products", productDTOs,
                        "totalPages", products.getTotalPages(),
                        "totalElements", products.getTotalElements(),
                        "page", products.getNumber() + 1
                )
        );
    }

    @GetMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<?> getProductById(
            @PathVariable int restaurantId,
            @PathVariable int productId
    ) {
        Product product = productService.readById(productId);
        if (product == null || product.getRestaurant().getRestaurantId() != restaurantId) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
            new ProductDTO(
                product,
                productService.getProductCategories(product)
            )
        );
    }

    @DeleteMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable int restaurantId,
            @PathVariable int productId
    ) {
        productService.delete(productId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}