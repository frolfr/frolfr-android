package com.frolfr.frolfrclient.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hightech on 2/6/17.
 */

public class RoundScorecard {

    private int roundId;
    private String courseName;
    private Date created;
    private int holeCount;
    private List<Scorecard> scorecards;

    public RoundScorecard(int roundId, String courseName, Date created, int holeCount) {
        this.roundId = roundId;
        this.courseName = courseName;
        this.created = created;
    }

    public void addScores(String user, List<HoleDetail> scoreDetail) {
        if (scorecards == null)
            scorecards = new ArrayList<>();

        scorecards.add(new Scorecard(user, scoreDetail));
    }

    public int getRoundId() {
        return roundId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Date getCreated() {
        return created;
    }

    public int getHoleCount() {
        return holeCount;
    }

    public List<Scorecard> getScorecards() {
        return scorecards;
    }
}
