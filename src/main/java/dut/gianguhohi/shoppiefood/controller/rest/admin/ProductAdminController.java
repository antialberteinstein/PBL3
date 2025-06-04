package dut.gianguhohi.shoppiefood.controller.rest.admin;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

import dut.gianguhohi.shoppiefood.dtos.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;
import dut.gianguhohi.shoppiefood.services.product.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductService service;

    @PutMapping("/allow")
    public ResponseEntity<?> allowProduct(@RequestParam int productId) {
        Product product = repository.findByProductId(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại");
        }

        product.setAllow(true);
        repository.save(product);


        return ResponseEntity.ok(Map.of("success", true, "message", "Kiểm duyệt thành công"));
    }

    @PutMapping("/reject")
    public ResponseEntity<?> rejectProduct(@RequestParam int productId, 
                                           @RequestParam String reason) {
        Product product = repository.findByProductId(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại");
        }

        product.setAllow(false);
        product.setRejectedReason(reason);
        repository.save(product);

        return ResponseEntity.ok(Map.of("success", true, "message", "Từ chối sản phẩm thành công"));
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        if (search != null && !search.isBlank()) {
            productPage = repository.findByNameOrDescriptionContains(search, pageable);
        } else {
            productPage = repository.findAll(pageable);
        }
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> new ProductDTO(product, service.getProductCategories(product)))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(Map.of(
                "products", productDTOs,
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements(),
                "page", productPage.getNumber()
        ));
    }

}