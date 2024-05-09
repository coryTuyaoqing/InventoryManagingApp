package com.example.warehousemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.ResultsRyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve search results from intent
        String searchResultsJsonString = getIntent().getStringExtra("searchResults");
        ArrayList<String> filter = getIntent().getStringArrayListExtra("filter");
        ArrayList<String> keyword = getIntent().getStringArrayListExtra("keyword");
        searchType = getIntent().getStringExtra("searchtype");

        System.out.println(searchResultsJsonString);

        try {
            JSONArray searchResultsArray = new JSONArray(searchResultsJsonString);
            if ("order".equals(searchType)) {
                ArrayList<Order> orders = parseOrders(searchResultsArray);
                adapter = new OrderRecViewAdaptor(this, orders);
            } else if ("article".equals(searchType)) {
                ArrayList<Article> articles = parseArticles(searchResultsArray);
                adapter = new ArticleRecViewAdaptor(this, articles);
            }
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            displayNoResultsMessage();
        }

        if (filter != null && keyword != null) {
            LinearLayout selectedFilterLayout = findViewById(R.id.SelectedFilterLayout);
            for (int i = 0; i < filter.size(); i++) {
                if ("1".equals(filter.get(i))) {
                    String filterName = getFilterName(i);
                    String filterValue = keyword.get(i);

                    TextView textView = new TextView(this);
                    textView.setText(filterName + ": " + filterValue);

                    selectedFilterLayout.addView(textView);
                }
            }
        } else {
            // Handle case when filter or keyword ArrayLists are null
            Toast.makeText(this, "No filters and keywords found", Toast.LENGTH_SHORT).show();
        }

        Button btnDataReturn = findViewById(R.id.BackSearchResults);
        btnDataReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<Order> parseOrders(JSONArray jsonArray) throws JSONException {
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("idOrder");
            String deadlineString = jsonObject.getString("deadline");
            LocalDate deadline = LocalDate.parse(deadlineString);
            String reference = jsonObject.getString("reference");
            orders.add(new Order(id, deadline, reference));
        }
        return orders;
    }

    private ArrayList<Article> parseArticles(JSONArray jsonArray) throws JSONException {
        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("idArticle");
            String name = jsonObject.getString("name");
            String color = jsonObject.getString("color");
            String size = jsonObject.getString("size");
            articles.add(new Article(id, name, color, size));
        }
        return articles;
    }

    private String getFilterName(int index) {
        // Determine the filter name based on the index and searchType
        if ("Order".equals(searchType)) {
            String[] orderFilters = getResources().getStringArray(R.array.filter_order);
            return orderFilters[index];
        } else if ("Article".equals(searchType)) {
            String[] articleFilters = getResources().getStringArray(R.array.filter_article);
            return articleFilters[index];
        }
        return ""; // Return empty string if searchType is not recognized
    }

    private void displayNoResultsMessage() {
        Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
    }
}
