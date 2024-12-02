package com.example.myapplication;

public class Component {
    private String id;
    private String name;
    private int quantity;

    // Default constructor required for calls to DataSnapshot.getValue(Component.class)
    public Component() {
    }

    public Component(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
