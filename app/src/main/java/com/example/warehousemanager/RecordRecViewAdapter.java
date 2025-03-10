package com.example.warehousemanager;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecordRecViewAdapter extends RecyclerView.Adapter<RecordRecViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Record> records;

    public RecordRecViewAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);

        // Set the record data
        holder.txtArticleNr.setText("Added: " + record.getArticleNr());
        holder.txtOperationTime.setText("Time of addition: " + record.getFormattedOperationTime());
        getStaffName(holder, record);

        // Fetch and set order and article information
        fetchOrderAndArticleInfo(record, holder);

        // Set the click listener for opening Article detail
        holder.articlesCardView.setOnClickListener(v -> {
            if (holder.article != null) {
                ArticleDetailsDialogFragment dialogFragment = new ArticleDetailsDialogFragment(holder.article, context);
                dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ArticleDetailsDialog");
            }
        });

        // Set the click listener for opening Order detail
        holder.orderCardView.setOnClickListener(v -> {
            if (holder.order != null) {
                OrderDetailsDialogFragment dialogFragment = new OrderDetailsDialogFragment(holder.order, context);
                dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "OrderDetailsDialog");
            }
        });
        holder.orderCardView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, OrderTableActivity.class);
            intent.putExtra("orderID", record.getIdOrder());
            context.startActivity(intent);
            return true;
        });
    }

    private String getStaffName(ViewHolder holder, Record record) {
        int staffID = record.getIdStaff();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DB.DB_URL + "get_staff_from_id/" + staffID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    JSONObject responseObject = responseArray.getJSONObject(0);
                    String name = responseObject.getString("name");
                    ((FragmentActivity)context).runOnUiThread(() -> {
                        holder.txtStaff.setText("Staff: " + name);
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return null;
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderId;
        public TextView txtArticleId;
        public TextView txtArticleNr;
        public TextView txtOperationTime;
        public TextView txtOrderDescription;
        public TextView txtOrderDeadline;
        public TextView txtArticleName;
        public TextView txtArticleInfo;
        public TextView txtStaff;

        public Article article;
        public CardView articlesCardView;

        public Order order;
        public CardView orderCardView;


        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtArticleId = itemView.findViewById(R.id.txtArticleId);
            txtArticleNr = itemView.findViewById(R.id.txtAdded);
            txtOperationTime = itemView.findViewById(R.id.txtOperationTime);
            txtOrderDescription = itemView.findViewById(R.id.txt_order_description);
            txtOrderDeadline = itemView.findViewById(R.id.txt_deadline);
            txtArticleName = itemView.findViewById(R.id.txt_article_name);
            txtArticleInfo = itemView.findViewById(R.id.txt_articles_info);
            articlesCardView = itemView.findViewById(R.id.articles_list_parent);
            orderCardView = itemView.findViewById(R.id.order_list_item_parent);
            txtStaff = itemView.findViewById(R.id.txtStaff);
        }
    }

    private void fetchOrderAndArticleInfo(Record record, ViewHolder holder) {
        String articleUrl = "https://studev.groept.be/api/a23PT308/get_article_fromID/" + record.getIdArticle();
        String orderUrl = "https://studev.groept.be/api/a23PT308/get_order_fromID/" + record.getIdOrder();

        OkHttpClient client = new OkHttpClient();

        Request articleRequest = new Request.Builder().url(articleUrl).build();
        Request orderRequest = new Request.Builder().url(orderUrl).build();

        client.newCall(articleRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: getting article", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    JSONObject responseObject = responseArray.getJSONObject(0);

                    String name = responseObject.getString("name");
                    String supplierName = responseObject.getString("SupplierName");
                    double price = responseObject.getDouble("Price");
                    String color = responseObject.getString("color");
                    String size = responseObject.getString("size");

                    Article article = new Article(record.getIdArticle(), name, supplierName, price, color, size);
                    holder.article = article;

                    ((Activity) context).runOnUiThread(() -> {
                        holder.txtArticleName.setText("Article Name: " + article.getArticleName());
                        holder.txtArticleInfo.setText("Supplier: " + article.getSupplierName() + "\nPrice: " + article.getPrice() + "\nColor: " + article.getColor() + "\nSize: " + article.getSize());
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: parsing article", e);
                }
            }
        });

        client.newCall(orderRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: getting order", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    JSONObject responseObject = responseArray.getJSONObject(0);

                    int orderId = responseObject.getInt("idOrder");
                    LocalDate deadline = LocalDate.parse(responseObject.getString("deadline"), DateTimeFormatter.ISO_LOCAL_DATE);
                    String description = responseObject.getString("reference");
                    String customer = responseObject.getString("Customer");
                    String responsible = responseObject.getString("Responsible");
                    int highlightedOrder = responseObject.getInt("HighlightedOrder");

                    Order order = new Order(orderId, deadline, description, customer, responsible, highlightedOrder);
                    holder.order = order;

                    ((Activity) context).runOnUiThread(() -> {
                        holder.txtOrderDescription.setText("Order Description: " + order.getDescription());
                        holder.txtOrderDeadline.setText("Deadline: " + order.getDeadline().toString());
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: parsing order", e);
                }
            }
        });
    }
}
