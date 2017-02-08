package com.frolfr.frolfrclient.entity;

/**
 * Created by hightech on 2/6/17.
 */
public enum SCORE_TYPE {
    EAGLE,
    BIRDIE,
    PAR,
    BOGIE,
    DOUBLE_BOGIE;

    public static SCORE_TYPE byScoreDiff(Integer scoreDiff) {
        if (scoreDiff == null)
            return null;

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
