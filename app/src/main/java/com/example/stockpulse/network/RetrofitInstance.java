package com.example.stockpulse.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitInstance {
    // using a retrofit instance to get the stock data from the yahoo finance, finnhub
    private static final String YF_API_BASE_URL = "https://finance.yahoo.com/";
    private static final String FH_API_BASE_URL = "https://finnhub.io/api/v1/";
    
    // ScalarsConverterFactory: A Converter.Factory for converting an HTTP response body to a String.
    // GsonConverterFactory: A Converter.Factory for converting Finnhub's JSON response to a Java object.
    private static Retrofit YF_retrofit = new Retrofit.Builder()
            .baseUrl(YF_API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    private static Retrofit FH_retrofit = new Retrofit.Builder()
            .baseUrl(FH_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit getYahooFinanceRetrofitInstance() {return YF_retrofit;}

    public static Retrofit getFinnhubRetrofitInstance() {return FH_retrofit;}
}
