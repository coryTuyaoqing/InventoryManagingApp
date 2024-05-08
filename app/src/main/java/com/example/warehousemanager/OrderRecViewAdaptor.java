package com.example.warehousemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class OrderRecViewAdaptor extends RecyclerView.Adapter<OrderRecViewAdaptor.ViewHolder>{
    private Context context;

    private ArrayList<Order> orders = new ArrayList<>();
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
}
