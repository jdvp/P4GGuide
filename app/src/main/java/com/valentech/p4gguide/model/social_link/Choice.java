package com.valentech.p4gguide.model.social_link;

import java.util.ArrayList;

/**
 * A single dialogue with options given by a social link character when ranking up
 *
 * Created by JD on 12/11/2016.
 */

public class Choice {
    private String dialogue;
    private String special;
    private boolean lovers;
    private ArrayList<Option> options;

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public boolean isLovers() {
        return lovers;
    }

    public void setLovers(boolean lovers) {
        this.lovers = lovers;
    }
}
