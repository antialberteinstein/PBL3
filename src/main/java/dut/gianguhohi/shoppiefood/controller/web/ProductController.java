package dut.gianguhohi.shoppiefood.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.services.product.ProductService;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.dtos.ProductDTO;

import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    // ==========================
    // == Product Info         ==
    // ==========================
    @GetMapping("/info/{id}")
    public String getProductInfo(
        @PathVariable("id") int productId,
        Model model,
        HttpSession session
    ) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        Product product = service.readById(productId);

        ProductDTO productDTO = new ProductDTO(
            product, service.getProductCategories(product));

        
        model.addAttribute("product", productDTO);
        model.addAttribute("productId", productId);
        
        

        return "product/info";
    }
}