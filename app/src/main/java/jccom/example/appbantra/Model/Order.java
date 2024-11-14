package jccom.example.appbantra.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("order_id") // Ánh xạ `order_id` từ JSON API
    private String id;
    @SerializedName("payment_method") // Thêm ánh xạ cho phương thức thanh toán
    private String paymentMethod;
    private String product_id;
    private String product_name;
    private int quantity;
    private double price;
    private double total_price;
    private String status; // Trạng thái đơn hàng
    private List<String> productIds;

    public Order() {
    }

    public Order(String id, String paymentMethod, String product_id, String product_name, int quantity, double price, double total_price, String status, List<String> productIds) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.status = status;
        this.productIds = productIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
