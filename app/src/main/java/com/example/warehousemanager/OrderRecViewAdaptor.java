package com.example.warehousemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehousemanager.model.Order;

import java.util.ArrayList;

public class OrderRecViewAdaptor extends RecyclerView.Adapter<OrderRecViewAdaptor.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtOrderID;
        private TextView txtArticleID;
        private CardView cardOrderItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order_ID);
            txtArticleID = itemView.findViewById(R.id.txt_article_ID);
            cardOrderItem = itemView.findViewById(R.id.order_list_item_parent);
        }
    }

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
        holder.txtOrderID.setText("orderID: " + order.getOrderID());
        holder.txtArticleID.setText("articleID: " + order.getArticleID());
        holder.cardOrderItem.setOnClickListener(v -> {
            Toast.makeText(context, "Order" + order.getOrderID() + "is clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
