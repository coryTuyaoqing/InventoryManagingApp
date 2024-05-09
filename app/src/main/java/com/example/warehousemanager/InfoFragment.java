package com.example.warehousemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    RecyclerView recyclerInfoRecentOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_info, container, false);

        recyclerInfoRecentOrder = view.findViewById(R.id.recyclerInfoRecentOrder);

        ArrayList<Order> orders = new ArrayList<>();
        OrderRecViewAdaptor adaptor = new OrderRecViewAdaptor(getContext(), orders);
        recyclerInfoRecentOrder.setAdapter(adaptor);
        recyclerInfoRecentOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        //get orders from the database
        String url = DBConst.DB_URL + "get_info_Orders";
        adaptor.getOrdersFromDB(url);

        return view;
    }
}