package com.frolfr.frolfrclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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

public class CourseActivity extends FrolfrActivity {

    private CourseArrayAdapter courseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            CourseFragment courseFragment = new CourseFragment();

            Log.d(getClass().getSimpleName(), "Creating courses fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, courseFragment)
                    .commit();

            // The ArrayAdapter will take data from a source and
            // use it to populate the ListView it's attached to.
            courseListAdapter =
                    new CourseArrayAdapter(
                            this,
                            R.layout.list_item_course, // The name of the layout ID.
                            new ArrayList<Course>());

        } else {
            Log.d(getClass().getSimpleName(), "CourseActivity fragment already created");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        new GetCourseInfoTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_courses, menu);
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



    public void onCourseItemClicked(Course selectedCourse) {
        Intent intent = new Intent(this, CourseScorecardActivity.class);
        intent.putExtra(CourseScorecardActivity.COURSE_ID_EXTRA, selectedCourse.id);
        startActivity(intent);
    }



    public CourseArrayAdapter getCourseListAdapter() {
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
