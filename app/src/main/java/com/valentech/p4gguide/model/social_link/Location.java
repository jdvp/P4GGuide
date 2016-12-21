package com.valentech.p4gguide.model.social_link;

/**
 * Location of a social link during certain times
 * Created by JD on 12/11/2016.
 */
public class Location {
    private String weekdays;
    private String sundays;

    public String getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }

    public String getSundays() {
        return sundays;
    }

    public void setSundays(String sundays) {
        this.sundays = sundays;
    }
}
