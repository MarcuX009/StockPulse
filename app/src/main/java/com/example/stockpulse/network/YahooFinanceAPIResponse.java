package com.example.stockpulse.network;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class YahooFinanceAPIResponse implements Serializable {
    private String stockSymbol;
    private double c; // 收盘价 close
    private double d; // 涨跌额 difference
    private double dp; // 涨跌幅 difference percent
    private double h; // 最高价 high
    private double l; // 最低价 low
    private double o; // 开盘价 open
    private double pc; // 昨日收盘价 previous close
    private int v; // 成交量 volume

    public YahooFinanceAPIResponse(String stockSymbol, double c, double d, double dp, double h, double l, double o, double pc, int v) {
        this.stockSymbol = stockSymbol;
        this.c = c;
        this.d = d;
        this.dp = dp;
        this.h = h;
        this.l = l;
        this.o = o;
        this.pc = pc;
        this.v = v;
    }
    public String getStockSymbol() {return stockSymbol;}
    public double getC() {return c;}
    public double getD() {return d;}
    public double getDp() {return dp;}
    public double getH() {return h;}
    public double getL() {return l;}
    public double getO() {return o;}
    public double getPc() {return pc;}
    public int getV() {return v;}

    public void setStockSymbol(String stockSymbol) {this.stockSymbol = stockSymbol;}
    public void setC(double c) {this.c = c;}
    public void setD(double d) {this.d = d;}
    public void setDp(double dp) {this.dp = dp;}
    public void setH(double h) {this.h = h;}
    public void setL(double l) {this.l = l;}
    public void setO(double o) {this.o = o;}
    public void setPc(double pc) {this.pc = pc;}
    public void setV(int v) {this.v = v;}

    @NonNull
    @Override
    public String toString() {
        return "{stock symbol: " + stockSymbol + ", "
                + "close: " + c + ", "
                + "difference: " + d + ", "
                + "difference percent: " + dp + ", "
                + "high: " + h + ", "
                + "low: " + l + ", "
                + "open: " + o + ", "
                + "previous close: " + pc + ", "
                + "volume: " + v + "}";
    }
}
