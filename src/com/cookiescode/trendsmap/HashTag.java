package com.cookiescode.trendsmap;

/**
 * Created by Ahmed Ali on 28/03/2016.
 */
public class HashTag {
    private String name;
    private Location location;

    public HashTag(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString(){
        return name + " " + location;
    }
}
