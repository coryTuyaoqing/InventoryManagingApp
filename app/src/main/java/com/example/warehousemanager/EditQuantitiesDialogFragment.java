package com.example.warehousemanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditQuantitiesDialogFragment extends DialogFragment {

    private static final String ARG_ARTICLE = "article";
    private static final String ARG_ORDER = "order";

    private Article article;
    private Order order;
    private Context context;
    private EditText RequiredEditText;
    private EditText InStockEditText;
    private static final OkHttpClient client = new OkHttpClient();

    public EditQuantitiesDialogFragment(Context context, Article article, Order order) {
        this.context = context;
        this.article = article;
        this.order = order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_quantities_dialog, container, false);
        RequiredEditText = view.findViewById(R.id.RequiredInput);
        InStockEditText = view.findViewById(R.id.InStockInput);
        Button saveButton = view.findViewById(R.id.save_button);
        fetchRequiredAndInStock(RequiredEditText, InStockEditText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                dismiss();
            }
        });

        return view;
    }
    private void fetchRequiredAndInStock(EditText requiredEditText, EditText inStockEditText) {
        String url = "https://studev.groept.be/api/a23PT308/Get_Required_InStock/";
        url = url + order.getOrderID() + "/" + article.getIdArticle();

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
                                try {
                                    System.out.println(jsonArray);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String required = jsonObject.getString("ArticleRequired");
                                    String inStock = jsonObject.getString("ArticlesInStock");
                                    requiredEditText.setText(required);
                                    inStockEditText.setText(inStock);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showToast("Failed to parse data");
                                }
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

    public void Save(){
        try {
            String requiredInput = RequiredEditText.getText().toString();
            String inStockInput = InStockEditText.getText().toString();

            String url = "https://studev.groept.be/api/a23PT308/Edit_ArticleInOrder/";
            url = url + requiredInput + "/" + inStockInput + "/" + order.getOrderID() + "/" + article.getIdArticle();
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
                        showToast("Data saved");
                    }
                    else {
                        showToast("Failed to save data");
                    }
                }
            });
        } catch (NumberFormatException e) {
            showToast("Invalid input. Please enter valid integers.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
