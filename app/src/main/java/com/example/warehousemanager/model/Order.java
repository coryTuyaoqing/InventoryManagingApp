package com.example.warehousemanager.model;

public class Order {
    private int orderID;
    private int articleID;

    public Order(int orderID, int articleID) {
        this.orderID = orderID;
        this.articleID = articleID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getArticleID() {
        return articleID;
    }
}
