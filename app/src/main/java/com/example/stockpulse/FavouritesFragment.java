package com.example.stockpulse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stockpulse.network.YahooFinanceAPIResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesFragment extends Fragment implements RecyclerViewInterface{
    private static final String PREFS_NAME = "StockPulse_Prefs";
    private List<YahooFinanceAPIResponse> stockItemList;
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
        stockAdapter = new stockAdapter(stockItemList, this);
        favRecyclerView.setAdapter(stockAdapter);
        return rootView;
    }

    public List<YahooFinanceAPIResponse> generateStockItem() {
        List<YahooFinanceAPIResponse> stockItemList = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Set<String> favouritesSet = sharedPreferences.getStringSet("FavouritesList", new HashSet<>());
        Log.d("DEBUG_LOG", "FavouritesList: " + favouritesSet);
        for (String favourite : favouritesSet) {
            Log.d("DEBUG_LOG", "Favourite: " + favourite);
            Log.d("DEBUG_LOG", "Info:" + sharedPreferences.getString(favourite,""));
            stockItemList.add(new YahooFinanceAPIResponse(favourite, sharedPreferences.getString(favourite,"")));
        }
        return stockItemList;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("favList","List number:"+position);
        Toast.makeText(getActivity(), "this is item number: " + position, Toast.LENGTH_SHORT).show();
    }
}