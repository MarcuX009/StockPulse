package com.example.stockpulse.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.stockpulse.network.FinnhubAPIResponse;
import com.example.stockpulse.network.FinnhubService;
import com.example.stockpulse.network.RetrofitInstance;
import com.example.stockpulse.network.YahooFinanceAPIResponse;
import com.example.stockpulse.network.YahooFinanceService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockAPIHelper {
    public interface ResponseListener {
        void onYFResponse(YahooFinanceAPIResponse response);
        void onFHResponse(FinnhubAPIResponse fhResponse);
        void onFailure(Throwable t);
    }

    public static void YFAPICall(String userInputStockSymbol, ResponseListener listener) {
        Log.d("DEBUG_LOG", "User Input in API Helper: " + userInputStockSymbol);
        YahooFinanceService YF_service = RetrofitInstance.getYahooFinanceRetrofitInstance().create(YahooFinanceService.class);
        Call<String> YF_call = YF_service.getStockData(userInputStockSymbol);
        YF_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String htmlContent = response.body();
                    assert htmlContent != null;
                    // Log.d("DEBUG_LOG", htmlContent);
                    Document doc = Jsoup.parse(htmlContent);
                    try {
                        String c = doc.selectFirst("fin-streamer[data-field=regularMarketPrice]").attr("value");
                        String d = doc.selectFirst("fin-streamer[data-field=regularMarketChange]").attr("value");
                        String dp = doc.selectFirst("fin-streamer[data-field=regularMarketChangePercent]").attr("value");
                        String[] daysRangeValues = doc.select("td[data-test='DAYS_RANGE-value']").first().text().split(" - ");
                        String h = daysRangeValues[1];
                        String l = daysRangeValues[0];
                        String o = doc.select("td[data-test='OPEN-value']").first().text();
                        String pc = doc.select("td[data-test='PREV_CLOSE-value']").first().text();
                        String v = doc.select("td[data-test='TD_VOLUME-value']").first().text().replace(",", "");
                        YahooFinanceAPIResponse responseData = new YahooFinanceAPIResponse(userInputStockSymbol, Double.parseDouble(c), Double.parseDouble(d), Double.parseDouble(dp), Double.parseDouble(h), Double.parseDouble(l), Double.parseDouble(o), Double.parseDouble(pc), Integer.parseInt(v));
                        Log.d("DEBUG_LOG", "Testing API call from YF:\n" + responseData.toString());
                        listener.onYFResponse(responseData);

                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error: " + e.getMessage());
                        listener.onFailure(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("DEBUG_LOG", "API request failed: " + t.getMessage());
                listener.onFailure(t);
            }
        });
    }

    public static void FHAPICall(String userInputStockSymbol, ResponseListener listener) {
        Log.d("DEBUG_LOG", "User Input in API Helper: " + userInputStockSymbol);
        FinnhubService FH_service = RetrofitInstance.getFinnhubRetrofitInstance().create(FinnhubService.class);
        Call<FinnhubAPIResponse> FH_call = FH_service.getStockData(userInputStockSymbol, "clmkkrhr01qjj8i8joi0clmkkrhr01qjj8i8joig");
        FH_call.enqueue(new Callback<FinnhubAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<FinnhubAPIResponse> call, @NonNull Response<FinnhubAPIResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        FinnhubAPIResponse responseData = response.body();
                        assert responseData != null;
                        responseData.setStockSymbol(userInputStockSymbol);
                        Log.d("DEBUG_LOG", "Testing API call from FH:\n" + responseData.toString());
                        listener.onFHResponse(responseData);
                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error: " + e.getMessage());
                        listener.onFailure(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FinnhubAPIResponse> call, @NonNull Throwable t) {
                Log.d("DEBUG_LOG", "API request failed: " + t.getMessage());
                listener.onFailure(t);
            }
        });
    }
}
