package jccom.example.appbantra.Model;

public class Revennue {
    private String product_name;
    private float totalRevennue;

    public Revennue(String product_name, float totalRevennue) {
        this.product_name = product_name;
        this.totalRevennue = totalRevennue;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getTotalRevennue() {
        return totalRevennue;
    }

    public void setTotalRevennue(float totalRevennue) {
        this.totalRevennue = totalRevennue;
    }
}
