package com.example.stockpulse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StockFragment extends Fragment {
    private String stockInfo;
    public StockFragment() {
        // Required empty public constructor
    }
    public static StockFragment newInstance(String stockInfo) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString("stockInfo", stockInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG_LOG", "StockFragment onCreate started...");
        if (getArguments() != null) {
            stockInfo = getArguments().getString("stockInfo");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG_LOG", "StockFragment onDestroy started...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        Log.d("DEBUG_LOG", "StockFragment onCreateView:\n" + stockInfo);
//        TextView textViewStockInfo = view.findViewById(R.id.textViewStockInfo);
//        textViewStockInfo.setText(stockInfo);
        return view;
    }
}