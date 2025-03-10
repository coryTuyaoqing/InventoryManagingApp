package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanResultActivity extends AppCompatActivity implements AbleToAddArticle{
    String url;
    String barcodeNr;
    Button btnBackScanResult;
    RecyclerView recyclerArticleScanResult, recyclerOrdersScanRelative;
    ArticleRecViewAdaptor articleRecViewAdaptor;
    OrderRecViewAdaptor orderRecViewAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        if(intent != null){
            barcodeNr = intent.getStringExtra("barcodeNr");
        }

        btnBackScanResult = findViewById(R.id.btnBackScanResult);
        btnBackScanResult.setOnClickListener(v -> finish());

        recyclerOrdersScanRelative = findViewById(R.id.recyclerOrdersScanRelative);
        recyclerArticleScanResult = findViewById(R.id.recyclerArticleScanResult);

        articleRecViewAdaptor = new ArticleRecViewAdaptor(this);
        url = DB.DB_URL + "get_article_fromID/" + barcodeNr;
        articleRecViewAdaptor.getArticlesFromDB(url);
        recyclerArticleScanResult.setAdapter(articleRecViewAdaptor);
        recyclerArticleScanResult.setLayoutManager(new LinearLayoutManager(this));

        //customize order recycler view with customize order detail dialog fragment
        orderRecViewAdaptor = new OrderRecViewAdaptor(this);
        url = DB.DB_URL + "get_order_from_article/" + barcodeNr;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrdersScanRelative.setAdapter(orderRecViewAdaptor);
        recyclerOrdersScanRelative.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public Article getArticle() {
        return articleRecViewAdaptor.getArticles().get(0);
    }

    @Override
    public void notifyDataSetChanged(){
        orderRecViewAdaptor.getOrdersFromDB(DB.DB_URL + "get_order_from_article/" + barcodeNr);
        articleRecViewAdaptor.getArticlesFromDB(DB.DB_URL + "get_article_fromID/" + barcodeNr);
    }
}