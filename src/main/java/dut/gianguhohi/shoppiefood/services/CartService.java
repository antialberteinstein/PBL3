package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.repositories.Orders.CartItemRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.models.Product.Product;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;

@Transactional
@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItemsByUser(User user) {
        validateUser(user);
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(User user, Product product, int quantity) {
        validateUser(user);
        validateProduct(product);
        validateQuantity(quantity);

        // Check if item already exists in cart
        CartItem existing = cartItemRepository.findByUserAndProduct(user, product);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cartItemRepository.save(existing);
        }

        CartItem cartItem = new CartItem(user, product, quantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem addToCart(User user, Product product) {
        return addToCart(user, product, 1);
    }

    public CartItem changeQuantity(CartItem cartItem, int newQuantity) {
        validateCartItem(cartItem);
        validateQuantity(newQuantity);
        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(CartItem cartItem) {
        validateCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(User user) {
        validateUser(user);
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    /* Validation phase */
    private void validateUser(User user) {
        if (user == null) {
            throw new AppServiceException("Người dùng không hợp lệ");
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new AppServiceException("Sản phẩm không hợp lệ");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new AppServiceException("Số lượng phải lớn hơn 0");
        }
    }

    private void validateCartItem(CartItem cartItem) {
        if (cartItem == null) {
            throw new AppServiceException("CartItem không hợp lệ");
        }
    }
}