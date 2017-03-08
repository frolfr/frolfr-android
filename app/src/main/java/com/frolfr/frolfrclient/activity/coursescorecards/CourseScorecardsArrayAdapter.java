package com.frolfr.frolfrclient.activity.coursescorecards;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.entity.Round;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wowens on 1/24/16.
 */
public class CourseScorecardsArrayAdapter extends ArrayAdapter {

    private static final DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

    public CourseScorecardsArrayAdapter(Activity activity, int listLayout, List<Round> rows) {
        super(activity, listLayout, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColumnView cv;
        if (convertView == null) {
            Log.d(getClass().getSimpleName(), "Scorecard array view is null - inflating a new list item");
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_course_scorecard, parent, false);
            cv = new ColumnView();
            cv.date = (TextView) convertView.findViewById(R.id.list_item_scorecard_date);
            cv.strokes = (TextView) convertView.findViewById(R.id.list_item_scorecard_total_strokes);
            cv.score = (TextView) convertView.findViewById(R.id.list_item_scorecard_total_score);
            convertView.setTag(cv);
        } else {
            Log.d(getClass().getSimpleName(), "Scorecard array view is NOT null - updating its data");
            cv = (ColumnView) convertView.getTag();
        }

        Round scorecard = (Round) getItem(position);
        cv.date.setText(df.format(scorecard.created));
        cv.strokes.setText(scorecard.totalStrokes + "");
        cv.score.setText((scorecard.totalScore > 0 ? "+" : "") + scorecard.totalScore);

        if (!scorecard.isCompleted) {
            cv.date.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            cv.strokes.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            cv.score.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }

        return convertView;
    }

    protected static class ColumnView {
        protected TextView date, strokes, score;
    }

}
