package com.example.stockpulse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private List<stockItem> stockItemList;
    private RecyclerView favRecyclerView;
    private stockAdapter stockAdapter;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
        stockItemList = generateStockItem();
        favRecyclerView = rootView.findViewById(R.id.favListLayout);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockAdapter = new stockAdapter(stockItemList);
        favRecyclerView.setAdapter(stockAdapter);
        return rootView;
    }

    public List<stockItem> generateStockItem() {
        List<stockItem> stockItemList = new ArrayList<>();
        stockItemList.add(new stockItem("AAPL", 195.71, 1.44));
        stockItemList.add(new stockItem("AMZN", 147.42, 0.54));
        stockItemList.add(new stockItem("GOOG",136.64,-1.81));
        return stockItemList;
    }
}