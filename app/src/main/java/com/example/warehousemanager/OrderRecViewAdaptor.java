package com.example.warehousemanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderRecViewAdaptor extends RecyclerView.Adapter<OrderRecViewAdaptor.ViewHolder>{
    private Context context;
    private ArrayList<Order> orders = new ArrayList<>();
    private static final String TAG = "OrderRecViewAdaptor";
    public OrderRecViewAdaptor(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.orders_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        Map<Article, Integer> articles = order.getArticlesNrMap();

        holder.txtOrderDescription.setText(order.getDescription());

        holder.txtArticles.setText("Deadline: " + order.getDeadline().toString());

        holder.cardOrderItem.setOnClickListener(v -> Toast.makeText(context, "Order" + order.getOrderID() + "is clicked", Toast.LENGTH_SHORT).show());

        ArticleRecViewAdaptor adaptor = new ArticleRecViewAdaptor(holder.itemView.getContext(), articles);
        holder.recyclerArticlesInOrderItem.setAdapter(adaptor);
        holder.recyclerArticlesInOrderItem.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtOrderDescription;
        private TextView txtArticles;
        private CardView cardOrderItem;
        private RecyclerView recyclerArticlesInOrderItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDescription = itemView.findViewById(R.id.txt_order_description);
            txtArticles = itemView.findViewById(R.id.txt_articles);
            cardOrderItem = itemView.findViewById(R.id.order_list_item_parent);
            recyclerArticlesInOrderItem = itemView.findViewById(R.id.recyclerArticlesInOrderItem);
        }
    }

    public void getOrdersFromDB(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: getting order", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: getting order");
                try{
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    for(int i=0; i<responseArray.length(); i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        int orderID = responseObject.getInt("idOrder");
                        String deadline = responseObject.getString("deadline");
                        String description = responseObject.getString("reference");
                        orders.add(new Order(orderID, LocalDate.now(), description));
                    }
                    if(context instanceof Activity){
                        ((Activity) context).runOnUiThread(() -> setOrders(orders));
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, "onResponse: parsing", e);
                }
            }
        });
    }
}
