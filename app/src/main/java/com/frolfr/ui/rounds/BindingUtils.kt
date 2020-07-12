package com.frolfr.ui.rounds

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frolfr.api.model.Course
import java.text.DateFormat
import java.text.SimpleDateFormat

private val isoDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
private val df: DateFormat = SimpleDateFormat("MM/dd/yy")

@BindingAdapter("userCourseLocation")
fun TextView.setLocation(item: Course) {
    text = item.location
}

@BindingAdapter("userCourseHoleCount")
fun TextView.setHoleCount(item: Course) {
    text = item.holeCount.toString()
}

@BindingAdapter("userCourseLastPlayed")
fun TextView.setLastPlayed(item: Course) {
    item.lastPlayedAt.let {
        val date = isoDateFormat.parse(it)
        date?.let { d ->
            text = df.format(d)
        }
    }
}