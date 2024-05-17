package com.example.warehousemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = records.get(position);

        // Set the order ID
        holder.txtOrderId.setText("Order ID: " + record.getIdOrder());

        // Set the article ID
        holder.txtArticleId.setText("Article ID: " + record.getIdArticle());

        // Set the article number
        holder.txtArticleNr.setText("Article Number: " + record.getArticleNr());

        // Set the operation time
        holder.txtOperationTime.setText("Operation Time: " + record.getFormattedOperationTime());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderId;
        public TextView txtArticleId;
        public TextView txtArticleNr;
        public TextView txtOperationTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtArticleId = itemView.findViewById(R.id.txtArticleId);
            txtArticleNr = itemView.findViewById(R.id.txtAdded);
            txtOperationTime = itemView.findViewById(R.id.txtOperationTime);
        }
    }
}
