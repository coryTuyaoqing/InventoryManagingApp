package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView txtProfileID, txtProfilePermission, txtProfileName, txtProfileEmail;
    Button btnLogout, btnProfileBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtProfileID = findViewById(R.id.txtProfileID);
        txtProfilePermission = findViewById(R.id.txtProfilePermission);
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfileBack = findViewById(R.id.btnProfileBack);

        StaffInfo staffInfo = new StaffInfo(getApplicationContext());
        staffInfo.readFile();
        txtProfileID.setText("ID: " + staffInfo.getStaffID());
        txtProfileName.setText("Name: " + staffInfo.getName());
        txtProfilePermission.setText("Permission: " + staffInfo.getPermission());
        txtProfileEmail.setText(staffInfo.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffInfo.deleteFile();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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