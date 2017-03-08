package com.frolfr.frolfrclient.activity.newgame;

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
public class CourseSpinnerAdapter extends ArrayAdapter {

    private static final DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

    public CourseSpinnerAdapter(Activity activity, int listLayout, List<Course> rows) {
        super(activity, listLayout, rows);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColumnView cv;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            cv = new ColumnView();
            cv.name = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(cv);
            Log.d(getClass().getSimpleName(), "Creating new view in spinner");
        } else {
            cv = (ColumnView) convertView.getTag();
            Log.d(getClass().getSimpleName(), "Reusing view in spinner, " + cv.toString());
        }

        Course course = (Course) getItem(position);

        String location = course.location;

        cv.name.setText(course.name + " - " + location);

        Log.i(getClass().getSimpleName(), "Created view for " + cv.name.getText());

        return convertView;
    }

    protected static class ColumnView {
        protected TextView name;
    }

}
