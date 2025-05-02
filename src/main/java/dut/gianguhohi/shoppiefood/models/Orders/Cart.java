package dut.gianguhohi.shoppiefood.models.Orders;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import dut.gianguhohi.shoppiefood.models.Users.Users;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Cart() {
        this.createdAt = LocalDateTime.now();
    }

    public Cart(Users user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
