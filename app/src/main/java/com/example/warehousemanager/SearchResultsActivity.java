package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Retrieve search results from intent
        String searchResultsJsonString = getIntent().getStringExtra("searchResults");

        try {
            JSONArray searchResultsArray = new JSONArray(searchResultsJsonString);
            displaySearchResults(searchResultsArray);
        } catch (JSONException e) {
            e.printStackTrace();
            displayNoResultsMessage();
        }

        // Set up back button
        Button backButton = findViewById(R.id.BackSearchResults);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displaySearchResults(JSONArray searchResultsArray) {
        LinearLayout searchResultsLayout = findViewById(R.id.ResultsLayout);

        try {
            for (int i = 0; i < searchResultsArray.length(); i++) {
                String result = searchResultsArray.getString(i);
                TextView textView = new TextView(this);
                textView.setText(result);
                searchResultsLayout.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            displayNoResultsMessage();
        }
    }

    private void displayNoResultsMessage() {
        LinearLayout searchResultsLayout = findViewById(R.id.ResultsLayout);
        TextView noResultsTextView = new TextView(this);
        noResultsTextView.setText("No search results found.");
        searchResultsLayout.addView(noResultsTextView);
    }
}
