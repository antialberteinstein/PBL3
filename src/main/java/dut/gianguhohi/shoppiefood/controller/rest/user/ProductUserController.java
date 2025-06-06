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
        List<ProductDTO> dtoList = products.getContent().stream().map(product -> {
        List<String> categories = productCategoryRepository.findByProduct(product)
                .stream().map(pc -> pc.getCategory().getName()).collect(Collectors.toList());
            return new ProductDTO(product, categories);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "content", dtoList,
                "totalPages", products.getTotalPages(),
                "totalElements", products.getTotalElements(),
                "page", products.getNumber()
        ));
    }
}