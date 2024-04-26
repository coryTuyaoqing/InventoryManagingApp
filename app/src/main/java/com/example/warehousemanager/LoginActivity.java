package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginDatabase";

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
                String userPassword = edtTxtPassword.getText().toString();
                String url = DBConst.DB_URL + "get_one_staff/" + id;

                //set up http request
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, "onFailure: get_one_staff", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d(TAG, "onResponse: get_one_staff");
                        try{
                            assert response.body() != null;
                            JSONArray responseArray = new JSONArray(response.body().string());
                            JSONObject responseObject = responseArray.getJSONObject(0);
                            String realPassword = responseObject.getString("password");
                            String name = responseObject.getString("name");
                            String permission = responseObject.getString("permission");

                            if(userPassword.equals(realPassword)){
                                //if the password is correct: jump to main page and save user data
                                StaffInfo newStaff = new StaffInfo(getApplicationContext());
                                newStaff.initInfo();
                                newStaff.writeInfo(id, name, permission);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e(TAG, "onResponse: JSONArray", e);
                        }
                    }
                });
            }
        });
    }
}