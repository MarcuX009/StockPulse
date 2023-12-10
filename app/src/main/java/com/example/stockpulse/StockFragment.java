package com.example.stockpulse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.YahooFinanceAPIResponse;


public class StockFragment extends Fragment {
    private static final String ARG_SATELLITES = "satellites";
    private YahooFinanceAPIResponse yf_Info;
    private FinnhubAPIResponse fh_Info;
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

    public StockFragment() {
        // Required empty public constructor
    }
    public static StockFragment newInstance(YahooFinanceAPIResponse yfStockInfo, FinnhubAPIResponse fhStockInfo) {
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
            if (getArguments().getSerializable(ARG_SATELLITES) instanceof YahooFinanceAPIResponse) {
                yf_Info = (YahooFinanceAPIResponse) getArguments().getSerializable(ARG_SATELLITES);
                Log.d("DEBUG_LOG", "StockFragment onCreate:\n" + yf_Info);
            } else if (getArguments().getSerializable(ARG_SATELLITES) instanceof FinnhubAPIResponse) {
                fh_Info = (FinnhubAPIResponse) getArguments().getSerializable(ARG_SATELLITES);
                Log.d("DEBUG_LOG", "StockFragment onCreate:\n" + fh_Info);
            }
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
        return view;
    }
}