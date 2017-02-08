package com.frolfr.frolfrclient;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.frolfr.frolfrclient.api.Round;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.RoundScorecard;
import com.frolfr.frolfrclient.entity.Scorecard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CourseScorecardDetailActivity extends FrolfrActivity {

    public static String ROUND_ID_EXTRA = "round_id";
    private RoundScorecard roundScorecard;

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


    public void setRoundDeatil(RoundScorecard roundScorecard) {
        this.roundScorecard = roundScorecard;
    }
    public RoundScorecard getRoundScorecard() {
        return roundScorecard;
    }


    /**
     * Represents an asynchronous call to get a player's scorecard for a given course
     */
    public class GetRoundDetail extends AsyncTask<Integer, Void, RoundScorecard> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected RoundScorecard doInBackground(Integer ... roundId) {
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

                    JSONObject round = json.getJSONObject("round");
                    JSONArray turns = json.getJSONArray("turns");
                    JSONArray scorecards = json.getJSONArray("scorecards");

                    String courseName = round.getString("course_name");
                    Date created = null;
                    try {
                        created = df.parse(round.getString("created_at"));
                    } catch (ParseException e) {
                        Log.e(getClass().getSimpleName(), "Failed to parse round create date", e);
                    }
                    int holeCount = round.getInt("hole_count");

                    RoundScorecard roundScorecard = new RoundScorecard(roundId[0], courseName, created, holeCount);

//                    JSONArray scorecardArr = json.getJSONArray("course_scorecards");
//                    Round[] scorecards = new Round[scorecardArr.length()];
//                    for (int i=0; i<scorecardArr.length(); i++) {
//                        JSONObject scorecard = scorecardArr.getJSONObject(i);
//                        Date created = null;
//                        try {
//                            created = df.parse(scorecard.getString("created_at"));
//                        } catch (ParseException e) {
//                            Log.e(getClass().getSimpleName(), "Failed to parse created_at from json", e);
//                        }
//                        scorecards[i] = new Round(scorecard.getInt("id"), scorecard.getInt("round_id"),
//                                created, scorecard.getInt("total_strokes"), scorecard.getInt("total_score"),
//                                scorecard.getBoolean("is_completed"));
//                    }
//
//                    return scorecards;
                    return new RoundScorecard(roundId[0], null, null, null);

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final RoundScorecard roundScorecardRet) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetRoundDetail");
            roundScorecard = roundScorecardRet;
//            courseScorecardArrayAdapter.clear();
//            if (courseScorecards == null)
//                return;
//            for (Round scorecard : courseScorecards) {
//                courseScorecardArrayAdapter.add(scorecard);
//            }
        }
    }

}
