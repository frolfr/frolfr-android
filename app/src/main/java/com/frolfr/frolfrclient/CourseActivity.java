package com.frolfr.frolfrclient;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frolfr.frolfrclient.api.Profile;
import com.frolfr.frolfrclient.config.PreferenceKeys;
import com.frolfr.frolfrclient.api.Courses.CoursesRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.courses_toolbar);
        setSupportActionBar(myToolbar);

//        getActionBar().setHomeButtonEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        } else {
            Log.e(getClass().getSimpleName(), "WHY IS THE NAV DRAWER FRAGMENT NULL???");
        }


        if (savedInstanceState == null) {
            Log.d(getClass().getSimpleName(), "Creating courses fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.courses_container, new CoursesFragment())
                    .commit();
        } else {
            Log.d(getClass().getSimpleName(), "CourseActivity fragment already created");
        }
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
}
