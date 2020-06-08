package com.frolfr.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.R
import com.frolfr.api.model.Course

class CourseAdapter : RecyclerView.Adapter<CourseItemViewHolder>() {

    var courses = listOf<Course>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val course = courses[position]
        holder.textView.text = course.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.user_course_view, parent, false)
        return CourseItemViewHolder(view as TextView)
    }

}