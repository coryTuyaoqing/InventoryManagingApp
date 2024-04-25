package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtTxtNameOrId = findViewById(R.id.login_name);
        EditText edtTxtPassword = findViewById(R.id.login_password);
        TextView txtSignUp = findViewById(R.id.sign_up_first);
        Button btnSignIn = findViewById(R.id.login_button);
        OkHttpClient client = new OkHttpClient();
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            //Todo: when sign up first is clicked, jump to registration page.
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "sign up is clicked", Toast.LENGTH_LONG).show();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtTxtNameOrId.getText().toString();
                String password = edtTxtPassword.getText().toString();

                //set up http request
                Request request = new Request.Builder()
                        .url(DBConst.DB_URL + "get_staff_password/" + id)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        System.out.println("fail to get password");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            String responseData = response.body().string();
                            System.out.println("success to get password: " + responseData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "success to get password: " + responseData, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            System.out.println("fail to get password: " + response.code());
                        }

                    }
                });
            }
        });
    }
}