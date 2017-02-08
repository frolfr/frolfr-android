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
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.Round;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseScorecardActivity extends FrolfrActivity {

    public static String COURSE_ID_EXTRA = "course_id";
    public static String COURSE_NAME_EXTRA = "course_name";
    private int courseId;
    private CourseScorecardArrayAdapter courseScorecardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            CourseScorecardFragment courseDetailFragment = new CourseScorecardFragment();

            Log.d(getClass().getSimpleName(), "Creating course detail fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, courseDetailFragment)
                    .commit();

            // The ArrayAdapter will take data from a source and
            // use it to populate the ListView it's attached to.
            courseScorecardArrayAdapter =
                    new CourseScorecardArrayAdapter(
                            this,
                            R.layout.list_item_course_scorecard, // The name of the layout ID.
                            new ArrayList<Round>());

        } else {
            Log.d(getClass().getSimpleName(), "CourseDetail fragment already created");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        courseId = getIntent().getIntExtra(CourseScorecardActivity.COURSE_ID_EXTRA, 0);
        String courseName = getIntent().getStringExtra(COURSE_NAME_EXTRA);
        setTitle(courseName);
        new GetPlayerScorecardsForCourse().execute(courseId);
    }


    public CourseScorecardArrayAdapter getCourseScorecardArrayAdapter() {
        return courseScorecardArrayAdapter;
    }

    public AdapterView.OnItemClickListener getOnCourseScorecardClickListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Round scorecard = (Round) courseScorecardArrayAdapter.getItem(position);

                Intent intent = new Intent(view.getContext(), CourseScorecardDetailActivity.class);
                intent.putExtra(CourseScorecardDetailActivity.ROUND_ID_EXTRA, scorecard.roundId);
                startActivity(intent);
            }
        };
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




    /**
     * Represents an asynchronous call to get a player's scorecard for a given course
     */
    public class GetPlayerScorecardsForCourse extends AsyncTask<Integer, Void, Round[]> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected Round[] doInBackground(Integer ... courseIds) {
            Log.d(getClass().getSimpleName(), "doInBackground - GetPlayerScorecardForCourse");
            SharedPreferences preferences = getSharedPreferences(PreferenceKeys.AuthKeys.class.getName(), MODE_PRIVATE);
            String email = preferences.getString(PreferenceKeys.AuthKeys.EMAIL.toString(), null);
            String authToken = preferences.getString(PreferenceKeys.AuthKeys.TOKEN.toString(), null);

            CourseScorecards.CourseScorecardRequest scorecardRequest = new CourseScorecards.CourseScorecardRequest(courseIds[0]);
            String jsonResponse = scorecardRequest.execute(email, authToken);

            if (!TextUtils.isEmpty(jsonResponse)) {
                JSONObject json = null;
                try {

                    Log.d(getClass().getSimpleName(), "Got JSON response for scorecards: " + jsonResponse);
                    json = new JSONObject(jsonResponse);

                    JSONArray scorecardArr = json.getJSONArray("course_scorecards");
                    Round[] scorecards = new Round[scorecardArr.length()];
                    for (int i=0; i<scorecardArr.length(); i++) {
                        JSONObject scorecard = scorecardArr.getJSONObject(i);
                        Date created = null;
                        try {
                            created = df.parse(scorecard.getString("created_at"));
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Failed to parse created_at from json", e);
                        }
                        scorecards[i] = new Round(scorecard.getInt("id"), scorecard.getInt("round_id"),
                                created, scorecard.getInt("total_strokes"), scorecard.getInt("total_score"),
                                scorecard.getBoolean("is_completed"));
                    }

                    return scorecards;

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final Round[] rounds) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetPlayerScorecardsForCourse");
            courseScorecardArrayAdapter.clear();
            if (rounds == null)
                return;
            for (Round scorecard : rounds) {
                courseScorecardArrayAdapter.add(scorecard);
            }
        }
    }
}
