package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    TextView txtProfileID, txtProfilePermission, txtProfileName, txtProfileEmail;
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtProfileID = view.findViewById(R.id.txtProfileID);
        txtProfilePermission = view.findViewById(R.id.txtProfilePermission);
        txtProfileName = view.findViewById(R.id.txtProfileName);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        Staff staff = Staff.getStaff(requireActivity().getApplicationContext());
        staff.readFile();
        txtProfileID.setText("ID: " + staff.getStaffID());
        txtProfileName.setText("Name: " + staff.getName());
        txtProfilePermission.setText("Permission: " + staff.getPermission());
        txtProfileEmail.setText(staff.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staff.deleteFile()){
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                }

            }
        });

        return view;
    }
}