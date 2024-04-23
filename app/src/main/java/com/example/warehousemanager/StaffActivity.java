package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StaffActivity extends AppCompatActivity {
    private int rowCount = 1;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffmanagement);
        tableLayout = findViewById(R.id.tableLayout3);
    }

    public void onAddButtonClick(View view) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView textView = new TextView(this);
        textView.setText("Name " + rowCount);
        row.addView(textView);

        Spinner spinner = new Spinner(this);
        row.addView(spinner);

        tableLayout.addView(row, rowCount++);
    }
}
