package com.example.warehousemanager;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleRecViewAdaptor extends RecyclerView.Adapter<ArticleRecViewAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<Article> articles;
    private ArrayList<Order.ArticleNr> articleNr;
    private static final String TAG = "ArticleRecViewAdaptor";

    public ArticleRecViewAdaptor(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
        articleNr = new ArrayList<>();
    }

    public ArticleRecViewAdaptor(Context context, Order order) {
        this.context = context;

        articles = new ArrayList<>(order.getArticlesNrMap().keySet());
        articleNr = new ArrayList<>(order.getArticlesNrMap().values());
    }

    public ArticleRecViewAdaptor(Context context){
        this.context = context;
        articles = new ArrayList<>();
        articleNr = new ArrayList<>();
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public void setArticleNr(ArrayList<Order.ArticleNr> articleNr) {
        this.articleNr = articleNr;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public ArrayList<Order.ArticleNr> getArticleNr() {
        return articleNr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.articles_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);

        // Set click listener for the item
        holder.cardArticleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the dialog when the item is clicked
                showArticleDetailsDialog(article);
            }
        });

        // Bind article data to the views
        holder.txtArticleName.setText(article.getArticleName());
        holder.txtArticleInfo.setText("Color: " + article.getColor() + ", Size: " + article.getSize());

        if(article instanceof ArticleInOrder){
            Order.ArticleNr nr = articleNr.get(position);
            int inStockNr = nr.getInStockNr();
            int requiredNr = nr.getRequiredNr();

            if(((ArticleInOrder)article).getMyOrder().isComplete(article)){
                setBackgroundColor(holder.cardArticleItem, R.color.dark_color);
                return;
            }

            TextView txtArticleInStock = new TextView(context);
            txtArticleInStock.setText("In stock: " + inStockNr);
            txtArticleInStock.setTextColor(context.getResources().getColor(R.color.purple));

            TextView txtArticleRequired = new TextView(context);
            txtArticleRequired.setText("Required: " + requiredNr);
            txtArticleRequired.setTextColor(context.getResources().getColor(R.color.purple));

            holder.linearArticleItem.addView(txtArticleRequired);
            holder.linearArticleItem.addView(txtArticleInStock);

        }
    }
        @Override
    public int getItemCount() {
        return articles.size();
    }

    private void setBackgroundColor(View view, int color) {
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(ContextCompat.getColor(context, color)));
    }

    public void getArticlesFromDB(String url){
        articles = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: success");
                try{
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    for(int i=0; i<responseArray.length(); i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        int articleID = responseObject.getInt("idArticle");
                        String name = responseObject.getString("name");
                        String supplierName = responseObject.getString("SupplierName");
                        double price = responseObject.getDouble("Price");
                        String color = responseObject.getString("color");
                        String size = responseObject.getString("size");
                        articles.add(new Article(articleID, name, supplierName, price, color, size));
                    }
                    if(context instanceof Activity){
                        if(articles.isEmpty() && context instanceof ScanResultActivity){
                            ((Activity) context).finish();
                            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "No article found", Toast.LENGTH_SHORT).show());
                        }
                        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, "onResponse: parsing", e);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtArticleName, txtArticleInfo;
        CardView cardArticleItem;
        LinearLayout linearArticleItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArticleName = itemView.findViewById(R.id.txt_article_name);
            txtArticleInfo = itemView.findViewById(R.id.txt_articles_info);
            cardArticleItem = itemView.findViewById(R.id.articles_list_parent);
            linearArticleItem = itemView.findViewById(R.id.linearArticleItem);
        }
    }

    private void showArticleDetailsDialog(Article article) {
        ArticleDetailsDialogFragment dialogFragment = new ArticleDetailsDialogFragment(article, context);
        dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ArticleDetailsDialog");
    }
}
