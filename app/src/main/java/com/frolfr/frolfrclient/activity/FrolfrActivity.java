package com.frolfr.frolfrclient.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.activity.courses.CoursesActivity;
import com.frolfr.frolfrclient.activity.newgame.NewGameActivity;
import com.frolfr.frolfrclient.activity.playerprofile.ProfileActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wowens on 6/12/16.
 */
public abstract class FrolfrActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ListView drawerList;
    public String[] drawerListItems;

    public static int currentActivityIdx = 1;
    private static Map<Integer, Class> drawerMap;
    {{
        drawerMap = new HashMap<Integer, Class>();
        drawerMap.put(0, null /*RoundActivty.class*/);
        drawerMap.put(1, CoursesActivity.class);
        drawerMap.put(2, null /*FriendActivity.class*/);
        drawerMap.put(3, ProfileActivity.class);
    }}

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_frolfr);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerListItems = getResources().getStringArray(R.array.navigation_drawer_array);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_drawer, drawerListItems));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(getClass().getSimpleName(), "You clicked item " + position + " with id " + id);

                if (currentActivityIdx == position) {
                    Log.d(getClass().getSimpleName(), "We're already on activity " + position + " nothing to do but close the drawer");
                    drawerLayout.closeDrawers();
                    return;
                }

                Intent intent = new Intent(view.getContext(), drawerMap.get(position));
                startActivity(intent);

                // TODO: does starting the next activity prevent this from being called?
                Log.d(getClass().getSimpleName(), "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        for (Map.Entry<Integer, Class> mapEntry : drawerMap.entrySet()) {
            if (this.getClass() == mapEntry.getValue()) {
                currentActivityIdx = mapEntry.getKey();
                Log.d(getClass().getSimpleName(), "Set currentActivityIdx to " + currentActivityIdx + " and selecting it");
                drawerList.setSelection(currentActivityIdx);
                return;
            }
        }
        Log.d(getClass().getSimpleName(), "Did not find current activity in the drawerMap!");
        currentActivityIdx = -1;
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

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_newgame:
                launchNewGameActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void launchNewGameActivity() {
        Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
        startActivity(intent);
    }

    protected void launchNewGameActivity(String courseId) {
        Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
        intent.putExtra(NewGameActivity.EXTRA_COURSE_ID, courseId);
        startActivity(intent);
    }

}
