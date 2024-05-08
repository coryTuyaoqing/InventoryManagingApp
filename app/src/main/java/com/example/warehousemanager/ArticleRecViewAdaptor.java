package com.example.warehousemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class ArticleRecViewAdaptor extends RecyclerView.Adapter<ArticleRecViewAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<Article> articles;
    private ArrayList<Integer> articleNr;

    public ArticleRecViewAdaptor(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
        articleNr = null;
    }

    public ArticleRecViewAdaptor(Context context, Map<Article,Integer> articlesNrMap) {
        this.context = context;
        articles = new ArrayList<>(articlesNrMap.keySet());
        articleNr = new ArrayList<>(articlesNrMap.values());
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public void setArticleNr(ArrayList<Integer> articleNr) {
        this.articleNr = articleNr;
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
        holder.txtArticleName.setText(articles.get(position).getArticleName());
        if(articleNr == null){
            holder.txtArticleInfo.setText(articles.get(position).toString());
        }
        else{
            holder.txtArticleInfo.setText("Number: " + articleNr.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
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
