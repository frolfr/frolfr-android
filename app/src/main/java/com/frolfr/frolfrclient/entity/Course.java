package com.frolfr.frolfrclient.entity;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by wowens on 1/26/16.
 */
public class Course {
    public int id;
    public String name;
    public int holeCount;
    public String location;
    public Date lastPlayed;

    public Course(int id, String name, int holeCount, String location, Date lastPlayed) {
        this.id = id;
        this.name = name;
        this.holeCount = holeCount;
        this.location = location;
        this.lastPlayed = lastPlayed;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getHoleCount() {
        return holeCount;
    }
    public String getLocation() {
        return location;
    }
    public Date getLastPlayed() {
        return lastPlayed;
    }
}
