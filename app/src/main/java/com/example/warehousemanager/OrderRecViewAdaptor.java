package com.example.warehousemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderRecViewAdaptor extends RecyclerView.Adapter<OrderRecViewAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    private CallBack callBack = order -> {
        // Create and show the dialog when the item is clicke
    }; //default callback function
    private static final String TAG = "OrderRecViewAdaptor";

    public OrderRecViewAdaptor(Context context, ArrayList<Order> orders) throws JSONException {
        this.context = context;
        this.orders = orders;
        for(Order order: orders){
            order.getArticlesOfOrders(new Order.GetArticlesCallback() {
                @Override
                public void AfterGetArticles(Order order) {
                    if(context instanceof Activity){
                        ((Activity)context).runOnUiThread(() -> notifyDataSetChanged());
                    }
                }
            });
        }
    }

    public OrderRecViewAdaptor(Context context) {
        this.context = context;
        orders = new ArrayList<>();
    }

    public OrderRecViewAdaptor(Context context, ArrayList<Order> orders, CallBack callBack) throws JSONException {
        this.context = context;
        this.orders = orders;
        this.callBack = callBack;
        for(Order order: orders){
            order.getArticlesOfOrders(new Order.GetArticlesCallback() {
                @Override
                public void AfterGetArticles(Order order) {
                    if(context instanceof Activity){
                        ((Activity)context).runOnUiThread(() -> notifyDataSetChanged());
                    }
                }
            });
        }
    }

    public OrderRecViewAdaptor(Context context, CallBack callBack) {
        this.context = context;
        orders = new ArrayList<>();
        this.callBack = callBack;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public ArrayList<Order> getOrders() {
        return orders;
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

        holder.cardOrderItem.setOnClickListener(v -> {
            OrderDetailsDialogFragment dialogFragment = new OrderDetailsDialogFragment(order, context);
            dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "OrderDetailsDialog");
        });

        holder.cardOrderItem.setOnLongClickListener(v -> {
            if(context instanceof OrderTableActivity){
                return false;
            }
            Toast.makeText(context, "long pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, OrderTableActivity.class);
            intent.putExtra("orderID", order.getOrderID());
            context.startActivity(intent);
            return true;
        });

        // Bind order data to the views
        holder.txtOrderDescription.setText(order.getDescription());
        holder.txtDeadline.setText("Deadline: " + order.getDeadline().toString());

        ArticleRecViewAdaptor adaptor = new ArticleRecViewAdaptor(holder.itemView.getContext(), order);
        holder.recyclerArticlesInOrderItem.setAdapter(adaptor);
        holder.recyclerArticlesInOrderItem.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void getOrdersFromDB(String URL) {
        orders = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: getting order", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: getting order");
                try {
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        int orderID = responseObject.getInt("idOrder");
                        String deadline = responseObject.getString("deadline");
                        String description = responseObject.getString("reference");
                        String customer = responseObject.getString("Customer");
                        String responsible = responseObject.getString("Responsible");
                        int highlightedOrder = responseObject.getInt("HighlightedOrder");
                        orders.add(new Order(orderID, LocalDate.parse(deadline), description, customer, responsible, highlightedOrder));
                    }
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
                    }

                    for(Order order: orders){
                        order.getArticlesOfOrders(o -> {
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: parsing", e);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderDescription;
        private TextView txtDeadline;
        private CardView cardOrderItem;
        private RecyclerView recyclerArticlesInOrderItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDescription = itemView.findViewById(R.id.txt_order_description);
            txtDeadline = itemView.findViewById(R.id.txt_deadline);
            cardOrderItem = itemView.findViewById(R.id.order_list_item_parent);
            recyclerArticlesInOrderItem = itemView.findViewById(R.id.recyclerArticlesInOrderItem);
        }
    }

    public interface CallBack {
        void OrderOnClick(Order order);
    }
}
