package com.frolfr.frolfrclient;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frolfr.frolfrclient.entity.Course;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wowens on 1/24/16.
 */
public class CourseArrayAdapter extends ArrayAdapter {

    private static final DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

    public CourseArrayAdapter(Activity activity, int listLayout, List<Course> rows) {
        super(activity, listLayout, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColumnView cv;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_course, null);
            cv = new ColumnView();
            cv.name = (TextView) convertView.findViewById(R.id.list_item_course_name);
            cv.city = (TextView) convertView.findViewById(R.id.list_item_course_city);
            cv.state = (TextView) convertView.findViewById(R.id.list_item_course_state);
            convertView.setTag(cv);
        } else {
            cv = (ColumnView) convertView.getTag();
        }

        Course course = (Course) getItem(position);

        String location = course.location;
        String city = course.location;
        String state = "N/A";
        try {
            int commaIndex = location.indexOf(',');
            city = location.substring(0, commaIndex);
            state = location.substring(commaIndex + 1);
        } catch (Exception e) {
            Log.w(getClass().getSimpleName(), "Failed to parse location for city/state: " + location);
        }

        cv.name.setText(course.name + " (" + course.holeCount + ")");
        cv.city.setText(city);
        cv.state.setText(state);

        return convertView;
    }

    protected static class ColumnView {
        protected TextView name, city, state;
    }

}
