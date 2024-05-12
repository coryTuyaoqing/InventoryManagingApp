package com.example.warehousemanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class ArticleDetailsDialogFragment extends DialogFragment {

    private Article article;
    private Context context;
    private Button editButton;
    private Order order;

    public ArticleDetailsDialogFragment(Article article, Context context, Order order) {
        this.article = article;
        this.context = context;
        this.order  = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_details_dialog, container, false);

        // Initialize views and display article details
        TextView articleNameTextView = view.findViewById(R.id.article_name_text_view);
        articleNameTextView.setText("Article Name: " + article.getArticleName());

        TextView supplierNameTextView = view.findViewById(R.id.supplier_name_text_view);
        supplierNameTextView.setText("Supplier Name: " + article.getSupplierName());

        TextView priceTextView = view.findViewById(R.id.price_text_view);
        priceTextView.setText("Price: " + article.getPrice());

        TextView colorTextView = view.findViewById(R.id.color_text_view);
        colorTextView.setText("Color: " + article.getColor());

        TextView sizeTextView = view.findViewById(R.id.size_text_view);
        sizeTextView.setText("Size: " + article.getSize());

        // Handle close button click
        Button closeButton = view.findViewById(R.id.Dialog_Back);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        editButton = view.findViewById(R.id.EditQuantities);
        toggleEditButtonVisibility();

        return view;
    }

    private void toggleEditButtonVisibility() {
        if (context instanceof SearchResultsActivity) {
            editButton.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditQuantitiesDialogFragment(article);
                    dismiss();
                }
            });
        } else {
            editButton.setVisibility(View.GONE);
        }
    }

    private void showEditQuantitiesDialogFragment(Article article) {
        EditQuantitiesDialogFragment dialogFragment = new EditQuantitiesDialogFragment(context, article, order);
        dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "EditQuantitiesDialog");
    }
}

