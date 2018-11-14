package com.frolfr.frolfrclient.activity.coursescorecards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.frolfr.frolfrclient.R;

import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseScorecardsFragment extends Fragment {

    public CourseScorecardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_scorecards, container, false);

        ListView courseScorecardsListView = (ListView) rootView.findViewById(R.id.list_view_course_scorecards);
        if (courseScorecardsListView != null) {
            Log.d(getClass().getSimpleName(), "Found the scorecards list view!");
        }

        CourseScorecardsActivity activity = (CourseScorecardsActivity) getActivity();

        courseScorecardsListView.setAdapter(activity.getCourseScorecardsArrayAdapter());
        courseScorecardsListView.setOnItemClickListener(activity.getOnCourseScorecardClickListener());

        return rootView;
    }
}
