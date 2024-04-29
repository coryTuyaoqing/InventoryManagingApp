package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView profileTest = findViewById(R.id.txtProfile);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnProfileBack = findViewById(R.id.btnProfileBack);

        StaffInfo staffInfo = new StaffInfo(getApplicationContext());
        staffInfo.readFile();
        String info = "id: " + staffInfo.getStaffID() +", name: " + staffInfo.getName() + ", permission: " + staffInfo.getPermission();
        profileTest.setText(info);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffInfo.deleteFile();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}