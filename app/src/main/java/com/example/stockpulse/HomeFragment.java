package com.example.stockpulse;

import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.stockpulse.network.StockAPIHelper;
import com.example.stockpulse.network.StockObject;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private static final String PREFS_NAME = "StockPulse_Prefs";
    private View view;
    private EditText userInputUI;
    private ImageButton searchButtonUI;
    private ToggleButton toggleButtonUI;
    private boolean isYahoo = false;
    private RecyclerView homeListView;
    private stockAdapter stockAdapter;
    private List<StockObject> stockItemList;

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
                            public void onYFResponse(StockObject responseData) {
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
                                Log.d("DEBUG_LOG", "Error in search button clicked: " + t.getMessage());
                            }

                            @Override
                            public void onFHResponse(StockObject fhResponse) {
                                // this should never be called, but it has to be implemented here to satisfy the interface
                            }
                        });
                    } else {
                        StockAPIHelper.FHAPICall(userInput, new StockAPIHelper.ResponseListener() {
                            @Override
                            public void onFHResponse(StockObject responseData) {
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
                            public void onYFResponse(StockObject yfResponse) {
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

    public List<StockObject> generateStockItem() {
        List<StockObject> stockItemList = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> AllStockList = new HashSet<>(sharedPreferences.getStringSet("AllStockList", new HashSet<>()));
        Log.d("DEBUG_LOG", "AllStockList in HomeFragment: " + AllStockList);
        // pick 10 random stocks from the list
        AtomicInteger completedRequests = new AtomicInteger(0); // using counter to keep track of how many requests have been completed
        int i = 0;
        while (i < 10) {
            int randomIndex = (int) (Math.random() * AllStockList.size());
            String randomStock = (String) AllStockList.toArray()[randomIndex];
            Log.d("DEBUG_LOG", "Random Stock: " + randomStock);
            if (!randomStock.equals("")) {
                try {
                    StockAPIHelper.YFAPICall(randomStock, new StockAPIHelper.ResponseListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onYFResponse(StockObject responseData) {
                            if (getActivity() != null) {
//                                getActivity().runOnUiThread(() -> {
                                    AllStockList.remove(responseData.getStockSymbol());
                                    stockItemList.add(responseData);
                                    if (completedRequests.incrementAndGet() == 10) {
                                        stockAdapter.notifyDataSetChanged();
                                    }
//                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                            Log.d("DEBUG_LOG", "Error in generateStockItem using YF: " + t.getMessage());
                        }

                        @Override
                        public void onFHResponse(StockObject fhResponse) {
                            // this should never be called, but it has to be implemented here to satisfy the interface
                        }
                    });
                    i++;
                } catch (Exception e) {
                    Log.d("DEBUG_LOG", "Error catched in generateStockItem: " + e.getMessage());
                }
            }
        }
        return stockItemList;
    }


    @Override
    public void onItemClick(int position) {
        Log.d("Home Frag", "List number:" + position);
        if (!stockItemList.isEmpty()) {
            StockObject element = stockItemList.get(position);
            String symbol = element.getStockSymbol();
            StockAPIHelper.YFAPICall(symbol, new StockAPIHelper.ResponseListener() {
                @Override
                public void onYFResponse(StockObject response) {
                    if (getActivity() != null) {
                        StockFragment fragment = StockFragment.newInstance(response, null);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }

                @Override
                public void onFHResponse(StockObject fhResponse) {
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getContext(), "Error code: YFAPIFAIL", Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG_LOG", "Error: " + t.getMessage());
                }
            });
        }

    }

    @Override
    public void onItemLongClick(int position) {
        Log.d("DEBUG_LOG", "Long Click detected :" + position);
    }
}