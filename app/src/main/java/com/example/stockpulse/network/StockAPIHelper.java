package com.example.stockpulse.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                    String c = "0";
                    String d = "0";
                    String dp = "0";
                    String h = "0";
                    String l = "0";
                    String o = "0";
                    String pc = "0";
                    String t = "0";
                    String v = "0";
                    int v_int;
                    try {
                        Element element = doc.selectFirst("fin-streamer[data-field=regularMarketPrice]");
                        c = element != null ? element.attr("value").replaceAll(",", "") : "0";
                        element = doc.selectFirst("fin-streamer[data-field=regularMarketChange]");
                        d = element != null ? element.attr("value").replaceAll(",", "") : "0";
                        d = String.format("%.3f", Double.parseDouble(d)); // only need to 3rd decimal place, yh give tooooo detailed XD
                        element = doc.selectFirst("fin-streamer[data-field=regularMarketChangePercent]");
                        dp = element != null ? element.attr("value").replaceAll(",", "") : "0";
                        dp = String.format("%.4f", Double.parseDouble(dp)); // only need to 4th decimal place, yh give tooooo detailed XD
                        element = doc.selectFirst("td[data-test='DAYS_RANGE-value']");
                        String[] daysRangeValues = element != null ? element.text().split(" - ") : new String[]{"0", "0"};
                        h = daysRangeValues[1].replaceAll(",", "");
                        l = daysRangeValues[0].replaceAll(",", "");
                        element = doc.selectFirst("td[data-test='OPEN-value']");
                        o = element != null ? element.text().replaceAll(",", "") : "0";
                        element = doc.selectFirst("td[data-test='PREV_CLOSE-value']");
                        pc = element != null ? element.text().replaceAll(",", "") : "0";
                        Elements elements = doc.select("fin-streamer[data-field=preMarketTime]").eq(1); // want the second one here
                        if (elements.size() > 0 && !elements.attr("value").isEmpty()) {
                            t = elements.attr("value");
                        }
                        element = doc.selectFirst("td[data-test='TD_VOLUME-value']");
                        v = element != null ? element.text().replace(",", "").replaceAll(",", "") : "0";
                        v_int = Integer.parseInt(element != null ? element.text().replace(",", "").replaceAll(",", "") : "0");
                        StockObject responseData = new StockObject(userInputStockSymbol, Double.parseDouble(c), Double.parseDouble(d), Double.parseDouble(dp), Double.parseDouble(h), Double.parseDouble(l), Double.parseDouble(o), Double.parseDouble(pc), Long.parseLong(t), v_int);
                        if (responseData.checkStockIsValaid()) {
                            Log.d("DEBUG_LOG", "Testing API call from YF:\n" + responseData.toJSONObject_all());
                            listener.onYFResponse(responseData);
                        } else {
                            Log.d("DEBUG_LOG", "Stock is invalid");
                            listener.onFailure(new Throwable("Stock is invalid"));
                        }
                    } catch (Exception e) {
                        Log.d("DEBUG_LOG", "Error in YFAPI call: " + e.getMessage());
                        Log.d("DEBUG_LOG", "Error in YFAPI call2: " + c + "|" + d + "|" + dp + "|" + h + "|" + l + "|" + o + "|" + pc + "|" + t + "|" + v);
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
