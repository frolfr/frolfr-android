package com.frolfr.frolfrclient.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wowens on 1/26/16.
 */
public class CourseScorecard implements Serializable {

    public int id;
    public int roundId;
    public Date created;
    public int totalStrokes;
    public int totalScore;
    public boolean isCompleted;

    public CourseScorecard(int id, int roundId, Date created,
                           int totalStrokes, int totalScore, boolean isCompleted) {
        this.id = id;
        this.roundId = roundId;
        this.created = created;
        this.totalStrokes = totalStrokes;
        this.totalScore = totalScore;
        this.isCompleted = isCompleted;
    }
}
