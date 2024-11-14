package jccom.example.appbantra.Model;

public class CartRequest {
    private String productId;  // Kiểu String là hợp lý cho _id của sản phẩm
    private int quantity;

    public CartRequest(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getter và Setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
