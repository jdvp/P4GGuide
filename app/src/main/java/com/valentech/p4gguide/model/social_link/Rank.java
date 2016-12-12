package com.valentech.p4gguide.model.social_link;

import java.util.ArrayList;

/**
 * Created by JD on 12/11/2016.
 */

public class Rank {
    private int level;
    private int points;
    private String skill;
    private ArrayList<Choice> choices;

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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }
}