package com.example.stockpulse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.StockObject;


import java.util.HashSet;
import java.util.Set;


public class StockFragment extends Fragment {
    private static final String PREFS_NAME = "StockPulse_Prefs";
    private static final String ARG_SATELLITES = "STOCK";
    private StockObject yf_Info;
    private StockObject fh_Info;
    private View view;
    private TextView stockSymbol_TextView;
    private TextView c_TextView;
    private TextView d_TextView;
    private TextView dp_TextView;
    private TextView h_TextView;
    private TextView l_TextView;
    private TextView o_TextView;
    private TextView pc_TextView;
    private TextView v_TextView;
    private Button saveButton;
    private Button shareButton;

    public StockFragment() {
        // Required empty public constructor
    }

    public static StockFragment newInstance(StockObject yfStockInfo, StockObject fhStockInfo) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        if (yfStockInfo != null) {
            args.putSerializable(ARG_SATELLITES, yfStockInfo);
        } else if (fhStockInfo != null) {
            args.putSerializable(ARG_SATELLITES, fhStockInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG_LOG", "StockFragment onCreate started...");
        if (getArguments() != null) {
            if (getArguments().getSerializable(ARG_SATELLITES) instanceof StockObject) {
                yf_Info = (StockObject) getArguments().getSerializable(ARG_SATELLITES);
                Log.d("DEBUG_LOG", "StockFragment onCreate:\n" + yf_Info);
            } else if (getArguments().getSerializable(ARG_SATELLITES) instanceof FinnhubAPIResponse) {
                fh_Info = (StockObject) getArguments().getSerializable(ARG_SATELLITES);
                Log.d("DEBUG_LOG", "StockFragment onCreate:\n" + fh_Info);
            }
        } else {
            Log.d("DEBUG_LOG", "StockFragment onCreate: getArguments() is null");
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
        view = inflater.inflate(R.layout.fragment_stock, container, false);
        stockSymbol_TextView = view.findViewById(R.id.stockNameValueLayout);
        c_TextView = view.findViewById(R.id.closeValueLayout);
        d_TextView = view.findViewById(R.id.differenceValueLayout);
        dp_TextView = view.findViewById(R.id.differencePercentValueLayout);
        h_TextView = view.findViewById(R.id.highValueLayout);
        l_TextView = view.findViewById(R.id.lowValueLayout);
        o_TextView = view.findViewById(R.id.openValueLayout);
        pc_TextView = view.findViewById(R.id.previousCloseValueLayout);
        v_TextView = view.findViewById(R.id.volumeValueLayout);
        saveButton = view.findViewById(R.id.saveButtonLayout);
        shareButton = view.findViewById(R.id.shareButtonLayout);
        if (yf_Info != null) {
            stockSymbol_TextView.setText(yf_Info.getStockSymbol());
            c_TextView.setText(String.valueOf(yf_Info.getC()));
            d_TextView.setText(String.valueOf(yf_Info.getD()));
            dp_TextView.setText(String.valueOf(yf_Info.getDp()));
            h_TextView.setText(String.valueOf(yf_Info.getH()));
            l_TextView.setText(String.valueOf(yf_Info.getL()));
            o_TextView.setText(String.valueOf(yf_Info.getO()));
            pc_TextView.setText(String.valueOf(yf_Info.getPc()));
            v_TextView.setText(String.valueOf(yf_Info.getV()));
        } else if (fh_Info != null) {
            stockSymbol_TextView.setText(fh_Info.getStockSymbol());
            c_TextView.setText(String.valueOf(fh_Info.getC()));
            d_TextView.setText(String.valueOf(fh_Info.getD()));
            dp_TextView.setText(String.valueOf(fh_Info.getDp()));
            h_TextView.setText(String.valueOf(fh_Info.getH()));
            l_TextView.setText(String.valueOf(fh_Info.getL()));
            o_TextView.setText(String.valueOf(fh_Info.getO()));
            pc_TextView.setText(String.valueOf(fh_Info.getPc()));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG_LOG", "Save button clicked");
                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> favourites = new HashSet<>(sharedPreferences.getStringSet("FavouritesList", new HashSet<>()));
                    if (yf_Info != null) {
                        favourites.add(yf_Info.getStockSymbol());
                        editor.putString(yf_Info.getStockSymbol(), yf_Info.toJSONObject_cd());
                    } else if (fh_Info != null) {
                        favourites.add(fh_Info.getStockSymbol());
                        editor.putString(fh_Info.getStockSymbol(), fh_Info.toJSONObject_cd());
                    }
                    editor.putStringSet("FavouritesList", favourites);
                    editor.apply();
                    Set<String> favouritesSet = sharedPreferences.getStringSet("FavouritesList", new HashSet<>());
                    Log.d("DEBUG_LOG", "FavouritesList: " + favouritesSet);
                    for (String favourite : favouritesSet) {
                        Log.d("DEBUG_LOG", "Favourite: " + favourite);
                        Log.d("DEBUG_LOG", "Info:" + sharedPreferences.getString(favourite, ""));
                    }
                } catch (Exception e) {
                    Log.d("DEBUG_LOG", "Error: " + e.getMessage());
                }
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG_LOG", "Share button clicked");
            }
        });

        return view;
    }
}