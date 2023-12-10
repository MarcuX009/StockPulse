package com.example.stockpulse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import com.example.stockpulse.network.FinnhubService;
import com.example.stockpulse.network.RetrofitInstance;
import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.YahooFinanceService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private View view;
    private ListView stockListViewUI;
    private EditText userInputUI;
    private Button searchButtonUI;
    String sampleData[] = {"data","data","data","data","data","data","data","data","data","data"};
    ArrayAdapter<String> arrayAdapter;

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
        stockListViewUI = view.findViewById(R.id.stockListLayout);
        userInputUI = view.findViewById(R.id.searchBarLayout);
        searchButtonUI = view.findViewById(R.id.searchButtonLayout);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sampleData);
        stockListViewUI.setAdapter(arrayAdapter);

//        stockListViewUI.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView temp = ((TextView) view);
//                temp.setText(sampleData[position]);
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });

        YahooFinanceService YF_service = RetrofitInstance.getYahooFinanceRetrofitInstance().create(YahooFinanceService.class);
        Call<String> YF_call = YF_service.getStockData("AAPL");
        YF_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String htmlContent = response.body();
                    assert htmlContent != null;
                    // Log.d("DEBUG_LOG", htmlContent);
                    Document doc = Jsoup.parse(htmlContent);
                    try {
                        Element priceElement = doc.selectFirst("fin-streamer[data-field=regularMarketPrice]");
                        String close = priceElement.attr("value");
                        Log.d("DEBUG_LOG", "Testing API call from YF -- close: " +close);
                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error: " + e.getMessage());
                    }
                    // this version of code will print the price of the Apple in the logcat
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("DEBUG_LOG", "API request failed: " + t.getMessage());
            }
        });

        FinnhubService FH_service = RetrofitInstance.getFinnhubRetrofitInstance().create(FinnhubService.class);
        Call<FinnhubAPIResponse> FH_call = FH_service.getStockData("AAPL", "clmkkrhr01qjj8i8joi0clmkkrhr01qjj8i8joig");
        FH_call.enqueue(new Callback<FinnhubAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<FinnhubAPIResponse> call, @NonNull Response<FinnhubAPIResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        FinnhubAPIResponse responseData = response.body();
                        assert responseData != null;
                        double close = responseData.getC();
                        Log.d("DEBUG_LOG", "Testing API call from FH -- close: " + close);
                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<FinnhubAPIResponse> call, @NonNull Throwable t) {
                Log.d("DEBUG_LOG", "API request failed: " + t.getMessage());
            }
           
        });


        // Inflate the layout for this fragment

        return view;
    }


}