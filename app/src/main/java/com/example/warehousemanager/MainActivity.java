package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button buttonOpenData;
    Button buttonOpenStaffManager;
    Button btnProfile;
    Button btnScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOpenData = findViewById(R.id.Search_button);
        buttonOpenStaffManager = findViewById(R.id.OpenStaff_button);
        btnProfile = findViewById(R.id.Profile_button);
        btnScanner = findViewById(R.id.Barcode_scanner_button);

        buttonOpenData.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataActivity.class)));

        buttonOpenStaffManager.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StaffActivity.class)));

        btnProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        btnScanner.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScannerActivity.class)));
    }


}
