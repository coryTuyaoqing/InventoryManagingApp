package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanResultActivity extends AppCompatActivity {
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
        url = DBConst.DB_URL + "get_article_fromID/" + barcodeNr;
        articleRecViewAdaptor.getArticlesFromDB(url);
        recyclerArticleScanResult.setAdapter(articleRecViewAdaptor);
        recyclerArticleScanResult.setLayoutManager(new LinearLayoutManager(this));

        //customize order recycler view with customize order detail dialog fragment
        orderRecViewAdaptor = new OrderRecViewAdaptor(this, order -> {
            OrderDetailsDialogFragment orderDetailsDialogFragment = new OrderDetailsDialogFragment(order);
            orderDetailsDialogFragment.setAddView(layout -> { //add bottom to the dialog window for adding article
                Button btnAddArticleToOrder = new Button(layout.getContext());
                btnAddArticleToOrder.setText("Add article to\nthis order");
                btnAddArticleToOrder.setOnClickListener(v -> {
                    Article article = articleRecViewAdaptor.getArticles().get(0);
                    AddArticleDialogFragment addArticleDialogFragment = new AddArticleDialogFragment(order, article);
                    addArticleDialogFragment.show(getSupportFragmentManager(), "add article to order");
                    orderDetailsDialogFragment.dismiss();
                });
                layout.addView(btnAddArticleToOrder);
            });
            orderDetailsDialogFragment.show(getSupportFragmentManager(), "OrderDetailsDialog");
        });
        url = DBConst.DB_URL + "get_order_from_article/" + barcodeNr;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrdersScanRelative.setAdapter(orderRecViewAdaptor);
        recyclerOrdersScanRelative.setLayoutManager(new LinearLayoutManager(this));
    }
}