package com.frolfr.frolfrclient.activity.newgame;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

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

/**
 * Created by wowens on 7/16/16.
 */
public class NewGameActivity extends FrolfrActivity {

    public static final String EXTRA_COURSE_ID = "courseId";

    private CourseSpinnerAdapter courseAdapter;
    private int selectedCourseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            courseAdapter = new CourseSpinnerAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<Course>());

            NewGameFragment newGameFragment = new NewGameFragment();

            Log.d(getClass().getSimpleName(), "Created new game fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, newGameFragment)
                    .commit();

        } else {
            Log.d(getClass().getSimpleName(), "NewGameFragment fragment already created");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        new GetCourseInfoTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
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
            Log.d(getClass().getSimpleName(), "Updating courses with " + courseResults.length + " results");
            courseAdapter.clear();
            if (courseResults == null)
                return;
            for (Course course : courseResults)
                courseAdapter.add(course);
        }
    }

    public CourseSpinnerAdapter getCourseAdapter() {
        return courseAdapter;
    }

    public void setSelectedCourseId(int courseId) {
        this.selectedCourseId = courseId;
    }

    public void startGame(View v) {
        Log.d(getClass().getSimpleName(), "Starting new game on course: " + "???");

        // TODO - first, allow the user to add players
        // then launch a new activity, passing in the users and course selected
//        Intent intent = new Intent(this, ActiveGameActivity.class);
//        intent.putExtra(ActiveGameActivity.COURSE_ID_EXTRA, selectedCourse.id);
//        startActivity(intent);
    }
}
