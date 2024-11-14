package jccom.example.appbantra.Model;

public class Revenue {
    private String product_name;
    private float totalRevenue;
    private String product_id;

    public Revenue(String product_name, float totalRevenue, String product_id) {
        this.product_name = product_name;
        this.totalRevenue = totalRevenue;
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
