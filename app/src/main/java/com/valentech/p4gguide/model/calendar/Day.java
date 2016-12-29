package com.valentech.p4gguide.model.calendar;

/**
 * A single day item
 *
 * Created by JD on 12/18/2016.
 */
public class Day {
    private Month month;
    private int day;

    public Day(Month month, int day) {
        this.month = month;
        this.day = day;
    }

    Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public static Day fromString(String dateString){
        dateString = dateString.replaceAll(" ", "_");
        try {
            String[] parts = dateString.split("_");
            Month month = Month.getMonth(parts[0]);

            return new Day(month, Integer.parseInt(parts[1].replaceAll("[^\\d]", "")));
        } catch (Exception ignored) {
            return new Day(Month.APRIL, 11);
        }
    }

    @Override
    public String toString() {
        String suffix = "th";
        switch(day) {
            case 1:
            case 21:
            case 31:
                suffix = "st";
                break;
            case 2:
            case 22:
                suffix = "nd";
                break;
            case 3:
            case 23:
                suffix = "rd";
                break;
        }
        String monthString = month.toString().toLowerCase();

        return monthString + "_" + day + suffix;
    }
}