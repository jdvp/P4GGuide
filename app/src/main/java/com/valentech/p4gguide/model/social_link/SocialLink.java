package com.valentech.p4gguide.model.social_link;

import java.util.ArrayList;

/**
 * Information pertaining to one social link character in the P4G game
 * Created by JD on 12/11/2016.
 */

public class SocialLink {
    private Availability availability;
    private ArrayList<Rank> rank;
    private String notes;
    private String title;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public ArrayList<Rank> getRank() {
        return rank;
    }

    public void setRank(ArrayList<Rank> rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
