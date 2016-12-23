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
import com.frolfr.frolfrclient.entity.CourseScorecard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseScorecardDetailActivity extends FrolfrActivity {

    public static String SCORECARD_EXTRA = "serialized_scorecard";
    private CourseScorecard scorecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            scorecard = (CourseScorecard) getIntent().getSerializableExtra(SCORECARD_EXTRA);

            // TODO - may need another API call to get related details (ie. Other player's scorecards)

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
    }


    public CourseScorecard getCourseScorecard() {
        return scorecard;
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
}
