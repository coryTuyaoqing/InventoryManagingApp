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
    private Map<Article,ArticleNr> articlesNrMap;

    public Order(int orderID, LocalDate deadline, String description, String customer, String responsible, int highlightedOrder) {
        this.orderID = orderID;
        this.description = description;
        this.deadline = deadline;
        this.customer = customer;
        this.responsible = responsible;
        this.highlightedOrder = highlightedOrder;
        this.articlesNrMap = new HashMap<>();
    }

    public Order(int orderID, String description, LocalDate deadline, Map<Article, ArticleNr> articlesNrMap) {
        this.orderID = orderID;
        this.description = description;
        this.deadline = deadline;
        this.articlesNrMap = articlesNrMap;
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

    public Map<Article, ArticleNr> getArticlesNrMap() {
        return articlesNrMap;
    }

    public void putArticle(Article article, ArticleNr articleNr){
        articlesNrMap.put(article, articleNr);
    }

    public void putArticle(Article article, int requiredNr, int finishedNr){
        articlesNrMap.put(article, new ArticleNr(requiredNr, finishedNr));
    }

    public static class ArticleNr{
        private int requiredNr;
        private int finishedNr;

        public ArticleNr(int requiredNr, int finishedNr) {
            this.requiredNr = requiredNr;
            this.finishedNr = finishedNr;
        }

        public int getRequiredNr() {
            return requiredNr;
        }

        public int getFinishedNr() {
            return finishedNr;
        }

        public void setFinishedNr(int finishedNr) {
            this.finishedNr = finishedNr;
        }
    }
}
