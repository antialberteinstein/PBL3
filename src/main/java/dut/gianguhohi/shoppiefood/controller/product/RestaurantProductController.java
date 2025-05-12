package dut.gianguhohi.shoppiefood.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dut.gianguhohi.shoppiefood.service.ProductService;
import dut.gianguhohi.shoppiefood.model.Product;
import java.util.List;



@RestController
public class RestaurantProductController {

    @Autowired
    private ProductService productService;
}