package com.frolfr.frolfrclient.activity.roundscorecard;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.entity.HoleDetail;

import java.util.List;

/**
 * Created by hightech on 2/6/17.
 */

public class HoleDetailAdapter extends ArrayAdapter {

    public HoleDetailAdapter(Activity activity, int listLayout, List<HoleDetail> scores) {
        super(activity, listLayout, scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoleDetailAdapter.ColumnView cv;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_scorecard, null);
            cv = new HoleDetailAdapter.ColumnView();
            cv.score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(cv);
        } else {
            cv = (HoleDetailAdapter.ColumnView) convertView.getTag();
        }

        HoleDetail holeDetail = (HoleDetail) getItem(position);

        String strokes = holeDetail.getStrokes() == null ? "-" : holeDetail.getStrokes()+"";
        cv.score.setText(strokes);
        // TODO - ART
        if (holeDetail.getScoreType() != null) {
            switch (holeDetail.getScoreType()) {
                case DOUBLE_BOGIE:
                    cv.score.setBackgroundColor(Color.RED);
                    break;
                case BOGIE:
                    cv.score.setBackgroundColor(Color.MAGENTA);
                    break;
                case PAR:
                    cv.score.setBackgroundColor(Color.WHITE);
                    break;
                case BIRDIE:
                    cv.score.setBackgroundColor(Color.GREEN);
                    break;
                case EAGLE:
                    cv.score.setBackgroundColor(Color.BLUE);
                    break;
            }
        } else {
            cv.score.setBackgroundColor(Color.GRAY);
        }

        return convertView;
    }

    protected static class ColumnView {
        protected TextView score;
    }
}
