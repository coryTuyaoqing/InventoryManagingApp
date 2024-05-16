package com.example.warehousemanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Order {
    private int orderID;
    private String description;
    private LocalDate deadline;
    private String customer;
    private String responsible;
    private int highlightedOrder;
    private Map<Article,ArticleNr> articlesNrMap;
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

    public void getArticlesOfOrders(GetArticlesCallback callback) throws JSONException{
        OkHttpClient client = new OkHttpClient();
        String url = DBConst.DB_URL + "get_articles_of_order/";
        Request request = new Request.Builder()
                .url(url + getOrderID())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                JSONArray responseArray;
                try {
                    assert response.body() != null;
                    responseArray = new JSONArray(response.body().string());
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        int articleID = responseObject.getInt("idArticle");
                        String name = responseObject.getString("name");
                        String supplierName = responseObject.getString("SupplierName");
                        String color = responseObject.getString("color");
                        String size = responseObject.getString("size");
                        int articleRequired = responseObject.getInt("articleRequired");
                        int articlesInStock = responseObject.getInt("articlesInStock");
                        double price;
                        try{price = responseObject.getDouble("Price");} catch (JSONException e){price = -1;}
                        putArticle(new ArticleInOrder(articleID, name, supplierName, price, color, size, Order.this),
                                new Order.ArticleNr(articleRequired, articlesInStock));
                    }
                    callback.AfterGetArticles(Order.this);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: parsing", e);
                }
            }
        });
    }

    public boolean isComplete(Article article){
        ArticleNr nr = getArticlesNrMap().get(article);
        assert nr != null;
        int inStockNr = nr.getInStockNr();
        int requiredNr = nr.getRequiredNr();
        return inStockNr == requiredNr;
    }

    public static class ArticleNr{
        private int requiredNr;
        private int inStockNr;

        public ArticleNr(int requiredNr, int finishedNr) {
            this.requiredNr = requiredNr;
            this.inStockNr = finishedNr;
        }

        public int getRequiredNr() {
            return requiredNr;
        }

        public int getInStockNr() {
            return inStockNr;
        }

        public void setInStockNr(int inStockNr) {
            this.inStockNr = inStockNr;
        }
    }
    public interface GetArticlesCallback{
        void AfterGetArticles(Order order);
    }
}
