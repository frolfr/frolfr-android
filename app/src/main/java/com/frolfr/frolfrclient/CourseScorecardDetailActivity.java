package com.frolfr.frolfrclient;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListAdapter;
import com.frolfr.frolfrclient.api.Round;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.HoleDetail;
import com.frolfr.frolfrclient.entity.RoundScorecard;
import com.frolfr.frolfrclient.entity.Scorecard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CourseScorecardDetailActivity extends FrolfrActivity {

    public static String ROUND_ID_EXTRA = "round_id";
    private RoundScorecard roundScorecard;

    private List<Scorecard> scorecards;
    private ListAdapter scorecardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            scorecards = new ArrayList<>();
            scorecardAdapter = new PlayerScorecardAdapter(this, 0, scorecards);

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

    public ListAdapter getScorecardAdapter() {
        return scorecardAdapter;
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

                    // setup base round scorecard information
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

                    // create hashmap of turn id to turn
                    Map<Integer, HoleDetail> turnMap = new HashMap<>();
                    for (int i=0; i<turns.length(); i++) {
                        JSONObject turn = turns.getJSONObject(i);
                        int turnId = turn.getInt("id");
                        int hole = turn.getInt("hole_number");
                        int strokes = turn.getInt("strokes");
                        int par = turn.getInt("par");
                        HoleDetail holeDetail = new HoleDetail(hole, strokes, par);
                        turnMap.put(turnId, holeDetail);
                    }

                    // create individual player scorecards
                    for (int i=0; i<scorecards.length(); i++) {
                        JSONObject sc = scorecards.getJSONObject(i);
                        String user = sc.getString("user_initials");
                        List<HoleDetail> scoreDetail = new ArrayList<>(holeCount);
                        for (int turnId : (int[])sc.get("turn_ids")) {
                            scoreDetail.add(turnMap.get(turnId));
                        }
                        Scorecard scorecard = new Scorecard(user, scoreDetail);
                        roundScorecard.addScorecard(scorecard);
                    }

                    return roundScorecard;

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

            String roundName = roundScorecard.getCourseName() + " | " + df.format(roundScorecard.getCreated());
            setTitle(roundName);
            scorecards.addAll(roundScorecard.getScorecards());
            scorecardAdapter.notify();  // TODO no notifyDataSetChanged() ?
        }
    }

}
