package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;

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

public class DataActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private Spinner filterSpinner;
    private EditText filterInputEditText;
    private Button searchButton;
    private ScrollView scrollView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Button btnDataReturn = findViewById(R.id.btnDataReturn);

        btnDataReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        typeSpinner = findViewById(R.id.Type);
        filterSpinner = findViewById(R.id.Filter);
        filterInputEditText = findViewById(R.id.filterInput);
        searchButton = findViewById(R.id.Search_Button);
        scrollView = findViewById(R.id.data_display);

        client = new OkHttpClient();

        setupSpinner(typeSpinner, R.array.types_array);

        setupSpinner(filterSpinner, R.array.filter_array);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                String filter = filterSpinner.getSelectedItem().toString();
                String keyword = filterInputEditText.getText().toString();

                if (keyword.isEmpty()) {
                    showToast("Please enter a keyword for the search.");
                    return;
                }

                performSearch(type, filter, keyword);
            }
        });
    }

    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(arrayResource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void performSearch(String type, String filter, String keyword) {
        String baseUrl = "https://studev.groept.be/api/a23PT308/";
        String endpoint;

        if (type.equals("Order")) {
            switch (filter.toLowerCase()) {
                case "id":
                    endpoint = "get_order_fromID/" + keyword;
                    break;
                case "most articles":
                    endpoint = "get_order_mostArticles";
                    break;
                default:
                    return;
            }
        } else {
            switch (filter.toLowerCase()) {
                case "size":
                    endpoint = "get_article_fromSize/" + keyword;
                    break;
                case "color":
                    endpoint = "get_article_fromColor/" + keyword;
                    break;
                case "name":
                    endpoint = "get_article_fromName/" + keyword;
                    break;
                case "id":
                    endpoint = "get_article_fromID/" + keyword;
                    break;
                default:
                    return;
            }
        }

        String url = baseUrl + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                showToast("Failed to fetch data");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateScrollView(jsonArray, type);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Failed to parse data");
                    }
                } else {
                    showToast("Failed to fetch data");
                }
            }
        });
    }

    private void populateScrollView(JSONArray jsonArray, String dataType) {
        scrollView.removeAllViews();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (dataType.equals("Article")) {
                    // Extract values for articles
                    int idArticle = jsonObject.getInt("idArticle");
                    String name = jsonObject.getString("name");
                    String color = jsonObject.getString("color");
                    String size = jsonObject.getString("size");

                    TextView textView = new TextView(this);
                    // Set the text to display the article details
                    textView.setText("ID: " + idArticle + "\nName: " + name + "\nColor: " + color + "\nSize: " + size);

                    textView.setPadding(0, 0, 0, 20);

                    scrollView.addView(textView);
                } else if (dataType.equals("Order")) {

                    int idOrder = jsonObject.getInt("idOrder");
                    int idArticle = jsonObject.getInt("idArticle");
                    int articleNr = jsonObject.getInt("ArticleNr");

                    TextView textView = new TextView(this);
                    textView.setText("Order ID: " + idOrder + "\nArticle ID: " + idArticle + "\nArticle Number: " + articleNr);

                    textView.setPadding(0, 0, 0, 20);

                    scrollView.addView(textView);
                } else {
                    showToast("Invalid data type");
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showToast(String message) {
        Toast.makeText(DataActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
