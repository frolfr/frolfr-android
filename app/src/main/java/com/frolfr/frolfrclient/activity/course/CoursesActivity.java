package com.frolfr.frolfrclient.activity.course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.frolfr.frolfrclient.activity.coursescorecard.CourseScorecardActivity;
import com.frolfr.frolfrclient.FrolfrActivity;
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
import java.util.Date;

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

            // The ArrayAdapter will take data from a source and
            // use it to populate the ListView it's attached to.
            courseListAdapter =
                    new CoursesArrayAdapter(
                            this,
                            R.layout.list_item_course, // The name of the layout ID.
                            new ArrayList<Course>());

        } else {
            Log.d(getClass().getSimpleName(), "CoursesActivity fragment already created");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        new GetCourseInfoTask().execute();
    }



    public void onCourseItemClicked(Course selectedCourse) {
        Intent intent = new Intent(this, CourseScorecardActivity.class);
        intent.putExtra(CourseScorecardActivity.COURSE_ID_EXTRA, selectedCourse.id);
        startActivity(intent);
    }



    public CoursesArrayAdapter getCourseListAdapter() {
        return courseListAdapter;
    }



    /**
     * Represents an asynchronous call to get Profile Statistics
     */
    public class GetCourseInfoTask extends AsyncTask<ArrayAdapter, Void, Course[]> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected Course[] doInBackground(ArrayAdapter... params) {
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

                    Course[] courseInfo = new Course[courseArr.length()];
                    for (int i=0; i<courseArr.length(); i++) {
                        JSONObject course = courseArr.getJSONObject(i);
                        Date lastPlayed = null;
                        try {
                            lastPlayed = df.parse(course.getString("last_played_at"));
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Failed to parse lastPlayedDate from json", e);
                        }
                        courseInfo[i] = new Course(course.getInt("id"), course.getString("name"), course.getInt("hole_count"),
                                course.getString("location"), lastPlayed);
                    }

                    return courseInfo;

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "Malformed JSON response:\n" + jsonResponse, e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(final Course[] courseResults) {
            Log.d(getClass().getSimpleName(), "onPostExecute - GetCourseInfoTask");
            courseListAdapter.clear();
            if (courseResults == null)
                return;
            for (Course course : courseResults)
                courseListAdapter.add(course);
        }
    }
}
