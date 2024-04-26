package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StaffActivity extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffmanagement);
        OkHttpClient client = new OkHttpClient();
        tableLayout = findViewById(R.id.StaffTable);
        String url = "https://studev.groept.be/api/a23PT308/get_info_Staffs";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("fail to get staff data");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println("success to get staff data: " + responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StaffActivity.this, "success to get staff data: " + responseData, Toast.LENGTH_LONG).show();
                        }
                    });
                    populateTable(responseData);
                }
                else {
                    System.out.println("fail to get staff data: " + response.code());
                }
            }
        });



        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < tableLayout.getChildCount(); i++)
                {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);

                    TextView nameTextView = (TextView) row.getChildAt(0);
                    Spinner permissionSpinner = (Spinner) row.getChildAt(1);

                    String staffName = nameTextView.getText().toString();
                    String selectedPermission = (String) permissionSpinner.getSelectedItem();
                    int permissionIndex = permissionSpinner.getSelectedItemPosition() + 1;

                    //implement code to send staff to Database
                }

                Toast.makeText(StaffActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
            }
        });


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void populateTable(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int ID = jsonObject.getInt("idStaff");
                String staffName = jsonObject.getString("name");
                int staffPermission = jsonObject.getInt("permission");

                TableRow row = new TableRow(this);

                TextView idTextView = new TextView(this);
                TextView nameTextView = new TextView(this);

                idTextView.setText(String.valueOf(ID));
                nameTextView.setText(staffName);

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                idTextView.setLayoutParams(params);
                nameTextView.setLayoutParams(params);

                Spinner permissionSpinner = new Spinner(this);
                ArrayAdapter<String> permissionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Permission 1", "Permission 2", "Permission 3"});
                permissionSpinner.setAdapter(permissionAdapter);
                permissionSpinner.setSelection(staffPermission - 1); // Adjust index to match permission values

                row.addView(idTextView);
                row.addView(nameTextView);
                row.addView(permissionSpinner);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tableLayout.addView(row);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
