package dut.gianguhohi.shoppiefood.controller.web.restaurant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    // ==========================
    // == Restaurant Home      ==
    // ==========================
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }

        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Hiện không ở trạng thái cửa hàng");
        }
        int restaurantId = (Integer) restaurantIdObj;
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("isOwner", true);
        return "restaurant/home";
    }

    // ==========================
    // == Create Restaurant    ==
    // ==========================
    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }

        return "restaurant/create";
    }

    @PostMapping("/create")
    public String createRestaurant(
        @RequestParam String name,
        @RequestParam String description,
        HttpSession session
    ) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để tạo nhà hàng");
        }
        Integer userId = (Integer) userIdObj;
        User seller = userService.readById(userId);
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người bán không tồn tại");
        }
        Restaurant restaurant = restaurantService.create(seller, name, description);
        session.setAttribute("restaurant", restaurant);
        return "redirect:/restaurant/enter/" + restaurant.getRestaurantId();
    }

    // ==========================
    // == Enter/Exit Restaurant ==
    // ==========================
    @GetMapping("/enter/{id}")
    public String enter(@PathVariable int id, HttpSession session, Model model) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }


        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj != null && (Integer) restaurantIdObj == id) {
            return "redirect:/restaurant/home";
        }

        session.setAttribute("role", "restaurant");
        session.setAttribute("restaurantId", id);
        return "redirect:/restaurant/home";
    }

    @GetMapping("/exit")
    public String exit(HttpSession session) {
        session.setAttribute("role", "user");
        session.removeAttribute("restaurantId");
        return "redirect:/user/home";
    }

    // ==========================
    // == Restaurant Detail    ==
    // ==========================
    @GetMapping("/detail/{id}")
    public String getRestaurantDetail(@PathVariable int id, Model model, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/auth/login";
        }
        try {
            int restaurantId = (Integer) session.getAttribute("restaurantId");
            if (restaurantId == id) {
                return "redirect:/restaurant/home";
            }
        } catch (Exception e) {
            // Ignore and continue
        }
        Restaurant restaurant = restaurantService.readById(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurant/home";
    }

    // ==========================
    // == Restaurant Profile   ==
    // ==========================
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        // Same as restaurant home

        return "redirect:/restaurant/home";
    }


    // ==========================
    // == Restaurant's orders ==
    // ==========================
    @GetMapping("/order/active")
    public String getActiveOrders(HttpSession session, Model model) {
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        int restaurantId = (Integer) restaurantIdObj;


        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("orderStatus", "active");
        return "restaurant/order";
    }

    @GetMapping("/order/history")
    public String getOrderHistory(HttpSession session, Model model) {
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        int restaurantId = (Integer) restaurantIdObj;
        Restaurant restaurant = restaurantService.readById(restaurantId);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhà hàng không tồn tại");
        }
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("orderStatus", "history");
        return "restaurant/order";
    }

    @GetMapping("/order")
    public String orderDefault(Model model, HttpSession session) {
        // Đoạn code này sẽ redirect về /restaurant/order/active
        return "redirect:/restaurant/order/active";
    }

    @GetMapping("/order/{status}")
    public String order(@PathVariable String status, Model model, HttpSession session) {
        // Kiểm tra quyền truy cập
        Object restaurantIdObj = session.getAttribute("restaurantId");
        if (restaurantIdObj == null) {
            return "redirect:/restaurant/login";
        }

        int restaurantId = (Integer) restaurantIdObj;
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("orderStatus", status);

        return "restaurant/order";
    }
}