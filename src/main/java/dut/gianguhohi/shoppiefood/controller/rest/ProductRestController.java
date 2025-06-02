package dut.gianguhohi.shoppiefood.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.services.ProductService;
import dut.gianguhohi.shoppiefood.services.RestaurantService;
import dut.gianguhohi.shoppiefood.dtos.ProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        Product product = productService.readById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ProductDTO(product));
    }

    // POST /api/products/byRestaurant/create
    @PostMapping("/byRestaurant/create")
    public ResponseEntity<?> createProductByRestaurant(
            @RequestParam int restaurantId,
            @RequestParam(required = false) String imageUrl,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam long price,
            @RequestParam(required = false) List<String> categories,
            @RequestParam long remainingQuantity
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        Product product = productService.create(
                restaurant, imageUrl, name, description, price, categories, remainingQuantity
        );
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @PutMapping("/byRestaurant/update/{id}")
    public ResponseEntity<?> updateProductByRestaurant(
            @PathVariable int id,
            @RequestParam int restaurantId,
            @RequestParam(required = false) String imageUrl,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam long price
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        Product product = productService.update(
                id, restaurant, imageUrl, name, description, price
        );
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @GetMapping("/byRestaurant/{restaurantId}")
    public ResponseEntity<?> getProductsByRestaurant(
            @PathVariable int restaurantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Restaurant restaurant = restaurantService.readById(restaurantId);
        int exactPage = (page > 0) ? page - 1 : 0;
        Page<Product> products = restaurantService.getProductsByRestaurant(restaurant, exactPage, size);
        List<ProductDTO> productDTOs = products.stream().map(ProductDTO::new).toList();
        return ResponseEntity.ok(
            java.util.Map.of(
                "products", productDTOs,
                "totalPages", products.getTotalPages(),
                "totalElements", products.getTotalElements(),
                "page", products.getNumber() + 1
            )
        );
    }

    @DeleteMapping("/byRestaurant/delete/{id}")
    public ResponseEntity<?> deleteProductByRestaurant(@PathVariable int id) {
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}