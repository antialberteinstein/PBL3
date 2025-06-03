package dut.gianguhohi.shoppiefood.services.product;

import dut.gianguhohi.shoppiefood.models.Product.Category;
import dut.gianguhohi.shoppiefood.models.Product.ProductCategory;
import dut.gianguhohi.shoppiefood.repositories.Products.CategoryRepository;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductCategoryRepository pCatRepository;

    // Create
    public Category create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục không được để trống");
        }
        if (categoryRepository.existsByName(name.trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh mục đã tồn tại");
        }
        Category category = new Category(name.trim());
        return categoryRepository.save(category);
    }

    // Read all
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // Read by name
    public Category getByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục");
        }
        return category;
    }

    // Read by id
    public Category getById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
    }

    public long countProducts(Category category) {
        return pCatRepository.countByCategory(category);
    }



    // Update
    public Category update(int id, String newName) {
        Category category = getById(id);
        if (newName == null || newName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục không được để trống");
        }
        if (!category.getName().equals(newName.trim()) && categoryRepository.existsByName(newName.trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục đã tồn tại");
        }
        category.setName(newName.trim());
        return categoryRepository.save(category);
    }

    // Delete
    public void delete(int id) {
        Category category = getById(id);
        // Get and delete all the productCategories.
        List<ProductCategory> lst = pCatRepository.findByCategory(category);
        if (lst != null && !lst.isEmpty()) {
            for (ProductCategory pc : lst) {
                pCatRepository.delete(pc);
            }
        }

        categoryRepository.delete(category);
    }
}