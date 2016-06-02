package com.frolfr.frolfrclient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frolfr.frolfrclient.api.*;
import com.frolfr.frolfrclient.api.Courses;
import com.frolfr.frolfrclient.config.PreferenceKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoursesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {

    private CourseArrayAdapter courseListAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance() {
        CoursesFragment fragment = new CoursesFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    public CoursesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        courseListAdapter =
                new CourseArrayAdapter(
                        getActivity(),
                        R.layout.list_item_course, // The name of the layout ID.
                        new ArrayList<Course>());

        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_courses);
        listView.setAdapter(courseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO:
                Toast toast = Toast.makeText(getActivity(), "You clicked list item " + position, Toast.LENGTH_SHORT);
                toast.show();
//                String forecast = courseListAdapter.getItem(position);
//                Intent intent = new Intent(this, DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        super.onStart();
        new GetCourseInfoTask().execute();
    }


    /**
     * Represents an asynchronous call to get Profile Statistics
     */
    public class GetCourseInfoTask extends AsyncTask<ArrayAdapter, Void, Course[]> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        protected Course[] doInBackground(ArrayAdapter... params) {
            Log.d(getClass().getSimpleName(), "doInBackground - GetCourseInfoTask");
            SharedPreferences preferences = getActivity().getSharedPreferences(PreferenceKeys.AuthKeys.class.getName(), getActivity().MODE_PRIVATE);
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
                        courseInfo[i] = new Course(course.getString("name"), course.getInt("hole_count"),
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
            Log.d(getClass().getSimpleName(), "onPostExecute - GetCourseInfoTask");
            courseListAdapter.clear();
            if (courseResults == null)
                return;
            for (Course course : courseResults)
                courseListAdapter.add(course);
        }
    }

}
