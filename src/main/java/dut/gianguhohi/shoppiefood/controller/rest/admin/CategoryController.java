package dut.gianguhohi.shoppiefood.controller.rest.admin;

import dut.gianguhohi.shoppiefood.models.Product.Category;
import dut.gianguhohi.shoppiefood.services.product.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Create category
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestParam String name) {
        Category category = categoryService.create(name);
        return ResponseEntity.ok(category);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAll();
        List<Map<String, Object>> response = categories.stream().map(category -> {
            long nproducts = categoryService.countProducts(category);
            return Map.of(
                    "category", category,
                    "nproducts", nproducts
            );
        }).toList();

        return ResponseEntity.ok(response);
    }

    // Get category by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getById(id);
        long nproducts = categoryService.countProducts(category);
        return ResponseEntity.ok(Map.of(
                "category", category,
                "nproducts", nproducts
        ));
    }

    // Get category by name
    @GetMapping("/by-name")
    public ResponseEntity<?> getCategoryByName(@RequestParam String name) {
        Category category = categoryService.getByName(name);
        long nproducts = categoryService.countProducts(category);
        return ResponseEntity.ok(Map.of(
                "category", category,
                "nproducts", nproducts
        ));
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestParam String name) {
        Category updated = categoryService.update(id, name);
        return ResponseEntity.ok(updated);
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        categoryService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}