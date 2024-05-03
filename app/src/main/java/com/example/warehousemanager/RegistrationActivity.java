package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehousemanager.model.DBConst;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    TextView txtSignupToSignin;
    Button btnSignup;
    EditText edtTxtSignupName, edtTxtSignupPassword, edtTxtSignupPasswordConfirmation;
    OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();

        txtSignupToSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign up operation
                String newStaffName = edtTxtSignupName.getText().toString();
                String newPassword = edtTxtSignupPassword.getText().toString();
                String repeatPassword = edtTxtSignupPasswordConfirmation.getText().toString();
                String companyEmail = newStaffName.toLowerCase() + "@warehouse.com";
                String url = DBConst.DB_URL + "registration/" + newStaffName + "/" + newPassword + "/" + companyEmail;

                if(newStaffName.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Name and password shouldn't be empty.", Toast.LENGTH_LONG).show();
                }
                else if(!newPassword.equals(repeatPassword)){
                    Toast.makeText(RegistrationActivity.this, "Repeat password doesn't match.", Toast.LENGTH_LONG).show();
                }
                else if(newPassword.length() < 8){
                    Toast.makeText(RegistrationActivity.this, "Password shouldn't less than 8 char.", Toast.LENGTH_LONG).show();
                }
                else{
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e(TAG, "onFailure: registration http connection fail", e);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.i(TAG, "onResponse: registration http connected");
                            try{
                                assert response.body() != null;
                                JSONArray responseArray = new JSONArray(response.body().string());
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            }
                            catch (JSONException e){
                                Log.e(TAG, "onResponse: fail to parse response message", e);
                                runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "Invalid user name.", Toast.LENGTH_LONG).show());
                            }
                        }
                    });
                }


            }
        });
    }

    private void initView() {
        txtSignupToSignin = findViewById(R.id.txtSignupToSignin);
        btnSignup = findViewById(R.id.btnSignup);
        edtTxtSignupName = findViewById(R.id.edtTxtSignupName);
        edtTxtSignupPassword = findViewById(R.id.edtTxtSignupPassword);
        edtTxtSignupPasswordConfirmation = findViewById(R.id.edtTxtSignupPasswordConfirmation);
        client = new OkHttpClient();
    }
}