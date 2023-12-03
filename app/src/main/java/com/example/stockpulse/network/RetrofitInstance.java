package com.example.stockpulse.network;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitInstance {
    // using a retrofit instance to get the stock data from the yahoo finance
    private static final String BASE_URL = "https://finance.yahoo.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    public static Retrofit getRetrofitInstance() {
        return retrofit;
    }
}
