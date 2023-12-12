package com.example.stockpulse.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockAPIHelper {
    public interface ResponseListener {
        void onYFResponse(StockObject response);
        void onFHResponse(StockObject fhResponse);
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
                        String t = "0";
//                        try{
//                            t = doc.select("fin-streamer[data-field=preMarketTime]").eq(1).attr("value");
//                        }catch (Exception e){
//                            Log.d("DEBUG_LOG", "No preMarketTime");
//                            t = "0";
//                        }
                        String v = doc.select("td[data-test='TD_VOLUME-value']").first().text().replace(",", "");
                        StockObject responseData = new StockObject(userInputStockSymbol, Double.parseDouble(c), Double.parseDouble(d), Double.parseDouble(dp), Double.parseDouble(h), Double.parseDouble(l), Double.parseDouble(o), Double.parseDouble(pc), Long.parseLong(t), Integer.parseInt(v));
                        if (responseData.checkStockIsValaid()){
                            Log.d("DEBUG_LOG", "Testing API call from YF:\n" + responseData.toJSONObject_all());
                            listener.onYFResponse(responseData);
                        } else {
                            Log.d("DEBUG_LOG", "Stock is invalid");
                            listener.onFailure(new Throwable("Stock is invalid"));
                        }
                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error in YFAPI call: " + e.getMessage());
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
        Call<StockObject> FH_call = FH_service.getStockData(userInputStockSymbol, "clmkkrhr01qjj8i8joi0clmkkrhr01qjj8i8joig");
        FH_call.enqueue(new Callback<StockObject>() {
            @Override
            public void onResponse(@NonNull Call<StockObject> call, @NonNull Response<StockObject> response) {
                if (response.isSuccessful()) {
                    try {
                        StockObject responseData = response.body();
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
            public void onFailure(@NonNull Call<StockObject> call, @NonNull Throwable t) {
                Log.d("DEBUG_LOG", "API request failed: " + t.getMessage());
                listener.onFailure(t);
            }
        });
    }
}
