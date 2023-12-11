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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.StockAPIHelper;
import com.example.stockpulse.network.YahooFinanceAPIResponse;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private static final String PREFS_NAME = "StockPulse_Prefs";
    private View view;
    private EditText userInputUI;
    private Button searchButtonUI;
    private ToggleButton toggleButtonUI;
    private boolean isYahoo = false;
    private RecyclerView homeListView;
    private stockAdapter stockAdapter;

    private List<YahooFinanceAPIResponse> stockItemList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        userInputUI = view.findViewById(R.id.searchBarLayout);
        searchButtonUI = view.findViewById(R.id.searchButtonLayout);
        toggleButtonUI = view.findViewById(R.id.toggleButtonLayout);

        homeListView = view.findViewById(R.id.homeListLayout);
        homeListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockItemList = generateStockItem();
        stockAdapter = new stockAdapter(stockItemList, this);
        homeListView.setAdapter(stockAdapter);


        toggleButtonUI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isYahoo = isChecked;
                Log.d("DEBUG_LOG", "isYahoo: " + isYahoo);
            }
        });

        searchButtonUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = userInputUI.getText().toString().toUpperCase();
                if (userInput.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a stock symbol", Toast.LENGTH_SHORT).show();
                } else {
                    if (isYahoo) {
                        StockAPIHelper.YFAPICall(userInput, new StockAPIHelper.ResponseListener() {
                            @Override
                            public void onYFResponse(YahooFinanceAPIResponse responseData) {
                                if (getActivity() != null) {
                                    StockFragment fragment = StockFragment.newInstance(responseData, null);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                            }
                            @Override
                            public void onFHResponse(FinnhubAPIResponse fhResponse) {
                                // this should never be called, but it has to be implemented here to satisfy the interface
                            }
                        });
                    } else {
                        StockAPIHelper.FHAPICall(userInput, new StockAPIHelper.ResponseListener() {
                            @Override
                            public void onFHResponse(FinnhubAPIResponse responseData) {
                                if (getActivity() != null) {
                                    StockFragment fragment = StockFragment.newInstance(null, responseData);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getContext(), "Error code: FHAPIFAIL", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                            }
                            @Override
                            public void onYFResponse(YahooFinanceAPIResponse yfResponse) {
                                // this should never be called, but it has to be implemented here to satisfy the interface
                            }
                        });
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
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