package com.frolfr.frolfrclient;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by wowens on 1/26/16.
 */
public class Course {
    public String name;
    public int holeCount;
    public String location;
    public Date lastPlayed;

    public Course(String name, int holeCount, String location, Date lastPlayed) {
        this.name = name;
        this.holeCount = holeCount;
        this.location = location;
        this.lastPlayed = lastPlayed;
    }
}
