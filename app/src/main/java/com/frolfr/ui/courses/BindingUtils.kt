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
    text = item.name
}