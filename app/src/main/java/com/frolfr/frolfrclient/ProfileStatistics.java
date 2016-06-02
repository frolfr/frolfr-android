package com.frolfr.frolfrclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.frolfr.frolfrclient.api.Authorization;
import com.frolfr.frolfrclient.api.Profile;
import com.frolfr.frolfrclient.config.PreferenceKeys.AuthKeys;

public class ProfileStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_statistics);

        new GetProfileStatisticsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_statistics, menu);
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
     * Represents an asynchronous call to get Profile Statistics
     */
    public class GetProfileStatisticsTask extends AsyncTask<Void, Void, String> {;

        @Override
        protected String doInBackground(Void... params) {
            // TODO: abstract most of this
            SharedPreferences preferences = getSharedPreferences(AuthKeys.class.getName(), MODE_PRIVATE);
            String email = preferences.getString(AuthKeys.EMAIL.toString(), null);
            String authToken = preferences.getString(AuthKeys.TOKEN.toString(), null);

            Profile.GetStatistics statsRequest = new Profile.GetStatistics();
            String jsonResponse = statsRequest.execute(email, authToken);

            if (jsonResponse != null) {
                Log.d(getClass().getName(), "Got a response from ajax GetProfileStatisticsTask");
            } else {
                Log.w(getClass().getName(), "No response from ajax GetProfileStatisticsTask");
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(final String jsonResponse) {
            if (!TextUtils.isEmpty(jsonResponse)) {
                TextView statsTextView = (TextView) findViewById(R.id.statistics_text);
                statsTextView.setText(jsonResponse);
            }
        }
    }
}
