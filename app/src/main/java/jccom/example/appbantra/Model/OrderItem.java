package jccom.example.appbantra.Model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;

    private int quantity;
    private double price;

    @SerializedName("total_price")
    private double totalPrice;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
