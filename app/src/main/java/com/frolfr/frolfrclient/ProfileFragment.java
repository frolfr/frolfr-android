package com.frolfr.frolfrclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by wowens on 6/14/16.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileActivity activity = (ProfileActivity) getActivity();
        activity.setTextView((TextView) rootView.findViewById(R.id.profile_statistics_text));

//        ListView courseScorecardsListView = (ListView) rootView.findViewById(R.id.list_view_course_scorecards);
//        if (courseScorecardsListView != null) {
//            Log.d(getClass().getSimpleName(), "Found the scorecards list view!");
//        }
//
//        CourseScorecardActivity activity = (CourseScorecardActivity) getActivity();
//
//        courseScorecardsListView.setAdapter(activity.getCourseScorecardArrayAdapter());
//        courseScorecardsListView.setOnItemClickListener(activity.getOnCourseScorecardClickListener());

        return rootView;
    }
}
