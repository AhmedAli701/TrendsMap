package com.cookiescode.trendsmap;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Ahmed Ali on 04/04/2016.
 */
public class Tweet implements Comparable<Tweet> {
    private long id;
    private long user;
    private List<String> hashTags;
    private Location location;
    private DateTime date;


    public Tweet(long id, long user, List<String> hashTags, Location location, DateTime date) {
        this.id = id;
        this.user = user;
        this.hashTags = hashTags;
        this.location = location;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public long getUser() {
        return user;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public Location getLocation() {
        return location;
    }

    public DateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(Tweet o) {
        return this.date.compareTo(o.date);
    }
}

