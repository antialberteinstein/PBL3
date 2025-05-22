package dut.gianguhohi.shoppiefood.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.services.RestaurantService;
import org.springframework.web.bind.annotation.RequestParam;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.misc.Address;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    
    @GetMapping("/restaurant/home")
    public String home(Model model, HttpSession session) {
        return "restaurant/home";
    }

    @GetMapping("/restaurant/create")
    public String create(HttpSession session, Model model) {
        return "restaurant/create";
    }

    @GetMapping("/restaurant/enter/{id}")
    public String enter(@PathVariable int id, HttpSession session, Model model) {
        session.setAttribute("role", "restaurant");
        session.setAttribute("restaurant", restaurantService.readById(id));


        return "redirect:/restaurant/home";
    }

    @GetMapping("/restaurant/exit")
    public String exit(HttpSession session) {
        session.setAttribute("role", "user");
        session.removeAttribute("restaurant");

        return "redirect:/user/home";
    }

    @GetMapping("/restaurant/detail/{id}")
    public String getRestaurantDetail(@PathVariable int id, Model model, HttpSession session) {
            try {
                Restaurant sessionRestaurant = (Restaurant) session.getAttribute("restaurant");
                if (sessionRestaurant != null) {
                    // Check if the session restaurant ID matches the requested ID
                    if (sessionRestaurant.getRestaurantId() == id) {
                        return "redirect:/restaurant/profile";
                    }
                }
            } catch (Exception e) {
                // Do nothing, just continue
            }

            Restaurant restaurant = restaurantService.readById(id);
            model.addAttribute("restaurant", restaurant);

            return "restaurant/profile";
    }

    @GetMapping("/restaurant/profile")
    public String profile(HttpSession session, Model model) {
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");

        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }

        model.addAttribute("restaurant", restaurant);

        // Danh dau ong tu soi chinh minh
        model.addAttribute("isOwner", true);



        return "restaurant/profile";
    }
} 