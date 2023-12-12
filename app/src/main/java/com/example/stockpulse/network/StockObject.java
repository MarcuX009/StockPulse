package com.example.stockpulse.network;

import java.io.Serializable;

public class StockObject implements Serializable{
    private String stockSymbol = null;
    private double c; // close
    private double d; // difference
    private double dp; // difference percent
    private double h; // high
    private double l; // low
    private double o; // open
    private double pc; // previous close
    private long t; // timestamp
    private int v; // volume

    public StockObject(){
        //empty constructor
    }

    public StockObject (String stockSymbol, double c, double d) {
        this.stockSymbol = stockSymbol;
        this.c = c;
        this.d = d;
    }

    public StockObject (String stockSymbol, double c, double d, double dp, double h, double l, double o, double pc, long t) {
        // for Finnhub API use
        this.stockSymbol = stockSymbol;
        this.c = c;
        this.d = d;
        this.dp = dp;
        this.h = h;
        this.l = l;
        this.o = o;
        this.pc = pc;
        this.t = t;
    }

    public StockObject(String stockSymbol, double c, double d, double dp, double h, double l, double o, double pc, long t, int v) {
        // for Yahoo Finance API use
        this.stockSymbol = stockSymbol;
        this.c = c;
        this.d = d;
        this.dp = dp;
        this.h = h;
        this.l = l;
        this.o = o;
        this.pc = pc;
        this.t = t;
        this.v = v;
    }

    public boolean checkStockIsValaid() {
        if (this.stockSymbol == null || this.c == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String toJSONObject_cd() {
        return "{c:"+c+",d:"+d+"}";
    }
    public String toJSONObject_all() {
        return "{c:"+c+",d:"+d+",dp:"+dp+",h:"+h+",l:"+l+",o:"+o+",pc:"+pc+",t:"+t+",v:"+v+"}";
    }

    public String getStockSymbol() {return stockSymbol;}
    public double getC() {return c;}
    public double getD() {return d;}
    public double getDp() {return dp;}
    public double getH() {return h;}
    public double getL() {return l;}
    public double getO() {return o;}
    public double getPc() {return pc;}
    public long getT() {return t;}
    public int getV() {return v;}

    public void setStockSymbol(String stockSymbol) {this.stockSymbol = stockSymbol;}
    public void setC(double c) {this.c = c;}
    public void setD(double d) {this.d = d;}
    public void setDp(double dp) {this.dp = dp;}
    public void setH(double h) {this.h = h;}
    public void setL(double l) {this.l = l;}
    public void setO(double o) {this.o = o;}
    public void setPc(double pc) {this.pc = pc;}
    public void setT(long t) {this.t = t;}
    public void setV(int v) {this.v = v;}
    
    // public StockObject(String stockSymbol, String stockDict) {
    //     // for Yahoo Finance API use
    //     this.stockSymbol = stockSymbol;
    //     stockDict = stockDict.substring(1, stockDict.length() - 1);
    //     String[] stockDictList = stockDict.split(",");
    //     for (String stockDictItem : stockDictList) {
    //         String[] stockDictItemPair = stockDictItem.split(":");
    //         // Log.d("DEBUG_LOG", "stockDictItemPair: " + stockDictItemPair[0] + "|" + stockDictItemPair[1]);
    //         switch (stockDictItemPair[0]) {
    //             case "c":
    //                 this.c = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "d":
    //                 this.d = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "dp":
    //                 this.dp = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "h":
    //                 this.h = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "l":
    //                 this.l = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "o":
    //                 this.o = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "pc":
    //                 this.pc = Double.parseDouble(stockDictItemPair[1]);
    //                 break;
    //             case "v":
    //                 this.v = Integer.parseInt(stockDictItemPair[1]);
    //                 break;
    //             default:
    //                 Log.d("DEBUG_LOG", "stockDictItemPair: " + stockDictItemPair[0] + "|" + stockDictItemPair[1]);
    //                 break;
    //         }
    //     }
    // }
}

