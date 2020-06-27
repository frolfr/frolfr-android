package com.frolfr.ui.createRound

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.frolfr.R
import com.frolfr.api.model.Course

class CoursesAdapter(context: Context)
    : ArrayAdapter<Course>(context, R.layout.layout_course_item_dropdown_view, R.id.text_course_item_name) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val course = getItem(position) ?: return convertView!!
        var view = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.layout_course_item_selected_view, parent, false)
        }
        view!!.findViewById<TextView>(R.id.text_course_item_selected_name).text = course.name
        view.findViewById<TextView>(R.id.text_course_item_selected_par).text = Integer.toString(course.holeCount)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val course = getItem(position) ?: return convertView!!
        var view = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.layout_course_item_dropdown_view, parent, false)
        }
        view!!.findViewById<TextView>(R.id.text_course_item_name).text = course.name
        view.findViewById<TextView>(R.id.text_course_item_location).text = course.location
        view.findViewById<TextView>(R.id.text_course_item_par).text = Integer.toString(course.holeCount)
        return view
    }
}