package com.frolfr.frolfrclient.activity.scorecard;

import android.app.Activity;
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

        cv.score.setText(holeDetail.getStrokes() + "");
        // TODO - ART
        switch (holeDetail.getScoreType()) {
            case DOUBLE_BOGIE:
                cv.score.append("_db"); break;
            case BOGIE:
                cv.score.append("_b"); break;
            case PAR:
                cv.score.append(""); break;
            case BIRDIE:
                cv.score.append("_b!"); break;
            case EAGLE:
                cv.score.append("_e!!"); break;
        }

        return convertView;
    }

    protected static class ColumnView {
        protected TextView score;
    }
}
