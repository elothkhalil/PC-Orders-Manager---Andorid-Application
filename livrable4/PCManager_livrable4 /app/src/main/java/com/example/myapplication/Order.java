package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Order {
    private String orderId;
    private String requesterId;
    private String componentId;
    private long createdAt;
    private long modifiedAt;
    private String status;

    // Constructeur par défaut requis pour Firebase
    public Order() {}

    // Constructeur avec tous les paramètres
    public Order(String orderId, String requesterId, String componentId, long createdAt, long modifiedAt, String status) {
        this.orderId = orderId;
        this.requesterId = requesterId;
        this.componentId = componentId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.status = status;
    }

    // Getters et Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Méthode pour obtenir la date de création formatée
    public String getFormattedCreatedAt() {
        return formatDate(createdAt);
    }

    // Méthode pour obtenir la date de modification formatée
    public String getFormattedModifiedAt() {
        return formatDate(modifiedAt);
    }

    // Méthode de formatage des dates en texte lisible
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
