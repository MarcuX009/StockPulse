package com.example.stockpulse.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface YahooFinanceService {
    // using retrofit to get the stock data from the yahoo finance
    @GET("quote/{stockSymbol}")
    Call<String> getStockData(@Path("stockSymbol") String stockSymbol);
}
