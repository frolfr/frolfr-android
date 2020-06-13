package com.frolfr.ui.course

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frolfr.api.model.Course
import java.text.DateFormat
import java.text.SimpleDateFormat

private val isoDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
private val df: DateFormat = SimpleDateFormat("MM/dd/yy")

@BindingAdapter("courseName")
fun TextView.setName(item: Course) {
    text = item.name
}

@BindingAdapter("courseHoleCount")
fun TextView.setHoleCount(item: Course) {
    text = "${item.holeCount} Holes"
}

@BindingAdapter("courseLocation")
fun TextView.setLocation(item: Course) {
    text = item.location
}

@BindingAdapter("courseLastPlayed")
fun TextView.setLastPlayed(item: Course) {
    item.let {
        val date = isoDateFormat.parse(it.lastPlayedAt)
        date?.let { d ->
            text = "Played ${df.format(d)}"
        }
    }
}