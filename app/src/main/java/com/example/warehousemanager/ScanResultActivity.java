package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanResultActivity extends AppCompatActivity {
    String url;
    String barcodeNr;
    RecyclerView recyclerArticleScanResult, recyclerOrdersScanRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        if(intent != null){
            barcodeNr = intent.getStringExtra("barcodeNr");
        }

        recyclerOrdersScanRelative = findViewById(R.id.recyclerOrdersScanRelative);
        recyclerArticleScanResult = findViewById(R.id.recyclerArticleScanResult);

        ArticleRecViewAdaptor articleRecViewAdaptor = new ArticleRecViewAdaptor(this);
        url = DBConst.DB_URL + "get_article_fromID/" + barcodeNr;
        articleRecViewAdaptor.getArticlesFromDB(url);
        recyclerArticleScanResult.setAdapter(articleRecViewAdaptor);
        recyclerArticleScanResult.setLayoutManager(new LinearLayoutManager(this));

        OrderRecViewAdaptor orderRecViewAdaptor = new OrderRecViewAdaptor(this,
                order -> Toast.makeText(ScanResultActivity.this, "add article to orders", Toast.LENGTH_SHORT).show());
        url = DBConst.DB_URL + "get_order_from_article/" + barcodeNr;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrdersScanRelative.setAdapter(orderRecViewAdaptor);
        recyclerOrdersScanRelative.setLayoutManager(new LinearLayoutManager(this));
    }
}