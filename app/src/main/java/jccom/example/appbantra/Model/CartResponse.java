package jccom.example.appbantra.Model;

import java.util.List;

public class CartResponse {
    private boolean success;
    private String message;
    private Cart cart;
    private double totalAmount;
    private double selectedTotalAmount;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getSelectedTotalAmount() { return selectedTotalAmount; }
    public void setSelectedTotalAmount(double selectedTotalAmount) { this.selectedTotalAmount = selectedTotalAmount; }

    public static class Cart {
        private String userId;
        private List<CartItem> products;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public List<CartItem> getProducts() { return products; }
        public void setProducts(List<CartItem> products) { this.products = products; }
    }
}
