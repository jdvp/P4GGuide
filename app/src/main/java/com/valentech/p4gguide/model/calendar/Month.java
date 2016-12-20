package com.valentech.p4gguide.model.calendar;

import java.util.HashMap;

/**
 * Created by JD on 12/18/2016.
 */

public enum Month{
    APRIL(4, 11, 30),
    MAY(5, 1, 31),
    JUNE(6, 1, 30),
    JULY(7, 1, 31),
    AUGUST(8, 1, 31),
    SEPTEMBER(9, 1, 30),
    OCTOBER(10, 1, 31),
    NOVEMBER(11, 1, 30),
    DECEMBER(12, 1, 31),
    JANUARY(13, 1, 31),
    FEBRUARY(14, 1, 16),
    MARCH(15, 20, 20);

    private static HashMap<Integer, Month> numToMonth;
    private final int number;
    private final int start;
    private final int end;

    public int getNumber() {
        return number;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    static {
        for(Month month : Month.values()){
            numToMonth.put(month.getNumber(), month);
        }
    }
    Month(int number, int start, int end) {
        this.number = number;
        this.start = start;
        this.end = end;
    }

    public static Day getNextDay(Day day) {
        Month month = day.getMonth();
        int dayNum = day.getDay();
        if(dayNum == month.getEnd() && month != MARCH) {
            month = numToMonth.get(month.getNumber() + 1);
            dayNum = month.getStart();
            return new Day(month, dayNum);
        } else if(month != MARCH){
            dayNum = dayNum + 1;
            return new Day(month, dayNum);
        }
        return null;
    }
}