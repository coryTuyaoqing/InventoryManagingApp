package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        String type = getIntent().getStringExtra("type");
        String keyword = getIntent().getStringExtra("keyword");
        String[] selectedFilters = getIntent().getStringArrayExtra("selectedFilters");

        LinearLayout selectedFiltersLayout = findViewById(R.id.SelectedFilterLayout);
        displaySelectedFilters(selectedFilters, selectedFiltersLayout);

        String[] searchResults = getIntent().getStringArrayExtra("searchResults");
        LinearLayout searchResultsLayout = findViewById(R.id.ResultsLayout);
        if (searchResults != null) {
            for (String result : searchResults) {
                TextView textView = new TextView(this);
                textView.setText(result);
                searchResultsLayout.addView(textView);
            }
        } else {
            TextView noResultsTextView = new TextView(this);
            noResultsTextView.setText("No search results found.");
            searchResultsLayout.addView(noResultsTextView);
        }

        Button backButton = findViewById(R.id.BackSearchResults);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displaySelectedFilters(String[] selectedFilters, LinearLayout selectedFiltersLayout) {
        for (String filter : selectedFilters) {
            TextView filterTextView = new TextView(this);
            filterTextView.setText(filter);
            selectedFiltersLayout.addView(filterTextView);
        }
    }
}
