package com.example.warehousemanager;

import androidx.annotation.Nullable;

public class Article {
    private int ID;
    private String articleName;
    private String color;
    private String size;

    public Article(int ID, String articleName, String color, String size) {
        this.ID = ID;
        this.articleName = articleName;
        this.color = color;
        this.size = size;
    }

    public int getID() {
        return ID;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        return getID();
    }

    @Override
    public boolean equals(@Nullable Object obj) { //two article is equal if they have the same ID
        if(obj == this)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        return ((Article)obj).getID() == getID();
    }

}
