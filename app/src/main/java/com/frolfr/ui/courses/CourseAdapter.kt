package com.frolfr.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.R
import com.frolfr.api.model.Course
import java.text.DateFormat
import java.text.SimpleDateFormat

class CourseAdapter : RecyclerView.Adapter<CourseItemViewHolder>() {

    var isoDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var df: DateFormat = SimpleDateFormat("MM/dd/yy")

    var courses = listOf<Course>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val course = courses[position]
        holder.name.text = course.name
        holder.location.text = course.location
        holder.holeCount.text = course.holeCount.toString()
        holder.lastPlayed.text = df.format(isoDateFormat.parse(course.lastPlayedAt))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.user_course_view, parent, false)
        return CourseItemViewHolder(view as ConstraintLayout)
    }

}