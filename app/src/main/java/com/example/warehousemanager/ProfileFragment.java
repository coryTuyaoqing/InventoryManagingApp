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
    Button OpenStaffManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Staff staff = Staff.getStaff(requireActivity().getApplicationContext());

        txtProfileID = view.findViewById(R.id.txtProfileID);
        txtProfilePermission = view.findViewById(R.id.txtProfilePermission);
        txtProfileName = view.findViewById(R.id.txtProfileName);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        OpenStaffManager = view.findViewById(R.id.Open_StaffManager);

        if(staff.getPermission().equals("3")){
            OpenStaffManager.setVisibility(View.VISIBLE);
        }
        else{
            OpenStaffManager.setVisibility(View.GONE);
        }
        txtProfileID.setText("ID: " + staff.getStaffID());
        txtProfileName.setText(staff.getName());
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

        OpenStaffManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staff.deleteFile()){
                    Intent intent = new Intent(requireContext(), StaffActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                }

            }
        });

        return view;
    }
}