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

import com.example.stockpulse.network.StockObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesFragment extends Fragment implements RecyclerViewInterface {
    private static final String PREFS_NAME = "StockPulse_Prefs";
    private List<StockObject> stockItemList;
    private RecyclerView favRecyclerView;
    private stockAdapter stockAdapter;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG_LOG", "favr onCreate: ");
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
        Log.d("DEBUG_LOG", "favr onCreateView: ");
        return rootView;
    }

    public List<StockObject> generateStockItem() {
        List<StockObject> stockItemList = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Set<String> favouritesSet = sharedPreferences.getStringSet("FavouritesList", new HashSet<>());
        Log.d("DEBUG_LOG", "FavouritesList: " + favouritesSet);
        for (String favourite : favouritesSet) {
            Log.d("DEBUG_LOG", "Favourite: " + favourite);
            Log.d("DEBUG_LOG", "Info:" + sharedPreferences.getString(favourite, ""));
            // split the sharedPreferences.getString(favourite,"")
             String[] stockDictList = sharedPreferences.getString(favourite, "").split(",");
             StockObject stockObject = new StockObject();
             stockObject.setStockSymbol(favourite);
             stockObject.setC(Double.parseDouble(stockDictList[0].split(":")[1]));
             stockObject.setD(Double.parseDouble(stockDictList[1].split(":")[1]));
             stockItemList.add(stockObject);
        }
        return stockItemList;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("favList", "List number:" + position);
        Toast.makeText(getActivity(), "this is item number: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {
        stockItemList.remove(position);
        stockAdapter.notifyItemRemoved(position);
        Toast.makeText(getActivity(), "Long Click detected stock removed", Toast.LENGTH_SHORT).show();
    }

}