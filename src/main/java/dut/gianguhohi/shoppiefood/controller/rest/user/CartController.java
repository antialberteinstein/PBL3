package dut.gianguhohi.shoppiefood.controller.rest.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.services.user.CartService;
import dut.gianguhohi.shoppiefood.services.product.ProductService;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import dut.gianguhohi.shoppiefood.dtos.CartItemDTO;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    // ==========================================================
    //                  TESTING ENDPOINTS
    // ==========================================================

    // ==========================
    // == Cart Query Endpoints ==
    // ==========================
    /* @GetMapping("/user")
    public ResponseEntity<?> getCartItems(
        @PathVariable int userId
    ) {

        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }
        List<CartItem> items = cartService.getCartItemsByUser(user);
        List<CartItemDTO> dtos = items.stream().map(CartItemDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    } */

    // ==========================
    // == Cart CRUD Endpoints  ==
    // ==========================
    /* @PostMapping("/user/{userId}/add")
    public ResponseEntity<?> addToCart(
            @PathVariable int userId,
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }
        Product product = productService.readById(productId);
        CartItem item = cartService.addToCart(user, product, quantity);
        return ResponseEntity.ok(new CartItemDTO(item));
    } */

    // ============================================================
    //                        REAL ENDPOINTS
    // ============================================================


    // ==========================
    // == Cart Query Endpoints ==
    // ==========================
    @GetMapping("/user")
    public ResponseEntity<?> getCartItems() {
        int userId = (int) session.getAttribute("userId");

        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }
        List<CartItem> items = cartService.getCartItemsByUser(user);
        List<CartItemDTO> dtos = items.stream().map(CartItemDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ==========================
    // == Cart CRUD Endpoints  ==
    // ==========================
    @PostMapping("/user/add")
    public ResponseEntity<?> addToCart(
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        int userId = (int) session.getAttribute("userId");
        User user = userService.readById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }
        Product product = productService.readById(productId);
        CartItem item = cartService.addToCart(user, product, quantity);
        return ResponseEntity.ok(new CartItemDTO(item));
    }

    @PutMapping("/change/{cartItemId}")
    public ResponseEntity<?> changeQuantity(
            @PathVariable int cartItemId,
            @RequestParam int quantity
    ) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        CartItem updated = cartService.changeQuantity(cartItem, quantity);
        return ResponseEntity.ok(new CartItemDTO(updated));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable int cartItemId
    ) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        cartService.removeFromCart(cartItem);
        return ResponseEntity.ok(Map.of("success", true));
    }

}