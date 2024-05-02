package com.example.warehousemanager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScannerActivity extends AppCompatActivity {
    Button btnScanner_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        btnScanner_scan = findViewById(R.id.btnScanner_scan);

        btnScanner_scan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setCaptureActivity(BarcodeActivity.class);
            options.setPrompt("Scan something");
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            new ActivityResultCallback<ScanIntentResult>() {
                @Override
                public void onActivityResult(ScanIntentResult result) {
                    if (result.getContents() == null) {
                        Toast.makeText(ScannerActivity.this, "cancelled.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ScannerActivity.this, "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    }
                }
            });
}