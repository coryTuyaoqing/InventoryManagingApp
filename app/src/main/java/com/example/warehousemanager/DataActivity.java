package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private EditText filterInputEditText;
    private Button searchButton;
    private String[] filterOptions;
    private ScrollView filterScrollView;

    private List<String> selectedFilters = new ArrayList<>();

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
        filterInputEditText = findViewById(R.id.filterInput);
        searchButton = findViewById(R.id.Search_Button);
        filterScrollView = findViewById(R.id.FilterSelectScroll);

        filterOptions = getResources().getStringArray(R.array.filter_array);

        populateFilters();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        filterInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && filterInputEditText.getText().toString().equals("Input Keyword")) {
                    filterInputEditText.setText("");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                String keyword = filterInputEditText.getText().toString();

                if (keyword.isEmpty() || keyword.equals("Input Keyword")) {
                    showToast("Please enter a keyword for the search.");
                    return;
                }

                performSearch(type, keyword);
            }
        });
    }


    private void populateFilters() {
        LinearLayout filterCheckboxContainer = findViewById(R.id.FIlters);

        for (String filter : filterOptions) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(filter);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedFilters.add(filter);
                    } else {
                        selectedFilters.remove(filter);
                    }
                }
            });
            filterCheckboxContainer.addView(checkBox);
        }
    }


    private void performSearch(String type, String keyword) {
        // Implement your search logic here
    }

    private void showToast(String message) {
        Toast.makeText(DataActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
