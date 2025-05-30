package dut.gianguhohi.shoppiefood.controller.rest;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.services.CartService;
import dut.gianguhohi.shoppiefood.services.ProductService;
import dut.gianguhohi.shoppiefood.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dut.gianguhohi.shoppiefood.dtos.CartItemDTO;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Lấy danh sách sản phẩm trong giỏ hàng của user hiện tại
    @GetMapping
    public ResponseEntity<?> getCartItems(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập để tiếp tục");
        }
        List<CartItem> items = cartService.getCartItemsByUser(user);
        List<CartItemDTO> dtos = items.stream().map(CartItemDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập để tiếp tục");
        }
        Product product = productService.readById(productId);
        CartItem item = cartService.addToCart(user, product, quantity);
        return ResponseEntity.ok(new CartItemDTO(item));
    }

    // Thay đổi số lượng sản phẩm trong giỏ hàng
    @PutMapping("/change")
    public ResponseEntity<?> changeQuantity(
            @RequestParam int cartItemId,
            @RequestParam int quantity
    ) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        CartItem updated = cartService.changeQuantity(cartItem, quantity);
        return ResponseEntity.ok(new CartItemDTO(updated));
    }

    // Xóa một sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(
            @RequestParam int cartItemId
    ) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        cartService.removeFromCart(cartItem);
        return ResponseEntity.ok().build();
    }
}