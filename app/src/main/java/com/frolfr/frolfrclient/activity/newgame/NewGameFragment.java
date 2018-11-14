package com.frolfr.frolfrclient.activity.newgame;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.frolfr.frolfrclient.R;
import com.frolfr.frolfrclient.entity.Course;

import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGameFragment extends Fragment {

    public NewGameFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_game, container, false);

        final NewGameActivity activity = (NewGameActivity) getActivity();

        Spinner spCourses = (Spinner) rootView.findViewById(R.id.new_game_course_spinner);
        CourseSpinnerAdapter adapter = activity.getCourseAdapter();
        spCourses.setAdapter(adapter);

        spCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(getClass().getSimpleName(), "Updating selected course to " + ((Course)parent.getItemAtPosition(position)).id);
                activity.setSelectedCourseId(((Course)parent.getItemAtPosition(position)).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                activity.setSelectedCourseId(-1);
            }
        });

        return rootView;
    }

}
