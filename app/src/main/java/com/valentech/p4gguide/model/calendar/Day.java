package com.valentech.p4gguide.model.calendar;
/**
 * Created by JD on 12/18/2016.
 */

public class Day {
    private Month month;
    private int day;

    public Day(Month month, int day) {
        this.month = month;
        this.day = day;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}