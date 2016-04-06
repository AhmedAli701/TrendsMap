package com.cookiescode.trendsmap;

import java.util.List;

/**
 * Created by Ahmed Ali on 04/04/2016.
 */
public class TagInfo implements Comparable<TagInfo>{
    private String name;
    private int amount;
    private String color;

    public TagInfo(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.color = "#000000";
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = "#" + color.substring(2, 8);
    }

    @Override
    public int compareTo(TagInfo o) {
        if(amount > o.getAmount())
            return -1;
        else if(amount < o.getAmount())
            return 1;
        else return 0;
    }
}
