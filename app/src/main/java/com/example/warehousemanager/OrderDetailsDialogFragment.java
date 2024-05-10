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
        TextView deadlineTextView = view.findViewById(R.id.deadline_text_view);
        TextView referenceTextView = view.findViewById(R.id.reference_text_view);
        TextView customerTextView = view.findViewById(R.id.customer_text_view);
        TextView responsibleTextView = view.findViewById(R.id.responsible_text_view);
        TextView highlightedOrderTextView = view.findViewById(R.id.highlighted_order_text_view);

        orderIdTextView.setText("Order ID: " + order.getOrderID());
        deadlineTextView.setText("Deadline: " + order.getDeadline());
        referenceTextView.setText("Reference: " + order.getDescription());
        customerTextView.setText("Customer: "+ order.getCustomer());
        responsibleTextView.setText("Responsible: "+ order.getResponsible());
        highlightedOrderTextView.setText("Highlighted: "+ order.getHighlightedOrder());


        // Handle close button click
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
