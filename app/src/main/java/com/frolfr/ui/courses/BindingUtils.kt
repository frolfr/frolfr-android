package com.frolfr.ui.courses

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frolfr.api.model.Course
import java.text.DateFormat
import java.text.SimpleDateFormat

private val isoDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
private val df: DateFormat = SimpleDateFormat("MM/dd/yy")

@BindingAdapter("userCourseName")
fun TextView.setName(item: Course) {
    text = item?.name
}

@BindingAdapter("userCourseLocation")
fun TextView.setLocation(item: Course) {
    text = item?.location
}

@BindingAdapter("userCourseHoleCount")
fun TextView.setHoleCount(item: Course) {
    text = item?.holeCount.toString()
}

@BindingAdapter("userCourseLastPlayed")
fun TextView.setLastPlayed(item: Course) {
    item?.let {
        text = df.format(isoDateFormat.parse(it.lastPlayedAt))
    }
}