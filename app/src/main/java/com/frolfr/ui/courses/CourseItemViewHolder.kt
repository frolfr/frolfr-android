package com.frolfr.ui.courses

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.R

class CourseItemViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout) {
    val name = layout.findViewById<TextView>(R.id.user_course_name)
    val location = layout.findViewById<TextView>(R.id.user_course_location)
    val holeCount = layout.findViewById<TextView>(R.id.user_course_hole_count)
    val lastPlayed = layout.findViewById<TextView>(R.id.user_course_last_played)
}
