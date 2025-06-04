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

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;
import dut.gianguhohi.shoppiefood.models.Users.Admin;

import org.springframework.web.bind.annotation.PostMapping;

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

    @Autowired
    private AdminRepository adminRepo;

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

    @PostMapping("/login")
    public String login(HttpSession session, Model model,
                        @RequestParam String loginName,
                        @RequestParam String password) {
        Admin admin = adminRepo.findByLoginName(loginName);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("adminId", admin.getAdminId());
            return "redirect:/admin/home";
        } else {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "admin/login";
        }
    }

    // ==========================
    // == Admin Home           ==
    // ==========================
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        return "redirect:/admin/dashboard";
    }

    @GetMapping
    public String adminPage() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        int adminId = (Integer) adminIdObj;
        model.addAttribute("adminId", adminId);
        // Add any other attributes needed for the dashboard

        model.addAttribute("numberOfProducts", productRepo.count());
        model.addAttribute("numberOfUsers", userRepo.count());
        model.addAttribute("numberOfOrders", orderRepo.count());
        model.addAttribute("numberOfRestaurants", restaurantRepo.count());
        // Fetch number of payments from external API
        Long numberOfPayments = 0L;
        try {
            RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            ResponseEntity<Long> response = restTemplate.getForEntity("http://157.245.52.20:8082/api/payment/number", Long.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            numberOfPayments = response.getBody();
            }
        } catch (Exception e) {
            // Log exception or handle as needed
        }
        model.addAttribute("numberOfPayments", numberOfPayments);
        model.addAttribute("numberOfFeedbacks", TEMP_NUMBER_OF_FEEDBACKS);
        return "admin/dashboard";
    }

    @GetMapping("/product")
    public String product(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/product";
    }

    @GetMapping("/user")
    public String user(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/user";
    }

    @GetMapping("/order")
    public String order(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/order";
    }

    @GetMapping("/restaurant")
    public String restaurant(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/restaurant";
    }

    @GetMapping("/payment")
    public String payment(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/payment";
    }

    @GetMapping("/feedback")
    public String feedback(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
        return "admin/feedback";
    }

    @GetMapping("/discount")
    public String discount(HttpSession session, Model model) {
        Object adminIdObj = session.getAttribute("adminId");
        if (adminIdObj == null) {
            return "redirect:/admin/login";
        }
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