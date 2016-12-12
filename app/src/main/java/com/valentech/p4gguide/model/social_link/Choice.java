package com.valentech.p4gguide.model.social_link;

import java.util.ArrayList;

/**
 * Created by JD on 12/11/2016.
 */

public class Choice {
    private String dialogue;
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
}
