package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.warehousemanager.Model.Staff;

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

        Staff staff = Staff.getStaff(getApplicationContext());
        staff.readFile();
        txtProfileID.setText("ID: " + staff.getStaffID());
        txtProfileName.setText("Name: " + staff.getName());
        txtProfilePermission.setText("Permission: " + staff.getPermission());
        txtProfileEmail.setText(staff.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staff.deleteFile()){
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

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