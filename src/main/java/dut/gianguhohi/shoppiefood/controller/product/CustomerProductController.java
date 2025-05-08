package dut.gianguhohi.shoppiefood.controller.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class CustomerProductController {

    @GetMapping("/customer/products")
    public String getCustomerProducts(Model model) {
        return "product/customer-list";
    }
}
