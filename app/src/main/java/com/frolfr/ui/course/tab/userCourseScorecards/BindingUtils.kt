package com.frolfr.ui.course.tab.userCourseScorecards

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frolfr.api.model.UserScorecard
import java.text.DateFormat
import java.text.SimpleDateFormat

// TODO put these somewhere common
private val isoDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
private val df: DateFormat = SimpleDateFormat("MM/dd/yy")

@BindingAdapter("date")
fun TextView.setDate(item: UserScorecard) {
    item.date.let {
        val date = isoDateFormat.parse(it)
        date?.let { d ->
            text = df.format(d)
        }
    }
}

@BindingAdapter("strokes")
fun TextView.setStrokes(item: UserScorecard) {
    text = item.strokes.toString()
}

@BindingAdapter("score")
fun TextView.setScore(item: UserScorecard) {
    val score = item.score
    text = when {
        score > 0 -> "+$score"
        else -> "$score"
    }
}

@BindingAdapter("rating")
fun TextView.setRating(item: UserScorecard) {
    text = if (item.rating == null) {
        "-"
    } else {
        item.rating.toInt().toString()
    }
}