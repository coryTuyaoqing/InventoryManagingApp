package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button btnWorkSpace;
    BottomNavigationView navigationView;
    FloatingActionButton btnScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation_view);
        replaceFragment(new HomeFragment());
        navigationView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.bottomHome) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.bottomScanInfo) {
                replaceFragment(new InfoFragment());
                return true;
            } else if (itemId == R.id.bottomSearch) {
                replaceFragment(new SearchFragment());
                return true;
            } else if (itemId == R.id.bottomProfile) {
                replaceFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        btnScan = findViewById(R.id.fab_barcode_scan);
        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setCaptureActivity(BarcodeActivity.class);
            options.setPrompt("Scan something");
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });

        btnWorkSpace = findViewById(R.id.btnWorkSpace);
        btnWorkSpace.setOnClickListener(v -> {
            if(OrderTableActivity.orderID == -1){
                Toast.makeText(MainActivity.this, "No order in workspace yet", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, OrderTableActivity.class);
            intent.putExtra("orderID", OrderTableActivity.orderID);
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, fragment)
                .commit();
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "cancelled.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, ScanResultActivity.class);
                    intent.putExtra("barcodeNr", result.getContents());
                    startActivity(intent);
                }
            });
}