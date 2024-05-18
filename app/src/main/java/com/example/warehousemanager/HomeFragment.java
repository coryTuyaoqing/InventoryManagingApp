package com.example.warehousemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
    RecyclerView recyclerHome;
    SwipeRefreshLayout swipeRefreshHome;
    OrderRecViewAdaptor adaptor;
    String url = DB.DB_URL + "get_highlighted_orders";
    private static final String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize recycler view
        recyclerHome = view.findViewById(R.id.recyclerHomeHighLight);
        adaptor = new OrderRecViewAdaptor(getContext());
        //get orders from the database
        recyclerHome.setAdapter(adaptor);
        recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshHome = view.findViewById(R.id.swipRefreshHome);
        swipeRefreshHome.setOnRefreshListener(() -> {
            adaptor.getOrdersFromDB(url);
            swipeRefreshHome.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        adaptor.getOrdersFromDB(url);
    }
}