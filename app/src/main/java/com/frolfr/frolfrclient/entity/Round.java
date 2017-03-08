package com.frolfr.frolfrclient.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wowens on 1/26/16.
 */
public class Round implements Serializable {

    public int id;
    public int roundId;
    public Date created;
    public int totalStrokes;
    public int totalScore;
    public boolean isCompleted;

    public Round(int id, int roundId, Date created,
                 int totalStrokes, int totalScore, boolean isCompleted) {
        this.id = id;
        this.roundId = roundId;
        this.created = created;
        this.totalStrokes = totalStrokes;
        this.totalScore = totalScore;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }
    public int getRoundId() {
        return roundId;
    }
    public Date getCreated() {
        return created;
    }
    public int getTotalStrokes() {
        return totalStrokes;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
}
