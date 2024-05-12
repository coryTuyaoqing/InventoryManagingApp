package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    private Spinner typeSpinner;
    private Button searchButton;
    private List<CheckBox> filterCheckBoxes = new ArrayList<>();
    private List<EditText> filterEditText = new ArrayList<>();
    private ArrayList<String> SelectedFilter = new ArrayList<>();
    private ArrayList<String> FilterValues = new ArrayList<>();
    private static final String BASE_URL = "https://studev.groept.be/api/a23PT308/";
    private static final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        typeSpinner = view.findViewById(R.id.Type);
        searchButton = view.findViewById(R.id.Search_Button);

        setupSpinner(typeSpinner, R.array.types_array);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearFilters();

                String[] filterOptions;
                if (position == 0) {
                    filterOptions = getResources().getStringArray(R.array.filter_order);
                } else {
                    filterOptions = getResources().getStringArray(R.array.filter_article);
                }
                setupFilterOptions(filterOptions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                setSelectedFilterList();
                setSelectedFilterList();
                if (setFilterValueList()) {
                    performSearch(type);
                    Toast.makeText(getContext(), "Search in progress", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(arrayResource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupFilterOptions(String[] filterOptions) {
        LinearLayout filterOptionsLayout = getView().findViewById(R.id.FIlters);
        LinearLayout keywordInputsLayout = getView().findViewById(R.id.KeywordInputs);

        for (String filter : filterOptions) {
            EditText editText = new EditText(getContext());
            editText.setHint("Input " + filter);
            editText.setTag(filter);
            editText.setVisibility(View.GONE);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && ((EditText) v).getText().toString().equals("Input " + filter)) {
                        ((EditText) v).setText("");
                    }
                }
            });
            keywordInputsLayout.addView(editText);
            filterEditText.add(editText);

            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(filter);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    EditText correspondingEditText = getView().findViewById(R.id.KeywordInputs).findViewWithTag(filter);
                    if (correspondingEditText != null) {
                        correspondingEditText.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                }
            });
            filterOptionsLayout.addView(checkBox);
            filterCheckBoxes.add(checkBox);

            if (filter.equals("ID")) {
                checkBox.setChecked(true);
                editText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearFilters() {
        LinearLayout filterOptionsLayout = requireView().findViewById(R.id.FIlters);
        LinearLayout keywordInputsLayout = requireView().findViewById(R.id.KeywordInputs);

        filterOptionsLayout.removeAllViews();
        keywordInputsLayout.removeAllViews();

        filterCheckBoxes.clear();
        filterEditText.clear();
        SelectedFilter.clear();
        FilterValues.clear();
    }

    private void setSelectedFilterList() {
        SelectedFilter.clear();
        for (CheckBox checkBox : filterCheckBoxes) {
            if (checkBox.isChecked()) {
                SelectedFilter.add("1");
            } else {
                SelectedFilter.add("0");
            }
        }
    }

    private boolean setFilterValueList() {
        FilterValues.clear();
        for (int i = 0; i < filterCheckBoxes.size(); i++) {
            CheckBox checkBox = filterCheckBoxes.get(i);
            EditText input = filterEditText.get(i);
            if (checkBox.isChecked() && input.getVisibility() == View.VISIBLE) {
                String value = input.getText().toString().trim();
                if (value.isEmpty()) {
                    showToast("Fill in all fields");
                    return false;
                }
                FilterValues.add(value);
            } else {
                FilterValues.add("0");
            }
        }
        return true;
    }

    private void performSearch(String type) {
        StringBuilder endpointBuilder = new StringBuilder();

        if ("Order".equals(type)) {
            endpointBuilder.append("Order_filter/");
        } else if ("Article".equals(type)) {
            endpointBuilder.append("Article_filter/");
        } else {
            showToast("Invalid search type");
            return;
        }

        for (int i = 0; i < SelectedFilter.size(); i++) {
            String filter = SelectedFilter.get(i);
            String value = FilterValues.get(i);

            endpointBuilder.append(filter).append("/").append(value).append("/");
        }

        if (endpointBuilder.charAt(endpointBuilder.length() - 1) == '/') {
            endpointBuilder.deleteCharAt(endpointBuilder.length() - 1);
        }

        String endpoint = endpointBuilder.toString();
        String url = BASE_URL + endpoint;
        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                showToast("Failed to fetch data");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String responseData = response.body().string();
                        final JSONArray jsonArray = new JSONArray(responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                navigateToSearchResults(jsonArray);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Failed to parse data");
                    }
                } else {
                    showToast("Failed to fetch data");
                }
            }
        });
    }

    private void navigateToSearchResults(JSONArray jsonArray) {
        Intent intent = new Intent(getContext(), SearchResultsActivity.class);
        intent.putExtra("searchResults", jsonArray.toString());
        intent.putExtra("filter", SelectedFilter);
        intent.putExtra("keyword", FilterValues);
        intent.putExtra("searchtype", typeSpinner.getSelectedItem().toString());
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
