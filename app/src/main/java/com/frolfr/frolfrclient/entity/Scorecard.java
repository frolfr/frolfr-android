package com.frolfr.frolfrclient.entity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wowens on 1/1/17.
 */
public class Scorecard {

    private String user;
    private List<HoleDetail> scoreDetail;
    private int totalScore;

    public Scorecard(String user, List<HoleDetail> scoreDetail) {
        this.user = user;
        this.scoreDetail = scoreDetail;

        this.totalScore = 0;
        Iterator<HoleDetail> holeIter = scoreDetail.iterator();
        while (holeIter.hasNext()) {
            HoleDetail detail = holeIter.next();
            if (detail.getScoreDiff() != null)
                this.totalScore += holeIter.next().getScoreDiff();
        }
    }

    public String getUser() {
        return user;
    }

    public List<HoleDetail> getScoreDetail() {
        return scoreDetail;
    }

    public int getScoreTotal() {
        return totalScore;
    }
}
