package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;

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
                String nameOrId = edtTxtNameOrId.getText().toString();
                String password = edtTxtPassword.getText().toString();
                int id;
                String name;

                id = Integer.parseInt(nameOrId);

                //set up http request
                Request request = new Request.Builder()
                        .url(DBConst.DB_URL + "get_staff_password/")
                        .build();
            }
        });
    }
}