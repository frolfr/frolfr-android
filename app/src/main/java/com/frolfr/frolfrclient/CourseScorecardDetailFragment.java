package com.frolfr.frolfrclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.frolfr.frolfrclient.entity.CourseScorecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseScorecardDetailFragment extends Fragment {

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yy");

    public CourseScorecardDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_scorecard_detail, container, false);

        CourseScorecard scorecard = ((CourseScorecardDetailActivity) getActivity()).getCourseScorecard();

        TextView courseName = (TextView) rootView.findViewById(R.id.courseNameText);
        TextView datePlayed = (TextView) rootView.findViewById(R.id.scorecardDateText);

        courseName.setText("Disc Golf Park X");  // TODO
        datePlayed.setText(df.format(scorecard.created));

        return rootView;
    }
}
