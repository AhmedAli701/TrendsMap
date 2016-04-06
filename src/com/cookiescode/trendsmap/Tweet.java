package com.cookiescode.trendsmap;

import java.util.List;

/**
 * Created by Ahmed Ali on 04/04/2016.
 */
public class Tweet {
    private long id;
    private long user;
    private String text;
    private List<String> hashTags;
    private Location location;

    public Tweet(long id, long user, String text, List<String> hashTags, Location location) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.hashTags = hashTags;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public long getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public Location getLocation() {
        return location;
    }
}

