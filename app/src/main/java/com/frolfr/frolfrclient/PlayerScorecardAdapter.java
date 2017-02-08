package com.frolfr.frolfrclient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.frolfr.frolfrclient.entity.HoleDetail;
import com.frolfr.frolfrclient.entity.SCORE_TYPE;
import com.frolfr.frolfrclient.entity.Scorecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wowens on 1/24/16.
 */
public class PlayerScorecardAdapter extends ArrayAdapter {

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yy");

    public PlayerScorecardAdapter(Activity activity, int listLayout, List<Scorecard> scorecards) {
        super(activity, listLayout, scorecards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColumnView cv;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_course_scorecard_detail, null);
            cv = new ColumnView();
            cv.playerName = (TextView) convertView.findViewById(R.id.scorecardDetailHeader);
            cv.scores = (ListView) convertView.findViewById(R.id.list_view_course_scorecard_detail);
            convertView.setTag(cv);
        } else {
            cv = (ColumnView) convertView.getTag();
        }

        Scorecard scorecard = (Scorecard) getItem(position);

        cv.playerName.setText(scorecard.getUser());
        cv.scores.setAdapter(new HoleDetailAdapter((Activity) getContext(), 0, scorecard.getScoreDetail()));

        // TODO - score total

        return convertView;
    }

    protected static class ColumnView {
        protected TextView playerName;
        protected ListView scores;
    }

}
