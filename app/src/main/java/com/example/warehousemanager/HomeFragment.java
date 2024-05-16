package com.example.warehousemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerHome;
    OrderRecViewAdaptor adaptor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize recycler view
        recyclerHome = view.findViewById(R.id.recyclerHomeHighLight);
        adaptor = new OrderRecViewAdaptor(getContext());
        //get orders from the database
        String url = DBConst.DB_URL + "get_highlighted_orders";
        adaptor.getOrdersFromDB(url);
        recyclerHome.setAdapter(adaptor);
        recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}