package dut.gianguhohi.shoppiefood.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Products.ProductRepository;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.repositories.Orders.OrderRepository;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.Order;
import dut.gianguhohi.shoppiefood.models.Product.Product;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private RestaurantRepository restaurantRepo;

    private final int TEMP_NUMBER_OF_FEEDBACKS = 2231;
    private final int TEMP_NUMBER_OF_PAYMENTS = 946;

    // ==========================
    // == Admin Login          ==
    // ==========================
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/home";
        }
        return "admin/login";
    }

    // ==========================
    // == Admin Home           ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        /* Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        int adminId = (Integer) adminIdObj;
        model.addAttribute("adminId", adminId); */
        // Add any other attributes needed for the dashboard

        model.addAttribute("numberOfProducts", productRepo.count());
        model.addAttribute("numberOfUsers", userRepo.count());
        model.addAttribute("numberOfOrders", orderRepo.count());
        model.addAttribute("numberOfRestaurants", restaurantRepo.count());
        model.addAttribute("numberOfFeedbacks", TEMP_NUMBER_OF_FEEDBACKS);
        model.addAttribute("numberOfPayments", TEMP_NUMBER_OF_PAYMENTS);
        return "admin/dashboard";
    }

    @GetMapping("/product")
    public String product(HttpSession session, Model model) {
        return "admin/product";
    }

    @GetMapping("/user")
    public String user(HttpSession session, Model model) {
        return "admin/user";
    }

    @GetMapping("/order")
    public String order(HttpSession session, Model model) {
        return "admin/order";
    }

    @GetMapping("/restaurant")
    public String restaurant(HttpSession session, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantRepo.findAll(pageable);
        model.addAttribute("restaurantPage", restaurantPage);
        return "admin/restaurant";
    }

    @GetMapping("/payment")
    public String payment(HttpSession session, Model model) {
        return "admin/payment";
    }

    @GetMapping("/feedback")
    public String feedback(HttpSession session, Model model) {
        return "admin/feedback";
    }

    @GetMapping("/discount")
    public String discount(HttpSession session, Model model) {
        return "admin/discount";
    }

    // ==========================
    // == Admin Logout         ==
    // ==========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("adminId");
        return "redirect:/admin/login";
    }
}