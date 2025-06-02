package dut.gianguhohi.shoppiefood.dtos;

import dut.gianguhohi.shoppiefood.models.Orders.CartItem;
import dut.gianguhohi.shoppiefood.models.Product.Product;

public class CartItemDTO {
    private int cartItemId;
    private int productId;
    private String productName;
    private String productImageUrl;
    private long productPrice;
    private int quantity;

    public CartItemDTO() {}

    public CartItemDTO(CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        Product product = cartItem.getProduct();
        if (product != null) {
            this.productId = product.getProductId();
            this.productName = product.getName();
            this.productImageUrl = product.getImageUrl();
            this.productPrice = product.getPrice();
        }
        this.quantity = cartItem.getQuantity();
    }

    // Getters and setters
    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public long getProductPrice() { return productPrice; }
    public void setProductPrice(long productPrice) { this.productPrice = productPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}