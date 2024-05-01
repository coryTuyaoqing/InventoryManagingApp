package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private Button searchButton;
    private List<CheckBox> filterCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Button btnDataReturn = findViewById(R.id.btnDataReturn);

        btnDataReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        typeSpinner = findViewById(R.id.Type);
        searchButton = findViewById(R.id.Search_Button);

        setupSpinner(typeSpinner, R.array.types_array);

        String[] filterOptions = getResources().getStringArray(R.array.filter_array);

        setupFilterOptions(filterOptions);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

        private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(arrayResource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupFilterOptions(String[] filterOptions) {
        LinearLayout filterOptionsLayout = findViewById(R.id.FIlters);
        LinearLayout keywordInputsLayout = findViewById(R.id.KeywordInputs);

        for (String filter : filterOptions) {
            EditText editText = new EditText(this);
            editText.setHint("Input " + filter);
            editText.setTag(filter);
            editText.setVisibility(View.GONE); // Initially hide the keyword input fields
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && ((EditText) v).getText().toString().equals("Input " + filter)) {
                        ((EditText) v).setText("");
                    }
                }
            });
            keywordInputsLayout.addView(editText); // Add to the keyword inputs layout

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(filter);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    EditText correspondingEditText = findViewById(R.id.KeywordInputs).findViewWithTag(filter);
                    if (correspondingEditText != null) {
                        correspondingEditText.setVisibility(isChecked ? View.VISIBLE : View.GONE); // Show/hide keyword input field based on checkbox state
                    }
                }
            });
            filterOptionsLayout.addView(checkBox);
            filterCheckBoxes.add(checkBox);

            if (filter.equals("ID")) {
                checkBox.setChecked(true); // Automatically check the checkbox for "ID"
                editText.setVisibility(View.VISIBLE); // Show the EditText field for "ID" by default
            }
        }
    }


    private void showToast(String message) {
        Toast.makeText(DataActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
