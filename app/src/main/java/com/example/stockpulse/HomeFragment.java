package com.example.stockpulse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stockpulse.network.RetrofitInstance;
import com.example.stockpulse.network.YahooFinanceService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

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

        YahooFinanceService service = RetrofitInstance.getRetrofitInstance().create(YahooFinanceService.class);
        Call<String> call = service.getStockData("AAPL");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String htmlContent = response.body();

                    assert htmlContent != null;
                    // Log.d("DEBUG_LOG", htmlContent);
                    Document doc = Jsoup.parse(htmlContent);

                    Element priceElement = doc.selectFirst("fin-streamer[data-field=regularMarketPrice]");
                    String priceValue = priceElement.attr("value");
                    Log.d("DEBUG_LOG", priceValue);
                    // this verison of code will print the price of the Apple in the logcat
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}