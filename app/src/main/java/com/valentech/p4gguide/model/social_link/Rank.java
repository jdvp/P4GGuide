package com.valentech.p4gguide.model.social_link;

import java.util.ArrayList;

/**
 * Contains information pertaining to one individual rank for one individual social link
 * Created by JD on 12/11/2016.
 */

public class Rank {
    private int level;
    private int points;
    private String results;
    private ArrayList<Choice> choices;
    private String special;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
