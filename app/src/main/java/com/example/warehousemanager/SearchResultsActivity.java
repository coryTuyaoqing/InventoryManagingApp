package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warehousemanager.Controller.OrderRecViewAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.time.LocalDate;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderRecViewAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.ResultsRyc);
        adapter = new OrderRecViewAdaptor(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve search results from intent
        String searchResultsJsonString = getIntent().getStringExtra("searchResults");

        try {
            JSONArray searchResultsArray = new JSONArray(searchResultsJsonString);
            displaySearchResults(searchResultsArray);
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

    private void displaySearchResults(JSONArray searchResultsArray) {
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < searchResultsArray.length(); i++) {
            try {
                Order order = parseOrderFromJson(searchResultsArray.getJSONObject(i));
                orders.add(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.setOrders(orders);
    }

    private Order parseOrderFromJson(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("idOrder");
        String deadlineString = jsonObject.getString("deadline");
        LocalDate deadline = LocalDate.parse(deadlineString); // Convert string to LocalDate
        String reference = jsonObject.getString("reference");

        // Create and return an Order object
        return new Order(id, deadline, reference);
    }



    private void displayNoResultsMessage() {
        Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
    }
}
