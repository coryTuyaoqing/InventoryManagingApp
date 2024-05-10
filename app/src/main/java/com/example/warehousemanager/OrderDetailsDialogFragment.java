package com.example.warehousemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class OrderDetailsDialogFragment extends DialogFragment {

    private Order order;

    public OrderDetailsDialogFragment(Order order) {
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_dialog, container, false);

        // Initialize views and display order details
        TextView orderIdTextView = view.findViewById(R.id.order_id_text_view);
        orderIdTextView.setText("Order ID: " + order.getOrderID());

        // Handle close button click
        Button closeButton = view.findViewById(R.id.Dialog_Back);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}