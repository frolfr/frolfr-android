package com.frolfr.frolfrclient;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.frolfr.frolfrclient.api.CourseScorecards;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.CourseScorecard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseDetailActivityFragment extends Fragment {

    private int courseId;
    private CourseScorecardArrayAdapter courseScorecardArrayAdapter;

    public CourseDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        courseScorecardArrayAdapter = new CourseScorecardArrayAdapter(
                getActivity(),
                R.layout.list_item_course_scorecard,
                new ArrayList<CourseScorecard>()
        );

        View rootView = inflater.inflate(R.layout.fragment_course_detail_player_history, container, false);

        ListView courseScorecardsListView = (ListView) rootView.findViewById(R.id.list_view_course_scorecards);
        if (courseScorecardsListView != null) {
            Log.d(getClass().getSimpleName(), "Found the scorecards list view!");
        }
        courseScorecardsListView.setAdapter(courseScorecardArrayAdapter);
        courseScorecardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                CourseScorecard selectedCourseScorecard = (CourseScorecard) courseScorecardArrayAdapter.getItem(position);

                Toast toast = Toast.makeText(getActivity(), "You clicked list item " + position + ": " + selectedCourseScorecard.id,
                        Toast.LENGTH_SHORT);
                toast.show();

//                Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
//                intent.putExtra(CourseDetailActivity.COURSE_NAME_EXTRA, selectedCourse.name);
//                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        courseId = getActivity().getIntent().getIntExtra(CourseDetailActivity.COURSE_ID_EXTRA, 0);
        new GetPlayerScorecardsForCourse().execute(courseId);
    }


    /**
     * Represents an asynchronous call to get a player's scorecard for a given course
     */
    public class GetPlayerScorecardsForCourse extends AsyncTask<Integer, Void, CourseScorecard[]> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected CourseScorecard[] doInBackground(Integer ... courseIds) {
            Log.d(getClass().getSimpleName(), "doInBackground - GetPlayerScorecardForCourse");
            SharedPreferences preferences = getActivity().getSharedPreferences(PreferenceKeys.AuthKeys.class.getName(), getActivity().MODE_PRIVATE);
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
                    CourseScorecard[] scorecards = new CourseScorecard[scorecardArr.length()];
                    for (int i=0; i<scorecardArr.length(); i++) {
                        JSONObject scorecard = scorecardArr.getJSONObject(i);
                        Date created = null;
                        try {
                            created = df.parse(scorecard.getString("created_at"));
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Failed to parse created_at from json", e);
                        }
                        scorecards[i] = new CourseScorecard(scorecard.getInt("id"), scorecard.getInt("round_id"),
                                created, scorecard.getInt("total_strokes"), scorecard.getInt("total_score"),
                                scorecard.getBoolean("is_completed"));
                    }

                    Log.d(getClass().getSimpleName(), "Found " + scorecards.length + " scorecards");

                    return scorecards;

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final CourseScorecard[] courseScorecards) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetPlayerScorecardsForCourse");
            courseScorecardArrayAdapter.clear();
            if (courseScorecards == null)
                return;
            for (CourseScorecard scorecard : courseScorecards) {
                Log.d(getClass().getSimpleName(), "Adding scorecard #" + scorecard.id + " to array adapter");
                courseScorecardArrayAdapter.add(scorecard);
            }
            Log.d(getClass().getSimpleName(), "Scorecard adapter has " + courseScorecardArrayAdapter.getCount() + " rows");
        }
    }
}
