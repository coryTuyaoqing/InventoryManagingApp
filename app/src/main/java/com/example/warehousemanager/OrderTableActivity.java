package com.example.warehousemanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OrderTableActivity extends AppCompatActivity {

    CardView cardOrderTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table);

        cardOrderTable = findViewById(R.id.cardOrderTable);
    }
}