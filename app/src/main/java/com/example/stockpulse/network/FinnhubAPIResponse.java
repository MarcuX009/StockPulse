package com.example.stockpulse.network;

public class FinnhubAPIResponse {
    private double c; // 收盘价 close
    private double d; // 涨跌额 difference
    private double dp; // 涨跌幅 difference percent
    private double h; // 最高价 high
    private double l; // 最低价 low
    private double o; // 开盘价 open
    private double pc; // 昨日收盘价 previous close
    private long t; // 时间戳 timestamp
        
    public FinnhubAPIResponse(double c, double d, double dp, double h, double l, double o, double pc, long t) {
        this.c = c;
        this.d = d;
        this.dp = dp;
        this.h = h;
        this.l = l;
        this.o = o;
        this.pc = pc;
        this.t = t;
    }

    public double getC() {return c;}
    public double getD() {return d;}
    public double getDp() {return dp;}
    public double getH() {return h;}
    public double getL() {return l;}
    public double getO() {return o;}
    public double getPc() {return pc;}
    public long getT() {return t;}

    public void setC(double c) {this.c = c;}
    public void setD(double d) {this.d = d;}
    public void setDp(double dp) {this.dp = dp;}   
    public void setH(double h) {this.h = h;}
    public void setL(double l) {this.l = l;}
    public void setO(double o) {this.o = o;}
    public void setPc(double pc) {this.pc = pc;}
    public void setT(long t) {this.t = t;}
}