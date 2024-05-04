package com.example.warehousemanager.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehousemanager.R;
import com.example.warehousemanager.Model.Article;

import java.util.ArrayList;
import java.util.Map;

public class ArticleRecViewAdaptor extends RecyclerView.Adapter<ArticleRecViewAdaptor.ViewHolder> {
    private Context context;
    private Map<Article, Integer> articlesNrMap;

    public ArticleRecViewAdaptor(Context context, Map<Article, Integer> articlesNrMap) {
        this.context = context;
        this.articlesNrMap = articlesNrMap;
    }

    public void setArticlesNrMap(Map<Article, Integer> articlesNrMap) {
        this.articlesNrMap = articlesNrMap;
        notifyDataSetChanged();
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
        ArrayList<Article> articles = new ArrayList<>(articlesNrMap.keySet());
        ArrayList<Integer> articleNr = new ArrayList<>(articlesNrMap.values());

        holder.txtArticleName.setText(articles.get(position).getArticleName());
        holder.txtArticleInfo.setText("Number: " + articleNr.get(position));
    }

    @Override
    public int getItemCount() {
        return articlesNrMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtArticleName, txtArticleInfo;
        CardView cardArticleItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArticleName = itemView.findViewById(R.id.txt_article_name);
            txtArticleInfo = itemView.findViewById(R.id.txt_articles_info);
            cardArticleItem = itemView.findViewById(R.id.articles_list_parent);
        }
    }
}
