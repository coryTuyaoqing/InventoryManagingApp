package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check if the user already login
        StaffInfo staffInfo = new StaffInfo(getApplicationContext());
        if (staffInfo.fileExist()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        EditText edtTxtNameOrId = findViewById(R.id.login_name);
        EditText edtTxtPassword = findViewById(R.id.login_password);
        TextView txtSignUp = findViewById(R.id.sign_up_first);
        Button btnSignIn = findViewById(R.id.login_button);
        OkHttpClient client = new OkHttpClient();

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        /*
        **btnSignIn.setOnClickListener()**
        A toast warning will be given if one of the following condition doesn't satisfied.
        1. ID, email, password shouldn't be empty
        2. ID should already exist
        3. Password should be correct
        4. Internet connection should work
         */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idOrEmail = edtTxtNameOrId.getText().toString();
                String userPassword = edtTxtPassword.getText().toString();
                if(idOrEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "ID, email or password shouldn't be empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                String url;
                //check if the user use email or id to login
                if(idOrEmail.chars().allMatch(Character::isDigit)){
                    url = DBConst.DB_URL + "get_one_staff_id/" + idOrEmail;
                }
                else{
                    url = DBConst.DB_URL + "get_one_staff_email/" + idOrEmail;
                }


                //set up http request
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Internet error.", Toast.LENGTH_LONG).show());
                        Log.e(TAG, "onFailure: get_one_staff", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d(TAG, "onResponse: get_one_staff");
                        try {
                            assert response.body() != null;
                            JSONArray responseArray = new JSONArray(response.body().string());
                            if(responseArray.length() == 0){
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid ID.", Toast.LENGTH_LONG).show());
                                return;
                            }
                            JSONObject responseObject = responseArray.getJSONObject(0);
                            String realPassword = responseObject.getString("password");
                            String name = responseObject.getString("name");
                            String permission = responseObject.getString("permission");
                            String email = responseObject.getString("email");
                            String id = responseObject.getString("idStaff");

                            if (userPassword.equals(realPassword)) {
                                //if the password is correct: jump to main page and save user data
                                StaffInfo newStaff = new StaffInfo(getApplicationContext());
                                newStaff.initInfo();
                                newStaff.writeInfo(id, name, permission, email);

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show());
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse: JSONArray", e);
                        }
                    }
                });
            }
        });
    }
}