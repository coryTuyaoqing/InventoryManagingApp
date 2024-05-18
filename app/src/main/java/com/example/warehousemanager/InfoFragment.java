package com.example.warehousemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfoFragment extends Fragment {
    private RecyclerView recyclerInfoRecentOrder;
    private SwipeRefreshLayout swipeRefreshInfo;
    private RecordRecViewAdapter adapter;
    private ArrayList<Record> records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        recyclerInfoRecentOrder = view.findViewById(R.id.recyclerInfoRecentOrder);
        swipeRefreshInfo = view.findViewById(R.id.swipeRefreshInfo);
        records = new ArrayList<>();
        adapter = new RecordRecViewAdapter(getContext(), records);
        recyclerInfoRecentOrder.setAdapter(adapter);
        recyclerInfoRecentOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch records only if the fragment is attached to an activity
        if (isAdded()) {
            fetchRecordsFromDB();
        }

        swipeRefreshInfo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRecordsFromDB();
                swipeRefreshInfo.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRecordsFromDB();
    }

    private void fetchRecordsFromDB() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://studev.groept.be/api/a23PT308/get_info_HandleRecords";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray responseArray = new JSONArray(response.body().string());
                        records.clear();
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject recordObject = responseArray.getJSONObject(i);
                            int idRecord = recordObject.getInt("idRecord");
                            int idStaff = recordObject.getInt("idStaff");
                            int idOrder = recordObject.getInt("idOrder");
                            int idArticle = recordObject.getInt("idArticle");
                            int articleNr = recordObject.getInt("articleNr");
                            String operationTime = recordObject.getString("operationTime");

                            Record record = new Record(idRecord, idStaff, idOrder, idArticle, articleNr, operationTime);
                            records.add(record);
                        }
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public void setRecords(int number) {
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.getOperationTimeAsDate().compareTo(o1.getOperationTimeAsDate());
            }
        });

        int size = Math.min(records.size(), number);
        ArrayList<Record> recentRecords = new ArrayList<>(records.subList(0, size));

        this.records = recentRecords;
    }
}
