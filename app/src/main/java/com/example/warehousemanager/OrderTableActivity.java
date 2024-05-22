package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderTableActivity extends AppCompatActivity implements AbleToAddArticle{
    Article article;
    RecyclerView recyclerOrderTable;
    FloatingActionButton fabOrderTable;
    OrderRecViewAdaptor orderRecViewAdaptor;
    ProgressBar progressBarOrderTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table);
        Intent intent = getIntent();
        int orderID = intent.getIntExtra("orderID", -1);

        recyclerOrderTable = findViewById(R.id.recyclerOrderTable);
        fabOrderTable = findViewById(R.id.fabOrderTable);
        progressBarOrderTable = findViewById(R.id.progressBarOrderTable);

        orderRecViewAdaptor = new OrderRecViewAdaptor(this);
        String url = DB.DB_URL + "get_order_fromID/" + orderID;
        orderRecViewAdaptor.getOrdersFromDB(url);
        recyclerOrderTable.setAdapter(orderRecViewAdaptor);
        recyclerOrderTable.setLayoutManager(new LinearLayoutManager(this));

        fabOrderTable.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setCaptureActivity(BarcodeActivity.class);
            options.setPrompt("Scan something");
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    private ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            new ActivityResultCallback<ScanIntentResult>() {
                @Override
                public void onActivityResult(ScanIntentResult o) {
                    if (o.getContents() == null) {
                        Toast.makeText(OrderTableActivity.this, "cancelled.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OrderTableActivity.this, "scanned: " + o.getContents(), Toast.LENGTH_SHORT).show();
                        Order order = orderRecViewAdaptor.getOrders().get(0);
                        getArticleFromID(o.getContents());
                        if(article == null){
                            return;
                        }
                        OrderDetailsDialogFragment orderDetailsDialogFragment = new OrderDetailsDialogFragment(order, OrderTableActivity.this);
                        orderDetailsDialogFragment.show(getSupportFragmentManager(), "tag");
                    }
                }
            });

    @Override
    public Article getArticle() {
        return article;
    }

    public void getArticleFromID(String articleID){
        Order order = orderRecViewAdaptor.getOrders().get(0);
        ArrayList<Article> articles = new ArrayList<>(order.getArticlesNrMap().keySet());
        for(Article a: articles){
            if(articleID.equals(String.valueOf(a.getIdArticle()))){
                article = a;
                return;
            }
        }
        Toast.makeText(this, "No article found", Toast.LENGTH_SHORT).show();
        article = null;
    }
}