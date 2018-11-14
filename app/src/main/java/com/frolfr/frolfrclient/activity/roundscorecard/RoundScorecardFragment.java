package com.frolfr.frolfrclient.activity.roundscorecard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.entity.RoundScorecard;
import com.frolfr.frolfrclient.entity.Scorecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class RoundScorecardFragment extends Fragment {

    private static final DateFormat df = new SimpleDateFormat("MM/dd/yy");

    LinearLayout playerScorecardLayout;
    ListView playerList;

    public RoundScorecardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_round_scorecard, container, false);

        playerScorecardLayout = (LinearLayout) rootView.findViewById(R.id.scorecardDetails);

//        playerList = (ListView) rootView.findViewById(R.id.list_view_course_scorecard_detail);
//        playerList.setAdapter(((RoundScorecardActivity)getActivity()).getScorecardAdapter());

        return rootView;
    }

    public void displayPlayerScorecards() {
        RoundScorecard scorecard = ((RoundScorecardActivity) getActivity()).getRoundScorecard();

        for (Scorecard s : scorecard.getScorecards()) {
            View playerScorecard = getActivity().getLayoutInflater().inflate(R.layout.list_item_round_scorecard, playerScorecardLayout, false);

            TextView scorecardHeader = (TextView) playerScorecard.findViewById(R.id.scorecardDetailHeader);
            scorecardHeader.setText(s.getUser());

            ListView roundDetail = (ListView) playerScorecard.findViewById(R.id.list_view_course_scorecard_detail);
            roundDetail.setAdapter(new HoleDetailAdapter(getActivity(), 0, s.getScoreDetail()));

            playerScorecardLayout.addView(playerScorecard);

            Log.d(getClass().getSimpleName(), "Created player scorecard for " + s.getUser() + ". There are now " + playerScorecardLayout.getChildCount() + " scorecards");
        }
    }
}
