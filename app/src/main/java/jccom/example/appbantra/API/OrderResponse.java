package jccom.example.appbantra.API;

import java.util.List;

import jccom.example.appbantra.Model.Order;

public class OrderResponse {


        private List<Order> orders;

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
    }


