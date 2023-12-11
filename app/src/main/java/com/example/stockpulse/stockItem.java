package com.example.stockpulse;

public class stockItem {
    private String stockName;
    private double stockValue;
    private double stockPercent;

    public stockItem(String stockName, double stockValue, double stockPercent) {
        this.stockName = stockName;
        this.stockValue = stockValue;
        this.stockPercent = stockPercent;
    }

    public String getStockName() {
        return stockName;
    }

    public double getStockValue() {
        return stockValue;
    }

    public double getStockPercent() {
        return stockPercent;
    }


}
