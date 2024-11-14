package jccom.example.appbantra.Model;

public class CartItem {
    private Product product;
    private int quantity;

    // Getter và Setter
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}