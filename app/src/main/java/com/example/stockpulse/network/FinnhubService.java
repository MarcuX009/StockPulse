package com.example.stockpulse.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FinnhubService {
    @GET("quote")
    Call<FinnhubAPIResponse> getStockData(@Query("symbol") String stockSymbol, @Query("token") String apiKey);
}
