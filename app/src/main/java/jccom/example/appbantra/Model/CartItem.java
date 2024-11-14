package jccom.example.appbantra.Model;

public class CartItem {
    private Product productId;  // Đổi tên thành productId thay vì product để đồng nhất với API
    private int quantity;

    // Getter và Setter
    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
