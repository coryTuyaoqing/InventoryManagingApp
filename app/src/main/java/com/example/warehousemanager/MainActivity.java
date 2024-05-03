package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button buttonOpenData, buttonOpenStaffManager, btnProfile, btnScanner, btnAddArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOpenData = findViewById(R.id.Search_button);
        buttonOpenStaffManager = findViewById(R.id.OpenStaff_button);
        btnProfile = findViewById(R.id.Profile_button);
        btnScanner = findViewById(R.id.Barcode_scanner_button);
        btnAddArticle = findViewById(R.id.btnAddArticle);

        buttonOpenData.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DataActivity.class)));

        buttonOpenStaffManager.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StaffActivity.class)));

        btnProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        btnScanner.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScannerActivity.class)));

        btnAddArticle.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddArticleActivity.class)));
    }


}
