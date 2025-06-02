package dut.gianguhohi.shoppiefood.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    // ==========================
    // == Product Info         ==
    // ==========================
    @GetMapping("/info/{id}")
    public String getProductInfo(
        @PathVariable("id") String productId,
        Model model,
        HttpSession session
    ) {
        model.addAttribute("productId", productId);
        return "product/info";
    }
}