package com.example.warehousemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.client.android.Intents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddArticleDialogFragment extends DialogFragment {
    Order order;
    Article article;
    int inStockNr, requiredNr;
    Spinner spinnerAddArticleDialog;
    Button btnAddArticleDialog;
    EditText edtTxtAddArticleDialog;
    private static final String TAG = "AddArticleDialogFragment";

    public AddArticleDialogFragment(Order order, Article article) {
        this.order = order;
        this.article = article;
        Order.ArticleNr nr = order.getArticlesNrMap().get(article);
        assert nr != null;
        inStockNr = nr.getInStockNr();
        requiredNr = nr.getRequiredNr();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_article_dialog, container, false);

        spinnerAddArticleDialog = view.findViewById(R.id.spinnerAddArticleDialog);
        edtTxtAddArticleDialog = view.findViewById(R.id.edtTxtAddArticleDialog);
        btnAddArticleDialog = view.findViewById(R.id.btnAddArticleDialog);

        ArrayList<Integer> articleNrs = new ArrayList<>();
        for(int i=0; i<40; i++){
            articleNrs.add(i);
        }

        ArrayAdapter<Integer> articleNrAdaptor = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                articleNrs
        );

        spinnerAddArticleDialog.setAdapter(articleNrAdaptor);

        spinnerAddArticleDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    addArticleToOrder(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddArticleDialog.setOnClickListener(v -> {
            int addNr = Integer.parseInt(edtTxtAddArticleDialog.getText().toString());
            addArticleToOrder(addNr);
        });


        return view;
    }

    private void addArticleToOrder(int addNr) {
        Log.d(TAG, "addArticleToOrder: ");
        //check if in stock number exceed required number
        if(inStockNr + addNr > requiredNr){
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "only " + (requiredNr - inStockNr) + "more can be added", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        int orderID = order.getOrderID();
        int articleID = article.getIdArticle();
        String url = DBConst.DB_URL + "add_in_stock_number/" + addNr + "/" + orderID + "/" + articleID;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "add successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
                requireActivity().finish();
            }
        });
    }
}