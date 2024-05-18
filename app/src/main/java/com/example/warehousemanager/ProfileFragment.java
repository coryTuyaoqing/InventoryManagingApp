package com.example.warehousemanager;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    private final int PICK_IMAGE = 40;
    private TextView txtProfileID, txtProfilePermission, txtProfileName, txtProfileEmail;
    private Button btnLogout, OpenStaffManager;
    private ImageView imgProfileAvatar;

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
        imgProfileAvatar = view.findViewById(R.id.imgProfileAvatar);

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
                Intent intent = new Intent(requireContext(), StaffActivity.class);
                startActivity(intent);
            }
        });

        Bitmap avatar = staff.getAvatarBitmap();
        if(avatar != null){
            imgProfileAvatar.setImageBitmap(avatar);
        }
        imgProfileAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                imgProfileAvatar.setImageBitmap(bitmap);
                String base64String = encodeToBase64(bitmap);
                Toast.makeText(getContext(), "get base64 string", Toast.LENGTH_SHORT).show();
                setAvatarOnDB(base64String);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAvatarOnDB(String avatar) {
        Staff staff = Staff.getStaff(requireActivity().getApplicationContext());
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("avatar", avatar)
                .add("id", staff.getStaffID())
                .build();
        Request request = new Request.Builder()
                .url(DB.DB_URL + "set_avatar")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Toast.makeText(getActivity(), "avatar upload success", Toast.LENGTH_SHORT).show();
                staff.refreshInfoFromDB();
            }
        });
    }

    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}