package com.example.warehousemanager;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TableRow;
import android.widget.TextView;
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
    private int rowCount = 1;
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
    }

    public void populateTable(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String staffName = jsonObject.getString("name");
                String staffPermission = jsonObject.getString("permission");

                // Create a new TableRow
                TableRow row = new TableRow(this);

                // Create TextViews for name and permission
                TextView nameTextView = new TextView(this);
                nameTextView.setText(staffName);
                TextView permissionTextView = new TextView(this);
                permissionTextView.setText(staffPermission);

                // Add TextViews to the TableRow
                row.addView(nameTextView);
                row.addView(permissionTextView);

                // Add the TableRow to the TableLayout
                tableLayout.addView(row);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
