package com.frolfr.frolfrclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by wowens on 6/12/16.
 */
public class FrolfrActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ListView drawerList;
    public String[] drawerListItems;

    public int currentActivityIdx = 1;

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

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent(view.getContext(), CourseActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

                // TODO: does starting the next activity prevent this from being called?
                drawerLayout.closeDrawers();
            }
        });
    }

}
