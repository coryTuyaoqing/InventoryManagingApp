package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanResultActivity extends AppCompatActivity {
    String url;
    String barcodeNr;
    Button btnBackScanResult;
    Article article;
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
        orderRecViewAdaptor = new OrderRecViewAdaptor(this, orderOnClickCallBack);
        url = DB.DB_URL + "get_order_from_article/" + barcodeNr;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrdersScanRelative.setAdapter(orderRecViewAdaptor);
        recyclerOrdersScanRelative.setLayoutManager(new LinearLayoutManager(this));
    }

    private final OrderRecViewAdaptor.CallBack orderOnClickCallBack = new OrderRecViewAdaptor.CallBack() {
        @Override
        public void OrderOnClick(Order order) {
            //check if order is complete
            article = articleRecViewAdaptor.getArticles().get(0);
            if(order.isComplete(article)){
                Toast.makeText(ScanResultActivity.this, "The order is complete", Toast.LENGTH_SHORT).show();
            }

            OrderDetailsDialogFragment orderDetailsDialogFragment = getOrderDetailsDialogFragment(order, article);
            orderDetailsDialogFragment.show(getSupportFragmentManager(), "OrderDetailsDialog");
        }
    };

    private OrderDetailsDialogFragment getOrderDetailsDialogFragment(Order order, Article article) {
        OrderDetailsDialogFragment orderDetailsDialogFragment = new OrderDetailsDialogFragment(order, this);
        orderDetailsDialogFragment.setAddView(layout -> { //add bottom to the dialog window for adding article
            if(order.isComplete(article))
                return;

            Button btnAddArticleToOrder = new Button(layout.getContext());
            btnAddArticleToOrder.setText("Add article to\nthis order");
            btnAddArticleToOrder.setOnClickListener(v -> {
                AddArticleDialogFragment addArticleDialogFragment = new AddArticleDialogFragment(order, article);
                addArticleDialogFragment.show(getSupportFragmentManager(), "add article to order");
                orderDetailsDialogFragment.dismiss();
            });
            layout.addView(btnAddArticleToOrder);
        });
        return orderDetailsDialogFragment;
    }
}