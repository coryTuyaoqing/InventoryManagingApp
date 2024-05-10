package com.example.warehousemanager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderID;
    private String description;
    private LocalDate deadline;
    private String customer;
    private String responsible;
    private int highlightedOrder;

    private Map<Article,Integer> articlesNrMap;
    private static final String TAG = "Order";

    public Order(int orderID, LocalDate deadline, String description, String customer, String responsible, int highlightedOrder) {
        this.orderID = orderID;
        this.description = description;
        this.deadline = deadline;
        this.customer = customer;
        this.responsible = responsible;
        this.highlightedOrder = highlightedOrder;
        this.articlesNrMap = new HashMap<>();
    }

    public int getOrderID() {
        return orderID;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public boolean getHighlightedOrder() {
        if(highlightedOrder == 1){
            return true;
        }
        else{
            return false;
        }
    }
    public String getResponsible() {
        return responsible;
    }

    public String getCustomer(){
        return customer;
    }

    public Map<Article, Integer> getArticlesNrMap() {
        return articlesNrMap;
    }

    public Integer putArticle(Article article, Integer number){
        return articlesNrMap.put(article, number);
    }
}
