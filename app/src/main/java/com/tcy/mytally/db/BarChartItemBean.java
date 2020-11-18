package com.tcy.mytally.db;

/**
 * 用于描述绘制柱状图，每一个柱子表示的对象
 */
public class BarChartItemBean {
    int year;
    int month;
    int day;
    float sumMoney;

    public BarChartItemBean(int year, int month, int day, float sumMoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sumMoney = sumMoney;
    }

    public BarChartItemBean() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(float sumMoney) {
        this.sumMoney = sumMoney;
    }
}
