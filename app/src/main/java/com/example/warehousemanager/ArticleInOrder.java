package com.example.warehousemanager;

public class ArticleInOrder extends Article {
    Order myOrder;
    public ArticleInOrder(int idArticle, String name, String supplierName, double price, String color, String size, Order order) {
        super(idArticle, name, supplierName, price, color, size);
        myOrder = order;
    }

    public Order getMyOrder() {
        return myOrder;
    }
}
