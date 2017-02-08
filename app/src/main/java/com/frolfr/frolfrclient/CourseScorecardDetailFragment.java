package com.frolfr.frolfrclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.frolfr.frolfrclient.entity.RoundScorecard;
import com.frolfr.frolfrclient.entity.Scorecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseScorecardDetailFragment extends Fragment {

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yy");

    ListView playerList;

    public CourseScorecardDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_scorecard_detail, container, false);

        RoundScorecard scorecard = ((CourseScorecardDetailActivity) getActivity()).getRoundScorecard();

        playerList = (ListView) rootView.findViewById(R.id.list_view_course_scorecard_detail);

        // TODO - scorecard lists. update all these views on ajax callback

        return rootView;
    }

    public void update(RoundScorecard roundScorecard) {
        String roundName = roundScorecard.getCourseName() + " | " + df.format(roundScorecard.getCreated());
        getActivity().setTitle(roundName);
        playerList.setAdapter(new PlayerScorecardAdapter(getActivity(), 0, roundScorecard.getScorecards()));
    }
}
