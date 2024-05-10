package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Article {
    private int idArticle;
    private String name;
    private String supplierName;
    private double price;
    private String color;
    private String size;

    public Article(int idArticle, String name, String supplierName, double price, String color, String size) {
        this.idArticle = idArticle;
        this.name = name;
        this.supplierName = supplierName;
        this.price = price;
        this.color = color;
        this.size = size;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public String getArticleName() {
        return name;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public double getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        return getIdArticle();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == this)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        return ((Article)obj).getIdArticle() == getIdArticle();
    }

    @NonNull
    @Override
    public String toString() {
        return "Article{" +
                "idArticle=" + idArticle +
                ", name='" + name + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
