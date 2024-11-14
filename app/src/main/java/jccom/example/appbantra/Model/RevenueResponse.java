package jccom.example.appbantra.Model;

import java.util.ArrayList;
import java.util.List;

public class RevenueResponse {
    private List<Revenue> revenue;

    public RevenueResponse(List<Revenue> revenue) {
        this.revenue = revenue != null ? revenue : new ArrayList<>();
    }

    public List<Revenue> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<Revenue> revenue) {
        this.revenue = revenue;
    }
}
