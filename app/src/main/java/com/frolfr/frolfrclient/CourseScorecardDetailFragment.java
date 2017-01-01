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
import com.frolfr.frolfrclient.entity.RoundDetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseScorecardDetailFragment extends Fragment {

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yy");

    private TextView courseName;
    private TextView datePlayed;

    public CourseScorecardDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_scorecard_detail, container, false);

        RoundDetail roundDetail = ((CourseScorecardDetailActivity) getActivity()).getRoundDetail();

        courseName = (TextView) rootView.findViewById(R.id.courseNameText);
        datePlayed = (TextView) rootView.findViewById(R.id.scorecardDateText);

        // TODO - scorecard lists. update all these views on ajax callback

        return rootView;
    }

    public void update(RoundDetail roundDetail) {
        courseName.setText(roundDetail.getCourseName());
        datePlayed.setText(df.format(roundDetail.getCreated()));
    }
}
