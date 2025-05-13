package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.repositories.Orders.CartItemRepository;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import java.math.BigDecimal;

@Service
@Transactional
public class CartService extends BaseService<CartItem, Integer> {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Override
    protected CartItemRepository getRepository() {
        return cartItemRepository;
    }

    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(User user, Product product, int quantity) {
        validateEntity(user);
        validateEntity(product);

        if (quantity <= 0) {
            throw new CartRelatedException("Quantity must be greater than zero");
        }

        if (!product.isAvailable()) {
            throw new CartRelatedException("Product is not available");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return save(item);
        }

        CartItem cartItem = new CartItem(user, product, quantity);
        return save(cartItem);
    }

    public CartItem updateQuantity(Integer cartItemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new CartRelatedException("Quantity must be greater than zero");
        }

        CartItem cartItem = findById(cartItemId)
            .orElseThrow(() -> new CartRelatedException("Cart item not found"));

        if (!cartItem.getProduct().isAvailable()) {
            throw new CartRelatedException("Product is not available");
        }

        cartItem.setQuantity(newQuantity);
        return save(cartItem);
    }

    public void removeFromCart(Integer cartItemId) {
        CartItem cartItem = findById(cartItemId)
            .orElseThrow(() -> new CartRelatedException("Cart item not found"));
        delete(cartItem);
    }

    public void clearCart(User user) {
        List<CartItem> cartItems = getCartItemsByUser(user);
        cartItems.forEach(this::delete);
    }

    public BigDecimal calculateTotal(User user) {
        List<CartItem> cartItems = getCartItemsByUser(user);
        return cartItems.stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount(User user) {
        return getCartItemsByUser(user).size();
    }

    public static class CartRelatedException extends RuntimeException {
        public CartRelatedException(String message) {
            super(message);
        }
    }
}
