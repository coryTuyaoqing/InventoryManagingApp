package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        try {
            JSONArray searchResultsArray = new JSONArray(searchResultsJsonString);
            if ("order".equals(searchType)) {
                List<Order> orders = parseOrders(searchResultsArray);
                adapter = new OrderRecViewAdaptor(this, new ArrayList<>(orders));
            } else if ("article".equals(searchType)) {
                List<Article> articles = parseArticles(searchResultsArray);
                Map<Article, Integer> articlesMap = convertToArticleMap(articles);
                adapter = new ArticleRecViewAdaptor(this, articlesMap);
            }
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            displayNoResultsMessage();
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
            LocalDate deadline = LocalDate.parse(deadlineString); // Convert string to LocalDate
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

    private Map<Article, Integer> convertToArticleMap(List<Article> articles) {
        Map<Article, Integer> articlesMap = new HashMap<>();
        for (Article article : articles) {
            articlesMap.put(article, 1);
        }
        return articlesMap;
    }

    private void displayNoResultsMessage() {
        Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
    }
}
