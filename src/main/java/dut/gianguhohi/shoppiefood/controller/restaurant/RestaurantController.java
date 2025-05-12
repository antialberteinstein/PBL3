
package dut.gianguhohi.shoppiefood.controller.restaurant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RestaurantController {

    @GetMapping("/restaurant/home")
    public String home(Model model) {
        return "restaurant/home";
    }
}