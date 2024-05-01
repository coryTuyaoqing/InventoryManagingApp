package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehousemanager.SearchResultsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private Button searchButton;
    private List<CheckBox> filterCheckBoxes = new ArrayList<>();
    private static final String BASE_URL = "https://studev.groept.be/api/a23PT308/";
    private static final OkHttpClient client = new OkHttpClient();

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
        searchButton = findViewById(R.id.Search_Button);

        setupSpinner(typeSpinner, R.array.types_array);

        String[] filterOptions = getResources().getStringArray(R.array.filter_array);
        setupFilterOptions(filterOptions);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                String filter = getSelectedFilter();
                String keyword = getKeywordForFilter(filter);
                performSearch(type, filter, keyword);
            }
        });
    }

    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(arrayResource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupFilterOptions(String[] filterOptions) {
        LinearLayout filterOptionsLayout = findViewById(R.id.FIlters);
        LinearLayout keywordInputsLayout = findViewById(R.id.KeywordInputs);

        for (String filter : filterOptions) {
            EditText editText = new EditText(this);
            editText.setHint("Input " + filter);
            editText.setTag(filter);
            editText.setVisibility(View.GONE);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && ((EditText) v).getText().toString().equals("Input " + filter)) {
                        ((EditText) v).setText("");
                    }
                }
            });
            keywordInputsLayout.addView(editText);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(filter);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    EditText correspondingEditText = findViewById(R.id.KeywordInputs).findViewWithTag(filter);
                    if (correspondingEditText != null) {
                        correspondingEditText.setVisibility(isChecked ? View.VISIBLE : View.GONE); // Show/hide keyword input field based on checkbox state
                    }
                }
            });
            filterOptionsLayout.addView(checkBox);
            filterCheckBoxes.add(checkBox);

            if (filter.equals("ID")) {
                checkBox.setChecked(true);
                editText.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getSelectedFilter() {
        for (CheckBox checkBox : filterCheckBoxes) {
            if (checkBox.isChecked()) {
                return checkBox.getText().toString();
            }
        }
        return "";
    }

    private String getKeywordForFilter(String filter) {
        EditText editText = findViewById(R.id.KeywordInputs).findViewWithTag(filter);
        if (editText != null) {
            return editText.getText().toString();
        }
        return "";
    }

    private void performSearch(String type, String filter, String keyword) {
        String endpoint;
        String url;

        if (type.equals("Order")) {
            switch (filter.toLowerCase()) {
                case "id":
                    endpoint = "get_order_fromID/" + keyword;
                    break;
                case "most articles":
                    endpoint = "get_order_mostArticles";
                    break;
                default:
                    showToast("Invalid filter for order");
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
                    showToast("Invalid filter for article");
                    return;
            }
        }

        url = BASE_URL + endpoint;

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
                        final JSONArray jsonArray = new JSONArray(responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                navigateToSearchResults(jsonArray);
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

    private void navigateToSearchResults(JSONArray jsonArray) {
        Intent intent = new Intent(DataActivity.this, SearchResultsActivity.class);
        intent.putExtra("searchResults", jsonArray.toString());
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(DataActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
