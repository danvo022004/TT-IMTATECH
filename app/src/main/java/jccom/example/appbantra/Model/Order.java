package jccom.example.appbantra.Model;

import com.google.gson.annotations.SerializedName;

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

    public Order() {
    }

    public Order(String id, String paymentMethod, String product_id, String product_name, int quantity, double price, double total_price, String status) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho product_id
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    // Getter và Setter cho product_name
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    // Getter và Setter cho price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter và Setter cho total_price
    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter và Setter cho status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
