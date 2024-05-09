package com.example.warehousemanager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.Map;

public class ScannerActivity extends AppCompatActivity {
    Button btnScanner_scan;
    RecyclerView recyclerArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        btnScanner_scan = findViewById(R.id.btnScanner_scan);
        recyclerArticles = findViewById(R.id.recyclerArticles);

        Article t_shirt = new Article(1, "t_shirt", "red", "L");
        Article shoes = new Article(2, "shoes", "black", "42");

        Map<Article, Integer> articleNrMap = new HashMap<>();
        articleNrMap.put(t_shirt, 30);
        articleNrMap.put(shoes, 40);

        ArticleRecViewAdaptor adaptor = new ArticleRecViewAdaptor(this, articleNrMap);
        recyclerArticles.setAdapter(adaptor);
        recyclerArticles.setLayoutManager(new GridLayoutManager(this, 2));

        btnScanner_scan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setCaptureActivity(BarcodeActivity.class);
            options.setPrompt("Scan something");
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(ScannerActivity.this, "cancelled.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ScannerActivity.this, "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });
}