package com.example.warehousemanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddArticleActivity extends AppCompatActivity {
    private RecyclerView ordersRcyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        //these data are just for test
        //todo: add
        Article t_shirt = new Article(1, "t_shirt", "test", 10, "red", "L");
        Article shoes = new Article(2, "shoes", "test", 10, "black", "42");

        Order order1 = new Order(1, LocalDate.of(2024,5,2), "test", "testname", "testname2", 0);
        order1.putArticle(t_shirt, 3);
        order1.putArticle(shoes, 10);

        Order order2 = new Order(2, LocalDate.of(2023,7,1), "test", "testname", "testname2", 0);
        order2.putArticle(t_shirt, 100);
        order2.putArticle(shoes, 50);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        ordersRcyView = findViewById(R.id.ordersRcyView);

        OrderRecViewAdaptor orderRecViewAdaptor = new OrderRecViewAdaptor(this, orders);
        ordersRcyView.setAdapter(orderRecViewAdaptor);
        ordersRcyView.setLayoutManager(new GridLayoutManager(this, 2));

//        ArticleRecViewAdaptor adaptor = new ArticleRecViewAdaptor(this, order1.getArticlesNrMap());
//        ordersRcyView.setAdapter(adaptor);
//        ordersRcyView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}