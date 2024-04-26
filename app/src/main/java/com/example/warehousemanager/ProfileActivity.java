package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView profileTest = findViewById(R.id.profile_test);
        StaffInfo staffInfo = new StaffInfo(getApplicationContext());
        staffInfo.readFile();
        String info = "id: " + staffInfo.getStaffID() +", name: " + staffInfo.getName() + ", permission: " + staffInfo.getPermission();
        profileTest.setText(info);
    }
}