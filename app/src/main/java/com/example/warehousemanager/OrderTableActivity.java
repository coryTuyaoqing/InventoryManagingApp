package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderTableActivity extends AppCompatActivity {
    RecyclerView recyclerOrderTable;
    private static final String TAG = "OrderTableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table);
        Intent intent = getIntent();
        int orderID = intent.getIntExtra("orderID", -1);

        recyclerOrderTable = findViewById(R.id.recyclerOrderTable);

        OrderRecViewAdaptor orderRecViewAdaptor = new OrderRecViewAdaptor(this);
        String url = DB.DB_URL + "get_order_fromID/" + orderID;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrderTable.setAdapter(orderRecViewAdaptor);
        recyclerOrderTable.setLayoutManager(new LinearLayoutManager(this));

    }
}