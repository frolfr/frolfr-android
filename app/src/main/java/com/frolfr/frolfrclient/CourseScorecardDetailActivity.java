package com.frolfr.frolfrclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.frolfr.frolfrclient.api.CourseScorecards;
import com.frolfr.frolfrclient.api.Round;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.CourseScorecard;
import com.frolfr.frolfrclient.entity.RoundDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseScorecardDetailActivity extends FrolfrActivity {

    public static String ROUND_ID_EXTRA = "round_id";
    private RoundDetail roundDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            CourseScorecardDetailFragment scorecardDetailFragment = new CourseScorecardDetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, scorecardDetailFragment)
                    .commit();

        } else {
            // Fragment already created
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        int roundId = getIntent().getIntExtra(ROUND_ID_EXTRA, 0);
        new GetRoundDetail().execute(roundId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setRoundDeatil(RoundDetail roundDetail) {
        this.roundDetail = roundDetail;
    }
    public RoundDetail getRoundDetail() {
        return roundDetail;
    }


    /**
     * Represents an asynchronous call to get a player's scorecard for a given course
     */
    public class GetRoundDetail extends AsyncTask<Integer, Void, RoundDetail> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected RoundDetail doInBackground(Integer ... roundId) {
            Log.d(getClass().getSimpleName(), "doInBackground - GetRoundDetail");
            SharedPreferences preferences = getSharedPreferences(PreferenceKeys.AuthKeys.class.getName(), MODE_PRIVATE);
            String email = preferences.getString(PreferenceKeys.AuthKeys.EMAIL.toString(), null);
            String authToken = preferences.getString(PreferenceKeys.AuthKeys.TOKEN.toString(), null);

            Round.RoundRequest scorecardRequest = new Round.RoundRequest(roundId[0]);
            String jsonResponse = scorecardRequest.execute(email, authToken);

            if (!TextUtils.isEmpty(jsonResponse)) {
                JSONObject json = null;
                try {

                    Log.d(getClass().getSimpleName(), "Got JSON response for round: " + jsonResponse);
                    json = new JSONObject(jsonResponse);

//                    JSONArray scorecardArr = json.getJSONArray("course_scorecards");
//                    CourseScorecard[] scorecards = new CourseScorecard[scorecardArr.length()];
//                    for (int i=0; i<scorecardArr.length(); i++) {
//                        JSONObject scorecard = scorecardArr.getJSONObject(i);
//                        Date created = null;
//                        try {
//                            created = df.parse(scorecard.getString("created_at"));
//                        } catch (ParseException e) {
//                            Log.e(getClass().getSimpleName(), "Failed to parse created_at from json", e);
//                        }
//                        scorecards[i] = new CourseScorecard(scorecard.getInt("id"), scorecard.getInt("round_id"),
//                                created, scorecard.getInt("total_strokes"), scorecard.getInt("total_score"),
//                                scorecard.getBoolean("is_completed"));
//                    }
//
//                    return scorecards;
                    return new RoundDetail(roundId[0], null, null, null);

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final RoundDetail roundDetailRet) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetRoundDetail");
            roundDetail = roundDetailRet;
//            courseScorecardArrayAdapter.clear();
//            if (courseScorecards == null)
//                return;
//            for (CourseScorecard scorecard : courseScorecards) {
//                courseScorecardArrayAdapter.add(scorecard);
//            }
        }
    }

}
