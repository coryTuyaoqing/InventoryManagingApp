package com.example.warehousemanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.warehousemanager.Controller.OrderRecViewAdaptor;
import com.example.warehousemanager.Model.Order;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

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
    }

    private void displaySearchResults(JSONArray searchResultsArray) {
        ArrayList<Order> orders = new ArrayList<>();
        System.out.println(searchResultsArray);
//        for (int i = 0; i < searchResultsArray.length(); i++) {
//            try {
//                // Parse JSON object and create Order instance
//                Order order = parseOrderFromJson(searchResultsArray.getJSONObject(i));
//                orders.add(order);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        // Update adapter data
//        adapter.setOrders(orders);
    }

//    private Order parseOrderFromJson(JSONObject jsonObject) throws JSONException {
//        // Parse JSON object and create Order instance
//        // You need to implement this method based on your JSON structure
//    }

    private void displayNoResultsMessage() {
        // Display no results message if needed
    }
}
