package com.example.warehousemanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddArticleActivity extends AppCompatActivity {
    private RecyclerView ordersRcyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(1,1));
        orders.add(new Order(2,1));
        orders.add(new Order(2,2));

        ordersRcyView = findViewById(R.id.ordersRcyView);

        OrderRecViewAdaptor orderRecViewAdaptor = new OrderRecViewAdaptor(this, orders);
        ordersRcyView.setAdapter(orderRecViewAdaptor);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        ordersRcyView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}