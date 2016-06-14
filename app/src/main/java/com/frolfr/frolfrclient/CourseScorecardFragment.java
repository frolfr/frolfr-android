package com.frolfr.frolfrclient;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseScorecardFragment extends Fragment {

    public CourseScorecardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_scorecard, container, false);

        ListView courseScorecardsListView = (ListView) rootView.findViewById(R.id.list_view_course_scorecards);
        if (courseScorecardsListView != null) {
            Log.d(getClass().getSimpleName(), "Found the scorecards list view!");
        }

        CourseScorecardActivity activity = (CourseScorecardActivity) getActivity();

        courseScorecardsListView.setAdapter(activity.getCourseScorecardArrayAdapter());
        courseScorecardsListView.setOnItemClickListener(activity.getOnCourseScorecardClickListener());

        return rootView;
    }
}
