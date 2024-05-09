package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button buttonOpenData, buttonOpenStaffManager, btnProfile, btnScanner, btnAddArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonOpenData = findViewById(R.id.Search_button);
        buttonOpenStaffManager = findViewById(R.id.OpenStaff_button);
        btnProfile = findViewById(R.id.Profile_button);
        btnScanner = findViewById(R.id.Barcode_scanner_button);
        btnAddArticle = findViewById(R.id.btnAddArticle);

        buttonOpenData.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, DataActivity.class)));

        buttonOpenStaffManager.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, StaffActivity.class)));

        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        btnScanner.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ScannerActivity.class)));

        btnAddArticle.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddArticleActivity.class)));

    }


}
