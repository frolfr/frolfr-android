package com.frolfr.frolfrclient.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wowens on 1/1/17.
 */
public class RoundDetail {
    private int roundId;
    private String courseName;
    private Date created;
    private Map<Integer, Integer> parMap;
    private Map<String, List<HoleDetail>> userScores;

    public RoundDetail(int roundId, String courseName, Date created, Map<Integer, Integer> parMap) {
        this.roundId = roundId;
        this.courseName = courseName;
        this.created = created;
        this.parMap = parMap;
    }

    public void addScores(String user, List<Integer> scores) {
        if (userScores == null)
            userScores = new HashMap<>();

        List<HoleDetail> holeDetail = new ArrayList<>(scores.size());
        Iterator<Integer> scoresIter = scores.iterator();
        Iterator<Integer> parsIter = parMap.values().iterator();
        while (scoresIter.hasNext()) {
            holeDetail.add(new HoleDetail(scoresIter.next(), parsIter.next()));
        }

        userScores.put(user, holeDetail);
    }


    private class HoleDetail {
        int strokes;
        int scoreDiff;
        SCORE_TYPE scoreType;

        public HoleDetail(int strokes, int par) {
            this.strokes = strokes;
            this.scoreDiff = par - strokes;
            this.scoreType = SCORE_TYPE.byScoreDiff(scoreDiff);
        }

        public int getStrokes() {
            return strokes;
        }
        public int getScoreDiff() {
            return scoreDiff;
        }
        public SCORE_TYPE getScoreType() {
            return scoreType;
        }
    }

    public enum SCORE_TYPE {
        EAGLE,
        BIRDIE,
        PAR,
        BOGIE,
        DOUBLE_BOGIE;

        public static SCORE_TYPE byScoreDiff(int scoreDiff) {
            if (scoreDiff >= 2)
                return EAGLE;
            else if (scoreDiff == 1)
                return BIRDIE;
            else if (scoreDiff == 0)
                return PAR;
            else if (scoreDiff == -1)
                return BOGIE;
            else
                return DOUBLE_BOGIE;
        }
    }


    public int getScoreTotal(String user) {
        int totalScore = 0;
        Iterator<HoleDetail> holeIter = userScores.get(user).iterator();
        while (holeIter.hasNext()) {
            totalScore += holeIter.next().getScoreDiff();
        }
        return totalScore;
    }

    public String getCourseName() {
        return courseName;
    }

    public Date getCreated() {
        return created;
    }
}
