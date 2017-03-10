package com.frolfr.frolfrclient.activity.courses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.frolfr.frolfrclient.activity.coursescorecards.CourseScorecardsActivity;
import com.frolfr.frolfrclient.activity.FrolfrActivity;
import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.api.Courses;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.entity.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CoursesActivity extends FrolfrActivity {

    private CoursesArrayAdapter courseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            CoursesFragment coursesFragment = new CoursesFragment();

            Log.d(getClass().getSimpleName(), "Creating courses fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, coursesFragment)
                    .commit();

            courseListAdapter =
                    new CoursesArrayAdapter(
                            this,
                            R.layout.list_item_course, // The name of the layout ID.
                            new ArrayList<Course>());

        } else {
            Log.d(getClass().getSimpleName(), "CoursesActivity fragment already created");

            // The ArrayAdapter will take data from a source and
            // use it to populate the ListView it's attached to.
            courseListAdapter =
                    new CoursesArrayAdapter(
                            this,
                            R.layout.list_item_course, // The name of the layout ID.
                            new ArrayList<Course>());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        new GetCourseInfoTask().execute();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        TODO - save all Course information so we don't have to re-issue web requests
    }



    public void onCourseItemClicked(Course selectedCourse) {
        Intent intent = new Intent(this, CourseScorecardsActivity.class);
        intent.putExtra(CourseScorecardsActivity.COURSE_ID_EXTRA, selectedCourse.id);
        startActivity(intent);
    }



    public CoursesArrayAdapter getCourseListAdapter() {
        return courseListAdapter;
    }



    /**
     * Represents an asynchronous call to get Profile Statistics
     */
    public class GetCourseInfoTask extends AsyncTask<ArrayAdapter, Void, List<Course>> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        /**
         * Sort courses by most recent date played first
         */
        private final Comparator<Course> recentScorecardComparator = new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                return c2.getLastPlayed().compareTo(c1.getLastPlayed());
            }
        };

        @Override
        protected List<Course> doInBackground(ArrayAdapter... params) {
            Log.d(getClass().getSimpleName(), "doInBackground - GetCourseInfoTask");
            SharedPreferences preferences = getSharedPreferences(PreferenceKeys.AuthKeys.class.getName(), MODE_PRIVATE);
            String email = preferences.getString(PreferenceKeys.AuthKeys.EMAIL.toString(), null);
            String authToken = preferences.getString(PreferenceKeys.AuthKeys.TOKEN.toString(), null);

            Courses.CoursesRequest coursesRequest = new Courses.CoursesRequest();
            String jsonResponse = coursesRequest.execute(email, authToken);

            if (!TextUtils.isEmpty(jsonResponse)) {
                JSONObject json = null;
                try {
                    json = new JSONObject(jsonResponse);
                    JSONArray courseArr = json.getJSONArray("courses");

                    List<Course> courseInfo = new ArrayList<>(courseArr.length());
                    for (int i=0; i<courseArr.length(); i++) {
                        JSONObject course = courseArr.getJSONObject(i);
                        Date lastPlayed = null;
                        try {
                            lastPlayed = df.parse(course.getString("last_played_at"));
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Failed to parse lastPlayedDate from json", e);
                        }
                        courseInfo.add(new Course(course.getInt("id"), course.getString("name"), course.getInt("hole_count"),
                                course.getString("location"), lastPlayed));
                    }

                    Collections.sort(courseInfo, recentScorecardComparator);

                    return courseInfo;

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Course> courseResults) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetCourseInfoTask");
            courseListAdapter.clear();
            if (courseResults == null)
                return;
            for (Course course : courseResults)
                courseListAdapter.add(course);
        }
    }
}
