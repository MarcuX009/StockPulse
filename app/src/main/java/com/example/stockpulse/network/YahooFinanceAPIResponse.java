package com.example.stockpulse.network;

import android.util.Log;

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

    public YahooFinanceAPIResponse(String stockSymbol, String stockDict) {
        this.stockSymbol = stockSymbol;
        stockDict = stockDict.substring(1, stockDict.length() - 1);
        String[] stockDictList = stockDict.split(",");
        for (String stockDictItem : stockDictList) {
            String[] stockDictItemPair = stockDictItem.split(":");
            // Log.d("DEBUG_LOG", "stockDictItemPair: " + stockDictItemPair[0] + "|" + stockDictItemPair[1]);
            switch (stockDictItemPair[0]) {
                case "c":
                    this.c = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "d":
                    this.d = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "dp":
                    this.dp = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "h":
                    this.h = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "l":
                    this.l = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "o":
                    this.o = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "pc":
                    this.pc = Double.parseDouble(stockDictItemPair[1]);
                    break;
                case "v":
                    this.v = Integer.parseInt(stockDictItemPair[1]);
                    break;
                default:
                    Log.d("DEBUG_LOG", "stockDictItemPair: " + stockDictItemPair[0] + "|" + stockDictItemPair[1]);
                    break;
            }
        }
    }
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
        return "{c:"+c+",d:"+d+",dp:"+dp+",h:"+h+",l:"+l+",o:"+o+",pc:"+pc+",v:"+v+"}";
    }
}
