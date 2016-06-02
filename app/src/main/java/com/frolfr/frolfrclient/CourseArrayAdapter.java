package com.frolfr.frolfrclient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            cv.location = (TextView) convertView.findViewById(R.id.list_item_course_location);
            cv.lastPlayed = (TextView) convertView.findViewById(R.id.list_item_course_lastPlayed);
            convertView.setTag(cv);
        } else {
            cv = (ColumnView) convertView.getTag();
        }

        Course course = (Course) getItem(position);
        cv.name.setText(course.name + " (" + course.holeCount + ")");
        cv.location.setText(course.location);
        cv.lastPlayed.setText(df.format(course.lastPlayed));

        return convertView;
    }

    protected static class ColumnView {
        protected TextView name, location, lastPlayed;
    }

}
