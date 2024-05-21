package com.example.warehousemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;

public class OrderDetailsDialogFragment extends DialogFragment {

    private Order order;
    private boolean orderWasHighlighted;
    private Context context;
    private LinearLayout linearOrderDialog;
    private static final String TAG = "OrderDetailsDialogFragm";

    public OrderDetailsDialogFragment(Order order, Context context) {
        super();
        this.order = order;
        this.context = context;
        orderWasHighlighted = order.isHighlighted();
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
        addView(linearOrderDialog);

        orderIdTextView.setText("Order ID: " + order.getOrderID());
        deadlineTextView.setText("Deadline: " + order.getDeadline());
        referenceTextView.setText("Reference: " + order.getDescription());
        customerTextView.setText("Customer: "+ order.getCustomer());
        responsibleTextView.setText("Responsible: "+ order.getResponsible());
        highlightedOrderTextView.setText("Highlighted: "+ order.isHighlighted());


        // Handle close button click
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());

        ImageButton btnHighLight = view.findViewById(R.id.btnHighLight);
        if(order.isHighlighted()){
            setBackgroundColor(btnHighLight, R.color.orange);
        }

        btnHighLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.isHighlighted()){
                    setBackgroundColor(btnHighLight, android.R.color.transparent);
                    order.setHighlighted(false);
                }
                else{
                    setBackgroundColor(btnHighLight, R.color.orange);
                    order.setHighlighted(true);
                }
            }
        });

        return view;
    }

    private void addView(ViewGroup viewGroup) {
        if(context instanceof ScanResultActivity){
            Article article = ((ScanResultActivity) context).getArticle();
            if(order.isComplete(article)){
                ((ScanResultActivity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "The order is complete", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            Button btnAddArticleToOrder = new Button(viewGroup.getContext());
            btnAddArticleToOrder.setText("Add article to\nthis order");
            btnAddArticleToOrder.setOnClickListener(v -> {
                AddArticleDialogFragment addArticleDialogFragment = new AddArticleDialogFragment(order, article);
                addArticleDialogFragment.show(((ScanResultActivity) context).getSupportFragmentManager(), "add article to order");
                dismiss();
            });
            viewGroup.addView(btnAddArticleToOrder);
        }
    }

    private void setBackgroundColor(View view, int color) {
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(ContextCompat.getColor(context, color)));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(order.isHighlighted() != orderWasHighlighted){
            String url = DB.DB_URL + "set_highlighted_order/" + (order.isHighlighted() ? 1 : 0) + "/" + order.getOrderID();
            DB.httpRequest(url);
        }
    }
}
