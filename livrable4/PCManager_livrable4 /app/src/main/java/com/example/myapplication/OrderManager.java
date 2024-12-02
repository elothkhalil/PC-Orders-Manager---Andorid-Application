package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class OrderManager {

    private final DatabaseReference database;

    public OrderManager(DatabaseReference database) {
        this.database = database;
    }

    public void addOrder(Order order) {
        if (order != null) {
            database.push().setValue(order);
        }
    }

    public void getOrderById(String orderId, ValueEventListener listener) {
        if (orderId != null) {
            database.child(orderId).addListenerForSingleValueEvent(listener);
        }
    }

    public void deleteOrder(String orderId) {
        if (orderId != null) {
            database.child(orderId).removeValue();
        }
    }

    public void updateOrderStatus(String orderId, String status) {
        if (orderId != null) {
            database.child(orderId).child("status").setValue(status);
        }
    }
}
