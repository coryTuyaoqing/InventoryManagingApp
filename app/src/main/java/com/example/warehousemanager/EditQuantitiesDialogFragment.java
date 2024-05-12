package com.example.warehousemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class EditQuantitiesDialogFragment extends DialogFragment {

    private static final String ARG_ARTICLE = "article";
    private Article article;

    public EditQuantitiesDialogFragment(Article article) {
        this.article = article;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_quantities_dialog, container, false);

        EditText RequiredEditText = view.findViewById(R.id.RequiredInput);
        EditText InStockEditText = view.findViewById(R.id.InStockInput);
        Button saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
