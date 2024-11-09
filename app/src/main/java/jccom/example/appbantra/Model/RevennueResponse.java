package jccom.example.appbantra.Model;

import java.util.List;

public class RevennueResponse {
    private List<Revennue> revenue;

    public RevennueResponse(List<Revennue> revenue) {
        this.revenue = revenue;
    }

    public List<Revennue> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<Revennue> revenue) {
        this.revenue = revenue;
    }
}
