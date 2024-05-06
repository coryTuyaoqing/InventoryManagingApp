package com.example.warehousemanager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderID;
    private String description;
    private LocalDate deadline;

    private Map<Article,Integer> articlesNrMap;

    public Order(int orderID, LocalDate deadline, String description) {
        this.orderID = orderID;
        this.description = description;
        this.deadline = deadline;
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

    public Map<Article, Integer> getArticlesNrMap() {
        return articlesNrMap;
    }

    public Integer putArticle(Article article, Integer number){
        return articlesNrMap.put(article, number);
    }
}
