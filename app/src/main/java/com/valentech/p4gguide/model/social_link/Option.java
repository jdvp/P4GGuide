package com.valentech.p4gguide.model.social_link;

/**
 * A single option available to a user when replying in a social-link ranking episode
 * Created by JD on 12/11/2016.
 */
public class Option {
    private String response;
    private String with;
    private String without;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getWithout() {
        return without;
    }

    public void setWithout(String without) {
        this.without = without;
    }
}
