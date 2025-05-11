package dut.gianguhohi.shoppiefood.services.Users;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.repositories.Orders.CartItemRepository;
import dut.gianguhohi.shoppiefood.models.Product.Product;


@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(User user, Product product, int quantity) {
        CartItem cartItem = new CartItem(user, product, quantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem addToCart(User user, Product product) {
        return addToCart(user, product, 1);
    }

    public CartItem changeQuantity(CartItem cartItem, int newQuantity) {
        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem removeFromCart(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
        return cartItem;
    }


}
