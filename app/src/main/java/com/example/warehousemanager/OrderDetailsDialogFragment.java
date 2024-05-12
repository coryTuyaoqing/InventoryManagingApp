package com.example.warehousemanager;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class OrderDetailsDialogFragment extends DialogFragment {

    private Order order;
    private LinearLayout linearOrderDialog;
    private AddView addView = view -> {}; //add view callback function, by default doing nothing, use it when you want to change the ui of dialog window

    public OrderDetailsDialogFragment(Order order) {
        super();
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

        linearOrderDialog = view.findViewById(R.id.linearOrderDialog);
        addView.addView(linearOrderDialog);

        orderIdTextView.setText("Order ID: " + order.getOrderID());
        deadlineTextView.setText("Deadline: " + order.getDeadline());
        referenceTextView.setText("Reference: " + order.getDescription());
        customerTextView.setText("Customer: "+ order.getCustomer());
        responsibleTextView.setText("Responsible: "+ order.getResponsible());
        highlightedOrderTextView.setText("Highlighted: "+ order.getHighlightedOrder());


        // Handle close button click
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    public void setAddView(AddView addView) {
        this.addView = addView;
    }

    public interface AddView{
        void addView(ViewGroup layout);
    }
}
